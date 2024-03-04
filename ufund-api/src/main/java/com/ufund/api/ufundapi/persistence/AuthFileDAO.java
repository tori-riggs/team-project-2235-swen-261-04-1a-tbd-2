package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.AuthCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    private boolean load() throws IOException {
        authCredentials = new HashMap<>();

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
}
