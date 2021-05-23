package com.emrygun.moviecollectionapplication;

import com.emrygun.moviecollectionapplication.Repository.ApplicationUserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = ApplicationUserRepository.class)
public class MovieCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieCollectionApplication.class, args);
    }

}