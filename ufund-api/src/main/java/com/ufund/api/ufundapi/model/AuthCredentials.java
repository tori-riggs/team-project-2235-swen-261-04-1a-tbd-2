package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.api.ufundapi.enums.AuthLevel;

import java.util.logging.Logger;

/**
 * Represents Authorization Credentials
 *
 * @author ptw8753
 */
public class AuthCredentials {
    private static final Logger LOG = Logger.getLogger(AuthCredentials.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "AuthCredentials [username=%s, password=%s, authLevel=%s]";

    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    @JsonProperty("authLevel") private AuthLevel authLevel;

    public AuthCredentials(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("authLevel") AuthLevel authLevel) {
        this.username = username;
        this.password = password;
        this.authLevel = authLevel;
    }

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public AuthLevel getAuthLevel() {return authLevel;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,username,password,authLevel);
    }
}