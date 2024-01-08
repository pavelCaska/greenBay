package com.pc.greenbay.repositories;

import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findByItem(Item item);
}
