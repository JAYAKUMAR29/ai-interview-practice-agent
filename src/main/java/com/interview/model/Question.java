package com.interview.model;

import java.util.List;

public class Question {
    private String id;
    private JobRole role;
    private DifficultyLevel difficulty;
    private String topic;
    private String questionText;
    private List<String> requiredKeywords;
    private List<String> expectedConcepts;
    private String sampleGoodAnswer;
    private String followUpQuestion;
    private List<String> followUpTriggerKeywords; // keywords that if missing or weakly used warrant follow up

    public Question() {
    }

    public Question(String id, JobRole role, DifficultyLevel difficulty, String topic, String questionText,
                    List<String> requiredKeywords, List<String> expectedConcepts, String sampleGoodAnswer,
                    String followUpQuestion, List<String> followUpTriggerKeywords) {
        this.id = id;
        this.role = role;
        this.difficulty = difficulty;
        this.topic = topic;
        this.questionText = questionText;
        this.requiredKeywords = requiredKeywords;
        this.expectedConcepts = expectedConcepts;
        this.sampleGoodAnswer = sampleGoodAnswer;
        this.followUpQuestion = followUpQuestion;
        this.followUpTriggerKeywords = followUpTriggerKeywords;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getRequiredKeywords() {
        return requiredKeywords;
    }

    public void setRequiredKeywords(List<String> requiredKeywords) {
        this.requiredKeywords = requiredKeywords;
    }

    public List<String> getExpectedConcepts() {
        return expectedConcepts;
    }

    public void setExpectedConcepts(List<String> expectedConcepts) {
        this.expectedConcepts = expectedConcepts;
    }

    public String getSampleGoodAnswer() {
        return sampleGoodAnswer;
    }

    public void setSampleGoodAnswer(String sampleGoodAnswer) {
        this.sampleGoodAnswer = sampleGoodAnswer;
    }

    public String getFollowUpQuestion() {
        return followUpQuestion;
    }

    public void setFollowUpQuestion(String followUpQuestion) {
        this.followUpQuestion = followUpQuestion;
    }

    public List<String> getFollowUpTriggerKeywords() {
        return followUpTriggerKeywords;
    }

    public void setFollowUpTriggerKeywords(List<String> followUpTriggerKeywords) {
        this.followUpTriggerKeywords = followUpTriggerKeywords;
    }
}
