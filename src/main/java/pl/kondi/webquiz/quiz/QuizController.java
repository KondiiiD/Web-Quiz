package pl.kondi.webquiz.quiz;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kondi.webquiz.quiz.dto.*;
import pl.kondi.webquiz.quiz.exceptions.NoExistingQuizException;
import pl.kondi.webquiz.quiz.exceptions.NotAuthorizedException;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class QuizController {
    private final QuizService quizService;

    QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/quizzes")
    ResponseEntity<Page<QuizDto>> getAllQuizzes(@RequestParam Integer page) {
        return ResponseEntity.ok(quizService.getAllQuizzes(page));
    }

    @GetMapping("/quizzes/completed")
    ResponseEntity<Page<CompletedQuizDto>> getCompletedQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(quizService.getAllCompletedQuizzes(page));
    }

    @GetMapping("/quizzes/{id}")
    ResponseEntity<QuizDto> getQuiz(@PathVariable Long id) {
        return quizService.getQuiz(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/quizzes/{id}/solve")
    ResponseEntity<QuizAnswerDto> selectAnswer(@PathVariable Long id,
                                               @RequestBody RequestAnswerDto answer) {
        return quizService.sendAnswer(id, answer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/quizzes")
    ResponseEntity<QuizDto> addQuestion(@Valid @RequestBody QuizRequestDto quiz) {
        QuizDto savedQuiz = quizService.addQuiz(quiz);
        URI savedQuizUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("id")
                .buildAndExpand(savedQuiz.id())
                .toUri();
        return ResponseEntity.created(savedQuizUri).body(savedQuiz);
    }

    @DeleteMapping("/quizzes/{id}")
    ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
        } catch (NoExistingQuizException e) {
            return ResponseEntity.notFound().build();
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
