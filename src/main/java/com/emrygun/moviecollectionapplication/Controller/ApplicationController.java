package com.emrygun.moviecollectionapplication.Controller;

import com.emrygun.moviecollectionapplication.Repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {
    @Autowired
    ApplicationUserRepository userRepository;

    @GetMapping("/users")
    public String UserPage(Model model) {
        //Insert users to model
        model.addAttribute("userList", userRepository.findAll());
        return "users";
    }
}
