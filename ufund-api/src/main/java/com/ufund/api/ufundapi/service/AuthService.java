package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.enums.AuthLevel;
import com.ufund.api.ufundapi.model.AuthCredentials;
import com.ufund.api.ufundapi.persistence.AuthDAO;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthService {
    private AuthDAO authDAO;
    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public boolean credentialsExist(String username, String password) {
        AuthCredentials credentials = authDAO.getAuthCredentials(username);
        //credentials exist if they are found by username and match the password
        if(credentials == null) {
            return false;
        }
        return password.equals(credentials.getPassword());
    }

    public boolean hasPermissionLevel(String username, String password, AuthLevel requiredAuthLevel) {
        if (!credentialsExist(username, password)) {
            //no login, no perms
            return false;
        }
        if (AuthLevel.USER ==  requiredAuthLevel) {
            //all logins have user perms
            return true;
        }
        //requiredAuthLevel must be admin
        AuthLevel authLevel = getPermissionLevel(username);
        return AuthLevel.ADMIN == authLevel;
    }

    public AuthLevel getPermissionLevel(String username) {
        return authDAO.getAuthCredentials(username).getAuthLevel();
    }
}
