package com.emrygun.moviecollectionapplication.entity;

import com.emrygun.moviecollectionapplication.entity.movie.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class ApplicationUser {
    //Unique User ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Unique Username
    @Column(nullable = false, unique = true)
    private String userName;

    //Password
    @Column(nullable = false)
    private String password;

    //Role of the User
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //Generates "user_id" column on movie to provide OneToMany relation
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Movie> movies;

    //Constructor
    public ApplicationUser(String userName, String password) {
        this.userName = userName;
        this.password =  password;
    }

    //Possible roles of user
    public enum Role {
        USER, ADMIN, FOUNDER,
        //These constants below are for spring security to recognize role.
        //Spring Security looks for Role enum if it has such member with ROLE_* tag.
        //!! Not a good practice
        ROLE_USER, ROLE_ADMIN, ROLE_FOUNDER;
    }

    /**
     * Provides UserDetails to authenticate User
     * @return Returns UserDetails to use in UserDetailsService on authentication
     */
    public UserDetails getUserDetails() {
        return User.withUsername(this.userName)
                .password(password)
                .roles(getRole().name())
                .build();
    }
}
