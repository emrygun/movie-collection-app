package com.emrygun.moviecollectionapplication.configuration;

import com.emrygun.moviecollectionapplication.entity.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;

    @Value("${testfounder.username}")
    private String founderUsername;
    @Value("${testfounder.password}")
    private String founderPassword;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());

        //Test founder user.
        auth.inMemoryAuthentication()
                .withUser(founderUsername)
                .password(getPasswordEncoder().encode(founderPassword))
                .roles(ApplicationUser.Role.FOUNDER.name());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users/**").hasAnyRole("ADMIN", "FOUNDER")
                .antMatchers("/").hasAnyRole("USER","ADMIN","FOUNDER")
                .and().formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .permitAll()
                .and().logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                .and().exceptionHandling().accessDeniedPage("/denied");
    }

    @Bean
    @Scope("singleton")
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(5);
     }
}
