package com.emrygun.moviecollectionapplication.Service;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
import com.emrygun.moviecollectionapplication.Model.ApplicationUser.Role;
import com.emrygun.moviecollectionapplication.Model.Movie.Movie;
import com.emrygun.moviecollectionapplication.Repository.ApplicationUserRepository;
import com.emrygun.moviecollectionapplication.Repository.MovieRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationUserService {
    @Autowired
    ApplicationUserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieMediaService mediaService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Gets User from database by authentication parameter
     * @param authorizedUser Authentication object which we get from controllers
     * @return Optional user
     */
    public Optional<ApplicationUser> getUserFromDbByAuthentication(Authentication authorizedUser) {
        return userRepository.findByUserName(authorizedUser.getName());
    }

    /**
     * Gets all users as list from database
     * @return List of users
     */
    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Registers user.
     * Checks if user exists on database. If not, encrypts it's password with
     * bcrypt encoder and inserts the entity to database table
     * @param user
     * @return true if successfully registered
     */
    public Boolean registerUser(ApplicationUser user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent())
            return false;
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.USER);
            userRepository.save(user);
            return true;
        }
    }

    /**
     * Adds a new movie relational with specific user (One-To-Many)
     * gets the user class, adds the movie than saves into database.
     * @param user
     * @param movie
     */
    @SneakyThrows
    public void userAddMovie(ApplicationUser user, Movie movie, MultipartFile media) {
        if (!media.isEmpty()) {
            String filename = mediaService.saveImage(media);
            movie.setMediaMD5(filename);
        }
        movie.setUser(user);
        movieRepository.save(movie);
    }

    /**
     * Gets all the movies by Username of user.
     * @param username
     * @return ArrayList<Movie> of all movies belongs to username
     */
    public ArrayList<Movie> getAllMoviesByUsername(String username) {
        return movieRepository.findMoviesByUser_UserName(username);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findMovieById(id);
    }
}
