package pro.sky.java.course2.examinerservice.service;

import pro.sky.java.course2.examinerservice.model.Question;

import java.util.Collection;

public interface QuestionService {
    Question add(String question, String answer);
    Question remove(String question, String answer);
    Collection<Question> getAll();
    Question getRandomQuestion();
}
