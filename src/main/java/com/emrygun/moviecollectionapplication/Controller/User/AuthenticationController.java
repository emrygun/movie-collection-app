package com.emrygun.moviecollectionapplication.Controller.User;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
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
    @GetMapping("/denied")
    public String NoPermissionPage() {
        return "forbidden";
    }

    @GetMapping("register")
    public String registerUser(Model model) {
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
}
