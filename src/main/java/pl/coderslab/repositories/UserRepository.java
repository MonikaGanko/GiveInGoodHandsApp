package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.models.Role;
import pl.coderslab.models.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,Long> {
    User findUserById(Long userId);
    User findUserByUserUUID(UUID uuid);
    User findUserByEmailIgnoreCase(String email);
    User findUserByEmailAndEnabledAndValidated(String email, boolean enabled, boolean validated);
    //List <User> findAllByRole(Set<Role> roles);
    List <User> findAllByRoles(Role role);
}