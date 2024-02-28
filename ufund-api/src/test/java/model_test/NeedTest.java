package model_test;
import com.ufund.api.ufundapi.model.Need;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;



/**
 * The unit test suite for the need class
 * 
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class NeedTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "mittens";
        int expected_cost = 10;
        int expected_quantity = 100;
        String expected_description = "these are mittens, they keep your hands warm";


        // Invoke
        Need need = new Need(expected_id,expected_name,expected_cost,expected_quantity,expected_description);

        // Analyze
        assertEquals(expected_id,need.getId());
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "mittens";
        int cost = 10;
        int quantity = 100;
        String description = "these are mittens, they keep your hands warm";

        Need need = new Need(id,name,cost,quantity,description);

        String expected_name = "Galactic Agent";

        // Invoke
        need.setName(expected_name);

        // Analyze
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testCost() {
        // Setup
        int id = 99;
        String name = "mittens";
        int cost = 10;
        int quantity = 100;
        String description = "these are mittens, they keep your hands warm";

        Need need = new Need(id,name,cost,quantity,description);

        int expected_cost = 50;

        // Invoke
        need.setCost(expected_cost);

        // Analyze
        assertEquals(expected_cost,need.getCost());
    }

    @Test
    public void testQuantity() {
        // Setup
        int id = 99;
        String name = "mittens";
        int cost = 10;
        int quantity = 100;
        String description = "these are mittens, they keep your hands warm";

        Need need = new Need(id,name,cost,quantity,description);

        int expected_quantity = 20;

        // Invoke
        need.setQuantity(expected_quantity);

        // Analyze
        assertEquals(expected_quantity,need.getQuantity());
    }

    @Test
    public void testDescription() {
        // Setup
        int id = 99;
        String name = "mittens";
        int cost = 10;
        int quantity = 100;
        String description = "these are mittens, they keep your hands warm";

        Need need = new Need(id,name,cost,quantity,description);

        String expected_description = "mittens keep your hands warm during winter";

        // Invoke
        need.setDescription(expected_description);

        // Analyze
        assertEquals(expected_description,need.getDescription());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "mittens";
        int cost = 10;
        int quantity = 100;
        String description = "these are mittens, they keep your hands warm";
        String expected_string = String.format(Need.STRING_FORMAT,id,name);
        Need need = new Need(id,name,cost,quantity,description);

        // Invoke
        String actual_string = need.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}