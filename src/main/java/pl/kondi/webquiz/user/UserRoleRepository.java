package pl.kondi.webquiz.user;

import org.springframework.data.repository.CrudRepository;
import pl.kondi.webquiz.user.entity.UserRole;

import java.util.Optional;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);
}
