package com.pc.greenbay.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

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

    public Purchase(Item item, User buyer, int purchaseAmount) {
        this.item = item;
        this.buyer = buyer;
        this.purchaseAmount = purchaseAmount;
    }
}
