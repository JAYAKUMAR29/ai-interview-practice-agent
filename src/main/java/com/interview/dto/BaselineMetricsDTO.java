package com.interview.dto;

public class BaselineMetricsDTO {
    private int totalSessionsStarted;
    private int totalSessionsCompleted;
    private double completionRatePercent;
    private int totalAnswersEvaluated;
    private int totalFollowUpsTriggered;
    private double followUpTriggerRatePercent;
    private double avgEvaluationLatencyMs;
    private double avgAnswerScore;
    private double avgCandidateResponseTimeSec;
    private String engineModel;
    private String engineCharacteristics;

    public BaselineMetricsDTO() {
    }

    public BaselineMetricsDTO(int totalSessionsStarted, int totalSessionsCompleted, double completionRatePercent,
                              int totalAnswersEvaluated, int totalFollowUpsTriggered, double followUpTriggerRatePercent,
                              double avgEvaluationLatencyMs, double avgAnswerScore, double avgCandidateResponseTimeSec,
                              String engineModel, String engineCharacteristics) {
        this.totalSessionsStarted = totalSessionsStarted;
        this.totalSessionsCompleted = totalSessionsCompleted;
        this.completionRatePercent = completionRatePercent;
        this.totalAnswersEvaluated = totalAnswersEvaluated;
        this.totalFollowUpsTriggered = totalFollowUpsTriggered;
        this.followUpTriggerRatePercent = followUpTriggerRatePercent;
        this.avgEvaluationLatencyMs = avgEvaluationLatencyMs;
        this.avgAnswerScore = avgAnswerScore;
        this.avgCandidateResponseTimeSec = avgCandidateResponseTimeSec;
        this.engineModel = engineModel;
        this.engineCharacteristics = engineCharacteristics;
    }

    public int getTotalSessionsStarted() {
        return totalSessionsStarted;
    }

    public int getTotalSessionsCompleted() {
        return totalSessionsCompleted;
    }

    public double getCompletionRatePercent() {
        return completionRatePercent;
    }

    public int getTotalAnswersEvaluated() {
        return totalAnswersEvaluated;
    }

    public int getTotalFollowUpsTriggered() {
        return totalFollowUpsTriggered;
    }

    public double getFollowUpTriggerRatePercent() {
        return followUpTriggerRatePercent;
    }

    public double getAvgEvaluationLatencyMs() {
        return avgEvaluationLatencyMs;
    }

    public double getAvgAnswerScore() {
        return avgAnswerScore;
    }

    public double getAvgCandidateResponseTimeSec() {
        return avgCandidateResponseTimeSec;
    }

    public String getEngineModel() {
        return engineModel;
    }

    public String getEngineCharacteristics() {
        return engineCharacteristics;
    }
}
