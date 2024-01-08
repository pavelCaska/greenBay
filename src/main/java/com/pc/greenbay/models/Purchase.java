package com.pc.greenbay.models;

import jakarta.persistence.*;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    private int purchaseAmount;

    public Purchase() {
    }

    public Purchase(Item item, User buyer, int purchaseAmount) {
        this.item = item;
        this.buyer = buyer;
        this.purchaseAmount = purchaseAmount;
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

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public int getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(int purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    // Builder class for fluent construction of Purchase instances
    public static class Builder {
        private Long id;
        private Item item;
        private User buyer;
        private int purchaseAmount;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder item(Item item) {
            this.item = item;
            return this;
        }

        public Builder buyer(User buyer) {
            this.buyer = buyer;
            return this;
        }

        public Builder purchaseAmount(int purchaseAmount) {
            this.purchaseAmount = purchaseAmount;
            return this;
        }

        public Purchase build() {
            return new Purchase(this);
        }
    }

    // Private constructor to be used by the builder
    private Purchase(Builder builder) {
        this.id = builder.id;
        this.item = builder.item;
        this.buyer = builder.buyer;
        this.purchaseAmount = builder.purchaseAmount;
    }
}
