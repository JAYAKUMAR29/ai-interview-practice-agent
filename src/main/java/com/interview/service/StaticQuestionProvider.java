package com.interview.service;

import com.interview.exception.InvalidRequestException;
import com.interview.model.DifficultyLevel;
import com.interview.model.JobRole;
import com.interview.model.Question;
import com.interview.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StaticQuestionProvider implements QuestionProvider {

    private final QuestionRepository questionRepository;

    public StaticQuestionProvider(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> selectQuestions(JobRole role, DifficultyLevel difficulty, int count) {
        if (role == null) {
            throw new InvalidRequestException("Job role must be specified");
        }
        if (difficulty == null) {
            throw new InvalidRequestException("Difficulty level must be specified");
        }
        if (count <= 0) {
            throw new InvalidRequestException("Question count must be greater than zero");
        }

        List<Question> matching = questionRepository.findByRoleAndDifficulty(role, difficulty);
        if (matching.isEmpty()) {
            throw new InvalidRequestException("No questions available for role " + role + " and difficulty " + difficulty);
        }

        List<Question> shuffled = new ArrayList<>(matching);
        Collections.shuffle(shuffled);

        // If requested count exceeds available pool, cycle or take what is available
        if (count <= shuffled.size()) {
            return new ArrayList<>(shuffled.subList(0, count));
        }

        // Return all available if count exceeds
        return shuffled;
    }
}
