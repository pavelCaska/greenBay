package com.pc.greenbay.repositories;

import com.pc.greenbay.models.Bid;
import com.pc.greenbay.models.Item;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByItem(Item item);
}
