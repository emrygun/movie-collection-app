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

    /**
     * Logs in authorized users.
     * @return redirects to "/" or returns "login" template depending on condition.
     */
    @GetMapping("/login")
    public String LoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return "login";
        else return "redirect:/";
    }

    /**
     * Denied page. (Access Denied)
     * @return returns "denied" template
     */
    @GetMapping("/denied")
    public String NoPermissionPage() {
        return "forbidden";
    }

    /**
     * Register page.
     * @param model model includes user object for form.
     * @return "register" template.
     */
    @GetMapping("register")
    public String registerUser(Model model) {
        ApplicationUser applicationUser = new ApplicationUser();
        model.addAttribute("applicationUser", applicationUser);
        return "register";
    }

    /**
     * Registers new user into database if username is not taken.
     * @param applicationUser user object model attribute.
     * @return redirects to "/login?newUser" or "/login?error" depending on the condition.
     */
    @PostMapping("register/newUser")
    public String registerUser(@ModelAttribute ApplicationUser applicationUser) {
        if (userService.registerUser(applicationUser))
            return "redirect:/login?newUser";
        else
            return "redirect:/register?error";
    }
}
