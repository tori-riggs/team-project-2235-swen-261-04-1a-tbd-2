package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.NeedCheckout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class NeedCheckoutFileDAO implements NeedCheckoutDAO {
    private static final Logger LOG = Logger.getLogger(NeedFileDAO.class.getName());
    Map<String, NeedCheckout> needCheckouts;   // Provides a local cache of the need objects

    private ObjectMapper objectMapper;  // Provides conversion between Need
    // objects and JSON text format written
    // to the file
    private String filename;    // Filename to read from and write to

    public NeedCheckoutFileDAO(@Value("${needCheckouts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the needs from the file
    }

    private NeedCheckout[] getNeedCheckoutArray() {
        ArrayList<NeedCheckout> needCheckoutArrayList = new ArrayList<>();

        for (NeedCheckout needCheckout : needCheckouts.values()) {
            needCheckoutArrayList.add(needCheckout);
        }

        NeedCheckout[] needCheckoutArray = new NeedCheckout[needCheckoutArrayList.size()];
        needCheckoutArrayList.toArray(needCheckoutArray);
        return needCheckoutArray;
    }

    private boolean save() throws IOException {
        NeedCheckout[] needCheckoutArray = getNeedCheckoutArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),needCheckoutArray);
        return true;
    }

    private boolean load() throws IOException {
        needCheckouts = new HashMap<>();

        // Deserializes the JSON objects from the file into an array of needCheckouts
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        NeedCheckout[] needCheckoutArray = objectMapper.readValue(new File(filename),NeedCheckout[].class);

        for (NeedCheckout needCheckout : needCheckoutArray) {
            needCheckouts.put(needCheckout.getUsername(),needCheckout);
        }
        return true;
    }

    @Override
    public NeedCheckout getNeedCheckout(String username) throws IOException {
        synchronized(needCheckouts) {
            if (needCheckouts.containsKey(username))
                return needCheckouts.get(username);
            else
                return null;
        }
    }

    @Override
    public NeedCheckout[] getNeedCheckouts() throws IOException {
        return getNeedCheckoutArray();
    }

    @Override
    public NeedCheckout createNeedCheckout(NeedCheckout needCheckout) throws IOException {
        synchronized(needCheckouts) {
            NeedCheckout newNeedCheckout = new NeedCheckout(needCheckout.getUsername(), needCheckout.getCheckoutIds());
            needCheckouts.put(newNeedCheckout.getUsername(), newNeedCheckout);
            save(); // may throw an IOException
            return newNeedCheckout;
        }
    }

    @Override
    public NeedCheckout updateNeedCheckout(NeedCheckout needCheckout) throws IOException {
        synchronized(needCheckouts) {
            if (needCheckouts.containsKey(needCheckout.getUsername()) == false)
                return null;  // need does not exist

            needCheckouts.put(needCheckout.getUsername(),needCheckout);
            save(); // may throw an IOException
            return needCheckout;
        }
    }
}
