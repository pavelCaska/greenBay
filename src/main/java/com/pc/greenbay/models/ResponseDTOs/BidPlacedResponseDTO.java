package com.pc.greenbay.models.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BidPlacedResponseDTO extends BidCommonResponseDTO {
    @JsonProperty("bid_placed")
    private int bidAmount;

    public BidPlacedResponseDTO() {
    }

    public BidPlacedResponseDTO(String name, String description, String photoURL, String sellerUsername, int bidAmount) {
        super(name, description, photoURL, sellerUsername);
        this.bidAmount = bidAmount;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }
}
