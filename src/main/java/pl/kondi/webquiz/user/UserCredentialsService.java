package pl.kondi.webquiz.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kondi.webquiz.user.dto.UserCredentialsDto;
import pl.kondi.webquiz.user.dto.UserRegistrationDto;
import pl.kondi.webquiz.user.entity.User;
import pl.kondi.webquiz.user.entity.UserRole;
import pl.kondi.webquiz.user.excpetion.EmailAlreadyRegisteredException;
import pl.kondi.webquiz.user.excpetion.IncorrectEmailException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserCredentialsService {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCredentialsService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        if (isCurrentUserAdmin()) {
            userRepository.deleteByEmail(email);
        }
    }

    @Transactional
    public void register(UserRegistrationDto registration) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        User user = new User();
        if (isAlreadyRegistered(registration.email())) {
            throw new EmailAlreadyRegisteredException("%s already registered".formatted(registration.email()));
        }
        if (!patternMatches(registration.email(), regex)) {
            throw new IncorrectEmailException("%s is not valid email".formatted(registration.email()));
        }

        user.setEmail(registration.email());
        String passwordHash = passwordEncoder.encode(registration.password());
        user.setPassword(passwordHash);
        Optional<UserRole> userRole = userRoleRepository.findByName(USER_ROLE);
        userRole.ifPresentOrElse(
                role -> user.getRoles().add(role),
                () -> {
                    throw new NoSuchElementException();
                }
        );
        userRepository.save(user);
    }

    private boolean patternMatches(String email, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    private boolean isAlreadyRegistered(String emailRegistration) {
        return userRepository.existsByEmail(emailRegistration);
    }

    private boolean isCurrentUserAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ADMIN_ROLE));
    }

}
