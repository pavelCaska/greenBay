package com.pc.greenbay.models.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ItemPageDTO {
    private UUID id;
    private String name;
//    private String description;
    private String photoURL;
    private int lastBid;
//    private boolean sellable;
    private String sellerUsername;

}
