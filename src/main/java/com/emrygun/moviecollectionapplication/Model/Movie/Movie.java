package com.emrygun.moviecollectionapplication.Model.Movie;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
public class Movie {
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

    //MD5 string of the png file.
    private String description;

    //User
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ApplicationUser user;

    //Many-To-Many relation with Actors
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
    private Set<Actor> actors = new HashSet<>();

    //Many-To-One relation with Languages Enum
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
        Horror, Comedy, Action, Drama, Adventure;
    }

    //Movie languages.
    //Form and movie objects gets the language constants from here
    public enum Language {
        Turkish, English, Italian, German, French;
    }

}
