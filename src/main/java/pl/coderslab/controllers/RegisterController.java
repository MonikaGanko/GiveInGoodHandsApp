package pl.coderslab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.SendEmail;
import pl.coderslab.models.CurrentUser;
import pl.coderslab.models.Gift;
import pl.coderslab.models.Role;
import pl.coderslab.models.User;
import pl.coderslab.repositories.GiftRepository;
import pl.coderslab.services.RoleService;
import pl.coderslab.services.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    GiftRepository giftRepository;

    @GetMapping("/register")
    public String registerForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerAction(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        User userEmailCheck = (User) userService.findByEmail(user.getEmail());
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        } else if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("incorrectPasswordConfirmation", true);
            model.addAttribute("user", user);
            return "register";
        } else if (userEmailCheck != null) {
            model.addAttribute("emailOccupied", true);
            model.addAttribute("user", user);
            return "register";
        } else {
            model.addAttribute("user", user);
            userService.saveUser(user);
            try {
                SendEmail.generateAndSendEmail(user.getEmail(), user.getUserUUID());
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return "registrationCompleted";
        }
    }

    @GetMapping("/activateAccount/{uuid}")
    public String activateAccount(@PathVariable UUID uuid) {
        User user = (User) userService.findByUUID(uuid);
        if (user != null) {
            user.setValidated(true);
            userService.updateUser(user);
            return "accountActivationCompleted";
        }
        return "redirect:/403";
    }

    @RequestMapping("/loginSuccess")
    public String loginRedirect(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        boolean isAdmin = false;
        boolean isUser = false;

        User entityUser = currentUser.getUser();
        Set<Role> userRoles = entityUser.getRoles();
        Role adminRole = (Role) roleService.findByRole("ROLE_ADMIN");
        Role userRole = (Role) roleService.findByRole("ROLE_USER");

        for (Role r : userRoles) {
            if (r.getRole().equals(adminRole.getRole())) {
                isAdmin = true;
                break;
            } else if (r.getRole().equals(userRole.getRole())) {
                isUser = true;
                break;
            }
        }
        if (isAdmin) {
            model.addAttribute("entityUser", entityUser);
            return "admin/dashboard";
        } else if (isUser) {
            model.addAttribute("entityUser", entityUser);
            return "user/dashboard";
        } else {
            return "403";
        }
    }
}


