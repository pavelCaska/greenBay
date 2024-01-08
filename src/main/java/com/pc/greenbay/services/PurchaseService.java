package com.pc.greenbay.services;

import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;

import java.util.Optional;

public interface PurchaseService {

    Purchase savePurchase(Purchase purchase);

    Optional<Purchase> getPurchaseByItem(Item item);

    void deletePurchase(Long id) throws Exception;
}
