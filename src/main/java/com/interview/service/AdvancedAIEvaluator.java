package com.interview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.model.EvaluationEngineType;
import com.interview.model.EvaluationResult;
import com.interview.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Intelligent AI Answer Evaluator that provides:
 * 1. Semantic concept mapping and lexical overlap calculation.
 * 2. Adaptive follow-up probe generation tailored specifically to what the candidate missed or left vague.
 * 3. Explainable score breakdown with evidence extraction and calibrated confidence estimation.
 * 4. Off-topic, empty, and adversarial prompt injection guardrails.
 * 5. Optional live integration with Google Gemini API when GEMINI_API_KEY is configured,
 *    with instant, resilient fallback to the internal semantic rubric engine.
 */
@Service
public class AdvancedAIEvaluator {

    private static final Logger log = LoggerFactory.getLogger(AdvancedAIEvaluator.class);
    private static final Pattern TOKEN_SPLIT = Pattern.compile("[\\s,;:.!?()\"\\[\\]{}]+");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-1.5-flash}")
    private String geminiModel;

    public EvaluationResult evaluateAnswer(Question question, String answer) {
        // Guardrail: Empty or blank inputs
        if (answer == null || answer.trim().isEmpty()) {
            return buildEmptyAnswerResult(question);
        }

        String cleaned = answer.trim();

        // Guardrail: Adversarial prompt injection or nonsense
        if (detectPromptInjection(cleaned)) {
            return buildSecurityAlertResult(question, cleaned);
        }

        // Try Gemini LLM if configured
        if (isGeminiConfigured()) {
            try {
                EvaluationResult geminiResult = callGeminiEvaluation(question, cleaned, null, null);
                if (geminiResult != null) {
                    return geminiResult;
                }
            } catch (Exception ex) {
                log.warn("Gemini evaluation invocation failed, gracefully falling back to AI Hybrid semantic engine: {}", ex.getMessage());
            }
        }

        // Fallback / High-speed Deterministic AI Hybrid Semantic Evaluator
        return evaluateSemantically(question, cleaned);
    }

    public EvaluationResult evaluateWithFollowUp(Question question, String initialAnswer, String followUpQ, String followUpAnswer, EvaluationResult initialEval) {
        if (followUpAnswer == null || followUpAnswer.trim().isEmpty()) {
            // No additional substance provided in follow-up
            EvaluationResult copy = evaluateSemantically(question, initialAnswer);
            copy.setFollowUpRecommended(false);
            copy.getAreasForImprovement().add("Follow-up response was empty; no additional clarification points awarded.");
            copy.setScore(Math.min(initialEval.getScore(), copy.getScore()));
            return copy;
        }

        String combinedAnswer = (initialAnswer != null ? initialAnswer : "") + "\n\nFollow-up Clarification: " + followUpAnswer.trim();

        if (isGeminiConfigured()) {
            try {
                EvaluationResult geminiResult = callGeminiEvaluation(question, combinedAnswer, followUpQ, followUpAnswer);
                if (geminiResult != null) {
                    geminiResult.setFollowUpRecommended(false);
                    return geminiResult;
                }
            } catch (Exception ex) {
                log.warn("Gemini follow-up evaluation failed, falling back: {}", ex.getMessage());
            }
        }

        EvaluationResult combinedEval = evaluateSemantically(question, combinedAnswer);
        combinedEval.setFollowUpRecommended(false);
        combinedEval.setFollowUpReason(null);
        combinedEval.setSuggestedFollowUpQuestion(null);

        // Clarification recovery: Award boost for meaningful follow-up clarification
        int baseFollowUpBoost = (countWords(followUpAnswer) >= 6) ? 18 : 8;
        int newScore = Math.max(initialEval.getScore() + baseFollowUpBoost, combinedEval.getScore());
        combinedEval.setScore(Math.min(100, Math.max(55, newScore)));
        combinedEval.getStrengths().add("Successfully clarified and addressed the probing follow-up question.");

        return combinedEval;
    }

    /**
     * Robust offline semantic & concept matching engine.
     * Computes multi-dimensional rubric scores:
     * - Relevance (0-25)
     * - Keyword & Terminology (0-25)
     * - Conceptual Completeness (0-25)
     * - Length & Structure (0-15)
     * - Communication Clarity (0-10)
     */
    public EvaluationResult evaluateSemantically(Question question, String answer) {
        EvaluationResult result = new EvaluationResult();
        result.setEngineUsed(EvaluationEngineType.AI_HYBRID);

        String lowerAns = answer.toLowerCase(Locale.ROOT);
        Set<String> answerTokens = tokenize(lowerAns);

        // 1. Keyword Extraction & Density
        List<String> matchedKws = new ArrayList<>();
        List<String> missingKws = new ArrayList<>();

        if (question.getRequiredKeywords() != null) {
            for (String kw : question.getRequiredKeywords()) {
                String kwLower = kw.toLowerCase(Locale.ROOT).trim();
                if (containsConcept(lowerAns, answerTokens, kwLower)) {
                    matchedKws.add(kw);
                } else {
                    missingKws.add(kw);
                }
            }
        }
        result.setMatchedKeywords(matchedKws);
        result.setMissingKeywords(missingKws);

        double kwRatio = (question.getRequiredKeywords() != null && !question.getRequiredKeywords().isEmpty())
                ? (double) matchedKws.size() / question.getRequiredKeywords().size()
                : 1.0;
        int keywordScore = (int) Math.round(kwRatio * 25.0);
        result.setKeywordScore(Math.min(25, Math.max(0, keywordScore)));

        // 2. Expected Concepts Check
        List<String> coveredConcepts = new ArrayList<>();
        List<String> uncoveredConcepts = new ArrayList<>();
        if (question.getExpectedConcepts() != null) {
            for (String concept : question.getExpectedConcepts()) {
                String cLower = concept.toLowerCase(Locale.ROOT);
                if (containsConcept(lowerAns, answerTokens, cLower)) {
                    coveredConcepts.add(concept);
                } else {
                    uncoveredConcepts.add(concept);
                }
            }
        }
        double conceptRatio = (question.getExpectedConcepts() != null && !question.getExpectedConcepts().isEmpty())
                ? (double) coveredConcepts.size() / question.getExpectedConcepts().size()
                : kwRatio;
        int completenessScore = (int) Math.round(conceptRatio * 25.0);
        result.setCompletenessScore(Math.min(25, Math.max(0, completenessScore)));

        // 3. Relevance & Semantic Proximity
        int wordCount = countWords(answer);
        boolean isOffTopic = (wordCount > 15 && matchedKws.isEmpty() && coveredConcepts.isEmpty());
        result.setOffTopicDetected(isOffTopic);

        int relevanceScore;
        if (isOffTopic) {
            relevanceScore = 3;
            result.getAreasForImprovement().add("Response appears off-topic or lacks key domain context for this specific question.");
        } else {
            double relWeight = (kwRatio * 0.6) + (conceptRatio * 0.4);
            relevanceScore = (int) Math.round(relWeight * 25.0);
            if (wordCount >= 20 && relevanceScore < 10) {
                relevanceScore = 12; // Base credit for effortful contextual explanation
            }
        }
        result.setRelevanceScore(Math.min(25, Math.max(0, relevanceScore)));

        // 4. Length & Detail Depth (0-15)
        int lengthScore;
        if (wordCount < 10) {
            lengthScore = 3;
            result.getAreasForImprovement().add("Response is too brief. Elaborate further with architecture details and practical examples.");
        } else if (wordCount < 25) {
            lengthScore = 8;
        } else if (wordCount <= 120) {
            lengthScore = 15; // Optimal conciseness
        } else if (wordCount <= 250) {
            lengthScore = 13;
        } else {
            lengthScore = 10; // Overly verbose
            result.getAreasForImprovement().add("Answer is somewhat verbose. Aim for crisp, focused technical summaries.");
        }
        result.setLengthScore(lengthScore);

        // 5. Clarity & Structural Flow (0-10)
        int clarityScore = 8;
        if (answer.contains("\n") || answer.contains(";") || answer.contains("-") || answer.contains("1.") || answer.contains(":")) {
            clarityScore += 2; // Formatting credit
        }
        if (wordCount < 8) {
            clarityScore = 3;
        }
        result.setClarityScore(Math.min(10, Math.max(1, clarityScore)));

        // Overall Score Calculation
        int totalScore = result.getRelevanceScore() + result.getKeywordScore() +
                result.getCompletenessScore() + result.getLengthScore() + result.getClarityScore();
        result.setScore(Math.min(100, Math.max(0, totalScore)));

        // Calibrated Confidence
        double confidence = 0.85 + (0.10 * Math.min(1.0, wordCount / 50.0)) - (isOffTopic ? 0.20 : 0.0);
        result.setConfidence(Math.round(confidence * 100.0) / 100.0);

        // Strengths & Improvements Generation
        if (!matchedKws.isEmpty()) {
            result.getStrengths().add("Accurately integrated key terminology: " + String.join(", ", matchedKws.subList(0, Math.min(3, matchedKws.size()))));
        }
        if (completenessScore >= 18) {
            result.getStrengths().add("Demonstrated thorough conceptual understanding of " + question.getTopic() + ".");
        }
        if (wordCount >= 30 && wordCount <= 120) {
            result.getStrengths().add("Well-calibrated length and structured technical phrasing.");
        }

        if (!missingKws.isEmpty()) {
            result.getAreasForImprovement().add("Consider explicitly mentioning: " + String.join(", ", missingKws.subList(0, Math.min(3, missingKws.size()))));
        }
        if (completenessScore < 15 && question.getSampleGoodAnswer() != null) {
            result.getAreasForImprovement().add("Include core trade-offs or implementation mechanisms as highlighted in the benchmark standard.");
        }

        // Evidence Points
        List<String> evidence = new ArrayList<>();
        evidence.add(String.format("Keyword alignment: %d/%d identified (%.0f%%)", matchedKws.size(),
                question.getRequiredKeywords() != null ? question.getRequiredKeywords().size() : 0, kwRatio * 100));
        evidence.add(String.format("Response density: %d words with %s depth", wordCount, wordCount >= 30 ? "sufficient" : "limited"));
        if (isOffTopic) {
            evidence.add("Off-topic risk flag triggered: low lexical connection with domain rubric.");
        }
        result.setKeyEvidencePoints(evidence);

        // Explanation rationale
        result.setExplanation(String.format(
                "Evaluated by AI Hybrid Engine. Score: %d/100 (Relevance: %d/25, Keywords: %d/25, Completeness: %d/25, Structure: %d/25). Confidence: %.0f%%.",
                totalScore, result.getRelevanceScore(), result.getKeywordScore(), result.getCompletenessScore(),
                result.getLengthScore() + result.getClarityScore(), result.getConfidence() * 100
        ));

        // Adaptive Follow-up Probing Logic
        // Trigger follow-up if:
        // a) candidate gave a brief/incomplete answer (score 35-72)
        // b) missing specific follow-up trigger keywords
        boolean shouldFollowUp = false;
        String followUpReason = "";

        if (totalScore >= 30 && totalScore <= 74 && question.getFollowUpQuestion() != null) {
            shouldFollowUp = true;
            followUpReason = "Answer shows foundational knowledge but lacks deep elaboration or distinction between key mechanisms.";
        } else if (question.getFollowUpTriggerKeywords() != null && !question.getFollowUpTriggerKeywords().isEmpty()) {
            boolean missingTriggers = question.getFollowUpTriggerKeywords().stream()
                    .anyMatch(trigger -> !lowerAns.contains(trigger.toLowerCase(Locale.ROOT)));
            if (missingTriggers && totalScore < 85 && question.getFollowUpQuestion() != null) {
                shouldFollowUp = true;
                followUpReason = "Key differentiating concept was not fully addressed in the initial answer.";
            }
        }

        result.setFollowUpRecommended(shouldFollowUp);
        result.setFollowUpReason(shouldFollowUp ? followUpReason : null);
        result.setSuggestedFollowUpQuestion(shouldFollowUp ? question.getFollowUpQuestion() : null);

        return result;
    }

    private EvaluationResult buildEmptyAnswerResult(Question question) {
        EvaluationResult res = new EvaluationResult();
        res.setScore(0);
        res.setRelevanceScore(0);
        res.setKeywordScore(0);
        res.setCompletenessScore(0);
        res.setLengthScore(0);
        res.setClarityScore(0);
        res.setConfidence(1.0);
        res.setEngineUsed(EvaluationEngineType.AI_HYBRID);
        res.setMissingKeywords(question.getRequiredKeywords() != null ? new ArrayList<>(question.getRequiredKeywords()) : new ArrayList<>());
        res.getAreasForImprovement().add("No answer provided. Please articulate your thoughts clearly.");
        res.setExplanation("The answer was completely empty. Zero points awarded.");
        res.setFollowUpRecommended(true);
        res.setFollowUpReason("Empty response detected");
        res.setSuggestedFollowUpQuestion(question.getFollowUpQuestion());
        res.getKeyEvidencePoints().add("Empty input detected by ingestion pipeline.");
        return res;
    }

    private EvaluationResult buildSecurityAlertResult(Question question, String input) {
        EvaluationResult res = new EvaluationResult();
        res.setScore(0);
        res.setRelevanceScore(0);
        res.setKeywordScore(0);
        res.setCompletenessScore(0);
        res.setLengthScore(0);
        res.setClarityScore(0);
        res.setConfidence(0.99);
        res.setEngineUsed(EvaluationEngineType.AI_HYBRID);
        res.setOffTopicDetected(true);
        res.getAreasForImprovement().add("Response was flagged by security filter (unrelated prompt instruction or script tag).");
        res.setExplanation("Input violates submission guidelines. Please provide genuine technical interview responses.");
        res.setFollowUpRecommended(false);
        res.getKeyEvidencePoints().add("Security filter: prompt injection pattern detected.");
        return res;
    }

    private boolean detectPromptInjection(String text) {
        String lower = text.toLowerCase(Locale.ROOT);
        return lower.contains("ignore previous instructions") ||
                lower.contains("disregard all prior") ||
                lower.contains("<script>") ||
                lower.contains("system prompt:") ||
                lower.contains("you are now a");
    }

    private boolean isGeminiConfigured() {
        return geminiApiKey != null && !geminiApiKey.isBlank() && !geminiApiKey.equalsIgnoreCase("none");
    }

    /**
     * Optional live Gemini API call for full generative evaluation.
     */
    private EvaluationResult callGeminiEvaluation(Question question, String answer, String followUpQ, String followUpAns) {
        try {
            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" + geminiModel + ":generateContent?key=" + geminiApiKey;

            String prompt = String.format("""
                You are a senior technical interviewer. Evaluate the candidate's answer for the following question:
                Role: %s
                Difficulty: %s
                Question: %s
                Required Keywords: %s
                Benchmark Reference: %s
                Candidate Answer: %s
                %s
                Return a STRICT JSON object with these keys:
                - score (0 to 100)
                - relevanceScore (0 to 25)
                - keywordScore (0 to 25)
                - completenessScore (0 to 25)
                - lengthScore (0 to 15)
                - clarityScore (0 to 10)
                - strengths (array of strings)
                - areasForImprovement (array of strings)
                - explanation (string)
                - followUpRecommended (boolean)
                - followUpReason (string or null)
                - suggestedFollowUpQuestion (string or null)
                """,
                    question.getRole(), question.getDifficulty(), question.getQuestionText(),
                    question.getRequiredKeywords(), question.getSampleGoodAnswer(), answer,
                    (followUpQ != null ? "Follow-Up Asked: " + followUpQ + "\nCandidate Follow-Up Answer: " + followUpAns : "")
            );

            Map<String, Object> reqBody = Map.of(
                    "contents", List.of(Map.of(
                            "parts", List.of(Map.of("text", prompt))
                    )),
                    "generationConfig", Map.of(
                            "responseMimeType", "application/json",
                            "temperature", 0.2
                    )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(reqBody)))
                    .timeout(Duration.ofSeconds(4))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                String jsonText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
                EvaluationResult res = objectMapper.readValue(jsonText, EvaluationResult.class);
                res.setEngineUsed(EvaluationEngineType.AI_GEMINI);
                res.setConfidence(0.98);
                return res;
            }
        } catch (Exception e) {
            log.debug("Gemini API call skipped or timed out: {}", e.getMessage());
        }
        return null;
    }

    private Set<String> tokenize(String text) {
        String[] parts = TOKEN_SPLIT.split(text);
        Set<String> set = new HashSet<>();
        for (String p : parts) {
            if (!p.isBlank()) set.add(p);
        }
        return set;
    }

    private boolean containsConcept(String fullText, Set<String> tokens, String concept) {
        if (fullText.contains(concept)) {
            return true;
        }
        // Sub-words check on non-stopwords
        String[] conceptTokens = TOKEN_SPLIT.split(concept);
        int meaningfulCount = 0;
        int matched = 0;
        for (String ct : conceptTokens) {
            if (ct.length() <= 3) continue; // skip small stop words like 'the', 'and', 'for'
            meaningfulCount++;
            if (tokens.contains(ct) || fullText.contains(ct)) {
                matched++;
            }
        }
        if (meaningfulCount == 0) return true;
        return ((double) matched / meaningfulCount) >= 0.40;
    }

    private int countWords(String text) {
        if (text == null || text.isBlank()) return 0;
        String[] words = text.trim().split("\\s+");
        return words.length;
    }
}
