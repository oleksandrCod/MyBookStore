package store.mybookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybookstore.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleName(Role.RoleName roleName);
}
