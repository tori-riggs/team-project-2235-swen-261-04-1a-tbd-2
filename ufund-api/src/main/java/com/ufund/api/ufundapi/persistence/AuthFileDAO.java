package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.AuthCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Implements the functionality for JSON file-based peristance for Auth credentials
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author ptw8753
 */
@Component
public class AuthFileDAO implements AuthDAO {
    private static final Logger LOG = Logger.getLogger(AuthFileDAO.class.getName());
    Map<String,AuthCredentials> authCredentials;   // Provides a local cache of the need objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Need
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Need File Data Access Object
     *
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     *
     * @throws IOException when file cannot be accessed or read from
     */
    public AuthFileDAO(@Value("${auth.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the needs from the file
    }

    private AuthCredentials[] getAuthCredentialsArray() { // if containsText == null, no filter
        ArrayList<AuthCredentials> credentialsArrayList = new ArrayList<>();

        for (AuthCredentials credentials : authCredentials.values()) {
            credentialsArrayList.add(credentials);
        }

        AuthCredentials[] credentialsArray = new AuthCredentials[credentialsArrayList.size()];
        credentialsArrayList.toArray(credentialsArray);
        return credentialsArray;
    }

    private boolean save() throws IOException {
        AuthCredentials[] authCredentialsArray = getAuthCredentialsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),authCredentialsArray);
        return true;
    }

    private boolean load() throws IOException {
        authCredentials = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of needs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        AuthCredentials[] authCredentialsArray = objectMapper.readValue(new File(filename),AuthCredentials[].class);

        for (AuthCredentials credentials : authCredentialsArray) {
            authCredentials.put(credentials.getUsername(),credentials);
        }
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public AuthCredentials getAuthCredentials(String username) {
        synchronized(authCredentials) {
            if (authCredentials.containsKey(username))
                return authCredentials.get(username);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public AuthCredentials createAuthCredentials(AuthCredentials credentials) throws IOException {
        synchronized(authCredentials) {
            AuthCredentials newCredentials = new AuthCredentials(credentials.getUsername(), credentials.getPassword());
            authCredentials.put(newCredentials.getUsername(),newCredentials);
            save(); // may throw an IOException
            return newCredentials;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public AuthCredentials updateAuthCredentials(AuthCredentials credentials) throws IOException {
        synchronized(authCredentials) {
            if (!authCredentials.containsKey(credentials.getUsername()))
                return null;  // auth does not exist

            authCredentials.put(credentials.getUsername(),credentials);
            save(); // may throw an IOException
            return credentials;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteAuthCredentials(String username) throws IOException {
        synchronized(authCredentials) {
            if (authCredentials.containsKey(username)) {
                authCredentials.remove(username);
                return save();
            }
            else
                return false;
        }
    }
}
