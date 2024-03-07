package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedCheckout;

/**
 * Defines the interface for Need object persistence
 *
 * @author SWEN Faculty
 */
public interface NeedCheckoutDAO {
    NeedCheckout getNeedCheckout(String username) throws IOException;
    NeedCheckout[] getNeedCheckouts() throws IOException;

    NeedCheckout createNeedCheckout(NeedCheckout need) throws IOException;

    NeedCheckout updateNeedCheckout(NeedCheckout needCheckout) throws IOException;
}
