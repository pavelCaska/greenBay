package com.pc.greenbay.models.ResponseDTOs;

import java.util.UUID;

public class ItemListDTO {
    private UUID id;
    private String name;
    private String description;
    private String photoURL;
    private int lastBid;
    private boolean sellable;
    private String sellerUsername;

    public ItemListDTO() {
    }

    public ItemListDTO(UUID id, String name, String description, String photoURL, int lastBid, boolean sellable, String sellerUsername) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.lastBid = lastBid;
        this.sellable = sellable;
        this.sellerUsername = sellerUsername;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public int getLastBid() {
        return lastBid;
    }

    public void setLastBid(int lastBid) {
        this.lastBid = lastBid;
    }

    public boolean isSellable() {
        return sellable;
    }

    public void setSellable(boolean sellable) {
        this.sellable = sellable;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
}
