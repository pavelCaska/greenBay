package com.pc.greenbay.services;

import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.repositories.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public Optional<Purchase> getPurchaseByItem(Item item) {
        return purchaseRepository.findByItem(item);
    }

//    @Override
//    public DeletePurchaseResponseDTO deletePurchase(Long id) {
//        DeletePurchaseResponseDTO response = new DeletePurchaseResponseDTO();
//        try {
//            purchaseRepository.deleteById(id);
//            response.setSuccess(true);
//            response.setMessage("Purchase deleted successfully.");
//        } catch (Exception e) {
//            response.setSuccess(false);
////            response.setMessage("Failed to delete purchase.");
//            response.setMessage(e.getMessage());
//        }
//        return response;
//    }

    @Override
    public void deletePurchase(Long id) throws Exception {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow();
        try {
            purchaseRepository.delete(purchase);
        } catch (Exception e) {
            throw new Exception("Database failure");
        }
    }
}
