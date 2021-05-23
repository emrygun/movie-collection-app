package com.emrygun.moviecollectionapplication.Model.User;

import com.emrygun.moviecollectionapplication.Model.Movie.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

    //Roles of user and relationship with Role. Joined in user_role table.
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Movie> movies;

    //Constructor
    public ApplicationUser(String userName, String password) {
        this.userName = userName;
        this.password =  password;
    }

    public ApplicationUser addRole(Role role) {
        this.roles.add(role);
        return this;
    }
    public ApplicationUser removeRole(Role role) {
        this.roles.remove(role);
        return this;
    }

    /**
     * Creates Set<GrantedAuthorities out of Set<Roles>
     * Used in creating UserDetails class which is used in authentication
     * @return Returns Set<GrantedAuthorities>
     */
    private Collection<GrantedAuthority> getUserRoleCollection() {
        //Create authorities collection
        return this.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Provides UserDetails to authenticate User
     * @return Returns UserDetails to use in UserDetailsService on authentication
     */
    public UserDetails getUserDetails() {
        return User.withUsername(this.userName)
                .password(password)
                .authorities(this.getUserRoleCollection())
                .build();
    }
}
