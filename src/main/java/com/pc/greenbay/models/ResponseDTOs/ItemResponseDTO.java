package com.pc.greenbay.models.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemResponseDTO {

    @JsonProperty("item_ID")
    private String itemID;
    private String name;
    private String description;
    private String photoURL;
    @JsonProperty("starting_price")
    private Integer startingPrice;
    @JsonProperty("purchase_price")
    private Integer purchasePrice;

    public ItemResponseDTO() {
    }

    public ItemResponseDTO(String itemID, String name, String description, String photoURL, Integer startingPrice, Integer purchasePrice) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.startingPrice = startingPrice;
        this.purchasePrice = purchasePrice;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Integer getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Integer startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
