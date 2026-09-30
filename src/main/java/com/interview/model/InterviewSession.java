package com.interview.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class InterviewSession {
    public enum SessionStatus {
        ACTIVE,
        AWAITING_FOLLOW_UP,
        COMPLETED,
        CANCELLED
    }

    private String sessionId;
    private JobRole role;
    private DifficultyLevel difficulty;
    private int totalQuestions;
    private int currentQuestionIndex; // 0-based
    private SessionStatus status;
    private EvaluationEngineType engineType = EvaluationEngineType.AI_HYBRID;

    private List<Question> selectedQuestions = new ArrayList<>();
    private List<QuestionResponse> responses = new ArrayList<>();

    private Instant startedAt;
    private Instant completedAt;

    public InterviewSession() {
        this.status = SessionStatus.ACTIVE;
        this.startedAt = Instant.now();
        this.engineType = EvaluationEngineType.AI_HYBRID;
    }

    public InterviewSession(String sessionId, JobRole role, DifficultyLevel difficulty, int totalQuestions) {
        this(sessionId, role, difficulty, totalQuestions, EvaluationEngineType.AI_HYBRID);
    }

    public InterviewSession(String sessionId, JobRole role, DifficultyLevel difficulty, int totalQuestions, EvaluationEngineType engineType) {
        this.sessionId = sessionId;
        this.role = role;
        this.difficulty = difficulty;
        this.totalQuestions = totalQuestions;
        this.currentQuestionIndex = 0;
        this.status = SessionStatus.ACTIVE;
        this.startedAt = Instant.now();
        this.engineType = (engineType != null) ? engineType : EvaluationEngineType.AI_HYBRID;
    }

    public EvaluationEngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EvaluationEngineType engineType) {
        this.engineType = engineType;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < selectedQuestions.size()) {
            return selectedQuestions.get(currentQuestionIndex);
        }
        return null;
    }

    public QuestionResponse getCurrentResponse() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < responses.size()) {
            return responses.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean isFinished() {
        return currentQuestionIndex >= totalQuestions || status == SessionStatus.COMPLETED;
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public JobRole getRole() {
        return role;
    }

    public void setRole(JobRole role) {
        this.role = role;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public List<Question> getSelectedQuestions() {
        return selectedQuestions;
    }

    public void setSelectedQuestions(List<Question> selectedQuestions) {
        this.selectedQuestions = selectedQuestions;
    }

    public List<QuestionResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<QuestionResponse> responses) {
        this.responses = responses;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}
