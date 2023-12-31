package com.pc.greenbay.models.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class BidListDTO {

    private Long id;
    private String bidderUsername;
    private int bidAmount;

}
