package com.emrygun.moviecollectionapplication.Repository;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
import com.emrygun.moviecollectionapplication.Model.Movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieById(Long id);

    ArrayList<Movie> findMoviesByUser_UserName(String username);
    Boolean existsMovieByIdAndUser(Long movieId, ApplicationUser user);
}
