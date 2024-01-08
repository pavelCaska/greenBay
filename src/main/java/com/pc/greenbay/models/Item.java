package com.pc.greenbay.models;

import com.pc.greenbay.models.RequestDTOs.ItemRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

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
}
