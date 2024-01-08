package com.pc.greenbay.models.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemBoughtResponseDTO extends BidCommonResponseDTO {
    @JsonProperty("buyer")
    private String buyerUsername;
    @JsonProperty("bought_at")
    private int buyingPrice;

    public ItemBoughtResponseDTO() {
    }

    public ItemBoughtResponseDTO(String name, String description, String photoURL, String sellerUsername, String buyerUsername, int buyingPrice) {
        super(name, description, photoURL, sellerUsername);
        this.buyerUsername = buyerUsername;
        this.buyingPrice = buyingPrice;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }
}
