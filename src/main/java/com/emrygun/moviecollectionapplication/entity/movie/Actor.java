package com.emrygun.moviecollectionapplication.entity.movie;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Actor {
    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "role")
    private String role;

    //Checks if actor objects equal depending on their fullName parameter.
    //Used for filtering by actors in Service.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;
        return fullName.equals(actor.fullName);
    }
}
