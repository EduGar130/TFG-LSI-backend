package com.tfg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.security.jwt.JwtUtils;
import com.tfg.security.model.JwtRequest;
import com.tfg.security.service.CustomUserDetailsService;
import com.tfg.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void testAuthenticate() throws Exception {
        JwtRequest request = new JwtRequest();
        request.setUsername("user");
        request.setPassword("pass");

        UserDetails userDetails = User.withUsername("user").password("pass").roles("USER").build();

        doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(userService.getWarehouseIdByUsername("user")).thenReturn(1L);
        when(userService.getPermissionsByUsername("user")).thenReturn("ROLE_USER");
        when(jwtUtils.generateToken("user", "ROLE_USER", 1L)).thenReturn("token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
