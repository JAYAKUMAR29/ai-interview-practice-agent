package com.interview.model;

import java.util.ArrayList;
import java.util.List;

public class EvaluationResult {
    private int score; // 0 - 100
    private int relevanceScore; // 0 - 25
    private int keywordScore; // 0 - 25
    private int completenessScore; // 0 - 25
    private int lengthScore; // 0 - 15
    private int clarityScore; // 0 - 10

    private List<String> matchedKeywords = new ArrayList<>();
    private List<String> missingKeywords = new ArrayList<>();
    private List<String> strengths = new ArrayList<>();
    private List<String> areasForImprovement = new ArrayList<>();
    private String explanation;

    private boolean followUpRecommended;
    private String followUpReason;
    private String suggestedFollowUpQuestion;

    public EvaluationResult() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(int relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public int getKeywordScore() {
        return keywordScore;
    }

    public void setKeywordScore(int keywordScore) {
        this.keywordScore = keywordScore;
    }

    public int getCompletenessScore() {
        return completenessScore;
    }

    public void setCompletenessScore(int completenessScore) {
        this.completenessScore = completenessScore;
    }

    public int getLengthScore() {
        return lengthScore;
    }

    public void setLengthScore(int lengthScore) {
        this.lengthScore = lengthScore;
    }

    public int getClarityScore() {
        return clarityScore;
    }

    public void setClarityScore(int clarityScore) {
        this.clarityScore = clarityScore;
    }

    public List<String> getMatchedKeywords() {
        return matchedKeywords;
    }

    public void setMatchedKeywords(List<String> matchedKeywords) {
        this.matchedKeywords = matchedKeywords;
    }

    public List<String> getMissingKeywords() {
        return missingKeywords;
    }

    public void setMissingKeywords(List<String> missingKeywords) {
        this.missingKeywords = missingKeywords;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getAreasForImprovement() {
        return areasForImprovement;
    }

    public void setAreasForImprovement(List<String> areasForImprovement) {
        this.areasForImprovement = areasForImprovement;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isFollowUpRecommended() {
        return followUpRecommended;
    }

    public void setFollowUpRecommended(boolean followUpRecommended) {
        this.followUpRecommended = followUpRecommended;
    }

    public String getFollowUpReason() {
        return followUpReason;
    }

    public void setFollowUpReason(String followUpReason) {
        this.followUpReason = followUpReason;
    }

    public String getSuggestedFollowUpQuestion() {
        return suggestedFollowUpQuestion;
    }

    public void setSuggestedFollowUpQuestion(String suggestedFollowUpQuestion) {
        this.suggestedFollowUpQuestion = suggestedFollowUpQuestion;
    }
}
