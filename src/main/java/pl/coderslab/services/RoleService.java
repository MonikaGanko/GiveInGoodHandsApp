package pl.coderslab.services;

import pl.coderslab.models.Role;

public interface RoleService{
    Role findByRole(String roleName);
    Long findIdByRole(String roleName);
}
