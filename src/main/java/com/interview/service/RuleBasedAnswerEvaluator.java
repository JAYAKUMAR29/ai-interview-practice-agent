package com.interview.service;

import com.interview.model.EvaluationResult;
import com.interview.model.Question;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class RuleBasedAnswerEvaluator implements AnswerEvaluator {

    private static final Pattern WORD_BOUNDARY_PATTERN = Pattern.compile("\\W+");
    private static final Set<String> STOP_WORDS = Set.of(
            "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for", "of", "with",
            "is", "are", "was", "were", "it", "this", "that", "these", "those", "can", "be", "as"
    );

    @Override
    public EvaluationResult evaluate(Question question, String answer) {
        EvaluationResult result = new EvaluationResult();

        // 1. Check for empty or whitespace-only answers
        if (answer == null || answer.trim().isEmpty()) {
            result.setScore(0);
            result.setRelevanceScore(0);
            result.setKeywordScore(0);
            result.setCompletenessScore(0);
            result.setLengthScore(0);
            result.setClarityScore(0);
            result.setMissingKeywords(new ArrayList<>(question.getRequiredKeywords()));
            result.getAreasForImprovement().add("No answer provided. Please provide a clear, detailed response addressing the question.");
            result.setExplanation("The answer was completely blank. A full explanation addressing core concepts is required.");
            result.setFollowUpRecommended(true);
            result.setFollowUpReason("Empty response detected");
            result.setSuggestedFollowUpQuestion(question.getFollowUpQuestion());
            return result;
        }

        String cleanedAnswer = answer.trim();
        String normalizedAnswer = cleanedAnswer.toLowerCase();
        String[] words = WORD_BOUNDARY_PATTERN.split(normalizedAnswer);
        int wordCount = 0;
        for (String w : words) {
            if (!w.isBlank()) wordCount++;
        }

        // 2. Keyword Matching (0 - 25 points)
        List<String> matchedKeywords = new ArrayList<>();
        List<String> missingKeywords = new ArrayList<>();

        if (question.getRequiredKeywords() != null) {
            for (String kw : question.getRequiredKeywords()) {
                String kwLower = kw.toLowerCase().trim();
                // Check if keyword is substring or individual token match
                if (normalizedAnswer.contains(kwLower)) {
                    matchedKeywords.add(kw);
                } else {
                    missingKeywords.add(kw);
                }
            }
        }

        int totalRequired = question.getRequiredKeywords() == null ? 1 : Math.max(1, question.getRequiredKeywords().size());
        double keywordRatio = (double) matchedKeywords.size() / totalRequired;
        int keywordScore = (int) Math.round(keywordRatio * 25.0);
        result.setKeywordScore(keywordScore);
        result.setMatchedKeywords(matchedKeywords);
        result.setMissingKeywords(missingKeywords);

        // 3. Relevance Scoring (0 - 25 points)
        // Checks topic and question domain terms
        int relevanceScore = calculateRelevance(question, normalizedAnswer, words);
        result.setRelevanceScore(relevanceScore);

        // 4. Completeness Scoring (0 - 25 points)
        // Checks expected concepts
        int completenessScore = calculateCompleteness(question, normalizedAnswer, matchedKeywords.size(), totalRequired);
        result.setCompletenessScore(completenessScore);

        // 5. Length & Depth Scoring (0 - 15 points)
        int lengthScore = calculateLengthScore(wordCount);
        result.setLengthScore(lengthScore);

        // 6. Clarity & Structure Scoring (0 - 10 points)
        int clarityScore = calculateClarityScore(cleanedAnswer, wordCount);
        result.setClarityScore(clarityScore);

        // Total Score
        int totalScore = relevanceScore + keywordScore + completenessScore + lengthScore + clarityScore;
        totalScore = Math.min(100, Math.max(0, totalScore));

        // Penalty for extremely irrelevant gibberish / nonsensical text
        if (relevanceScore <= 5 && keywordScore <= 5) {
            totalScore = Math.min(totalScore, 20);
        }

        result.setScore(totalScore);

        // Build Strengths, Improvements, and Explanation
        generateFeedbackDetails(question, result, wordCount, keywordRatio);

        // Determine if follow-up question is recommended
        boolean needsFollowUp = determineFollowUpNeed(question, result, normalizedAnswer, wordCount);
        result.setFollowUpRecommended(needsFollowUp);
        if (needsFollowUp) {
            result.setSuggestedFollowUpQuestion(question.getFollowUpQuestion());
        }

        return result;
    }

    @Override
    public EvaluationResult evaluateWithFollowUp(Question question, String initialAnswer, String followUpQuestion, String followUpAnswer, EvaluationResult initialEval) {
        // Evaluate follow up combined with initial answer
        if (followUpAnswer == null || followUpAnswer.trim().isEmpty()) {
            return initialEval; // Keep initial evaluation if follow up was skipped or empty
        }

        String combinedAnswer = initialAnswer + " Additionally, regarding " + followUpQuestion + ": " + followUpAnswer;
        EvaluationResult reEvaluated = evaluate(question, combinedAnswer);

        // Follow-up adjustment bonus for clarifying incomplete thoughts (up to +15 pts improvement if valid)
        int scoreDelta = reEvaluated.getScore() - initialEval.getScore();
        if (scoreDelta > 0) {
            reEvaluated.getStrengths().add(0, "Successfully addressed the follow-up question with clarifying details (+ " + scoreDelta + " pts).");
        } else {
            // Keep at least the initial score
            reEvaluated.setScore(Math.max(reEvaluated.getScore(), initialEval.getScore()));
        }

        // Once follow-up is provided, no further follow-up is triggered for this single question
        reEvaluated.setFollowUpRecommended(false);
        reEvaluated.setFollowUpReason(null);
        reEvaluated.setSuggestedFollowUpQuestion(null);

        return reEvaluated;
    }

    private int calculateRelevance(Question question, String normalizedAnswer, String[] words) {
        String topicLower = question.getTopic() != null ? question.getTopic().toLowerCase() : "";
        String questionLower = question.getQuestionText().toLowerCase();

        // Extract distinctive content words from question and topic
        Set<String> promptTerms = new HashSet<>();
        for (String term : WORD_BOUNDARY_PATTERN.split(questionLower + " " + topicLower)) {
            if (term.length() > 3 && !STOP_WORDS.contains(term)) {
                promptTerms.add(term);
            }
        }

        if (promptTerms.isEmpty()) {
            return 15;
        }

        int matches = 0;
        for (String term : promptTerms) {
            if (normalizedAnswer.contains(term)) {
                matches++;
            }
        }

        double matchFraction = (double) matches / promptTerms.size();
        if (matchFraction >= 0.5) {
            return 25;
        } else if (matchFraction >= 0.3) {
            return 20;
        } else if (matchFraction >= 0.15) {
            return 14;
        } else if (matchFraction > 0) {
            return 8;
        } else {
            return 3; // Barely relevant or off-topic
        }
    }

    private int calculateCompleteness(Question question, String normalizedAnswer, int matchedKwCount, int totalKwCount) {
        int score = 0;
        // Check concept coverage
        if (question.getExpectedConcepts() != null && !question.getExpectedConcepts().isEmpty()) {
            int conceptMatches = 0;
            for (String concept : question.getExpectedConcepts()) {
                String[] conceptWords = WORD_BOUNDARY_PATTERN.split(concept.toLowerCase());
                int matchedConceptWords = 0;
                int totalConceptWords = 0;
                for (String cw : conceptWords) {
                    if (cw.length() > 3 && !STOP_WORDS.contains(cw)) {
                        totalConceptWords++;
                        if (normalizedAnswer.contains(cw)) {
                            matchedConceptWords++;
                        }
                    }
                }
                if (totalConceptWords > 0 && ((double) matchedConceptWords / totalConceptWords) >= 0.5) {
                    conceptMatches++;
                }
            }
            double conceptRatio = (double) conceptMatches / question.getExpectedConcepts().size();
            score = (int) Math.round(conceptRatio * 25.0);
        } else {
            // Fallback to keyword coverage
            double kwRatio = (double) matchedKwCount / Math.max(1, totalKwCount);
            score = (int) Math.round(kwRatio * 25.0);
        }
        return Math.min(25, score);
    }

    private int calculateLengthScore(int wordCount) {
        // Target answer: 35 - 180 words
        if (wordCount < 10) {
            return 2; // Very short / one-liner
        } else if (wordCount < 25) {
            return 7; // Minimal
        } else if (wordCount < 40) {
            return 11; // Acceptable brevity
        } else if (wordCount <= 220) {
            return 15; // Optimal technical depth
        } else {
            return 13; // Excessively long / verbose
        }
    }

    private int calculateClarityScore(String rawAnswer, int wordCount) {
        int clarity = 5; // Base clarity

        // Checks for punctuation and sentence formation
        boolean hasPunctuation = rawAnswer.contains(".") || rawAnswer.contains(";") || rawAnswer.contains("!");
        if (hasPunctuation && wordCount >= 15) {
            clarity += 2;
        }

        // Checks for structural formatting (bullet points, numbering, paragraphs)
        if (rawAnswer.contains("\n") || rawAnswer.contains("- ") || rawAnswer.contains("1.") || rawAnswer.contains("•")) {
            clarity += 2;
        }

        // Checks for meaningful average word length (avoiding keyboard spam like "asdasdsad")
        double avgWordLen = rawAnswer.length() / (double) Math.max(1, wordCount);
        if (avgWordLen >= 3.5 && avgWordLen <= 9.0) {
            clarity += 1;
        }

        return Math.min(10, clarity);
    }

    private void generateFeedbackDetails(Question question, EvaluationResult result, int wordCount, double keywordRatio) {
        // Strengths
        if (result.getScore() >= 80) {
            result.getStrengths().add("Thorough and comprehensive technical explanation with strong terminology.");
        } else if (result.getScore() >= 60) {
            result.getStrengths().add("Good baseline understanding of core topic fundamentals.");
        }

        if (keywordRatio >= 0.6) {
            result.getStrengths().add("Accurately mentioned essential domain keywords: " + String.join(", ", result.getMatchedKeywords()) + ".");
        } else if (!result.getMatchedKeywords().isEmpty()) {
            result.getStrengths().add("Identified key concepts: " + String.join(", ", result.getMatchedKeywords()) + ".");
        }

        if (wordCount >= 35 && wordCount <= 200) {
            result.getStrengths().add("Well-proportioned response depth and structured explanation.");
        }

        if (result.getStrengths().isEmpty()) {
            result.getStrengths().add("Attempted to address the question prompt.");
        }

        // Areas for Improvement
        if (wordCount < 25) {
            result.getAreasForImprovement().add("Response is too brief (" + wordCount + " words). Elaborate with concrete definitions and architectural context.");
        }

        if (!result.getMissingKeywords().isEmpty()) {
            result.getAreasForImprovement().add("Consider incorporating key concepts: " + String.join(", ", result.getMissingKeywords()) + ".");
        }

        if (result.getRelevanceScore() < 12) {
            result.getAreasForImprovement().add("Answer diverged from the specific question scenario. Focus closely on: " + question.getTopic() + ".");
        }

        if (result.getCompletenessScore() < 12) {
            result.getAreasForImprovement().add("Incorporate practical trade-offs, real-world examples, or underlying execution mechanics.");
        }

        // Explanation text
        StringBuilder explanation = new StringBuilder();
        explanation.append("Your response scored ").append(result.getScore()).append("/100 based on ");
        explanation.append("Relevance (").append(result.getRelevanceScore()).append("/25), ");
        explanation.append("Keyword Match (").append(result.getKeywordScore()).append("/25), ");
        explanation.append("Completeness (").append(result.getCompletenessScore()).append("/25), ");
        explanation.append("Length & Depth (").append(result.getLengthScore()).append("/15), and ");
        explanation.append("Clarity (").append(result.getClarityScore()).append("/10). ");

        if (result.getScore() >= 80) {
            explanation.append("This is a solid, interview-ready answer demonstrating proficient knowledge.");
        } else if (result.getScore() >= 60) {
            explanation.append("Good foundational answer, but missing deeper technical nuance and specific keywords.");
        } else {
            explanation.append("The response requires significant elaboration and alignment with standard technical definitions.");
        }
        result.setExplanation(explanation.toString());
    }

    private boolean determineFollowUpNeed(Question question, EvaluationResult result, String normalizedAnswer, int wordCount) {
        // Trigger follow-up if:
        // 1. Candidate answer is quite short (< 25 words) but not empty
        // 2. Score is between 30 and 70 (partially complete, candidate has some idea but needs probing)
        // 3. Or question has specific followUpTriggerKeywords and answer contains partial clues
        if (wordCount >= 3 && wordCount < 25) {
            result.setFollowUpReason("Answer is concise; follow-up requested to gauge deeper conceptual understanding.");
            return true;
        }

        if (result.getScore() >= 30 && result.getScore() < 70 && question.getFollowUpQuestion() != null) {
            result.setFollowUpReason("Initial answer touched upon fundamentals but left key trade-offs unaddressed.");
            return true;
        }

        // Check if any specific follow up trigger keywords were mentioned without full explanation
        if (question.getFollowUpTriggerKeywords() != null) {
            for (String trigger : question.getFollowUpTriggerKeywords()) {
                if (normalizedAnswer.contains(trigger.toLowerCase()) && result.getScore() < 75) {
                    result.setFollowUpReason("You mentioned '" + trigger + "'; follow-up requested to explore this critical distinction.");
                    return true;
                }
            }
        }

        return false;
    }
}
