package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Need entity
 * 
 * @author SWEN Faculty
 */
public class Need {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Hero [id=%d, name=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("name") private int cost;
    @JsonProperty("name") private int quantity;
    @JsonProperty("name") private String description;

    /**
     * Create a hero with the given id and name
     * @param id The id of the hero
     * @param name The name of the hero
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cost") int cost, @JsonProperty("quantity") int quantity, @JsonProperty("description") String description ) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.description = description;
    }

    /**
     * Retrieves the id of the need
     * @return The id of the need
     */
    public int getId() {return id;}

    /**
     * Sets the name of the need - necessary for JSON object to Java object deserialization
     * @param name The name of the need
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the need
     * @return The name of the need
     */
    public String getName() {return name;}

    /**
     * Retrieves the cost of the need
     * @return The cost of the need
     */
    public int getCost(){return cost;}

    /**
     * sets the cost equal to new_cost
     * @param new_cost new cost of the need
     */
    public void setCost(int new_cost){
        cost = new_cost;
    }

    /**
     * Retrieves the quantity of the need
     * @return The quantity of the need
     */
    public int getQuantity(){return quantity;}

    /**
     * sets the quantity equal to the new_quantity
     * @param new_quantity the quantity of the need
     */
    public void setQuantity(int new_quantity){
        quantity = new_quantity;
    }

    /**
     * Retrieves the description of the need
     * @return The description of the need
     */
    public String getDescription(){return description;}

    /**
     * sets the description equal to a new description
     * @param new_description a description of the need
     */
    public void setDescription(String new_description){
        description = new_description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name);
    }
}