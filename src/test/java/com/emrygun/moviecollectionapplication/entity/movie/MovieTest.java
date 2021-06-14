package com.emrygun.moviecollectionapplication.entity.movie;

import com.emrygun.moviecollectionapplication.repository.ApplicationUserRepository;
import com.emrygun.moviecollectionapplication.repository.MovieRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback
class MovieTest {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ApplicationUserRepository userRepository;

    @Test
    @Disabled
    public void addMovieWithActorTest() {
        /*
        ApplicationUser mockUser = new ApplicationUser("mockTest", "mockTest");
        Movie mockMovie = new Movie();
        mockMovie.setName("test");
        mockMovie.setGenre(Movie.Genre.Horror);
        mockMovie.setYear(1999);

        MovieActor mockActor = new MovieActor();
        mockActor.setFullName("TEST");
        mockActor.setRole("TEST");

        mockMovie.getMovieActors().add(mockActor);
        mockUser.getMovies().add(mockMovie);

        userRepository.save(mockUser);
         */
    }
}