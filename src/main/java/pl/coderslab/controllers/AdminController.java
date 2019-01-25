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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @RequestMapping("/usersManagement")
    public String getUsersPage() {
        return "admin/usersManagement";
    }

    @GetMapping("/adminsList")
    public String getAdminsList(Model model) {
        Role adminRole = (Role) roleService.findByRole("ROLE_ADMIN");
        List<User> admins = userService.findAllByRoles(adminRole);
        model.addAttribute("admins", admins);
        return "admin/adminsList";
    }

    @GetMapping("/delete/{id}")
    public String delete(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable Long id, Model model) {
        boolean isAdmin = false;
        User entityUser = currentUser.getUser();
        Set<Role> userRoles = entityUser.getRoles();
        Role adminRole = (Role) roleService.findByRole("ROLE_ADMIN");

        for (Role r : userRoles) {
            if (r.getRole().equals(adminRole.getRole())) {
                isAdmin = true;
                break;
            }
        }
        if (isAdmin) {
            userService.deleteUser(userService.findById(id));
            model.addAttribute("entityUser", entityUser);
            return "redirect:../adminsList";
        } else {
            return "403";
        }
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
           /* Set <Role> roles = admin.getRoles();
            if (roles==null){
                roles = new HashSet<>();
            }
            Role adminRole = roleService.findByRole("ROLE_ADMIN");
            roles.add(adminRole);
            admin.setRoles(roles);*/
            userService.saveAdmin(admin);

            return "redirect: /../adminsList";
        }
    }

    @GetMapping("/adminIdCheck/{id}")
    public String checkAdminId(@AuthenticationPrincipal CurrentUser currentUser, Model model, @PathVariable Long id) {
        User entityUser = currentUser.getUser();
        if (entityUser.getId().equals(id)) {
            Role adminRole = (Role) roleService.findByRole("ROLE_ADMIN");
            List<User> admins = userService.findAllByRoles(adminRole);
            model.addAttribute("admins", admins);
            model.addAttribute("id", id);
            return "admin/deleteOperationForbidden";
        }
        return "redirect: /../../delete/" + id;
    }


    @GetMapping("/update/{id}")
    public String update(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable Long id, Model model) {
        boolean isAdmin = false;
        User entityUser = currentUser.getUser();
        Set<Role> userRoles = entityUser.getRoles();
        Role adminRole = (Role) roleService.findByRole("ROLE_ADMIN");

        for (Role r : userRoles) {
            if (r.getRole().equals(adminRole.getRole())) {
                isAdmin = true;
                break;
            }
        }
        if (isAdmin) {
            userService.updateUser(userService.findById(id));
            User admin = userService.findById(id);
            model.addAttribute("admin", admin);
            return "admin/updateAdmin";
        } else {
            return "403";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute @Valid User admin, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/updateAdmin";
        }
        userService.updateUser(admin);
        return "redirect:../adminsList";
    }


}
