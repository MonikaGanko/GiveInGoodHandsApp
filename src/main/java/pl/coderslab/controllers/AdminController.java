package pl.coderslab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.models.CurrentUser;
import pl.coderslab.models.Role;
import pl.coderslab.models.User;
import pl.coderslab.services.RoleService;
import pl.coderslab.services.UserService;

import javax.validation.Valid;
import java.util.Currency;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @RequestMapping("/adminsManagement")
    public String getAdminsPage() {
        return "admin/adminsManagement";
    }

    @GetMapping("/adminsList")
    public String getAdminsList(Model model) {
        Role adminRole = (Role) roleService.findByRole("ROLE_ADMIN");
        List<User> admins = userService.findAllByRoles(adminRole);
        model.addAttribute("admins", admins);
        return "admin/adminsList";
    }

    @GetMapping("/delete/{id}")
    public String delete(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable Long id) {
        User entityUser = currentUser.getUser();
        if (entityUser.getRoles().equals("ROLE_ADMIN")) {
            userService.deleteUser(userService.findById(id));
        }
        return "redirect:../admin/adminsList";
    }

    @GetMapping("/addAdmin")
    public String show(Model model) {
        model.addAttribute("admin", new User());
        return "admin/addAdmin";
    }

    @PostMapping("/addAdmin")
    public String add(@ModelAttribute @Valid User admin, BindingResult result, Model model) {
        User emailCheck = (User) userService.findByEmail(admin.getEmail());
        if (result.hasErrors()) {
            model.addAttribute("admin", admin);
            return "admin/addAdmin";
        } else if (!admin.getPassword().equals(admin.getConfirmPassword())) {
            model.addAttribute("incorrectPasswordConfirmation", true);
            model.addAttribute("admin", admin);
            return "admin/addAdmin";
        } else if (emailCheck != null) {
            model.addAttribute("emailOccupied", true);
            model.addAttribute("admin", admin);
            return "admin/addAdmin";
        } else {
            userService.saveUser(admin);
            return "redirect:/admin/adminsList";
        }

    }
}
