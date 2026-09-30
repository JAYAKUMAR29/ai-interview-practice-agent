package com.interview.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HumanOverrideRequest {

    @NotNull(message = "Question number is required")
    @Min(value = 1, message = "Question number must be at least 1")
    private Integer questionNumber;

    @NotNull(message = "Adjusted score is required")
    @Min(value = 0, message = "Score cannot be less than 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer adjustedScore;

    @NotBlank(message = "Override notes/justification required for auditability")
    private String overrideNotes;

    public HumanOverrideRequest() {
    }

    public HumanOverrideRequest(Integer questionNumber, Integer adjustedScore, String overrideNotes) {
        this.questionNumber = questionNumber;
        this.adjustedScore = adjustedScore;
        this.overrideNotes = overrideNotes;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Integer getAdjustedScore() {
        return adjustedScore;
    }

    public void setAdjustedScore(Integer adjustedScore) {
        this.adjustedScore = adjustedScore;
    }

    public String getOverrideNotes() {
        return overrideNotes;
    }

    public void setOverrideNotes(String overrideNotes) {
        this.overrideNotes = overrideNotes;
    }
}
