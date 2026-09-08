package com.interview.dto;

import jakarta.validation.constraints.NotNull;

public class SubmitAnswerRequest {

    @NotNull(message = "Answer cannot be null")
    private String answer;

    private long responseTimeMs; // Time candidate spent answering this question

    public SubmitAnswerRequest() {
    }

    public SubmitAnswerRequest(String answer, long responseTimeMs) {
        this.answer = answer;
        this.responseTimeMs = responseTimeMs;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }
}
