package com.pc.greenbay.models.RequestDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

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

    public ItemRequestDTO() {
    }

    public ItemRequestDTO(String name, String description, String photoURL, Integer startingPrice, Integer purchasePrice) {
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.startingPrice = startingPrice;
        this.purchasePrice = purchasePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Integer getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Integer startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
