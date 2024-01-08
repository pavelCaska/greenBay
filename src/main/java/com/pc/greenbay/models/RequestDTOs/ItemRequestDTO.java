package com.pc.greenbay.models.RequestDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ItemRequestDTO {
    @NotBlank(message = "Item name is empty or missing.")
    private String name;
    @NotBlank(message = "Item description is empty or missing.")
    private String description;
    @NotBlank(message = "URL is empty or missing.")
    @Pattern(regexp = "^\\/img\\/[a-zA-Z0-9_-]+\\.(?:jpg|gif|png)$", message = "Invalid path")
//    @URL(message = "Please provide a valid URL")
    private String photoURL;
    @NotNull(message = "Starting price is empty or missing.")
    @Min(value = 1, message = "Starting price must be greater than or equal to 1" )
    private Integer startingPrice;
    @NotNull(message = "Purchase price is empty or missing.")
    @Min(value = 1, message = "Purchase price must be greater than or equal to 1")
    private Integer purchasePrice;

}
