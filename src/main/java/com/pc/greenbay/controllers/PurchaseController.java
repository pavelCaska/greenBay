package com.pc.greenbay.controllers;

import com.pc.greenbay.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")

public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @DeleteMapping("/purchase/{id}/delete")
    public ResponseEntity<String> deletePurchase(@PathVariable Long id) {
        try {
            purchaseService.deletePurchase(id);
            return new ResponseEntity<String>("Purchase deleted successfully.", HttpStatus.NO_CONTENT);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<String>("PurchaseID not found!", HttpStatus.BAD_REQUEST);
            
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
