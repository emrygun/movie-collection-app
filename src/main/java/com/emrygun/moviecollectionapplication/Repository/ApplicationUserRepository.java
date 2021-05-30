package com.emrygun.moviecollectionapplication.Repository;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUserName(String userName);
}
