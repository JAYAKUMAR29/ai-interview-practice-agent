package com.interview.dto;

import com.interview.model.DifficultyLevel;
import com.interview.model.JobRole;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StartInterviewRequest {

    @NotNull(message = "Job role is required")
    private JobRole role;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficulty;

    @Min(value = 1, message = "Interview must contain at least 1 question")
    @Max(value = 10, message = "Interview cannot exceed 10 questions per session")
    private int questionCount = 5;

    public StartInterviewRequest() {
    }

    public StartInterviewRequest(JobRole role, DifficultyLevel difficulty, int questionCount) {
        this.role = role;
        this.difficulty = difficulty;
        this.questionCount = questionCount;
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

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
