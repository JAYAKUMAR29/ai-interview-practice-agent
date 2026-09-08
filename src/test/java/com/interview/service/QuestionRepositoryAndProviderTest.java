package com.interview.service;

import com.interview.exception.InvalidRequestException;
import com.interview.model.DifficultyLevel;
import com.interview.model.JobRole;
import com.interview.model.Question;
import com.interview.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionRepositoryAndProviderTest {

    private QuestionRepository questionRepository;
    private StaticQuestionProvider questionProvider;

    @BeforeEach
    void setUp() {
        questionRepository = new QuestionRepository();
        questionProvider = new StaticQuestionProvider(questionRepository);
    }

    @Test
    @DisplayName("Verify every required role has at least 5 questions for each difficulty tier")
    void testQuestionCoverageForAllRolesAndDifficulties() {
        for (JobRole role : JobRole.values()) {
            for (DifficultyLevel diff : DifficultyLevel.values()) {
                List<Question> questions = questionRepository.findByRoleAndDifficulty(role, diff);
                assertTrue(questions.size() >= 5,
                        "Role " + role + " and difficulty " + diff + " must have >= 5 questions, found: " + questions.size());

                for (Question q : questions) {
                    assertNotNull(q.getId());
                    assertNotNull(q.getQuestionText());
                    assertNotNull(q.getRequiredKeywords());
                    assertFalse(q.getRequiredKeywords().isEmpty());
                    assertNotNull(q.getFollowUpQuestion());
                }
            }
        }
    }

    @Test
    @DisplayName("Dynamic selection returns requested question count")
    void testQuestionSelection() {
        List<Question> selected = questionProvider.selectQuestions(JobRole.JAVA_DEVELOPER, DifficultyLevel.INTERMEDIATE, 5);
        assertEquals(5, selected.size());

        List<Question> pythonSel = questionProvider.selectQuestions(JobRole.PYTHON_DEVELOPER, DifficultyLevel.BEGINNER, 3);
        assertEquals(3, pythonSel.size());
    }

    @Test
    @DisplayName("Invalid role or difficulty should throw InvalidRequestException")
    void testInvalidSelection() {
        assertThrows(InvalidRequestException.class, () -> questionProvider.selectQuestions(null, DifficultyLevel.BEGINNER, 5));
        assertThrows(InvalidRequestException.class, () -> questionProvider.selectQuestions(JobRole.WEB_DEVELOPER, null, 5));
        assertThrows(InvalidRequestException.class, () -> questionProvider.selectQuestions(JobRole.WEB_DEVELOPER, DifficultyLevel.ADVANCED, 0));
    }
}
