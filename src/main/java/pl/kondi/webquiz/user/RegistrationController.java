package pl.kondi.webquiz.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.kondi.webquiz.user.dto.UserRegistrationDto;
import pl.kondi.webquiz.user.excpetion.EmailAlreadyRegisteredException;
import pl.kondi.webquiz.user.excpetion.IncorrectEmailException;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class RegistrationController {
    private final UserCredentialsService userService;

    public RegistrationController(UserCredentialsService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registration(@Valid @RequestBody UserRegistrationDto registrationUser) {
        try {
            userService.register(registrationUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        } catch (EmailAlreadyRegisteredException | IncorrectEmailException e) {
            return ResponseEntity.badRequest().header("Error", e.getMessage()).build();
        }
        return ResponseEntity.ok().build();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
