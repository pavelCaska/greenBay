package com.pc.greenbay.controllers;

import com.pc.greenbay.models.DTOs.ErrorDTO;
import com.pc.greenbay.models.RequestDTOs.BidRequestDTO;
import com.pc.greenbay.models.User;
import com.pc.greenbay.services.BidService;
import com.pc.greenbay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BidController {

    private final BidService bidService;
    private final UserService userService;

    @Autowired
    public BidController(BidService bidService, UserService userService) {
        this.bidService = bidService;
        this.userService = userService;
    }
    @PostMapping("/bid/{itemId}")
    public ResponseEntity<?> placeBidPost(@PathVariable UUID itemId,
                                          @RequestBody BidRequestDTO bidRequestDTO,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        User bidder = userService.findByUsername(userDetails.getUsername()).orElse(null);
        try {
            return ResponseEntity.ok(bidService.placeBid(itemId, bidder, bidRequestDTO.getBidAmount()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }
}
