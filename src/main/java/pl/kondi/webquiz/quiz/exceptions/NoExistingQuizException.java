package pl.kondi.webquiz.quiz.exceptions;

public class NoExistingQuizException extends RuntimeException {
    public NoExistingQuizException(String message) {
        super(message);
    }
}
