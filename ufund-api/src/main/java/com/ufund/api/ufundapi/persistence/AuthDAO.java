package com.ufund.api.ufundapi.persistence;

import com.ufund.api.ufundapi.model.AuthCredentials;

import java.io.IOException;


/**
 * Defines the interface for Auth credential persistance
 * 
 * @author ptw8753
 */
public interface AuthDAO {
    AuthCredentials getAuthCredentials(String username);

}
