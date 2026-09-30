package com.interview.service;

import com.interview.dto.*;
import com.interview.exception.InvalidRequestException;
import com.interview.exception.ResourceNotFoundException;
import com.interview.model.*;
import com.interview.repository.InterviewSessionRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class InterviewSessionService {

    private final InterviewSessionRepository sessionRepository;
    private final QuestionProvider questionProvider;
    private final AnswerEvaluator answerEvaluator; // baseline rule-based
    private final AdvancedAIEvaluator advancedAIEvaluator; // AI hybrid / Gemini
    private final BaselineMetricsService baselineMetricsService;

    public InterviewSessionService(InterviewSessionRepository sessionRepository,
                                   QuestionProvider questionProvider,
                                   AnswerEvaluator answerEvaluator,
                                   AdvancedAIEvaluator advancedAIEvaluator,
                                   BaselineMetricsService baselineMetricsService) {
        this.sessionRepository = sessionRepository;
        this.questionProvider = questionProvider;
        this.answerEvaluator = answerEvaluator;
        this.advancedAIEvaluator = advancedAIEvaluator;
        this.baselineMetricsService = baselineMetricsService;
    }

    public InterviewSession startInterview(StartInterviewRequest request) {
        if (request.getRole() == null) {
            throw new InvalidRequestException("Job role must be selected");
        }
        if (request.getDifficulty() == null) {
            throw new InvalidRequestException("Difficulty level must be selected");
        }

        int count = request.getQuestionCount() > 0 ? request.getQuestionCount() : 5;
        List<Question> selectedQuestions = questionProvider.selectQuestions(request.getRole(), request.getDifficulty(), count);

        String sessionId = UUID.randomUUID().toString();
        InterviewSession session = new InterviewSession(
                sessionId,
                request.getRole(),
                request.getDifficulty(),
                selectedQuestions.size(),
                request.getEngineType()
        );
        session.setSelectedQuestions(selectedQuestions);

        sessionRepository.save(session);
        baselineMetricsService.recordSessionStarted();

        return session;
    }

    public QuestionResponseDTO getCurrentQuestion(String sessionId) {
        InterviewSession session = getSessionOrThrow(sessionId);

        if (session.isFinished()) {
            return new QuestionResponseDTO(sessionId, session.getTotalQuestions(), session.getTotalQuestions(),
                    null, session.getRole(), session.getDifficulty(), "Completed", "Interview has ended.", true);
        }

        Question currentQ = session.getCurrentQuestion();
        if (currentQ == null) {
            throw new ResourceNotFoundException("No current question found for session " + sessionId);
        }

        int currentNumber = session.getCurrentQuestionIndex() + 1;
        return new QuestionResponseDTO(
                sessionId,
                currentNumber,
                session.getTotalQuestions(),
                currentQ.getId(),
                session.getRole(),
                session.getDifficulty(),
                currentQ.getTopic(),
                currentQ.getQuestionText(),
                false
        );
    }

    public EvaluationResponseDTO submitAnswer(String sessionId, SubmitAnswerRequest request) {
        InterviewSession session = getSessionOrThrow(sessionId);

        if (session.isFinished()) {
            throw new InvalidRequestException("Interview session is already completed");
        }

        Question currentQ = session.getCurrentQuestion();
        if (currentQ == null) {
            throw new ResourceNotFoundException("No active question for session: " + sessionId);
        }

        long startEval = System.currentTimeMillis();
        EvaluationResult evaluation;
        if (session.getEngineType() == EvaluationEngineType.RULE_BASED) {
            evaluation = answerEvaluator.evaluate(currentQ, request.getAnswer());
            evaluation.setEngineUsed(EvaluationEngineType.RULE_BASED);
        } else {
            evaluation = advancedAIEvaluator.evaluateAnswer(currentQ, request.getAnswer());
        }
        long evalDuration = System.currentTimeMillis() - startEval;

        // Record in session
        QuestionResponse response = new QuestionResponse(currentQ);
        response.setCandidateAnswer(request.getAnswer());
        response.setInitialEvaluation(evaluation);
        response.setFinalEvaluation(evaluation);
        response.setResponseTimeMs(request.getResponseTimeMs());

        session.getResponses().add(response);

        // Update baseline metrics
        baselineMetricsService.recordAnswerEvaluated(
                evalDuration,
                evaluation.getScore(),
                evaluation.isFollowUpRecommended(),
                request.getResponseTimeMs()
        );

        int currentNumber = session.getCurrentQuestionIndex() + 1;

        if (evaluation.isFollowUpRecommended()) {
            session.setStatus(InterviewSession.SessionStatus.AWAITING_FOLLOW_UP);
            response.setFollowUpQuestion(evaluation.getSuggestedFollowUpQuestion());
            sessionRepository.save(session);

            return new EvaluationResponseDTO(
                    sessionId,
                    currentNumber,
                    session.getTotalQuestions(),
                    evaluation,
                    false,
                    true,
                    evaluation.getSuggestedFollowUpQuestion(),
                    evaluation.getFollowUpReason(),
                    evalDuration
            );
        } else {
            // Move directly to next question index
            session.setCurrentQuestionIndex(session.getCurrentQuestionIndex() + 1);
            if (session.getCurrentQuestionIndex() >= session.getTotalQuestions()) {
                session.setStatus(InterviewSession.SessionStatus.COMPLETED);
                session.setCompletedAt(Instant.now());
                baselineMetricsService.recordSessionCompleted();
            } else {
                session.setStatus(InterviewSession.SessionStatus.ACTIVE);
            }
            sessionRepository.save(session);

            return new EvaluationResponseDTO(
                    sessionId,
                    currentNumber,
                    session.getTotalQuestions(),
                    evaluation,
                    session.isFinished(),
                    false,
                    null,
                    null,
                    evalDuration
            );
        }
    }

    public EvaluationResponseDTO submitFollowUpAnswer(String sessionId, SubmitFollowUpRequest request) {
        InterviewSession session = getSessionOrThrow(sessionId);

        if (session.getStatus() != InterviewSession.SessionStatus.AWAITING_FOLLOW_UP) {
            throw new InvalidRequestException("Current session is not awaiting a follow-up answer");
        }

        QuestionResponse currentResp = session.getCurrentResponse();
        if (currentResp == null) {
            throw new InvalidRequestException("No active question response to attach follow-up");
        }

        long startEval = System.currentTimeMillis();
        EvaluationResult finalEval;
        if (session.getEngineType() == EvaluationEngineType.RULE_BASED) {
            finalEval = answerEvaluator.evaluateWithFollowUp(
                    currentResp.getQuestion(),
                    currentResp.getCandidateAnswer(),
                    currentResp.getFollowUpQuestion(),
                    request.getFollowUpAnswer(),
                    currentResp.getInitialEvaluation()
            );
            finalEval.setEngineUsed(EvaluationEngineType.RULE_BASED);
        } else {
            finalEval = advancedAIEvaluator.evaluateWithFollowUp(
                    currentResp.getQuestion(),
                    currentResp.getCandidateAnswer(),
                    currentResp.getFollowUpQuestion(),
                    request.getFollowUpAnswer(),
                    currentResp.getInitialEvaluation()
            );
        }
        long evalDuration = System.currentTimeMillis() - startEval;

        currentResp.setFollowUpAnswer(request.getFollowUpAnswer());
        currentResp.setFinalEvaluation(finalEval);
        currentResp.setResponseTimeMs(currentResp.getResponseTimeMs() + request.getResponseTimeMs());

        // Advance question pointer
        session.setCurrentQuestionIndex(session.getCurrentQuestionIndex() + 1);
        if (session.getCurrentQuestionIndex() >= session.getTotalQuestions()) {
            session.setStatus(InterviewSession.SessionStatus.COMPLETED);
            session.setCompletedAt(Instant.now());
            baselineMetricsService.recordSessionCompleted();
        } else {
            session.setStatus(InterviewSession.SessionStatus.ACTIVE);
        }
        sessionRepository.save(session);

        int questionNum = session.getCurrentQuestionIndex(); // because we just advanced, questionNum matches the answered question
        return new EvaluationResponseDTO(
                sessionId,
                questionNum,
                session.getTotalQuestions(),
                finalEval,
                session.isFinished(),
                false,
                null,
                null,
                evalDuration
        );
    }

    public InterviewSummary getInterviewSummary(String sessionId) {
        InterviewSession session = getSessionOrThrow(sessionId);

        InterviewSummary summary = new InterviewSummary();
        summary.setSessionId(sessionId);
        summary.setRole(session.getRole());
        summary.setDifficulty(session.getDifficulty());
        summary.setTotalQuestions(session.getTotalQuestions());
        summary.setAnsweredQuestions(session.getResponses().size());

        if (session.getStartedAt() != null) {
            Instant end = session.getCompletedAt() != null ? session.getCompletedAt() : Instant.now();
            summary.setTotalDurationSeconds(Duration.between(session.getStartedAt(), end).getSeconds());
        }

        if (session.getResponses().isEmpty()) {
            summary.setOverallScore(0);
            summary.setOverallRecommendation("No questions were completed during this session.");
            summary.setPerformanceLevel("Incomplete");
            return summary;
        }

        int scoreSum = 0;
        int relevanceSum = 0;
        int keywordSum = 0;
        int completenessSum = 0;
        int lengthClaritySum = 0;
        long totalResponseTimeMs = 0;

        List<InterviewSummary.QuestionSummaryItem> items = new ArrayList<>();
        Set<String> strengthsAgg = new LinkedHashSet<>();
        Set<String> improvementsAgg = new LinkedHashSet<>();

        for (int i = 0; i < session.getResponses().size(); i++) {
            QuestionResponse resp = session.getResponses().get(i);
            EvaluationResult eval = resp.getFinalEvaluation() != null ? resp.getFinalEvaluation() : resp.getInitialEvaluation();

            scoreSum += eval.getScore();
            relevanceSum += eval.getRelevanceScore();
            keywordSum += eval.getKeywordScore();
            completenessSum += eval.getCompletenessScore();
            lengthClaritySum += (eval.getLengthScore() + eval.getClarityScore());
            totalResponseTimeMs += resp.getResponseTimeMs();

            InterviewSummary.QuestionSummaryItem item = new InterviewSummary.QuestionSummaryItem();
            item.setQuestionNumber(i + 1);
            item.setQuestionText(resp.getQuestion().getQuestionText());
            item.setTopic(resp.getQuestion().getTopic());
            item.setCandidateAnswer(resp.getCandidateAnswer());
            item.setHadFollowUp(resp.getFollowUpQuestion() != null);
            item.setFollowUpQuestion(resp.getFollowUpQuestion());
            item.setFollowUpAnswer(resp.getFollowUpAnswer());
            item.setScore(eval.getScore());
            item.setMatchedKeywords(eval.getMatchedKeywords());
            item.setStrengths(eval.getStrengths());
            item.setImprovements(eval.getAreasForImprovement());
            item.setExplanation(eval.getExplanation());
            item.setConfidence(eval.getConfidence());
            item.setOffTopicDetected(eval.isOffTopicDetected());
            item.setHumanOverridden(eval.isHumanOverridden());

            strengthsAgg.addAll(eval.getStrengths());
            improvementsAgg.addAll(eval.getAreasForImprovement());
            items.add(item);
        }

        int totalAnswered = session.getResponses().size();
        int overallScore = Math.round((float) scoreSum / totalAnswered);
        summary.setOverallScore(overallScore);
        summary.setEngineType(session.getEngineType());
        summary.setAvgRelevance((double) relevanceSum / totalAnswered);
        summary.setAvgKeywordMatch((double) keywordSum / totalAnswered);
        summary.setAvgCompleteness((double) completenessSum / totalAnswered);
        summary.setAvgLengthClarity((double) lengthClaritySum / totalAnswered);
        summary.setAvgResponseTimeSeconds((double) totalResponseTimeMs / (totalAnswered * 1000.0));

        double avgConf = items.stream().mapToDouble(InterviewSummary.QuestionSummaryItem::getConfidence).average().orElse(0.95);
        summary.setAvgConfidence(Math.round(avgConf * 100.0) / 100.0);
        summary.setHasHumanOverride(items.stream().anyMatch(InterviewSummary.QuestionSummaryItem::isHumanOverridden));

        summary.setQuestionBreakdown(items);
        summary.setKeyStrengths(new ArrayList<>(strengthsAgg).subList(0, Math.min(4, strengthsAgg.size())));
        summary.setKeyImprovements(new ArrayList<>(improvementsAgg).subList(0, Math.min(4, improvementsAgg.size())));

        // Performance Classification and Recommendations
        if (overallScore >= 85) {
            summary.setPerformanceLevel("Excellent / Strong Hire");
            summary.setOverallRecommendation("Demonstrates strong conceptual grasp and clear technical articulation. Ready for real-world interviews at the " + session.getDifficulty().getDisplayName() + " tier.");
        } else if (overallScore >= 70) {
            summary.setPerformanceLevel("Good / Competent");
            summary.setOverallRecommendation("Good solid foundation. Enhance answers by citing specific architecture patterns, production trade-offs, and standard library mechanisms.");
        } else if (overallScore >= 50) {
            summary.setPerformanceLevel("Fair / Needs Improvement");
            summary.setOverallRecommendation("Candidate understands basic terminology but frequently leaves answers incomplete or missing core architectural details. Focus on deep-dive topic reviews.");
        } else {
            summary.setPerformanceLevel("Developing / Needs Preparation");
            summary.setOverallRecommendation("Significant preparation needed in " + session.getRole().getDisplayName() + " fundamentals. Review core documentation and practice explaining technical concepts aloud.");
        }

        return summary;
    }

    public InterviewSummary applyHumanOverride(String sessionId, HumanOverrideRequest overrideReq) {
        InterviewSession session = getSessionOrThrow(sessionId);
        int qIdx = overrideReq.getQuestionNumber() - 1;
        if (qIdx < 0 || qIdx >= session.getResponses().size()) {
            throw new InvalidRequestException("Invalid question number: " + overrideReq.getQuestionNumber());
        }

        QuestionResponse resp = session.getResponses().get(qIdx);
        EvaluationResult eval = resp.getFinalEvaluation() != null ? resp.getFinalEvaluation() : resp.getInitialEvaluation();

        eval.setScore(overrideReq.getAdjustedScore());
        eval.setHumanOverridden(true);
        eval.setHumanOverrideNotes(overrideReq.getOverrideNotes());
        eval.getStrengths().add("Human Interviewer Adjusted: " + overrideReq.getOverrideNotes());

        sessionRepository.save(session);
        return getInterviewSummary(sessionId);
    }

    public InterviewSession getSessionOrThrow(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new InvalidRequestException("Session ID cannot be empty");
        }
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview session not found for ID: " + sessionId));
    }
}
