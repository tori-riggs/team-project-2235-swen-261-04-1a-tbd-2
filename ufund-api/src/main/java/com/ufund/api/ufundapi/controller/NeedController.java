package com.ufund.api.ufundapi.controller;

import com.ufund.api.ufundapi.enums.AuthLevel;
import com.ufund.api.ufundapi.enums.SortingOption;
import com.ufund.api.ufundapi.model.NeedCheckout;
import com.ufund.api.ufundapi.service.AuthService;
import com.ufund.api.ufundapi.service.NeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Need;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Need resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 *
 * @author SWEN Faculty
 */

@RestController
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedService needService;
    private AuthService authService;


    public NeedController(NeedService needService, AuthService authService) {
        this.needService = needService;
        this.authService = authService;
    }

    /**
     * Responds to the GET request for a {@linkplain Need need} for the given id
     *
     * @param id The id used to locate the {@link Need need}
     *
     * @return ResponseEntity with {@link Need need} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * @throws IOException
     */
    @GetMapping("cupboard/{id}")
    public ResponseEntity<Need> get(@PathVariable int id) throws IOException {
        LOG.info("GET /needs/" + id);
        try {
            Need need = needService.getNeedFromCupboard(id);
            if (need != null)
                return new ResponseEntity<Need>(need,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Need needs}
     *
     * @return ResponseEntity with array of {@link Need need} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("cupboard")
    public ResponseEntity<Need[]> getNeeds() {
        LOG.info("GET /needs");

        try {
            Need[] needs = needService.getNeedsFromCupboard();
            return new ResponseEntity<>(needs, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** 
    @GetMapping("cupboard")
    public ResponseEntity<Need[]> getNeeds(@RequestParam SortingOption sortingOption) {
        LOG.info("GET /needs/cupboard with sorting option");

        try {
            Need[] needs = needService.getNeedsFromCupboard(sortingOption);
            return new ResponseEntity<>(needs, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    /**
     * Responds to the GET request for all {@linkplain Need needs} whose name contains
     * the text in name
     *
     * @param name The name parameter which contains the text used to find the {@link Need needs}
     *
     * @return ResponseEntity with array of {@link Need need} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all needs that contain the text "ma"
     * GET http://localhost:8080/needs/?name=ma
     */
    @GetMapping("cupboard/")
    public ResponseEntity<Need[]> searchNeeds(@RequestParam String name) {
        LOG.info("GET /needs/?name="+name);

        try {
            Need[] foundNeed = needService.findMatchingNeedsFromCupboard(name);
            return new ResponseEntity<>(foundNeed, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Need need} with the provided need object
     *
     * @param need - The {@link Need need} to create
     *
     * @return ResponseEntity with created {@link Need need} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Need need} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("cupboard")
    public ResponseEntity<Need> createNeed(@RequestBody Need need, @RequestParam String username, @RequestParam String password) {
        LOG.info("POST /needs " + need);

        try {
            if(!authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Need newNeed = needService.createNeedInCupboard(need);
            if(newNeed == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(newNeed, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Need need} with the provided {@linkplain Need need} object, if it exists
     *
     * @param need The {@link Need need} to update
     *
     * @return ResponseEntity with updated {@link Need need} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("cupboard")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need, @RequestParam String username, @RequestParam String password) {
        LOG.info("PUT /needs " + need);

        try {
            if(!authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Need updated = needService.updateNeedInCupboard(need);
            if(updated == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Need need} with the given id
     *
     * @param id The id of the {@link Need need} to deleted
     *
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("cupboard/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id, @RequestParam String username, @RequestParam String password) {
        LOG.info("DELETE /needs/" + id);

        if(!authService.hasPermissionLevel(username, password, AuthLevel.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            boolean deleted = needService.deleteNeedFromCupboard(id);
            if(deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("funding-basket")
    public ResponseEntity<NeedCheckout> getNeedCheckout(@RequestParam String username, @RequestParam String password) {
        try {
            if(!authService.hasPermissionLevel(username, password, AuthLevel.USER)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            NeedCheckout checkout = needService.getFundingBasket(username);
            return new ResponseEntity<>(checkout, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("funding-basket")
    public ResponseEntity<NeedCheckout> addNeedToFundingBasket(@RequestParam int id, @RequestBody int quantity, @RequestParam String username, @RequestParam String password) {
        LOG.info("add to funding basket");
        try {
            if(!authService.hasPermissionLevel(username, password, AuthLevel.USER)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            needService.addNeedToFundingBasket(username, id, quantity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("funding-basket")
    public ResponseEntity<NeedCheckout> removeNeedFromFundingBasket(@RequestParam int id, @RequestParam String username, @RequestParam String password) {
        try {
            if(!authService.hasPermissionLevel(username, password, AuthLevel.USER)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            needService.removeNeedFromFundingBasket(username, id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("funding-basket/checkout")
    public ResponseEntity<NeedCheckout> checkout(@RequestParam String username, @RequestParam String password) {
        try {
            if(!authService.hasPermissionLevel(username, password, AuthLevel.USER)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            needService.checkout(username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
