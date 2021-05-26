package com.emrygun.moviecollectionapplication.Controller.Application;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
import com.emrygun.moviecollectionapplication.Model.Movie.Movie;
import com.emrygun.moviecollectionapplication.Service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Controller
public class MovieController {
    @Autowired
    ApplicationUserService userService;

    /**
     * Home Page (Movie List)
     * Gets the user details and adds the movies of the user to model
     * @param model
     * @return index.html template with required model
     */
    @GetMapping("/")
    public String HomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ArrayList<Movie> movies = userService.getAllMoviesByUsername(authentication.getName());
        model.addAttribute("movies", movies);
        return "index";
    }

    /**
     * Returns Movie Creation Page
     * Takes the genre and language constants from enumeration classes inside Movie
     * @param model
     * @return createMovie.html template
     */
    @GetMapping("/createMovie")
    public String createMovie(Model model) {
        model.addAttribute("movieGenre", Movie.Genre.values());
        model.addAttribute("movieLanguage", Movie.Language.values());

        model.addAttribute("actors", new HashSet<Movie>());
        model.addAttribute("movie", new Movie());
        return "createMovie";
    }

    @PostMapping(value = "/createMovie/create", params = "action")
    public String createMovie() {
        return "redirect:/createMovie?newActor";
    }

    /**
     * Handles post request about creating new movie
     * @param movie holds the movie entity created on /createMovie
     * @return redirects home page
     */
    @PostMapping(value = "/createMovie/create")
    public String createMovie(
            @ModelAttribute Movie movie,
            @RequestParam(value = "fileImage", required = false) MultipartFile media) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<ApplicationUser> user = userService.getUserFromDbByAuthentication(authentication);
        user.ifPresent((userParam) -> userService.userAddMovie(userParam, movie, media));
        return "redirect:/";
    }

    @GetMapping(value = "/movieDetails/{id}")
    public String getUserMovieDetails(@PathVariable(value = "id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Movie movie = userService.getMovieById(id);

        if (movie == null) return "forbidden";
        else if (authentication.getName().equals(movie.getUser().getUserName())) {
            model.addAttribute("movie", movie);
            return "userMovieDetails";
        }
        return "forbidden";
    }
}
