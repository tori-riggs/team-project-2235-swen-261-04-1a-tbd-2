package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Checked out Need entity
 *
 * @author SWEN Faculty
 */
public class Message {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Message [username=%s, timestamp=%s, text=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("username") private String username;
    @JsonProperty("timestamp") private String timestamp;
    @JsonProperty("text") private String text;

    public Message(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("timestamp") String timestamp, @JsonProperty("text") String text) {
        this.id = id;
        this.username = username;
        this.timestamp = timestamp;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,username,timestamp,text);
    }
}