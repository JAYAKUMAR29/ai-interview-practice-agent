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

        String responseBody = startResult.getResponse().getContentAsString();
        String sessionId = objectMapper.readTree(responseBody).get("sessionId").asText();
        String questionText = objectMapper.readTree(responseBody).get("questionText").asText();

        // 2. Submit short answer (to trigger follow-up probing)
        String initialAnswer;
        if (questionText.contains("OOP") || questionText.contains("pillars")) {
            initialAnswer = "Encapsulation hides data and inheritance shares code.";
        } else if (questionText.contains("Stack") || questionText.contains("Heap")) {
            initialAnswer = "Stack stores primitive variables while heap stores objects.";
        } else if (questionText.contains("equals") || questionText.contains("==")) {
            initialAnswer = "== checks memory reference while equals checks content.";
        } else if (questionText.contains("ArrayList") || questionText.contains("LinkedList")) {
            initialAnswer = "ArrayList uses dynamic array and LinkedList uses node pointers.";
        } else {
            initialAnswer = "Inversion of control delegates bean creation to the Spring container.";
        }

        MvcResult ansResult = mockMvc.perform(post("/api/interview/" + sessionId + "/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SubmitAnswerRequest(initialAnswer, 12000))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followUpRequired", is(true)))
                .andExpect(jsonPath("$.followUpQuestion").isNotEmpty())
                .andReturn();

        // 3. Submit follow-up response addressing the probed topic
        String followUpAnswer;
        if (questionText.contains("OOP") || questionText.contains("pillars")) {
            followUpAnswer = "Method overloading happens at compile-time with different parameter signatures, while method overriding occurs at runtime when a subclass replaces a superclass method in polymorphism.";
        } else if (questionText.contains("Stack") || questionText.contains("Heap")) {
            followUpAnswer = "The JVM garbage collector traces reference roots; when an object in the heap has no reachable references, it is eligible for garbage collection.";
        } else if (questionText.contains("equals") || questionText.contains("==")) {
            followUpAnswer = "Overriding equals and hashCode ensures proper hash bucket placement and retrieval in collections like HashMap.";
        } else if (questionText.contains("ArrayList") || questionText.contains("LinkedList")) {
            followUpAnswer = "When ArrayList capacity is exceeded, it allocates a new array of 1.5x size and copies elements.";
        } else {
            followUpAnswer = "Constructor injection is preferred over field injection because it ensures immutability and simplifies unit testing without reflection.";
        }

        SubmitFollowUpRequest followReq = new SubmitFollowUpRequest(followUpAnswer, 8000);
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
    @DisplayName("Benchmark report endpoint should return comparative metrics and sample test cases")
    void testGetBenchmarkReport() throws Exception {
        mockMvc.perform(get("/api/interview/benchmark-report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleTestCasesCount", greaterThanOrEqualTo(5)))
                .andExpect(jsonPath("$.baselineAvgLatencyMs").isNumber())
                .andExpect(jsonPath("$.aiAvgLatencyMs").isNumber())
                .andExpect(jsonPath("$.operationalEfficiencyImprovementPercent", greaterThan(10.0)))
                .andExpect(jsonPath("$.failureScenarioAnalysis.missingDataHandling").isNotEmpty());
    }

    @Test
    @DisplayName("Human-in-the-loop override endpoint updates score and notes with auditability")
    void testHumanOverride() throws Exception {
        // Start a 1-question interview
        StartInterviewRequest startReq = new StartInterviewRequest(JobRole.WEB_DEVELOPER, DifficultyLevel.BEGINNER, 1);
        MvcResult startResult = mockMvc.perform(post("/api/interview/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startReq)))
                .andExpect(status().isCreated())
                .andReturn();

        String sessionId = objectMapper.readTree(startResult.getResponse().getContentAsString()).get("sessionId").asText();

        // Submit answer
        mockMvc.perform(post("/api/interview/" + sessionId + "/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SubmitAnswerRequest("HTML structures page content, CSS formats presentation styling, and JavaScript adds interactive logic.", 15000))))
                .andExpect(status().isOk());

        // Apply human override
        com.interview.dto.HumanOverrideRequest overrideReq = new com.interview.dto.HumanOverrideRequest(1, 95, "Candidate explained the DOM separation clearly in follow-up discussion.");
        mockMvc.perform(post("/api/interview/" + sessionId + "/override")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overrideReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallScore", is(95)))
                .andExpect(jsonPath("$.hasHumanOverride", is(true)));
    }
}
