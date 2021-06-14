package com.emrygun.moviecollectionapplication.repository;

import com.emrygun.moviecollectionapplication.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUserName(String userName);
}
