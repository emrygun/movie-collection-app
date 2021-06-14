package com.emrygun.moviecollectionapplication.controller.application;

import com.emrygun.moviecollectionapplication.entity.ApplicationUser;
import com.emrygun.moviecollectionapplication.entity.movie.Movie;
import com.emrygun.moviecollectionapplication.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    ApplicationUserService userService;

    /**
     * Users Page (Users list).
     * @param model model including list of users.
     * @return "userPage" Thymeleaf template.
     */
    @GetMapping("/users")
    public String UserPage(Model model) {
        //Insert users to model
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", authentication.getAuthorities().toString());
        model.addAttribute("userList", userService.getAllUsers());
        return "usersPage";
    }

    /**
     * Switches users role between USER and ADMIN.
     * Only FOUNDER role can access it
     * @param id ID of the user
     * @return redirects to user page
     */
    @PreAuthorize("hasAuthority('ROLE_FOUNDER')")
    @PostMapping(value = "/users/switchRole")
    public String userSwitchRole(@RequestParam(value = "swithRole") Long id) {
        if (userService.switchUserRoleById(id))
            return "redirect:/users";
        else
            return "redirect:/users?error";
    }

    /**
     * Deletes user by Id from database. Only founder can use it
     * @param id User id
     * @return redirects to /user or /user?error depending on condition.
     */
    @PreAuthorize("hasAuthority('ROLE_FOUNDER')")
    @PostMapping(value = "/users/delete")
    public String userDelete(@RequestParam(value = "delete") Long id) {
        ApplicationUser user = userService.getUserById(id);
        if (user != null && user.getRole() != ApplicationUser.Role.FOUNDER) {
            userService.deleteUserById(id);
            return "redirect:/users";
        }
        else return "redirect:/users?error";
    }

    /**
     * Gets users collection. Available for "founder" and "admin" roles.
     * @param user userName of user
     * @param parameterMap map of request parameters.
     * @param model model which template uses. including movie list and some form constants.
     * @return returns "getUser" with users movie collection or redirects to "/users?error" depending on the condition
     */
    @PreAuthorize("hasAuthority('ROLE_FOUNDER') or hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/users/getUser/{user}")
    public String getUserCollection(
            @PathVariable(name = "user") String user,
            @RequestParam(required = false) Map<String, String> parameterMap,
            Model model) {
        ArrayList<Movie> movies = userService.getAllMoviesByUsername(user);

        if (parameterMap.get("sort") != null && parameterMap.get("sort").equals("true")) {
            Collections.sort(movies);
            Collections.reverse(movies);
        }

        if (movies != null) {
            if (parameterMap.get("search") != null)
                movies = userService.filterMovies(
                        parameterMap.get("text"),
                        parameterMap.get("searchBy"),
                        parameterMap.get("genre"),
                        movies);

            model.addAttribute("movies",movies);
            model.addAttribute("genres", Movie.Genre.values());
            model.addAttribute("username",user);
            return "getUser";
        }
        else return "redirect:/users?error";
    }
}