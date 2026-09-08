package com.interview.controller;

import com.interview.dto.*;
import com.interview.model.InterviewSession;
import com.interview.model.InterviewSummary;
import com.interview.service.BaselineMetricsService;
import com.interview.service.InterviewSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = "*") // Allow easy local frontend communication
public class InterviewController {

    private final InterviewSessionService interviewSessionService;
    private final BaselineMetricsService baselineMetricsService;

    public InterviewController(InterviewSessionService interviewSessionService,
                               BaselineMetricsService baselineMetricsService) {
        this.interviewSessionService = interviewSessionService;
        this.baselineMetricsService = baselineMetricsService;
    }

    /**
     * Retrieves supported job roles, difficulty levels, and default settings.
     */
    @GetMapping("/config")
    public ResponseEntity<ConfigMetadataDTO> getConfig() {
        return ResponseEntity.ok(new ConfigMetadataDTO());
    }

    /**
     * Initializes a new interview session.
     */
    @PostMapping("/start")
    public ResponseEntity<QuestionResponseDTO> startInterview(@Valid @RequestBody StartInterviewRequest request) {
        InterviewSession session = interviewSessionService.startInterview(request);
        QuestionResponseDTO firstQuestion = interviewSessionService.getCurrentQuestion(session.getSessionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(firstQuestion);
    }

    /**
     * Retrieves the current question for an ongoing session.
     */
    @GetMapping("/{sessionId}/question")
    public ResponseEntity<QuestionResponseDTO> getCurrentQuestion(@PathVariable String sessionId) {
        QuestionResponseDTO current = interviewSessionService.getCurrentQuestion(sessionId);
        return ResponseEntity.ok(current);
    }

    /**
     * Submits an answer for the current question and returns immediate explainable evaluation.
     */
    @PostMapping("/{sessionId}/answer")
    public ResponseEntity<EvaluationResponseDTO> submitAnswer(
            @PathVariable String sessionId,
            @Valid @RequestBody SubmitAnswerRequest request) {
        EvaluationResponseDTO response = interviewSessionService.submitAnswer(sessionId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Submits an answer for a triggered follow-up question.
     */
    @PostMapping("/{sessionId}/follow-up")
    public ResponseEntity<EvaluationResponseDTO> submitFollowUp(
            @PathVariable String sessionId,
            @Valid @RequestBody SubmitFollowUpRequest request) {
        EvaluationResponseDTO response = interviewSessionService.submitFollowUpAnswer(sessionId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the final comprehensive result summary of the completed interview session.
     */
    @GetMapping("/{sessionId}/result")
    public ResponseEntity<InterviewSummary> getResult(@PathVariable String sessionId) {
        InterviewSummary summary = interviewSessionService.getInterviewSummary(sessionId);
        return ResponseEntity.ok(summary);
    }

    /**
     * Retrieves baseline measurements (evaluation latencies, session statistics, completion rates).
     */
    @GetMapping("/baseline-metrics")
    public ResponseEntity<BaselineMetricsDTO> getBaselineMetrics() {
        return ResponseEntity.ok(baselineMetricsService.getMetrics());
    }
}
