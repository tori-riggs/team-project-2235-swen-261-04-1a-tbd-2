package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.enums.SortingOption;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedCheckout;
import com.ufund.api.ufundapi.persistence.NeedCheckoutDAO;
import com.ufund.api.ufundapi.persistence.NeedDAO;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

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

    public Need[] sortingNeedsFromCupboard(SortingOption sortingOption) throws IOException {
        Need[] needs = needDAO.getNeeds();
        System.out.println(needs.length);
        switch (sortingOption) {
            case ALPHABETICAL:
                return sortAlphabetical(needs);
            case ALPHABETICAL_REVERSE:
                return sortAlphabeticalReverse(needs);
            case NUMERICAL:
                return sortNumerical(needs);
            case NUMERICAL_REVERSE:
                return sortNumericalReverse(needs);
        }
        System.out.println("test");
        return needs;
    }

    private Need[] sortAlphabetical(Need[] needs) {
        return streamToArray(Arrays.stream(needs).sorted((a, b) -> a.getName().compareTo(b.getName())));
    }

    private Need[] sortAlphabeticalReverse(Need[] needs) {
        return streamToArray(Arrays.stream(needs).sorted((a, b) -> b.getName().compareTo(a.getName())));
    }

    private Need[] sortNumerical(Need[] needs) {
        return streamToArray(Arrays.stream(needs).sorted((a, b) -> Math.min(1, Math.max(-1, a.getCost()-b.getCost()))));
    }

    private Need[] sortNumericalReverse(Need[] needs) {
        return streamToArray(Arrays.stream(needs).sorted((a, b) -> Math.min(1, Math.max(-1, b.getCost()-a.getCost()))));
    }

    private Need[] streamToArray(Stream<Need> stream) {
        return stream.toArray(Need[]::new);
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
            checkout = needCheckoutDAO.createNeedCheckout(new NeedCheckout(username, new HashMap<>()));
        }
        return checkout;
    }

    public NeedCheckout addNeedToFundingBasket(String username, int id, int quantity) throws IOException {
        //if there is no need checkout for username, create a new one
        NeedCheckout checkout = needCheckoutDAO.getNeedCheckout(username);
        if(checkout == null) {
            checkout = needCheckoutDAO.createNeedCheckout(new NeedCheckout(username, new HashMap<>()));
        }
        int currentQuantity = 0;
        //add current quantity if the id is already in chcekout
        if(checkout.getCheckoutIds().containsKey(id)) {
            currentQuantity = checkout.getCheckoutIds().get(id);
        }
        checkout.getCheckoutIds().put(id, quantity + currentQuantity);
        return needCheckoutDAO.updateNeedCheckout(checkout);
    }

    public NeedCheckout removeNeedFromFundingBasket(String username, int id) throws IOException {
        NeedCheckout checkout = needCheckoutDAO.getNeedCheckout(username);
        //if this need is in a cart, remove it
        if (checkout.getCheckoutIds().containsKey(id)) {
            checkout.getCheckoutIds().remove(id);
        }
        //update
        return needCheckoutDAO.updateNeedCheckout(checkout);
    }

    public NeedCheckout checkout(String username) throws IOException {
        NeedCheckout checkout = needCheckoutDAO.getNeedCheckout(username);
        Iterator<Map.Entry<Integer, Integer>> iterator = checkout.getCheckoutIds().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            Integer checkoutId = entry.getKey();
            Integer quant = entry.getValue(); 
            Need newNeed = needDAO.getNeed(checkoutId);
            newNeed.setQuantity(newNeed.getQuantity() - quant); //changes quantity by how many things are in the cart
            needDAO.updateNeed(newNeed);
            iterator.remove(); //removes entry from the checkout area
        }
        return needCheckoutDAO.updateNeedCheckout(checkout);
    }
}
