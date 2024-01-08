package com.pc.greenbay.models;

import jakarta.persistence.*;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "bidder_id")
    private User bidder;

    private int bidAmount;

    public Bid() {
    }

    public Bid(Item item, User bidder, int bidAmount) {
        this.item = item;
        this.bidder = bidder;
        this.bidAmount = bidAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    // Builder class for fluent construction of Bid instances
    public static class Builder {
        private Long id;
        private Item item;
        private User bidder;
        private int bidAmount;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder item(Item item) {
            this.item = item;
            return this;
        }

        public Builder bidder(User bidder) {
            this.bidder = bidder;
            return this;
        }

        public Builder bidAmount(int bidAmount) {
            this.bidAmount = bidAmount;
            return this;
        }

        public Bid build() {
            return new Bid(this);
        }
    }

    // Private constructor to be used by the builder
    private Bid(Builder builder) {
        this.id = builder.id;
        this.item = builder.item;
        this.bidder = builder.bidder;
        this.bidAmount = builder.bidAmount;
    }
}
