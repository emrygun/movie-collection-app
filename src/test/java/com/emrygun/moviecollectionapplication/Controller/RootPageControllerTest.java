package com.emrygun.moviecollectionapplication.Controller;

import com.emrygun.moviecollectionapplication.Controller.User.AuthenticationController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthenticationController.class)
@Rollback
class RootPageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void createWhitelistedUser() {
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "USER")
    void UserRoleLoginTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/login")
                        .accept(MediaType.ALL))
                        .andExpect(status().isOk());
    }
}