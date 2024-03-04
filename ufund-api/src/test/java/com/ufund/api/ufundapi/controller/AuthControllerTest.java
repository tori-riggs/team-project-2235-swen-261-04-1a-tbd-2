package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.enums.AuthLevel;
import com.ufund.api.ufundapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Tag("Controller-tier")
public class AuthControllerTest {
    private AuthController authController;
    private AuthService authService;

    @BeforeEach
    public void setupNeedController() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    public void whenGetPermissionLevel_WithCredentials_ThenReturn200() {
        // given
        when(authService.credentialsExist(any(), any())).thenReturn(true);
        when(authService.getPermissionLevel(any())).thenReturn(AuthLevel.ADMIN);

        // when
        ResponseEntity<AuthLevel> response = authController.getPermissionLevel("username", "password");

        // then
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(AuthLevel.ADMIN, response.getBody());
    }

    @Test
    public void whenGetPermissionLevel_WithNoCredentials_ThenReturn404() {  // getNeed may throw IOException
        // given
        when(authService.credentialsExist(any(), any())).thenReturn(false);

        // when
        ResponseEntity<AuthLevel> response = authController.getPermissionLevel("username", "password");

        // then
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
}
