package com.pc.greenbay.controllers;

import com.pc.greenbay.models.DTOs.ErrorDTO;
import com.pc.greenbay.models.RequestDTOs.ItemRequestDTO;
import com.pc.greenbay.models.User;
import com.pc.greenbay.services.ItemService;
import com.pc.greenbay.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping("/item")
    public ResponseEntity<?> createItem(
            @Valid @RequestBody ItemRequestDTO itemRequestDTO, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(itemService.buildErrorResponseForItemCreation(bindingResult));
        }

        User seller = userService.findByUsername(userDetails.getUsername()).orElse(null);
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(itemService.createItem(itemRequestDTO, seller));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(e.getMessage()));
        }
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<?> showItemDetails(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(itemService.showItemDetails(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(e.getMessage()));
        }
    }
    @GetMapping("/item")
    public ResponseEntity<?> listItemsPages(@RequestParam(name = "page", defaultValue = "1", required = false) int page) {
        if(page > 0) {
            return itemService.getItemsPaged(page);
        }
        return ResponseEntity.badRequest().body(new ErrorDTO("Invalid parameters"));
    }


    @GetMapping("/show")
    public ResponseEntity<?> showAllSellable() {
        return ResponseEntity.ok(itemService.listItems());
    }

}
