package com.pc.greenbay.models.ResponseDTOs;

import java.util.UUID;

public class ItemPageDTO {
    private UUID id;
    private String name;
//    private String description;
    private String photoURL;
    private int lastBid;
//    private boolean sellable;
    private String sellerUsername;

    public ItemPageDTO() {
    }

    public ItemPageDTO(UUID id, String name, String photoURL, int lastBid, String sellerUsername) {
        this.id = id;
        this.name = name;
        this.photoURL = photoURL;
        this.lastBid = lastBid;
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

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
}
