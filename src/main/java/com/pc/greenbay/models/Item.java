package com.pc.greenbay.models;

import com.pc.greenbay.models.RequestDTOs.ItemRequestDTO;
import jakarta.persistence.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String photoURL;
    private int startingPrice;
    private int purchasePrice;
    private int lastBid = 0;
    private boolean sellable = true;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

    public Item() {
    }

    public Item(String name, String description, String photoURL, int startingPrice, int purchasePrice, int lastBid, boolean sellable, User seller) {
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.startingPrice = startingPrice;
        this.purchasePrice = purchasePrice;
        this.lastBid = lastBid;
        this.sellable = sellable;
        this.seller = seller;
    }
    public Item(String name, String description, String photoURL, int startingPrice, int purchasePrice, User seller) {
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.startingPrice = startingPrice;
        this.purchasePrice = purchasePrice;
        this.seller = seller;
    }

    public Item(ItemRequestDTO itemRequestDTO, User seller) {
        this.name = itemRequestDTO.getName();
        this.description = itemRequestDTO.getDescription();
        this.photoURL = itemRequestDTO.getPhotoURL();
        this.startingPrice = itemRequestDTO.getStartingPrice();
        this.purchasePrice = itemRequestDTO.getPurchasePrice();
        this.seller = seller;
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

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public boolean isSellable() {
        return sellable;
    }

    public void setSellable(boolean sellable) {
        this.sellable = sellable;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public int getLastBid() {
        return lastBid;
    }

    public void setLastBid(int lastBid) {
        this.lastBid = lastBid;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private String photoURL;
        private int startingPrice;
        private int purchasePrice;
        private int lastBid = 0;
        private boolean sellable = true;
        private User seller;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder photoURL(String photoURL) {
            this.photoURL = photoURL;
            return this;
        }

        public Builder startingPrice(int startingPrice) {
            this.startingPrice = startingPrice;
            return this;
        }

        public Builder purchasePrice(int purchasePrice) {
            this.purchasePrice = purchasePrice;
            return this;
        }

        public Builder lastBid(int lastBid) {
            this.lastBid = lastBid;
            return this;
        }

        public Builder sellable(boolean sellable) {
            this.sellable = sellable;
            return this;
        }

        public Builder seller(User seller) {
            this.seller = seller;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }

    // Private constructor to be used by the builder
    private Item(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.photoURL = builder.photoURL;
        this.startingPrice = builder.startingPrice;
        this.purchasePrice = builder.purchasePrice;
        this.lastBid = builder.lastBid;
        this.sellable = builder.sellable;
        this.seller = builder.seller;
    }
}
