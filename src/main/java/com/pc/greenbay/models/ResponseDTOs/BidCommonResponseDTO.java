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

public class BidCommonResponseDTO {
    private String name;
    private String description;
    private String photoURL;
    @JsonProperty("seller")
    private String sellerUsername;

}
