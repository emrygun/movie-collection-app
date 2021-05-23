package com.emrygun.moviecollectionapplication.Controller;

import com.emrygun.moviecollectionapplication.Model.User.ApplicationUser;
import com.emrygun.moviecollectionapplication.Model.User.Role;
import com.emrygun.moviecollectionapplication.Repository.ApplicationUserRepository;
import com.emrygun.moviecollectionapplication.Service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthenticationController {
    @Autowired
    ApplicationUserService userService;

    @GetMapping("/login")
    public String LoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return "login";
        else return "redirect:/";
    }

    @RequestMapping("register")
    public String registerForm(Model model) {
        ApplicationUser applicationUser = new ApplicationUser();
        model.addAttribute("applicationUser", applicationUser);
        return "register";
    }

    @PostMapping("register/newUser")
    public String registerUser(@ModelAttribute ApplicationUser applicationUser) {
        if (userService.registerUser(applicationUser))
            return "redirect:/login?newUser";
        else
            return "redirect:/register?error";
    }

    @GetMapping("/denied")
    public String NoPermissionPage() {
        return "forbidden";
    }
}
