package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.Service.AuthService;
import com.ufund.api.ufundapi.model.AuthCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Need;

import java.io.IOException;
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
    public ResponseEntity<AuthCredentials> getAuthCredentials(@RequestParam String username, @RequestParam String password) {
        LOG.info("GET /auth/");
        boolean authExists = authService.credentialsExist(username, password);
        if (authExists)
            return new ResponseEntity<AuthCredentials>(HttpStatus.OK);
        else
            return new ResponseEntity<AuthCredentials>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<String> createAuthCredentials(@RequestParam String username, @RequestParam String password) {
        LOG.info("POST /auth/");
        try {
            boolean successful = authService.createCredentials(username, password);
            if (successful)
                return new ResponseEntity<>(HttpStatus.CREATED);
            else
                return ResponseEntity.internalServerError().body("Username is already taken!");
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<AuthCredentials> updateAuthCredentials(@RequestParam String username, @RequestParam String password) {
        LOG.info("PUT /auth/");
        boolean authExists = authService.credentialsExist(username, password);
        if (authExists)
            return new ResponseEntity<AuthCredentials>(HttpStatus.OK);
        else
            return new ResponseEntity<AuthCredentials>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("")
    public ResponseEntity<AuthCredentials> deleteAuthCredentials(@RequestParam String username, @RequestParam String password) {
        LOG.info("DELETE /auth/");
        try {
            boolean deleted = authService.deleteCredentials(username, password);
            if(deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}