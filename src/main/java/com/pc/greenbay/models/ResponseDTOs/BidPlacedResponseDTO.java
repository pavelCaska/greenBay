package com.pc.greenbay.models.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor

public class BidPlacedResponseDTO extends BidCommonResponseDTO {
    @JsonProperty("bid_placed")
    private int bidAmount;

    public BidPlacedResponseDTO(String name, String description, String photoURL, String sellerUsername, int bidAmount) {
        super(name, description, photoURL, sellerUsername);
        this.bidAmount = bidAmount;
    }

}
