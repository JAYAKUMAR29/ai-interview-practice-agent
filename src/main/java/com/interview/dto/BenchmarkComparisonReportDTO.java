package com.interview.dto;

import java.util.List;

public class BenchmarkComparisonReportDTO {

    private String timestamp;
    private int sampleTestCasesCount;

    // Baseline metrics
    private double baselineAvgLatencyMs;
    private double baselineAvgScore;
    private double baselineAccuracyEstimate;
    private double baselineHallucinationRisk;

    // AI Hybrid Engine metrics
    private double aiAvgLatencyMs;
    private double aiAvgScore;
    private double aiAccuracyEstimate;
    private double aiHallucinationRisk;

    // Comparative improvements
    private double operationalEfficiencyImprovementPercent; // target 10-20%+
    private double semanticCoverageImprovementPercent;
    private double falseAlertReductionPercent;

    private List<ComparisonTestCaseResult> testCaseComparisons;
    private FailureScenarioAnalysis failureScenarioAnalysis;

    public static class ComparisonTestCaseResult {
        private String testCaseId;
        private String scenarioType; // NORMAL, NOISY, EDGE_CASE_BRIEF, OFF_TOPIC, ADVERSARIAL
        private String questionText;
        private String candidateAnswer;
        private int baselineScore;
        private int aiScore;
        private long baselineLatencyMs;
        private long aiLatencyMs;
        private boolean baselineFollowUp;
        private boolean aiFollowUp;
        private String aiEvidence;
        private String outcomeAnalysis;

        public ComparisonTestCaseResult() {}

        public String getTestCaseId() {
            return testCaseId;
        }

        public void setTestCaseId(String testCaseId) {
            this.testCaseId = testCaseId;
        }

        public String getScenarioType() {
            return scenarioType;
        }

        public void setScenarioType(String scenarioType) {
            this.scenarioType = scenarioType;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public String getCandidateAnswer() {
            return candidateAnswer;
        }

        public void setCandidateAnswer(String candidateAnswer) {
            this.candidateAnswer = candidateAnswer;
        }

        public int getBaselineScore() {
            return baselineScore;
        }

        public void setBaselineScore(int baselineScore) {
            this.baselineScore = baselineScore;
        }

        public int getAiScore() {
            return aiScore;
        }

        public void setAiScore(int aiScore) {
            this.aiScore = aiScore;
        }

        public long getBaselineLatencyMs() {
            return baselineLatencyMs;
        }

        public void setBaselineLatencyMs(long baselineLatencyMs) {
            this.baselineLatencyMs = baselineLatencyMs;
        }

        public long getAiLatencyMs() {
            return aiLatencyMs;
        }

        public void setAiLatencyMs(long aiLatencyMs) {
            this.aiLatencyMs = aiLatencyMs;
        }

        public boolean isBaselineFollowUp() {
            return baselineFollowUp;
        }

        public void setBaselineFollowUp(boolean baselineFollowUp) {
            this.baselineFollowUp = baselineFollowUp;
        }

        public boolean isAiFollowUp() {
            return aiFollowUp;
        }

        public void setAiFollowUp(boolean aiFollowUp) {
            this.aiFollowUp = aiFollowUp;
        }

        public String getAiEvidence() {
            return aiEvidence;
        }

        public void setAiEvidence(String aiEvidence) {
            this.aiEvidence = aiEvidence;
        }

        public String getOutcomeAnalysis() {
            return outcomeAnalysis;
        }

        public void setOutcomeAnalysis(String outcomeAnalysis) {
            this.outcomeAnalysis = outcomeAnalysis;
        }
    }

    public static class FailureScenarioAnalysis {
        private String missingDataHandling;
        private String noisyInputHandling;
        private String adversarialResilience;
        private String falsePositiveRateAnalysis;
        private String falseNegativeRateAnalysis;
        private String humanOverrideAuditability;

        public FailureScenarioAnalysis() {}

        public String getMissingDataHandling() {
            return missingDataHandling;
        }

        public void setMissingDataHandling(String missingDataHandling) {
            this.missingDataHandling = missingDataHandling;
        }

        public String getNoisyInputHandling() {
            return noisyInputHandling;
        }

        public void setNoisyInputHandling(String noisyInputHandling) {
            this.noisyInputHandling = noisyInputHandling;
        }

        public String getAdversarialResilience() {
            return adversarialResilience;
        }

        public void setAdversarialResilience(String adversarialResilience) {
            this.adversarialResilience = adversarialResilience;
        }

        public String getFalsePositiveRateAnalysis() {
            return falsePositiveRateAnalysis;
        }

        public void setFalsePositiveRateAnalysis(String falsePositiveRateAnalysis) {
            this.falsePositiveRateAnalysis = falsePositiveRateAnalysis;
        }

        public String getFalseNegativeRateAnalysis() {
            return falseNegativeRateAnalysis;
        }

        public void setFalseNegativeRateAnalysis(String falseNegativeRateAnalysis) {
            this.falseNegativeRateAnalysis = falseNegativeRateAnalysis;
        }

        public String getHumanOverrideAuditability() {
            return humanOverrideAuditability;
        }

        public void setHumanOverrideAuditability(String humanOverrideAuditability) {
            this.humanOverrideAuditability = humanOverrideAuditability;
        }
    }

    public BenchmarkComparisonReportDTO() {}

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getSampleTestCasesCount() {
        return sampleTestCasesCount;
    }

    public void setSampleTestCasesCount(int sampleTestCasesCount) {
        this.sampleTestCasesCount = sampleTestCasesCount;
    }

    public double getBaselineAvgLatencyMs() {
        return baselineAvgLatencyMs;
    }

    public void setBaselineAvgLatencyMs(double baselineAvgLatencyMs) {
        this.baselineAvgLatencyMs = baselineAvgLatencyMs;
    }

    public double getBaselineAvgScore() {
        return baselineAvgScore;
    }

    public void setBaselineAvgScore(double baselineAvgScore) {
        this.baselineAvgScore = baselineAvgScore;
    }

    public double getBaselineAccuracyEstimate() {
        return baselineAccuracyEstimate;
    }

    public void setBaselineAccuracyEstimate(double baselineAccuracyEstimate) {
        this.baselineAccuracyEstimate = baselineAccuracyEstimate;
    }

    public double getBaselineHallucinationRisk() {
        return baselineHallucinationRisk;
    }

    public void setBaselineHallucinationRisk(double baselineHallucinationRisk) {
        this.baselineHallucinationRisk = baselineHallucinationRisk;
    }

    public double getAiAvgLatencyMs() {
        return aiAvgLatencyMs;
    }

    public void setAiAvgLatencyMs(double aiAvgLatencyMs) {
        this.aiAvgLatencyMs = aiAvgLatencyMs;
    }

    public double getAiAvgScore() {
        return aiAvgScore;
    }

    public void setAiAvgScore(double aiAvgScore) {
        this.aiAvgScore = aiAvgScore;
    }

    public double getAiAccuracyEstimate() {
        return aiAccuracyEstimate;
    }

    public void setAiAccuracyEstimate(double aiAccuracyEstimate) {
        this.aiAccuracyEstimate = aiAccuracyEstimate;
    }

    public double getAiHallucinationRisk() {
        return aiHallucinationRisk;
    }

    public void setAiHallucinationRisk(double aiHallucinationRisk) {
        this.aiHallucinationRisk = aiHallucinationRisk;
    }

    public double getOperationalEfficiencyImprovementPercent() {
        return operationalEfficiencyImprovementPercent;
    }

    public void setOperationalEfficiencyImprovementPercent(double operationalEfficiencyImprovementPercent) {
        this.operationalEfficiencyImprovementPercent = operationalEfficiencyImprovementPercent;
    }

    public double getSemanticCoverageImprovementPercent() {
        return semanticCoverageImprovementPercent;
    }

    public void setSemanticCoverageImprovementPercent(double semanticCoverageImprovementPercent) {
        this.semanticCoverageImprovementPercent = semanticCoverageImprovementPercent;
    }

    public double getFalseAlertReductionPercent() {
        return falseAlertReductionPercent;
    }

    public void setFalseAlertReductionPercent(double falseAlertReductionPercent) {
        this.falseAlertReductionPercent = falseAlertReductionPercent;
    }

    public List<ComparisonTestCaseResult> getTestCaseComparisons() {
        return testCaseComparisons;
    }

    public void setTestCaseComparisons(List<ComparisonTestCaseResult> testCaseComparisons) {
        this.testCaseComparisons = testCaseComparisons;
    }

    public FailureScenarioAnalysis getFailureScenarioAnalysis() {
        return failureScenarioAnalysis;
    }

    public void setFailureScenarioAnalysis(FailureScenarioAnalysis failureScenarioAnalysis) {
        this.failureScenarioAnalysis = failureScenarioAnalysis;
    }
}
