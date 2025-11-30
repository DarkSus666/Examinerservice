package pro.sky.java.course2.examinerservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import pro.sky.java.course2.examinerservice.model.Question;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExaminerServiceImplTest {

    @Mock
    private QuestionService questionService;

    private ExaminerServiceImpl examinerService;

    @BeforeEach
    void setUp() {
        examinerService = new ExaminerServiceImpl(questionService);
    }

    @Test
    void getQuestions_ShouldReturnUniqueQuestions_WhenAmountIsValid() {

        int amount = 3;
        Set<Question> mockQuestions = Set.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2"),
                new Question("Q3", "A3"),
                new Question("Q4", "A4"),
                new Question("Q5", "A5")
        );

        when(questionService.getAll()).thenReturn(mockQuestions);
        when(questionService.getRandomQuestion())
                .thenReturn(
                        new Question("Q1", "A1"),
                        new Question("Q2", "A2"),
                        new Question("Q3", "A3")
                );

        Collection<Question> result = examinerService.getQuestions(amount);

        assertNotNull(result);
        assertEquals(amount, result.size());

        Set<Question> uniqueQuestions = new HashSet<>(result);
        assertEquals(amount, uniqueQuestions.size());

        verify(questionService, times(1)).getAll();
        verify(questionService, atLeast(amount)).getRandomQuestion();
    }

    @Test
    void getQuestions_ShouldThrowException_WhenAmountExceedsAvailableQuestions() {

        int amount = 5;
        Set<Question> mockQuestions = Set.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2"),
                new Question("Q3", "A3")
        );

        when(questionService.getAll()).thenReturn(mockQuestions);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> examinerService.getQuestions(amount)
        );

        assertTrue(exception.getMessage().contains("Запрошенное количество (5) превышает доступное (3)"));
        verify(questionService, times(1)).getAll();
        verify(questionService, never()).getRandomQuestion();
    }

    @Test
    void getQuestions_ShouldThrowException_WhenAmountIsZero() {

        int amount = 0;

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> examinerService.getQuestions(amount)
        );

        assertEquals("400 BAD_REQUEST \"Количество должно быть больше 0\"", exception.getMessage());
        verify(questionService, never()).getAll();
        verify(questionService, never()).getRandomQuestion();
    }

    @Test
    void getQuestions_ShouldThrowException_WhenAmountIsNegative() {

        int amount = -1;

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> examinerService.getQuestions(amount)
        );

        assertEquals("400 BAD_REQUEST \"Количество должно быть больше 0\"", exception.getMessage());
        verify(questionService, never()).getAll();
        verify(questionService, never()).getRandomQuestion();
    }

    @Test
    void getQuestions_ShouldHandleDuplicateRandomQuestions() {

        int amount = 2;
        Set<Question> mockQuestions = Set.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2"),
                new Question("Q3", "A3")
        );

        when(questionService.getAll()).thenReturn(mockQuestions);

        when(questionService.getRandomQuestion())
                .thenReturn(
                        new Question("Q1", "A1"),
                        new Question("Q1", "A1"), // дубликат
                        new Question("Q2", "A2")  // уникальный
                );

        Collection<Question> result = examinerService.getQuestions(amount);

        assertNotNull(result);
        assertEquals(amount, result.size());

        Set<Question> uniqueQuestions = new HashSet<>(result);
        assertEquals(amount, uniqueQuestions.size());

        verify(questionService, atLeast(amount + 1)).getRandomQuestion();
    }

    @Test
    void getQuestions_ShouldReturnExactAmount_WhenEnoughQuestionsAvailable() {

        int amount = 2;
        Set<Question> mockQuestions = Set.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2"),
                new Question("Q3", "A3")
        );

        when(questionService.getAll()).thenReturn(mockQuestions);
        when(questionService.getRandomQuestion())
                .thenReturn(
                        new Question("Q1", "A1"),
                        new Question("Q2", "A2")
                );

        Collection<Question> result = examinerService.getQuestions(amount);

        assertNotNull(result);
        assertEquals(amount, result.size());
    }

    @Test
    void getQuestions_ShouldWorkWithSingleQuestion() {

        int amount = 1;
        Set<Question> mockQuestions = Set.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2")
        );

        when(questionService.getAll()).thenReturn(mockQuestions);
        when(questionService.getRandomQuestion()).thenReturn(new Question("Q1", "A1"));

        Collection<Question> result = examinerService.getQuestions(amount);

        assertNotNull(result);
        assertEquals(amount, result.size());
        assertTrue(result.contains(new Question("Q1", "A1")));
    }
}