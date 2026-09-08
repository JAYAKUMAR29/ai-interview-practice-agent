package com.interview.service;

import com.interview.dto.BaselineMetricsDTO;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BaselineMetricsService {

    private final AtomicInteger totalSessionsStarted = new AtomicInteger(0);
    private final AtomicInteger totalSessionsCompleted = new AtomicInteger(0);
    private final AtomicInteger totalAnswersEvaluated = new AtomicInteger(0);
    private final AtomicInteger totalFollowUpsTriggered = new AtomicInteger(0);

    private final AtomicLong cumulativeEvaluationTimeMs = new AtomicLong(0);
    private final AtomicLong cumulativeCandidateResponseTimeMs = new AtomicLong(0);
    private final AtomicLong cumulativeTotalScore = new AtomicLong(0);

    private final ConcurrentLinkedQueue<Integer> recentScores = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Long> recentEvalLatencies = new ConcurrentLinkedQueue<>();

    public void recordSessionStarted() {
        totalSessionsStarted.incrementAndGet();
    }

    public void recordSessionCompleted() {
        totalSessionsCompleted.incrementAndGet();
    }

    public void recordAnswerEvaluated(long evalDurationMs, int score, boolean hadFollowUp, long candidateResponseTimeMs) {
        totalAnswersEvaluated.incrementAndGet();
        cumulativeEvaluationTimeMs.addAndGet(evalDurationMs);
        cumulativeCandidateResponseTimeMs.addAndGet(candidateResponseTimeMs);
        cumulativeTotalScore.addAndGet(score);

        if (hadFollowUp) {
            totalFollowUpsTriggered.incrementAndGet();
        }

        recentScores.add(score);
        while (recentScores.size() > 100) {
            recentScores.poll();
        }

        recentEvalLatencies.add(evalDurationMs);
        while (recentEvalLatencies.size() > 100) {
            recentEvalLatencies.poll();
        }
    }

    public BaselineMetricsDTO getMetrics() {
        int answers = totalAnswersEvaluated.get();
        int started = totalSessionsStarted.get();
        int completed = totalSessionsCompleted.get();

        double avgEvalTimeMs = answers > 0 ? (double) cumulativeEvaluationTimeMs.get() / answers : 0.0;
        double avgScore = answers > 0 ? (double) cumulativeTotalScore.get() / answers : 0.0;
        double avgResponseTimeSec = answers > 0 ? (double) cumulativeCandidateResponseTimeMs.get() / (answers * 1000.0) : 0.0;
        double completionRate = started > 0 ? ((double) completed / started) * 100.0 : 0.0;
        double followUpRate = answers > 0 ? ((double) totalFollowUpsTriggered.get() / answers) * 100.0 : 0.0;

        return new BaselineMetricsDTO(
                started,
                completed,
                completionRate,
                answers,
                totalFollowUpsTriggered.get(),
                followUpRate,
                avgEvalTimeMs,
                avgScore,
                avgResponseTimeSec,
                "Rule-Based Deterministic Engine (Baseline Phase 1 - 35%)",
                "Sub-millisecond latency (<5ms), deterministic keyword & concept rubric, no external API costs"
        );
    }
}
