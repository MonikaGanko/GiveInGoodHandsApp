package pl.coderslab.services;

public interface RoleService<Role> {
    Role findByRole(String roleName);
}
