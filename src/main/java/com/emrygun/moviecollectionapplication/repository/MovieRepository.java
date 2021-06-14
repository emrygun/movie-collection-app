package com.emrygun.moviecollectionapplication.repository;

import com.emrygun.moviecollectionapplication.entity.ApplicationUser;
import com.emrygun.moviecollectionapplication.entity.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieById(Long id);

    ArrayList<Movie> findMoviesByUser_UserName(String username);
    Boolean existsMovieByIdAndUser(Long movieId, ApplicationUser user);
}
