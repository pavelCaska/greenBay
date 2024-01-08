package com.pc.greenbay.models.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor

public class ItemSellableResponseDTO extends ItemCommonResponseDTO {
    private List<BidListDTO> bidList;

    public ItemSellableResponseDTO(String name, String description, String photoURL, String sellerUsername, List<BidListDTO> bidList) {
        super(name, description, photoURL, sellerUsername);
        this.bidList = bidList;
    }
}
