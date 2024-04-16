package pl.kondi.webquiz.quiz;

import pl.kondi.webquiz.quiz.dto.QuizDto;
import pl.kondi.webquiz.quiz.dto.QuizRequestDto;
import pl.kondi.webquiz.quiz.entity.Quiz;

class QuizDtoMapper {

    static QuizDto map(Quiz quiz) {
        return new QuizDto(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
    }

    static Quiz map(QuizRequestDto quizRequestDto) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizRequestDto.title());
        quiz.setText(quizRequestDto.text());
        quiz.setOptions(quizRequestDto.options());
        quiz.setAnswer(quizRequestDto.answer());
        return quiz;
    }

}
