package pl.coderslab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.SendEmail;
import pl.coderslab.models.User;
import pl.coderslab.repositories.RoleRepository;
import pl.coderslab.services.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.validation.Valid;
import java.util.UUID;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/register")
    public String registerForm (Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerAction (@ModelAttribute @Valid User user, BindingResult result, Model model) {
        User userCheckEmail = (User)userService.findByEmail(user.getEmail());
        if (result.hasErrors()) {
            model.addAttribute("user",user);
            return "register";
        }
        else if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("incorrectPasswordConfirmation", true);
            model.addAttribute("user",user);
            return "register";
        }
        else if (userCheckEmail != null){
            model.addAttribute("emailOccupied", true);
            model.addAttribute("user",user);
            return "register";
        }
        else {
            model.addAttribute("user",user);
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

    @RequestMapping("/activateAccount/{uuid}")
    public String activateAccount(@PathVariable UUID uuid){
        User user = (User)userService.findByUUID(uuid);
        if(user != null){
            user.setValidated(true);
            userService.updateUser(user);
            return "accountActivationCompleted";
        }
        return "redirect:/403";
    }
}
