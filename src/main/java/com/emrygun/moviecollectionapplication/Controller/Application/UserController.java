package com.emrygun.moviecollectionapplication.Controller.Application;

import com.emrygun.moviecollectionapplication.Service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    ApplicationUserService userService;

    /**
     * Users Page (Users list)
     * @param model
     * @return usersPage.html Template
     */
    @GetMapping("/users")
    public String UserPage(Model model) {
        //Insert users to model
        model.addAttribute("userList", userService.getAllUsers());
        return "usersPage";
    }
}
