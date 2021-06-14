package com.emrygun.moviecollectionapplication.controller.application;

import com.emrygun.moviecollectionapplication.entity.ApplicationUser;
import com.emrygun.moviecollectionapplication.entity.movie.Actor;
import com.emrygun.moviecollectionapplication.entity.movie.Movie;
import com.emrygun.moviecollectionapplication.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller
public class MovieController {
    @Autowired
    ApplicationUserService userService;

    /**
     * Home Page (Movie List). Returns users movie collection with additional settings.
     * Gets the movies belonging to user, adjusts the list depending on the request parameters,
     * adds the movieList and some required stuffs (form related etc.) to model,
     * returns the Thymeleaf template
     * @param parameterMap Map of the request parameters
     * @param model The model which template uses.
     * @return Thymeleaf template
     */
    @GetMapping("/")
    public String HomePage(
            @RequestParam(required = false) Map<String, String> parameterMap,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ArrayList<Movie> movies = userService.getAllMoviesByUsername(authentication.getName());

        if (parameterMap.get("sort") != null && parameterMap.get("sort").equals("true")) {
            Collections.sort(movies);
            Collections.reverse(movies);
        }
        if (parameterMap.get("search") != null)
            movies = userService.filterMovies(
                    parameterMap.get("text"),
                    parameterMap.get("searchBy"),
                    parameterMap.get("genre"),
                    movies);

        model.addAttribute("genres", Movie.Genre.values());
        model.addAttribute("movies", movies);
        return "index";
    }

    /**
     * Returns Movie Creation Page
     * Takes the genre and language constants from enumeration classes inside Movie and adds them into model.
     * Then returns Thymeleaf template.
     * @param modelMap Map of the model parameters.
     * @return createMovie.html Thymeleaf template.
     */
    @GetMapping("/movie/create")
    public String createMovie(ModelMap modelMap) {
        modelMap.addAttribute("movieGenre", Movie.Genre.values());
        modelMap.addAttribute("movieLanguage", Movie.Language.values());

        Movie movie = new Movie();
        modelMap.addAttribute("movie", movie);
        return "createMovie";
    }

    /**
     * Edits an existing movie entry. Gets the movie from database by id, adds it to model with some additional
     * form parameters (enum constants etc.) then returns the template depending on the user and movie condition.
     * @param id Movie id
     * @param modelMap Map of model
     * @return Thymeleaf template depending on the condition. "editMovie" or "forbidden"
     */
    @PostMapping(value = "/movie/edit")
    public String editMovie(@RequestParam(value = "movieId") Long id, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Movie movie = userService.getMovieById(id);
        if (movie == null) return "forbidden";
        else if (userService.isAuthOwnerOfMovie(authentication, movie)) {
            modelMap.addAttribute("movie", movie);
            modelMap.addAttribute("movieGenre", Movie.Genre.values());
            modelMap.addAttribute("movieLanguage", Movie.Language.values());
            return "editMovie";
        }
        return "forbidden";
    }

    /**
     * Adds or removes Actors input row from form. The create and edit forms relies on PRG (Post/Redirect/Get) Design.
     * Gets the model and request parameters from existing form, adds or removes Actor field and object to movie which is in model.
     * Returns the Thymeleaf template depending on the operation (createMovie or editMovie).
     * @param movie movie ModelAttribute returned from previous page.
     * @param modelMap map of Model attributes.
     * @param media media of the movie returned from previous page.
     * @param op add or delete row operation. -1 means add and the other values represents the index of row to delete
     * @param operation edit or create operation.
     * @return Thymeleaf template depending on condition. "createMovie", "editMovie" or "forbidden"
     */
    @PostMapping(value = "/movie/create_edit/{operation}", params = {"editRow"})
    public String addRowMovie(
            @ModelAttribute(name = "movie") Movie movie, ModelMap modelMap,
            @RequestParam(value = "fileImage", required = false) MultipartFile media,
            @RequestParam(value = "editRow") int op,
            @PathVariable("operation") String operation) {
        modelMap.addAttribute("movieGenre", Movie.Genre.values());
        modelMap.addAttribute("movieLanguage", Movie.Language.values());

        if (op == -1) movie.getActors().add(new Actor());
        else if (op <= movie.getActors().size()) movie.getActors().remove(op);
        else return "forbidden";

        if (operation.equals("create")) return "createMovie";
        else if (operation.equals("edit")) return "editMovie";
        else return "forbidden";
    }

    /**
     * Handles post request about creating new movie
     * @param movie movie entity which is returned from previous pages model
     * @return redirects home page or returns "forbidden" depending on the condition
     */
    @PostMapping(value = "/movie/create_edit/{operation}", params = {"save"})
    public String saveMovie(
            @ModelAttribute(name = "movie") Movie movie,
            @RequestParam(value = "fileImage", required = false) MultipartFile media,
            @PathVariable("operation") String operation) {
        Optional<ApplicationUser> user = userService
                .getUserFromDbByAuthentication(SecurityContextHolder.getContext().getAuthentication());
        if (operation.equals("create")) {
            user.ifPresent((userParam) -> userService.userAddMovie(userParam, movie, media));
            return "redirect:/";
        }
        else if (operation.equals("edit")) {
            user.ifPresent((userParam) -> userService.userUpdateMovie(userParam, movie, media));
            return "redirect:/";
        }
        else return "forbidden";
    }

    /**
     * Deletes movie belonging to user from database
     * @param id Id of movie
     * @return redirects home page or returns "forbidden" depending on the condition
     */
    @PostMapping(value = "/movie/delete")
    public String deleteMovie(@RequestParam(value = "movieId") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Movie movie = userService.getMovieById(id);
        if (movie == null) return "forbidden";
        else if (userService.isAuthOwnerOfMovie(authentication, movie)) {
            userService.deleteMovieById(movie.getId());
            return "redirect:/";
        }
        return "forbidden";
    }

    /**
     * Returns movie details from database.
     * @param id Id of the movie.
     * @param model model that page uses. Includes movie information.
     * @return redirect to movie detail if successful, else returns forbidden.
     */
    @PostMapping(value = "/movie/detail")
    public String getUserMovieDetails(@RequestParam(value = "movieId") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Movie movie = userService.getMovieById(id);

        if (movie == null) return "forbidden";
        else if (userService.isAuthOwnerOfMovie(authentication, movie)) {
            model.addAttribute("movie", movie);
            return "userMovieDetails";
        }
        return "forbidden";
    }
}