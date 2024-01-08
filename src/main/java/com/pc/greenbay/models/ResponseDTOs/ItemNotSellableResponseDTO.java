package com.pc.greenbay.models.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class ItemNotSellableResponseDTO extends ItemCommonResponseDTO {
    @JsonProperty("buyer")
    private String buyerUsername;
    @JsonProperty("buying_price")
    private int buyingPrice;

    public ItemNotSellableResponseDTO(String name, String description, String photoURL, String sellerUsername, String buyerUsername, int buyingPrice) {
        super(name, description, photoURL, sellerUsername);
        this.buyerUsername = buyerUsername;
        this.buyingPrice = buyingPrice;
    }

}
