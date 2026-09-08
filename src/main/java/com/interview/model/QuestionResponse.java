package com.interview.model;

import java.time.Instant;

public class QuestionResponse {
    private Question question;
    private String candidateAnswer;
    private String followUpQuestion;
    private String followUpAnswer;
    private EvaluationResult initialEvaluation;
    private EvaluationResult finalEvaluation;
    private long responseTimeMs;
    private Instant answeredAt;

    public QuestionResponse() {
        this.answeredAt = Instant.now();
    }

    public QuestionResponse(Question question) {
        this.question = question;
        this.answeredAt = Instant.now();
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getCandidateAnswer() {
        return candidateAnswer;
    }

    public void setCandidateAnswer(String candidateAnswer) {
        this.candidateAnswer = candidateAnswer;
    }

    public String getFollowUpQuestion() {
        return followUpQuestion;
    }

    public void setFollowUpQuestion(String followUpQuestion) {
        this.followUpQuestion = followUpQuestion;
    }

    public String getFollowUpAnswer() {
        return followUpAnswer;
    }

    public void setFollowUpAnswer(String followUpAnswer) {
        this.followUpAnswer = followUpAnswer;
    }

    public EvaluationResult getInitialEvaluation() {
        return initialEvaluation;
    }

    public void setInitialEvaluation(EvaluationResult initialEvaluation) {
        this.initialEvaluation = initialEvaluation;
    }

    public EvaluationResult getFinalEvaluation() {
        return finalEvaluation;
    }

    public void setFinalEvaluation(EvaluationResult finalEvaluation) {
        this.finalEvaluation = finalEvaluation;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public Instant getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Instant answeredAt) {
        this.answeredAt = answeredAt;
    }
}
