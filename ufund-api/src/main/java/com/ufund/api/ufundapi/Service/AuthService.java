package com.ufund.api.ufundapi.Service;

import com.ufund.api.ufundapi.model.AuthCredentials;
import com.ufund.api.ufundapi.persistence.AuthDAO;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AuthService {
    private AuthDAO authDAO;
    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public boolean credentialsExist(String username, String password) {
        AuthCredentials credentials = authDAO.getAuthCredentials(username);
        if(credentials != null && credentials.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public boolean createCredentials(String username, String password) throws IOException {
        if(credentialsExist(username, password)) {
            return false;
        }
        authDAO.createAuthCredentials(new AuthCredentials(username, password));
        return true;
    }

    public boolean deleteCredentials(String username, String password) throws IOException {
        if(credentialsExist(username, password)) {
            authDAO.deleteAuthCredentials(username);
            return true;
        }
        return false;
    }
}
