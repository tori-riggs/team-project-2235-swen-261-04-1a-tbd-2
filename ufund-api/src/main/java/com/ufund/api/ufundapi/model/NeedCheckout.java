package com.ufund.api.ufundapi.model;

import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Checked out Need entity
 *
 * @author SWEN Faculty
 */
public class NeedCheckout {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Need [username=%s, checkoutIds=%s]";

    @JsonProperty("username") private String username;
    @JsonProperty("checkoutIds") private Map<Integer, Integer> checkoutIds;

    public NeedCheckout(@JsonProperty("username") String username, @JsonProperty("checkoutIds") Map<Integer, Integer> checkoutIds) {
        this.username = username;
        this.checkoutIds = checkoutIds;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public Map<Integer, Integer> getCheckoutIds() {return checkoutIds;}

    public void setCheckoutIds(Map<Integer, Integer> checkoutIds) {this.checkoutIds = checkoutIds;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,username,checkoutIds);
    }
}