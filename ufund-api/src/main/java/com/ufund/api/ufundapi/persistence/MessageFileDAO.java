package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Message;
import com.ufund.api.ufundapi.model.Need;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Needs
 *
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 *
 * @author SWEN Faculty
 */
@Component
public class MessageFileDAO implements MessageDAO {
    private static final Logger LOG = Logger.getLogger(MessageFileDAO.class.getName());
    Map<Integer,Message> messages;   // Provides a local cache of the need objects
    // so that we don't need to read from the file
    // each time
    private ObjectMapper objectMapper;  // Provides conversion between Need
    // objects and JSON text format written
    // to the file
    private static int nextId;  // The next Id to assign to a new need
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Need File Data Access Object
     *
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     *
     * @throws IOException when file cannot be accessed or read from
     */
    public MessageFileDAO(@Value("${messages.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the needs from the file
    }

    /**
     * Generates the next id for a new {@linkplain Need need}
     *
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Need needs} from the tree map
     *
     * @return  The array of {@link Need needs}, may be empty
     */
    private Message[] getMessagesArray() {
        return getMessagesArray(null);
    }

    private Message[] getMessagesArray(String username) { // if containsText == null, no filter
        ArrayList<Message> messageArrayList = new ArrayList<>();

        for (Message message : messages.values()) {
            if (username == null || message.getUsername().equals(username)) {
                messageArrayList.add(message);
            }
        }

        Message[] messageArray = new Message[messageArrayList.size()];
        messageArrayList.toArray(messageArray);
        return messageArray;
    }

    private boolean save() throws IOException {
        Message[] messageArray = getMessagesArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),messageArray);
        return true;
    }

    private boolean load() throws IOException {
        messages = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of needs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Message[] messageArray = objectMapper.readValue(new File(filename),Message[].class);

        // Add each need to the tree map and keep track of the greatest id
        for (Message message : messageArray) {
            messages.put(message.getId(),message);
            if (message.getId() > nextId)
                nextId = message.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Message[] getAllMessages() {
        synchronized(messages) {
            return getMessagesArray();
        }
    }

    @Override
    public Message[] getMessagesByUsername(String username) {
        synchronized (messages) {
            return getMessagesArray(username);
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Message createMessage(Message message) throws IOException {
        System.out.println(message);
        synchronized(messages) {
            // We create a new need object because the id field is immutable
            // and we need to assign the next unique id
            System.out.println(message.getTimestamp());
            Message newMessage = new Message(nextId(),message.getUsername(),message.getTimestamp(),message.getText());
            messages.put(newMessage.getId(),newMessage);
            save(); // may throw an IOException
            return newMessage;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean deleteMessage(int id) throws IOException {
        synchronized(messages) {
            if (messages.containsKey(id)) {
                System.out.println("teststst");
                messages.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
