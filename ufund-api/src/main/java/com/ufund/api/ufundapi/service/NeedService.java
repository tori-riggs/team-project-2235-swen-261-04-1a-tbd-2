package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedCheckout;
import com.ufund.api.ufundapi.persistence.NeedCheckoutDAO;
import com.ufund.api.ufundapi.persistence.NeedDAO;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class NeedService {
    private NeedDAO needDAO;
    private NeedCheckoutDAO needCheckoutDAO;
    public NeedService(NeedDAO needDAO, NeedCheckoutDAO needCheckoutDAO) {
        this.needDAO = needDAO;
        this.needCheckoutDAO = needCheckoutDAO;
    }

    public Need getNeedFromCupboard(int id) throws IOException {
        return needDAO.getNeed(id);
    }

    public Need[] getNeedsFromCupboard() throws IOException {
        return needDAO.getNeeds();
    }

    public Need[] findMatchingNeedsFromCupboard(String name) throws IOException {
        return needDAO.findNeeds(name);
    }

    public Need createNeedInCupboard(Need need) throws IOException {
        return needDAO.createNeed(need);
    }

    public Need updateNeedInCupboard(Need need) throws IOException {
        return needDAO.updateNeed(need);
    }

    public boolean deleteNeedFromCupboard(int id) throws IOException {
        //remove from cupboard
        boolean deleted = needDAO.deleteNeed(id);
        //remove this id from each funding basket
        NeedCheckout[] fundingBaskets = needCheckoutDAO.getNeedCheckouts();
        for (NeedCheckout checkout: fundingBaskets) {
            removeNeedFromFundingBasket(checkout.getUsername(), id);
        }
        return deleted;
    }

    public NeedCheckout getFundingBasket(String username) throws IOException {
        NeedCheckout checkout = needCheckoutDAO.getNeedCheckout(username);
        if(checkout == null) {
            checkout = needCheckoutDAO.createNeedCheckout(new NeedCheckout(username, new int[0]));
        }
        return checkout;
    }

    public NeedCheckout addNeedToFundingBasket(String username, int id) throws IOException {
        //if there is no need checkout for username, create a new one
        NeedCheckout checkout = needCheckoutDAO.getNeedCheckout(username);
        if(checkout == null) {
            checkout = needCheckoutDAO.createNeedCheckout(new NeedCheckout(username, new int[0]));
        }
        //remove id if it's currently in there
        checkout.setCheckoutIds(Arrays.stream(checkout.getCheckoutIds()).filter(x -> x!=id).toArray());
        //add id to the need checkout
        int[] newArray = new int[checkout.getCheckoutIds().length+1];
        for(int i = 0; i<checkout.getCheckoutIds().length; i++) {
            newArray[i] = checkout.getCheckoutIds()[i];
        }
        newArray[newArray.length-1] = id;
        checkout.setCheckoutIds(newArray);
        return needCheckoutDAO.updateNeedCheckout(checkout);
    }

    public NeedCheckout removeNeedFromFundingBasket(String username, int id) throws IOException {
        NeedCheckout checkout = needCheckoutDAO.getNeedCheckout(username);
        //if this need is in a cart, remove it
        checkout.setCheckoutIds(Arrays.stream(checkout.getCheckoutIds()).filter(x -> x!=id).toArray());
        //update
        return needCheckoutDAO.updateNeedCheckout(checkout);
    }
}
