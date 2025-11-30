package pro.sky.java.course2.examinerservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pro.sky.java.course2.examinerservice.model.Question;

import java.util.*;

@Service
public class JavaQuestionService implements QuestionService {
    private final Set<Question> questions = new HashSet<>();
    private final Random random = new Random();

    @Override
    public Question add(String question, String answer) {
        Question newQuestion = new Question(question, answer);
        if (questions.contains(newQuestion)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Вопрос уже существует");
        }
        questions.add(newQuestion);
        return newQuestion;
    }

    @Override
    public Question remove(String question, String answer) {
        Question questionToRemove = new Question(question, answer);
        if (!questions.contains(questionToRemove)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Вопрос не найден");
        }
        questions.remove(questionToRemove);
        return questionToRemove;
    }

    @Override
    public Collection<Question> getAll() {
        return Collections.unmodifiableSet(questions);
    }

    @Override
    public Question getRandomQuestion() {
        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нет доступных вопросов");
        }

        int randomIndex = random.nextInt(questions.size());
        Iterator<Question> iterator = questions.iterator();

        for (int i = 0; i < randomIndex; i++) {
            iterator.next();
        }

        return iterator.next();
    }
}