package com.interview.service;

import com.interview.model.DifficultyLevel;
import com.interview.model.JobRole;
import com.interview.model.Question;

import java.util.List;

/**
 * Strategy interface for providing interview questions.
 * In the first 35%, this is backed by the static QuestionRepository.
 * In Phase 2/3, an AIQuestionProvider can implement this interface to inject dynamic LLM questions.
 */
public interface QuestionProvider {
    List<Question> selectQuestions(JobRole role, DifficultyLevel difficulty, int count);
}
