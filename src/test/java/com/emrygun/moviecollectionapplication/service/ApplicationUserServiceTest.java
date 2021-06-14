package com.emrygun.moviecollectionapplication.service;

import com.emrygun.moviecollectionapplication.entity.ApplicationUser;
import com.emrygun.moviecollectionapplication.entity.movie.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback
class ApplicationUserServiceTest {

    @Autowired
    ApplicationUserService userService;

    @Test
    void addMovieToUser() {
        ApplicationUser user = new ApplicationUser("test132", "test123");
        userService.registerUser(user);
        Movie movie = new Movie();
        movie.setDescription("test123");
        movie.setGenre(Movie.Genre.Drama);
        movie.setYear(1992);

        userService.userAddMovie(user, movie, null);
    }

    @Test
    void getAllMoviesByUsername() {
        ApplicationUser user = new ApplicationUser("test132", "test123");
        userService.registerUser(user);
        Movie movie = new Movie();
        movie.setDescription("test123");
        movie.setGenre(Movie.Genre.Drama);
        movie.setYear(1992);

        userService.userAddMovie(user, movie, null);
        userService.getAllMoviesByUsername(user.getUserName());
    }


}