package com.pc.greenbay.models.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class ItemCommonResponseDTO {
    private String name;
    private String description;
    private String photoURL;
    @JsonProperty("seller")
    private String sellerUsername;

    public ItemCommonResponseDTO(String name, String description, String photoURL, String sellerUsername) {
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.sellerUsername = sellerUsername;
    }
}
