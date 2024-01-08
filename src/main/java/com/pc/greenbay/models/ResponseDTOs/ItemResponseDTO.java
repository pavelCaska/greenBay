package com.pc.greenbay.models.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ItemResponseDTO {

    @JsonProperty("item_ID")
    private String itemID;
    private String name;
    private String description;
    private String photoURL;
    @JsonProperty("starting_price")
    private Integer startingPrice;
    @JsonProperty("purchase_price")
    private Integer purchasePrice;

}
