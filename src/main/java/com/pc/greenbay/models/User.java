package com.pc.greenbay.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true)
    private String username;
    private String password;
    private double balance;
    private String roles = "ROLE_USER";
    @OneToMany(mappedBy = "seller")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "bidder")
    private List<Bid> bids = new ArrayList<>();

    @OneToMany(mappedBy = "buyer")
    private List<Purchase> purchases = new ArrayList<>();

    public User(String username, String password,
                double balance, String roles) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.roles = roles;
    }
}
