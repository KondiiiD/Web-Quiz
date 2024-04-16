package pl.kondi.webquiz.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kondi.webquiz.quiz.dto.*;
import pl.kondi.webquiz.quiz.entity.CompletedQuiz;
import pl.kondi.webquiz.quiz.entity.Quiz;
import pl.kondi.webquiz.quiz.exceptions.NoExistingQuizException;
import pl.kondi.webquiz.quiz.exceptions.NotAuthorizedException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final CompletedQuizRepository completedQuizRepository;

    QuizService(QuizRepository quizRepository, CompletedQuizRepository completedQuizRepository) {
        this.quizRepository = quizRepository;
        this.completedQuizRepository = completedQuizRepository;
    }

    public Page<QuizDto> getAllQuizzes(Integer page) {
        return quizRepository.findAll(PageRequest.of(page, 10))
                .map(QuizDtoMapper::map);
    }

    public Page<CompletedQuizDto> getAllCompletedQuizzes(Integer page) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return completedQuizRepository
                .findAllByCompletedByOrderByCompletedAtDesc(currentUser, PageRequest.of(page, 10))
                .map(completedQuiz -> new CompletedQuizDto(completedQuiz.getQuiz().getId(), completedQuiz.getCompletedAt()));
    }

    public Optional<QuizDto> getQuiz(Long id) {
        return quizRepository.findById(id)
                .map(QuizDtoMapper::map);
    }

    @Transactional
    public Optional<QuizAnswerDto> sendAnswer(Long id, RequestAnswerDto answer) {
        List<Integer> correctAnswer = quizRepository.findById(id)
                .map(Quiz::getAnswer)
                .stream()
                .flatMap(Collection::stream)
                .sorted()
                .toList();

        List<Integer> requestedAnswer = answer.answer()
                .stream()
                .sorted()
                .toList();

        return checkAnswer(id, requestedAnswer, correctAnswer);
    }

    private Optional<QuizAnswerDto> checkAnswer(Long id, List<Integer> requestedAnswer, List<Integer> correctAnswer) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if ((requestedAnswer.isEmpty() && correctAnswer.isEmpty()) || Objects.equals(requestedAnswer, correctAnswer)) {
            markQuizCompleted(currentUser, id);
            return Optional.of(new QuizAnswerDto(true, "Congratulations, you're right!"));
        } else
            return Optional.of(new QuizAnswerDto(false, "Wrong answer! Please, try again."));
    }

    private void markQuizCompleted(String user, Long id) {
        quizRepository.findById(id)
                .ifPresent(quiz ->
                        completedQuizRepository.save(new CompletedQuiz(LocalDateTime.now(), user, quiz)));
    }

    @Transactional
    public QuizDto addQuiz(QuizRequestDto quiz) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Quiz quizToSave = QuizDtoMapper.map(quiz);
        quizToSave.setAuthor(currentUser);
        Quiz savedQuiz = quizRepository.save(quizToSave);
        return QuizDtoMapper.map(savedQuiz);
    }

    @Transactional
    public void deleteQuiz(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!quizRepository.existsById(id)) {
            throw new NoExistingQuizException("Quiz with id %d doesn't exist".formatted(id));
        }
        quizRepository.findById(id).filter(quiz -> quiz.isAuthor(auth) || quiz.isAdmin(auth))
                .ifPresentOrElse(quiz -> quizRepository.deleteById(quiz.getId()),
                        () -> {
                            throw new NotAuthorizedException("User is not authorized to delete");
                        });
    }
}
