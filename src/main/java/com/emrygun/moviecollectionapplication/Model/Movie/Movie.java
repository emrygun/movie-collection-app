package com.emrygun.moviecollectionapplication.Model.Movie;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
public class Movie implements Comparable<Movie> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(nullable = false)
    private int year;

    //MD5 string of the png file.
    private String mediaMD5;

    private String description;

    //User
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ApplicationUser user;

    //One-To-Many relation with Actors
    @ElementCollection
    @CollectionTable(
            name="movie_actor",
            joinColumns=@JoinColumn(name="movie")
    )
    private List<Actor> actors = new ArrayList<>();

    //One-To-Many relation with Languages Enum
    @ElementCollection(targetClass = Language.class, fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_language",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id")
    )
    @Column(name = "language")
    private Set<Language> languages = EnumSet.noneOf(Language.class);

    //Movie genres.
    //Form and movie objects gets the genre constants from here
    public enum Genre {
        Horror, Comedy, Action, Drama, Adventure
    }

    //Movie languages.
    //Form and movie objects gets the language constants from here
    public enum Language {
        Turkish, English, Italian, German, French
    }

    //Compares movies depending on year
    @Override
    public int compareTo(Movie o) {
        return this.year - o.year;
    }
}
