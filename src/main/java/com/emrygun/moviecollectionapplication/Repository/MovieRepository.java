package com.emrygun.moviecollectionapplication.Repository;

import com.emrygun.moviecollectionapplication.Model.Movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    ArrayList<Movie> findMoviesByUser_UserName(String username);
    Movie findMovieById(Long id);
}
