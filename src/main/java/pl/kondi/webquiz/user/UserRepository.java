package pl.kondi.webquiz.user;

import org.springframework.data.repository.CrudRepository;
import pl.kondi.webquiz.user.entity.User;

import java.util.Optional;

interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
