package pl.coderslab.services;

import pl.coderslab.models.Role;
import pl.coderslab.models.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    void saveUser(User user);
    void saveAdmin(User user);
    void updateUser(User user);
    void deleteUser(User user);
    void changePassword(User user);
    User findById(Long userId);
    User findByUUID(UUID uuid);
    User findByEmail(String email);
    User findByEmailEnabledValidated(String email, boolean enabled, boolean validated);
    List<User> findAllUsers();
   /* User findByUsername(String username);*/
   /* User findByUsernameStatus(String username, boolean enabled);*/

    //List <User> findAllByRole(Set<Role> roles);
    List <User> findAllByRoles(Role role);

}