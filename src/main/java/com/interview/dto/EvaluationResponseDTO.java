package com.interview.dto;

import com.interview.model.EvaluationResult;

public class EvaluationResponseDTO {
    private String sessionId;
    private int questionNumber;
    private int totalQuestions;
    private EvaluationResult evaluation;
    private boolean isFinished;
    private boolean followUpRequired;
    private String followUpQuestion;
    private String followUpReason;
    private long evaluationTimeMs;

    public EvaluationResponseDTO() {
    }

    public EvaluationResponseDTO(String sessionId, int questionNumber, int totalQuestions,
                                 EvaluationResult evaluation, boolean isFinished,
                                 boolean followUpRequired, String followUpQuestion,
                                 String followUpReason, long evaluationTimeMs) {
        this.sessionId = sessionId;
        this.questionNumber = questionNumber;
        this.totalQuestions = totalQuestions;
        this.evaluation = evaluation;
        this.isFinished = isFinished;
        this.followUpRequired = followUpRequired;
        this.followUpQuestion = followUpQuestion;
        this.followUpReason = followUpReason;
        this.evaluationTimeMs = evaluationTimeMs;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public EvaluationResult getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EvaluationResult evaluation) {
        this.evaluation = evaluation;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isFollowUpRequired() {
        return followUpRequired;
    }

    public void setFollowUpRequired(boolean followUpRequired) {
        this.followUpRequired = followUpRequired;
    }

    public String getFollowUpQuestion() {
        return followUpQuestion;
    }

    public void setFollowUpQuestion(String followUpQuestion) {
        this.followUpQuestion = followUpQuestion;
    }

    public String getFollowUpReason() {
        return followUpReason;
    }

    public void setFollowUpReason(String followUpReason) {
        this.followUpReason = followUpReason;
    }

    public long getEvaluationTimeMs() {
        return evaluationTimeMs;
    }

    public void setEvaluationTimeMs(long evaluationTimeMs) {
        this.evaluationTimeMs = evaluationTimeMs;
    }
}
