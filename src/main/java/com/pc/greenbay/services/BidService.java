package com.pc.greenbay.services;

import com.pc.greenbay.models.Bid;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.ResponseDTOs.BidCommonResponseDTO;
import com.pc.greenbay.models.ResponseDTOs.BidListDTO;
import com.pc.greenbay.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BidService {

    BidCommonResponseDTO placeBid(UUID itemId, User bidder, int bidAmount) throws Exception;

    List<BidListDTO> findBidsByItem(Item item);
}
