package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.service.AuthService;
import com.ufund.api.ufundapi.enums.AuthLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("auth")
public class AuthController {

    AuthService authService;
    private static final Logger LOG = Logger.getLogger(AuthController.class.getName());

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("")
    public ResponseEntity<AuthLevel> getPermissionLevel(@RequestParam String username, @RequestParam String password) {
        LOG.info("GET /auth/");
        boolean authExists = authService.credentialsExist(username, password);
        if (authExists)
            return ResponseEntity.ok(authService.getPermissionLevel(username));
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}