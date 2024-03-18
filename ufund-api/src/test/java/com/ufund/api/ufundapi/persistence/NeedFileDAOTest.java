package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.NeedFileDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Need File DAO class
 */
@Tag("Persistence-tier")
public class NeedFileDAOTest {
    NeedFileDAO needFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * 
     * @throws IOException
     */
    @BeforeEach
    public void setupNeedFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[3];
        testNeeds[0] = new Need(99, "Paper towels", 5, 5, "Paper towels needed");
        testNeeds[1] = new Need(100, "Beds", 30, 10, "Building new beds");
        testNeeds[2] = new Need(101, "Shampoo", 10, 10, "New shampoo.");

        when(mockObjectMapper
            .readValue(new File("test.txt"), Need[].class)).thenReturn(testNeeds);

        needFileDAO = new NeedFileDAO("test.txt", mockObjectMapper);
    }

    @Test
    public void testGetNeeds() {
        // Invoke
        Need[] needs = needFileDAO.getNeeds();

        // Analyze
        assertEquals(needs.length, testNeeds.length);
        for (int i = 0; i < testNeeds.length; ++i) {
            assertEquals(needs[i], testNeeds[i]);
        }
    }

    @Test
    public void testFindNeeds() {
        // case sensitive, might need to fix this
        Need[] needs = needFileDAO.findNeeds("s");

        // Analyze
        assertEquals(needs.length, 2);
        assertEquals(needs[0], testNeeds[0]);
        assertEquals(needs[1], testNeeds[1]);
    }

    @Test
    public void testGetNeed() {
        Need need = needFileDAO.getNeed(99);

        assertEquals(need, testNeeds[0]);
    }

    @Test
    public void testDeleteNeed() {
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(99),
            "Unexpected exception thrown");
        
        assertEquals(result, true);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because need attribute of NeedFileDAO is package private
        // we can access it directly
        assertEquals(needFileDAO.needs.size(), testNeeds.length-1);
    }

    @Test
    public void testCreateNeed() {
        Need need = new Need(102, "Books", 15, 15, "Books for entertainment");

        Need result = assertDoesNotThrow(() -> needFileDAO.createNeed(need),
            "Unexpected exception thrown");

        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(actual.getId(), need.getId());
        assertEquals(actual.getName(), need.getName());
    }

    @Test
    public void testUpdateNeed() {
        Need need = new Need(99, "Socks", 10, 20, "A pair of socks.");

        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need), 
                                            "Unexpected exception thrown");
        
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(actual, need);
    }

    @Test
    public void testSaveException() throws IOException {
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(Need[].class));

        Need need = new Need(102, "Books", 15, 15, "Books for entertainment");
        assertThrows(IOException.class,
            () -> needFileDAO.createNeed(need),
                "IOException not thrown");
    }

    @Test
    public void testGetNeedNotFound() {
        Need need = needFileDAO.getNeed(98);
        assertEquals(need, null);
    }

    @Test
    public void testDeleteNeedNotFound() {
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(98),
                                            "Unexpected exception thrown");
        
        assertEquals(result, false);
        assertEquals(needFileDAO.needs.size(), testNeeds.length);
    }

    @Test
    public void testUpdateNeedNotFound() {
        Need need = new Need(98, "Socks", 8, 20, "Socks");

        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                                "Unexpected exception thrown");

        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);

        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("test.txt"), Need[].class);
        
        assertThrows(IOException.class,
                        () -> new NeedFileDAO("test.txt", mockObjectMapper),
                            "IOException not thrown");
    }
}