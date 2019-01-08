package pl.coderslab.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.models.Role;
import pl.coderslab.models.User;
import pl.coderslab.repositories.RoleRepository;
import pl.coderslab.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

/*    @Override
    public void saveUser(User user) {
        Role userRole = roleRepository.findRoleByRole("ROLE_USER");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }*/


    @Override
    public void saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Role userRole = roleRepository.findRoleByRole("ROLE_USER");
    user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
    userRepository.save(user);
}

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void changePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public User findByUUID(UUID uuid) {
        return userRepository.findUserByUserUUID(uuid);
    }

    /*@Override
    public User findByUsername(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username);
    }*/

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    @Override
    public User findByEmailEnabledValidated(String email, boolean enabled, boolean validated) {
        return userRepository.findUserByEmailAndEnabledAndValidated(email,true, true);
    }

 /*   @Override
    public User findByUsernameStatus(String username, boolean enabled) {
        return userRepository.findUserByUsernameIgnoreCaseAndEnabled(username, enabled);
    }*/

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}