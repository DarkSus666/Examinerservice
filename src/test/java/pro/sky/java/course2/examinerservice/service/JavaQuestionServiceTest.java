package pro.sky.java.course2.examinerservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pro.sky.java.course2.examinerservice.model.Question;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JavaQuestionServiceTest {

    private JavaQuestionService javaQuestionService;

    @BeforeEach
    void setUp() {
        javaQuestionService = new JavaQuestionService();
    }

    @Test
    void add_ShouldAddQuestion_WhenQuestionDoesNotExist() {
       
        String question = "Рандомный вопрос?";
        String answer = "Рандомный ответ";

        Question result = javaQuestionService.add(question, answer);

        assertNotNull(result);
        assertEquals(question, result.getQuestion());
        assertEquals(answer, result.getAnswer());
        assertEquals(1, javaQuestionService.getAll().size());
        assertTrue(javaQuestionService.getAll().contains(result));
    }

    @Test
    void add_ShouldThrowException_WhenQuestionAlreadyExists() {

        String question = "Рандомный вопрос?";
        String answer = "Рандомный ответ";
        javaQuestionService.add(question, answer);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> javaQuestionService.add(question, answer)
        );
        assertEquals("400 BAD_REQUEST \"Вопрос уже существует\"", exception.getMessage());
    }

    @Test
    void remove_ShouldRemoveQuestion_WhenQuestionExists() {

        String question = "Рандомный вопрос?";
        String answer = "Рандомный ответ";
        Question addedQuestion = javaQuestionService.add(question, answer);
        assertEquals(1, javaQuestionService.getAll().size());

        Question removedQuestion = javaQuestionService.remove(question, answer);

        assertNotNull(removedQuestion);
        assertEquals(question, removedQuestion.getQuestion());
        assertEquals(answer, removedQuestion.getAnswer());
        assertEquals(0, javaQuestionService.getAll().size());
        assertFalse(javaQuestionService.getAll().contains(removedQuestion));
    }

    @Test
    void remove_ShouldThrowException_WhenQuestionDoesNotExist() {

        String question = "Рандомный вопрос?";
        String answer = "Рандомный ответ";

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> javaQuestionService.remove(question, answer)
        );
        assertEquals("404 NOT_FOUND \"Вопрос не найден\"", exception.getMessage());
    }

    @Test
    void getAll_ShouldReturnEmptyCollection_WhenNoQuestions() {

        Collection<Question> result = javaQuestionService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAll_ShouldReturnAllQuestions_WhenQuestionsExist() {

        Question question1 = javaQuestionService.add("Q1", "A1");
        Question question2 = javaQuestionService.add("Q2", "A2");

        Collection<Question> result = javaQuestionService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(question1));
        assertTrue(result.contains(question2));
    }

    @Test
    void getRandomQuestion_ShouldReturnQuestion_WhenQuestionsExist() {

        javaQuestionService.add("Q1", "A1");
        javaQuestionService.add("Q2", "A2");
        javaQuestionService.add("Q3", "A3");

        Question result = javaQuestionService.getRandomQuestion();

        assertNotNull(result);
        assertNotNull(result.getQuestion());
        assertNotNull(result.getAnswer());
    }

    @Test
    void getRandomQuestion_ShouldThrowException_WhenNoQuestions() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> javaQuestionService.getRandomQuestion()
        );
        assertEquals("400 BAD_REQUEST \"Нет доступных вопросов\"", exception.getMessage());
    }

    @Test
    void getRandomQuestion_ShouldReturnDifferentQuestions_WhenCalledMultipleTimes() {

        javaQuestionService.add("Q1", "A1");
        javaQuestionService.add("Q2", "A2");
        javaQuestionService.add("Q3", "A3");

        Question result1 = javaQuestionService.getRandomQuestion();
        Question result2 = javaQuestionService.getRandomQuestion();
        Question result3 = javaQuestionService.getRandomQuestion();

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
    }

    @Test
    void questionEquality_ShouldWorkCorrectly() {

        Question question1 = new Question("Q", "A");
        Question question2 = new Question("Q", "A");
        Question question3 = new Question("Q", "B");
        Question question4 = new Question("P", "A");

        assertEquals(question1, question2);
        assertNotEquals(question1, question3);
        assertNotEquals(question1, question4);
        assertEquals(question1.hashCode(), question2.hashCode());
    }
}