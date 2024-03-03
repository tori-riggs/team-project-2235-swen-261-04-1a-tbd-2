package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Need Controller class
 * 
 * @author Team 1 
 */
@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedDAO mockNeedDAO;

    /**
     * Before each test, create a new NeedController object and inject
     * a mock Need DAO
     */
    @BeforeEach
    public void setupNeedController() {
        mockNeedDAO = mock(NeedDAO.class);
        needController = new NeedController(mockNeedDAO);
    }

    @Test
    public void testGetNeed() throws IOException {  // getNeed may throw IOException
        // Setup
        Need need = new Need(99,"Mittens", 10, 5, "A pair of mittens.");
        // When the same id is passed in, our mock Need DAO will return the Need object
        when(mockNeedDAO.getNeed(need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.get(need.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When the same id is passed in, our mock Need DAO will return null, simulating
        // no need found
        when(mockNeedDAO.getNeed(needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.get(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.get(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all NeedController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateNeed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(95,"Socks", 7, 20, "A pair of socks.");
        // when createNeed is called, return true simulating successful
        // creation and save
        when(mockNeedDAO.createNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testCreateNeedFailed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(95,"Socks", 7, 20, "A pair of socks.");
        // when createNeed is called, return false simulating failed
        // creation and save
        when(mockNeedDAO.createNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(95,"Socks", 7, 20, "A pair of socks.");

        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).createNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(95,"Socks", 7, 20, "A pair of socks.");
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setCost(20);

        // Invoke
        response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(95,"Socks", 7, 20, "A pair of socks.");
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(95,"Socks", 7, 20, "A pair of socks.");
        // When updateNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws IOException { // getNeeds may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(95,"Socks", 7, 20, "A pair of socks.");
        needs[1] = new Need(96,"Socks2", 8, 21, "A pair of socks2.");
        needs[1] = new Need(99,"Mittens", 10, 5, "A pair of mittens.");
        // When getNeeds is called return the needs created above
        when(mockNeedDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { // getNeeds may throw IOException
        // Setup
        // When getNeeds is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchNeeds() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "la";
        Need[] needs = new Need[2];
        needs[0] = new Need(95,"Socks", 7, 20, "A pair of socks.");
        needs[1] = new Need(96,"Socks2", 8, 21, "A pair of socks2.");
        needs[1] = new Need(99,"Mittens", 10, 5, "A pair of mittens.");
        // When findNeeds is called with the search string, return the two
        /// needs above
        when(mockNeedDAO.findNeeds(searchString)).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testSearchNeedsHandleException() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "an";
        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).findNeeds(searchString);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // when deleteNeed is called return true, simulating successful deletion
        when(mockNeedDAO.deleteNeed(needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // when deleteNeed is called return false, simulating failed deletion
        when(mockNeedDAO.deleteNeed(needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // When deleteNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).deleteNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
