package com.pc.greenbay.models.ResponseDTOs;

import java.util.List;

public class ItemSellableResponseDTO extends ItemCommonResponseDTO {
    private List<BidListDTO> bidList;

    public ItemSellableResponseDTO() {
    }

    public ItemSellableResponseDTO(String name, String description, String photoURL, String sellerUsername, List<BidListDTO> bidList) {
        super(name, description, photoURL, sellerUsername);
        this.bidList = bidList;
    }

    public List<BidListDTO> getBidList() {
        return bidList;
    }

    public void setBidList(List<BidListDTO> bidList) {
        this.bidList = bidList;
    }
}
