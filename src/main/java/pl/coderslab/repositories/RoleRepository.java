package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.models.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByRole(String roleName);
}