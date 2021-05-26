package com.emrygun.moviecollectionapplication.Service.UserDetail;

import com.emrygun.moviecollectionapplication.Model.ApplicationUser;
import com.emrygun.moviecollectionapplication.Repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    @Autowired
    ApplicationUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<ApplicationUser> user = userRepository.findByUserName(userName);
        //Get the user if exists or throw exception
        return user.map(ApplicationUser::getUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found: " + userName));
    }
}
