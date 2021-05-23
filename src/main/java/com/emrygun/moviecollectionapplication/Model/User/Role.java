package com.emrygun.moviecollectionapplication.Model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    //Unique Role ID
    @Id
    private Integer id;

    //Unique Role name
    @Column(nullable = false, unique = true)
    private String name;

    //Set of ApplicationUsers with Roles
    @ManyToMany(mappedBy = "roles")
    private Set<ApplicationUser> users;

    @Override
    public String toString() {
        return this.name;
    }

    private Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Role roleUser = new Role(1, "ROLE_USER");
    public static Role roleAdmin = new Role(2, "ROLE_ADMIN");
    public static Role roleFounder = new Role(3, "ROLE_FOUNDER");
}
