package com.emrygun.moviecollectionapplication.Service;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
import com.emrygun.moviecollectionapplication.Model.ApplicationUser.Role;
import com.emrygun.moviecollectionapplication.Model.Movie.Actor;
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

import static java.util.stream.Collectors.toCollection;

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

    public ApplicationUser getUserById(Long id) {
        Optional<ApplicationUser> user = userRepository.findById(id);
        return user.orElse(null);
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
     * @param user user
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

    public void deleteUserById(Long id) {
        Optional<ApplicationUser> user = userRepository.findById(id);
        user.ifPresent((param) -> userRepository.deleteById(id));
    }

    public Boolean switchUserRoleById(Long id) {
        ApplicationUser user = userRepository.findById(id).orElse(null);
        if (user == null || user.getRole().equals(Role.FOUNDER)) return false;
        else {
            user.setRole(user.getRole() == Role.USER ? Role.ADMIN : Role.USER);
            userRepository.save(user);
            return true;
        }
    }

    /**
     * Adds a new movie relational with specific user (One-To-Many)
     * gets the user class, adds the movie than saves into database.
     * @param user User
     * @param movie Movie
     */
    @SneakyThrows
    public void userAddMovie(ApplicationUser user, Movie movie, MultipartFile media) {
        if (!media.isEmpty()) {
            String filenameMD5 = mediaService.saveImage(media);
            movie.setMediaMD5(filenameMD5);
        }
        movie.setUser(user);
        movieRepository.save(movie);
    }

    /**
     * Adds a new movie relational with specific user (One-To-Many)
     * gets the user class, adds the movie than saves into database.
     * @param user User
     * @param movie movie
     */
    @SneakyThrows
    public void userUpdateMovie(ApplicationUser user, Movie movie, MultipartFile media) {
        if (media != null) {
            String filenameMD5 = mediaService.saveImage(media);
            movie.setMediaMD5(filenameMD5);
        }
        if (movieRepository.existsMovieByIdAndUser(movie.getId(), user)) {
            movie.setUser(user);
            movieRepository.save(movie);
        }
    }

    /**
     * Gets all the movies by Username of user.
     * @param username Users username
     * @return ArrayList<Movie> of all movies belongs to username
     */
    public ArrayList<Movie> getAllMoviesByUsername(String username) {
        return movieRepository.findMoviesByUser_UserName(username);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findMovieById(id);
    }

    public void deleteMovieById(Long id) {
        movieRepository.deleteById(id);
    }

    public Boolean isAuthOwnerOfMovie(Authentication auth, Movie movie) {
        return auth.getName().equals(movie.getUser().getUserName());
    }

    /**
     * Filters movie list depending on the parameters.
     * @param text input box text.
     * @param searchBy searchBy actor or movie name
     * @param genre genre
     * @param movies movies list
     * @return reconstructed movies list
     */
    public ArrayList<Movie> filterMovies(
            String text,
            String searchBy,
            String genre,
            ArrayList<Movie> movies) {
        ArrayList<Movie> filteredMovies = movies;
        if(!genre.equals("Any")) {
            filteredMovies = movies.stream()
                    .filter(movie -> movie.getGenre().name().equals(genre))
                    .collect(toCollection(ArrayList::new));
        }
        if(!text.isEmpty()){
            if (searchBy.equals("name")) {
                filteredMovies = movies.stream()
                        .filter(movie -> movie.getName().equals(text))
                        .collect(toCollection(ArrayList::new));
            }
            else if (searchBy.equals("actor")) {
                Actor tempActor = new Actor();
                tempActor.setFullName(text);
                filteredMovies = movies.stream()
                        .filter(movie -> movie.getActors().contains(tempActor))
                        .collect(toCollection(ArrayList::new));
            }
        }
        return filteredMovies;
    }
}
