package com.interview.dto;

import com.interview.model.DifficultyLevel;
import com.interview.model.JobRole;

public class QuestionResponseDTO {
    private String sessionId;
    private int questionNumber; // 1-based (e.g. 1)
    private int totalQuestions; // e.g. 5
    private String questionId;
    private JobRole role;
    private DifficultyLevel difficulty;
    private String topic;
    private String questionText;
    private boolean isFinished;

    public QuestionResponseDTO() {
    }

    public QuestionResponseDTO(String sessionId, int questionNumber, int totalQuestions, String questionId,
                               JobRole role, DifficultyLevel difficulty, String topic, String questionText, boolean isFinished) {
        this.sessionId = sessionId;
        this.questionNumber = questionNumber;
        this.totalQuestions = totalQuestions;
        this.questionId = questionId;
        this.role = role;
        this.difficulty = difficulty;
        this.topic = topic;
        this.questionText = questionText;
        this.isFinished = isFinished;
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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
