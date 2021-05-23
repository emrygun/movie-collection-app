package com.emrygun.moviecollectionapplication.Service;

import com.emrygun.moviecollectionapplication.Model.User.ApplicationUser;
import com.emrygun.moviecollectionapplication.Model.User.Role;
import com.emrygun.moviecollectionapplication.Repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {
    @Autowired
    ApplicationUserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Boolean registerUser(ApplicationUser user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent())
            return false;
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user.addRole(Role.roleUser));
            return true;
        }
    }
}
