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
        //boolean isUser = false;
        User entityUser = currentUser.getUser();
        Set<Role> userRoles = entityUser.getRoles();
        Role adminRole = (Role) roleService.findByRole("ROLE_ADMIN");
        //Role userRole = (Role) roleService.findByRole("ROLE_USER");
         /*   if (entityUser.getRoles().equals(adminRole.getRole())) {
                userService.deleteUser(userService.findById(id));
                return "redirect:../adminsList";
            }
            return "/admin/dashboard";
*/

        for (Role r : userRoles) {
            if (r.getRole().equals(adminRole.getRole())) {
                isAdmin = true;
                break;
                // } else if (r.getRole().equals(userRole.getRole())) {
                //     isUser = true;
                //    break;
            }
        }
        if (isAdmin) {
            userService.deleteUser(userService.findById(id));
            model.addAttribute("entityUser", entityUser);
            return "redirect:../adminsList";
/*        } else if (isUser) {
            return "user/dashboard";*/
            //  }
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
            userService.saveUser(admin);
           /* Long newAdminId = admin.getId();
            Long adminRoleId = roleService.findIdByRole("ROLE_ADMIN");
            admin.setRoles();*/
            return "redirect: /../adminsList";
        }
    }

    @GetMapping("/adminIdCheck/{id}")
    public String checkAdminId(@AuthenticationPrincipal CurrentUser currentUser, Model model, @PathVariable Long id) {
        User entityUser = currentUser.getUser();
        if (entityUser.getId() == id) {
            Role adminRole = (Role) roleService.findByRole("ROLE_ADMIN");
            List<User> admins = userService.findAllByRoles(adminRole);
            model.addAttribute("admins", admins);
            model.addAttribute("id", id);
            return "admin/deleteOperationForbidden";
        }
        return "redirect: /../../delete/" + id;
    }

}
