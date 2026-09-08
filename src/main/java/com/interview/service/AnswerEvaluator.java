package com.interview.service;

import com.interview.model.EvaluationResult;
import com.interview.model.Question;

/**
 * Strategy interface for evaluating candidate answers.
 * In the first 35%, this is backed by the RuleBasedAnswerEvaluator.
 * In Phase 2/3, an LLMAnswerEvaluator will implement this same interface.
 */
public interface AnswerEvaluator {
    EvaluationResult evaluate(Question question, String answer);
    EvaluationResult evaluateWithFollowUp(Question question, String initialAnswer, String followUpQuestion, String followUpAnswer, EvaluationResult initialEval);
}
