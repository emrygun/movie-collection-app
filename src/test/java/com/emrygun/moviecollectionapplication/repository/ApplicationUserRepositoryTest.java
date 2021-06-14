package com.emrygun.moviecollectionapplication.repository;

import com.emrygun.moviecollectionapplication.entity.ApplicationUser;
import com.emrygun.moviecollectionapplication.entity.ApplicationUser.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase()
@Rollback()
class ApplicationUserRepositoryTest {
    @Autowired
    private ApplicationUserRepository repo;

    @Test
    public void testCreateUser() {
        ApplicationUser user = new ApplicationUser("user", "user");
        user.setRole(Role.USER);

        ApplicationUser admin = new ApplicationUser("admin", "admin");
        admin.setRole(Role.ADMIN);

        ApplicationUser founder = new ApplicationUser("founder", "founder");
        founder.setRole(Role.FOUNDER);

        repo.save(user);
        repo.save(admin);
        repo.save(founder);

        assertEquals(repo.findByUserName("user").get().getUserName(), "user");
        assertEquals(repo.findByUserName("admin").get().getUserName(), "admin");
        assertEquals(repo.findByUserName("founder").get().getUserName(), "founder");
    }
}