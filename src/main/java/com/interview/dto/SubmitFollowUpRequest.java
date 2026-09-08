package com.interview.dto;

import jakarta.validation.constraints.NotNull;

public class SubmitFollowUpRequest {

    @NotNull(message = "Follow-up answer cannot be null")
    private String followUpAnswer;

    private long responseTimeMs;

    public SubmitFollowUpRequest() {
    }

    public SubmitFollowUpRequest(String followUpAnswer, long responseTimeMs) {
        this.followUpAnswer = followUpAnswer;
        this.responseTimeMs = responseTimeMs;
    }

    public String getFollowUpAnswer() {
        return followUpAnswer;
    }

    public void setFollowUpAnswer(String followUpAnswer) {
        this.followUpAnswer = followUpAnswer;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }
}
