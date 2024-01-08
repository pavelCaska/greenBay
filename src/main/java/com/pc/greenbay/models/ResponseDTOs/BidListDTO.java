package com.pc.greenbay.models.ResponseDTOs;

public class BidListDTO {

    private Long id;
    private String bidderUsername;
    private int bidAmount;

    public BidListDTO() {
    }

    public BidListDTO(Long id, String bidderUsername, int bidAmount) {
        this.id = id;
        this.bidderUsername = bidderUsername;
        this.bidAmount = bidAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBidderUsername() {
        return bidderUsername;
    }

    public void setBidderUsername(String bidderUsername) {
        this.bidderUsername = bidderUsername;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }
}
