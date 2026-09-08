package com.interview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.dto.StartInterviewRequest;
import com.interview.dto.SubmitAnswerRequest;
import com.interview.dto.SubmitFollowUpRequest;
import com.interview.model.DifficultyLevel;
import com.interview.model.JobRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InterviewWorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Fetch configuration metadata should return all 5 roles and 3 difficulties")
    void testGetConfig() throws Exception {
        mockMvc.perform(get("/api/interview/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles", hasSize(5)))
                .andExpect(jsonPath("$.difficulties", hasSize(3)))
                .andExpect(jsonPath("$.defaultQuestionCount", is(5)));
    }

    @Test
    @DisplayName("Complete end-to-end interview flow: Start -> Answer -> Follow-up -> Result")
    void testEndToEndInterviewFlow() throws Exception {
        // 1. Start Interview
        StartInterviewRequest startReq = new StartInterviewRequest(JobRole.JAVA_DEVELOPER, DifficultyLevel.BEGINNER, 1);
        MvcResult startResult = mockMvc.perform(post("/api/interview/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sessionId").isNotEmpty())
                .andExpect(jsonPath("$.questionNumber", is(1)))
                .andExpect(jsonPath("$.totalQuestions", is(1)))
                .andExpect(jsonPath("$.questionText").isNotEmpty())
                .andReturn();

        String sessionId = objectMapper.readTree(startResult.getResponse().getContentAsString()).get("sessionId").asText();

        // 2. Submit short answer (to trigger follow-up)
        SubmitAnswerRequest ansReq = new SubmitAnswerRequest("Encapsulation hides data and inheritance shares code.", 12000);
        MvcResult ansResult = mockMvc.perform(post("/api/interview/" + sessionId + "/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ansReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followUpRequired", is(true)))
                .andExpect(jsonPath("$.followUpQuestion").isNotEmpty())
                .andReturn();

        // 3. Submit follow-up response
        SubmitFollowUpRequest followReq = new SubmitFollowUpRequest(
                "Method overloading happens at compile-time with different parameter signatures, while method overriding occurs at runtime when a subclass replaces a superclass method.",
                8000
        );
        mockMvc.perform(post("/api/interview/" + sessionId + "/follow-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(followReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finished", is(true)))
                .andExpect(jsonPath("$.evaluation.score", greaterThan(50)));

        // 4. Retrieve Final Summary
        mockMvc.perform(get("/api/interview/" + sessionId + "/result"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId", is(sessionId)))
                .andExpect(jsonPath("$.overallScore", greaterThan(50)))
                .andExpect(jsonPath("$.questionBreakdown", hasSize(1)))
                .andExpect(jsonPath("$.keyStrengths").isNotEmpty())
                .andExpect(jsonPath("$.performanceLevel").isNotEmpty());

        // 5. Inspect Baseline Metrics
        mockMvc.perform(get("/api/interview/baseline-metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSessionsStarted", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.totalSessionsCompleted", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.avgEvaluationLatencyMs").isNumber());
    }

    @Test
    @DisplayName("Error handling: non-existent session ID should return 404")
    void testInvalidSessionId() throws Exception {
        mockMvc.perform(get("/api/interview/non-existent-uuid/question"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("RESOURCE_NOT_FOUND")));
    }

    @Test
    @DisplayName("Error handling: invalid start request should return 400")
    void testInvalidStartRequest() throws Exception {
        String invalidJson = "{\"role\": \"NON_EXISTENT_ROLE\", \"difficulty\": \"BEGINNER\", \"questionCount\": 5}";
        mockMvc.perform(post("/api/interview/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
