package com.pc.greenbay.models.RequestDTOs;

public class BidRequestDTO {
    private int bidAmount;

    public BidRequestDTO() {
    }
    public BidRequestDTO(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }
}
