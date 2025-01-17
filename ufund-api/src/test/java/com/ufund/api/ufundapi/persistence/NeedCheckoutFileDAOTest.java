package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.NeedCheckout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Persistence-tier")
public class NeedCheckoutFileDAOTest {
    NeedCheckoutFileDAO needCheckoutFileDAO;
    NeedCheckout[] testNeedCheckoutArr;
    // int[][] checkoutIdsArr;
    Map<String, NeedCheckout> testNeedCheckoutMap;
    ObjectMapper mockObjectMapper;
    // String filename;

    @BeforeEach
    public void setupNeedCheckoutFileDAO() throws IOException {
        /**
         * NeedCheckout is the object in the NeedCheckoutDAO.
         * NeedCheckoutFile is a json that maps an ID to a username string
         */
        mockObjectMapper = mock(ObjectMapper.class);
        testNeedCheckoutMap = new HashMap<>();
        
        testNeedCheckoutArr = new NeedCheckout[3];
        // checkoutIdsArr = new int[3][];
        Map<Integer, Integer> testCheckoutIdsArr1 = new HashMap<>();
        testCheckoutIdsArr1.put(90, 1);
        testCheckoutIdsArr1.put(91, 10);
        testCheckoutIdsArr1.put(92, 5);
        Map<Integer, Integer> testCheckoutIdsArr2 = new HashMap<>();
        testCheckoutIdsArr2.put(93, 5);
        testCheckoutIdsArr2.put(94, 2);
        testCheckoutIdsArr2.put(95, 1);
        Map<Integer, Integer> testCheckoutIdsArr3 = new HashMap<>();
        testCheckoutIdsArr3.put(96, 3);
        testCheckoutIdsArr3.put(97, 2);
        testCheckoutIdsArr3.put(98, 10);
        // checkoutIdsArr[1] = testCheckoutIdsArr2;
        // checkoutIdsArr[2] = testCheckoutIdsArr3;
        
        testNeedCheckoutArr[0] = new NeedCheckout("Ingo", testCheckoutIdsArr1);
        testNeedCheckoutArr[1] = new NeedCheckout("Emmet", testCheckoutIdsArr2);
        testNeedCheckoutArr[2] = new NeedCheckout("Elesa", testCheckoutIdsArr3);
        
        testNeedCheckoutMap.put("Ingo", testNeedCheckoutArr[0]);
        testNeedCheckoutMap.put("Emmet", testNeedCheckoutArr[1]);
        testNeedCheckoutMap.put("Elesa", testNeedCheckoutArr[2]);
        
        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the NeedCheckout array above
        when(mockObjectMapper
            .readValue(new File("test.txt"), NeedCheckout[].class))
                .thenReturn(testNeedCheckoutArr);
        needCheckoutFileDAO = new NeedCheckoutFileDAO("test.txt", mockObjectMapper);
    }

    /**
     * Test the getNeedCheckoutArray method, which gets all the NeedCheckout arrays
     * in the file.
     */
    @Test
    public void testGetNeedCheckouts() throws IOException {
        NeedCheckout[] checkouts = needCheckoutFileDAO.getNeedCheckouts();

        assertEquals(checkouts.length, testNeedCheckoutArr.length);
    }

    @Test
    public void testGetNeedCheckout() throws IOException {
        NeedCheckout checkout = needCheckoutFileDAO.getNeedCheckout("Emmet");
        
        assertEquals(checkout, testNeedCheckoutArr[1]);
    }

    @Test
    public void testCreateNeedCheckout() throws IOException {
        Map<Integer, Integer> checkoutIds = new HashMap<>();
        checkoutIds.put(99, 5);
        checkoutIds.put(100, 15);
        checkoutIds.put(101, 8);
        NeedCheckout checkout = new NeedCheckout("N", checkoutIds);

        NeedCheckout result 
            = assertDoesNotThrow(() -> needCheckoutFileDAO.createNeedCheckout(checkout),
                                "Unexpected exception thrown");
        
        assertNotNull(result);
        NeedCheckout actual = needCheckoutFileDAO.getNeedCheckout(checkout.getUsername());
        assertEquals(actual.getCheckoutIds(), checkout.getCheckoutIds());
        assertEquals(actual.getUsername(), checkout.getUsername());
    }

    @Test
    public void testUpdateNeedCheckout() throws IOException {
        Map<Integer, Integer> checkoutIds = new HashMap<>();
        checkoutIds.put(99, 5);
        checkoutIds.put(100, 15);
        checkoutIds.put(101, 8);
        NeedCheckout checkout = new NeedCheckout("Ingo", checkoutIds);

        NeedCheckout result = assertDoesNotThrow(() -> needCheckoutFileDAO.updateNeedCheckout(checkout), "Unexpected exception thrown");

        assertNotNull(result);
        NeedCheckout actual = needCheckoutFileDAO.getNeedCheckout(checkout.getUsername());
        assertEquals(actual, checkout);
    }

    @Test
    public void testSaveException() throws IOException {
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(NeedCheckout[].class));
        
        Map<Integer, Integer> checkoutIds = new HashMap<>();
        checkoutIds.put(20, 1);
        NeedCheckout checkout = new NeedCheckout("Akari", checkoutIds);

        assertThrows(IOException.class,
                        () -> needCheckoutFileDAO.createNeedCheckout(checkout),
                            "IOException not thrown");
    }

    @Test
    public void testUpdateNeedCheckoutNotFound() {
        Map<Integer, Integer> checkoutIds = new HashMap<>();
        checkoutIds.put(20, 1);
        NeedCheckout checkout = new NeedCheckout("Akari", checkoutIds);

        NeedCheckout result 
            = assertDoesNotThrow(
                () -> needCheckoutFileDAO.updateNeedCheckout(checkout),
                    "Unexpected exception thrown");
        
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);

        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("test.txt"), NeedCheckout[].class);

        assertThrows(IOException.class, 
                        () -> new NeedCheckoutFileDAO("test.txt", mockObjectMapper),
                            "IOException not thrown");
    }
}
