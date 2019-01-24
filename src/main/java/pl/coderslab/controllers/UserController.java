package pl.coderslab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.models.*;
import pl.coderslab.repositories.GiftRepository;
import pl.coderslab.services.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    ReceiverService receiverService;

    @Autowired
    GiftService giftService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    DonationService donationService;

    @Autowired
    CityService cityService;

    @RequestMapping("/usersList")
    public String getAdminsList(Model model) {

        Role userRole = (Role) roleService.findByRole("ROLE_USER");
        List<User> users = userService.findAllByRoles(userRole);
        model.addAttribute("users", users);
        return "admin/usersList";
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
            return "redirect:../usersList";
        } else {
            return "403";
        }
    }

    @GetMapping("/addAdmin")
    public String show(Model model) {
        model.addAttribute("admin", new User());
        return "admin/addAdmin";
    }


    @PostMapping("/addUser")
    public String add(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        User emailCheck = (User) userService.findByEmail(user.getEmail());
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "admin/addUser";
        } else if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("incorrectPasswordConfirmation", true);
            model.addAttribute("user", user);
            return "admin/addUser";
        } else if (emailCheck != null) {
            model.addAttribute("emailOccupied", true);
            model.addAttribute("user", user);
            return "user/addUser";
        } else {
            userService.saveUser(user);
           /* Long newAdminId = admin.getId();
            Long UserRoleId = roleService.findIdByRole("ROLE_USER");
            user.setRoles();*/
            return "redirect: /../usersList";
        }
    }

    @GetMapping("/completeForm")
    public String completeForm(Model model) {
        List<Gift> gifts = giftService.findAll();
        List<Receiver> receivers = receiverService.findAll();
        List<City> cities = cityService.findAll();

        model.addAttribute("donation", new Donation());
        model.addAttribute("gifts", gifts);
        model.addAttribute("receivers", receivers);
        model.addAttribute("cities", cities);
        return "form";
    }

    @PostMapping("/completeForm")
    public String completeForm(@ModelAttribute @Valid Donation donation, @AuthenticationPrincipal CurrentUser currentUser, BindingResult result) {
        if (result.hasErrors()) {
            return "form";
        }
        User entityUser = currentUser.getUser();
        donation.setUser(entityUser);
        donationService.save(donation);

        return "user/dashboard";
    }

}
