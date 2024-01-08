package com.pc.greenbay.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

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

    public Bid(Item item, User bidder, int bidAmount) {
        this.item = item;
        this.bidder = bidder;
        this.bidAmount = bidAmount;
    }
}
