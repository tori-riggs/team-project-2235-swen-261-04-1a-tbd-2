package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.logging.Logger;

/**
 * Represents Authorization Credentials
 *
 * @author ptw8753
 */
public class AuthCredentials {
    private static final Logger LOG = Logger.getLogger(AuthCredentials.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "AuthCredentials [username=%d, password=%s]";

    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;

    public AuthCredentials(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,username,password);
    }
}