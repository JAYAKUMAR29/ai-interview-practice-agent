package com.interview.model;

import java.util.ArrayList;
import java.util.List;

public class InterviewSummary {
    private String sessionId;
    private JobRole role;
    private DifficultyLevel difficulty;
    private int totalQuestions;
    private int answeredQuestions;
    private int overallScore; // 0 - 100

    private double avgRelevance;
    private double avgKeywordMatch;
    private double avgCompleteness;
    private double avgLengthClarity;

    private List<String> keyStrengths = new ArrayList<>();
    private List<String> keyImprovements = new ArrayList<>();
    private String overallRecommendation;
    private String performanceLevel; // Excellent, Good, Fair, Needs Preparation

    private long totalDurationSeconds;
    private double avgResponseTimeSeconds;
    private List<QuestionSummaryItem> questionBreakdown = new ArrayList<>();

    public static class QuestionSummaryItem {
        private int questionNumber;
        private String questionText;
        private String topic;
        private String candidateAnswer;
        private boolean hadFollowUp;
        private String followUpQuestion;
        private String followUpAnswer;
        private int score;
        private List<String> matchedKeywords;
        private List<String> strengths;
        private List<String> improvements;
        private String explanation;

        public QuestionSummaryItem() {}

        public int getQuestionNumber() {
            return questionNumber;
        }

        public void setQuestionNumber(int questionNumber) {
            this.questionNumber = questionNumber;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getCandidateAnswer() {
            return candidateAnswer;
        }

        public void setCandidateAnswer(String candidateAnswer) {
            this.candidateAnswer = candidateAnswer;
        }

        public boolean isHadFollowUp() {
            return hadFollowUp;
        }

        public void setHadFollowUp(boolean hadFollowUp) {
            this.hadFollowUp = hadFollowUp;
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

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public List<String> getMatchedKeywords() {
            return matchedKeywords;
        }

        public void setMatchedKeywords(List<String> matchedKeywords) {
            this.matchedKeywords = matchedKeywords;
        }

        public List<String> getStrengths() {
            return strengths;
        }

        public void setStrengths(List<String> strengths) {
            this.strengths = strengths;
        }

        public List<String> getImprovements() {
            return improvements;
        }

        public void setImprovements(List<String> improvements) {
            this.improvements = improvements;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }

    public InterviewSummary() {}

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

    public int getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(int answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public double getAvgRelevance() {
        return avgRelevance;
    }

    public void setAvgRelevance(double avgRelevance) {
        this.avgRelevance = avgRelevance;
    }

    public double getAvgKeywordMatch() {
        return avgKeywordMatch;
    }

    public void setAvgKeywordMatch(double avgKeywordMatch) {
        this.avgKeywordMatch = avgKeywordMatch;
    }

    public double getAvgCompleteness() {
        return avgCompleteness;
    }

    public void setAvgCompleteness(double avgCompleteness) {
        this.avgCompleteness = avgCompleteness;
    }

    public double getAvgLengthClarity() {
        return avgLengthClarity;
    }

    public void setAvgLengthClarity(double avgLengthClarity) {
        this.avgLengthClarity = avgLengthClarity;
    }

    public List<String> getKeyStrengths() {
        return keyStrengths;
    }

    public void setKeyStrengths(List<String> keyStrengths) {
        this.keyStrengths = keyStrengths;
    }

    public List<String> getKeyImprovements() {
        return keyImprovements;
    }

    public void setKeyImprovements(List<String> keyImprovements) {
        this.keyImprovements = keyImprovements;
    }

    public String getOverallRecommendation() {
        return overallRecommendation;
    }

    public void setOverallRecommendation(String overallRecommendation) {
        this.overallRecommendation = overallRecommendation;
    }

    public String getPerformanceLevel() {
        return performanceLevel;
    }

    public void setPerformanceLevel(String performanceLevel) {
        this.performanceLevel = performanceLevel;
    }

    public long getTotalDurationSeconds() {
        return totalDurationSeconds;
    }

    public void setTotalDurationSeconds(long totalDurationSeconds) {
        this.totalDurationSeconds = totalDurationSeconds;
    }

    public double getAvgResponseTimeSeconds() {
        return avgResponseTimeSeconds;
    }

    public void setAvgResponseTimeSeconds(double avgResponseTimeSeconds) {
        this.avgResponseTimeSeconds = avgResponseTimeSeconds;
    }

    public List<QuestionSummaryItem> getQuestionBreakdown() {
        return questionBreakdown;
    }

    public void setQuestionBreakdown(List<QuestionSummaryItem> questionBreakdown) {
        this.questionBreakdown = questionBreakdown;
    }
}
