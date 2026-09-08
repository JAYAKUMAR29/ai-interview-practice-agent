package com.interview.service;

import com.interview.model.DifficultyLevel;
import com.interview.model.EvaluationResult;
import com.interview.model.JobRole;
import com.interview.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleBasedAnswerEvaluatorTest {

    private RuleBasedAnswerEvaluator evaluator;
    private Question sampleQuestion;

    @BeforeEach
    void setUp() {
        evaluator = new RuleBasedAnswerEvaluator();
        sampleQuestion = new Question(
                "TEST-01",
                JobRole.JAVA_DEVELOPER,
                DifficultyLevel.INTERMEDIATE,
                "Concurrency & Multithreading",
                "Explain how the Java Memory Model ensures thread safety with volatile, synchronized, and atomic classes.",
                List.of("volatile", "synchronized", "visibility", "atomicity", "thread safety"),
                List.of("volatile guarantees visibility across CPU caches", "synchronized guarantees mutual exclusion and atomicity", "atomic classes use CAS without locking"),
                "Volatile ensures visibility, synchronized provides mutual exclusion and atomicity, atomic classes use CAS.",
                "Can volatile replace synchronized for increment operations like count++? Why or why not?",
                List.of("atomicity", "compound", "race condition")
        );
    }

    @Test
    @DisplayName("Empty answer should receive score 0 and trigger follow-up/prompt")
    void testEmptyAnswer() {
        EvaluationResult result = evaluator.evaluate(sampleQuestion, "");
        assertEquals(0, result.getScore());
        assertTrue(result.isFollowUpRecommended());
        assertTrue(result.getAreasForImprovement().size() > 0);
        assertNotNull(result.getSuggestedFollowUpQuestion());
    }

    @Test
    @DisplayName("Whitespace only answer should receive score 0")
    void testWhitespaceAnswer() {
        EvaluationResult result = evaluator.evaluate(sampleQuestion, "   \n\t  ");
        assertEquals(0, result.getScore());
        assertTrue(result.isFollowUpRecommended());
    }

    @Test
    @DisplayName("Short answer with few keywords should trigger follow-up with lower score")
    void testShortAnswer() {
        EvaluationResult result = evaluator.evaluate(sampleQuestion, "Volatile provides visibility.");
        assertTrue(result.getScore() > 0 && result.getScore() < 60);
        assertTrue(result.isFollowUpRecommended());
        assertTrue(result.getMatchedKeywords().contains("volatile") || result.getMatchedKeywords().contains("visibility"));
    }

    @Test
    @DisplayName("Comprehensive answer covering all keywords and concepts should score high")
    void testComprehensiveAnswer() {
        String answer = "In the Java Memory Model, thread safety is achieved through multiple complementary mechanisms. " +
                "The volatile keyword guarantees visibility across CPU caches by forcing direct reads and writes to main memory, " +
                "though it does not provide atomicity for compound operations. In contrast, synchronized provides both mutual exclusion " +
                "and atomicity, ensuring only one thread executes a critical block using monitor locks. " +
                "Atomic classes like AtomicInteger utilize CPU-level Compare-And-Swap (CAS) instructions to guarantee lock-free thread safety.";

        EvaluationResult result = evaluator.evaluate(sampleQuestion, answer);
        assertTrue(result.getScore() >= 80, "Expected score >= 80 but got: " + result.getScore());
        assertEquals(5, result.getMatchedKeywords().size());
        assertTrue(result.getStrengths().size() > 0);
        assertNotNull(result.getExplanation());
    }

    @Test
    @DisplayName("Irrelevant answer should receive low score and penalty")
    void testIrrelevantAnswer() {
        String answer = "I like cooking pasta with tomato sauce, garlic, olive oil, and lots of mozzarella cheese for dinner on Sundays.";
        EvaluationResult result = evaluator.evaluate(sampleQuestion, answer);
        assertTrue(result.getScore() <= 20, "Expected score <= 20 for irrelevant answer but got: " + result.getScore());
        assertEquals(0, result.getMatchedKeywords().size());
    }

    @Test
    @DisplayName("Follow-up response should improve score and resolve follow-up requirement")
    void testFollowUpFlow() {
        String initialAnswer = "Volatile gives visibility and synchronized provides thread safety.";
        EvaluationResult initial = evaluator.evaluate(sampleQuestion, initialAnswer);

        String followUpAnswer = "No, volatile cannot replace synchronized for count++ because count++ is a compound read-modify-write operation requiring atomicity, which volatile lacks.";
        EvaluationResult finalEval = evaluator.evaluateWithFollowUp(sampleQuestion, initialAnswer, sampleQuestion.getFollowUpQuestion(), followUpAnswer, initial);

        assertTrue(finalEval.getScore() > initial.getScore());
        assertFalse(finalEval.isFollowUpRecommended());
    }
}
