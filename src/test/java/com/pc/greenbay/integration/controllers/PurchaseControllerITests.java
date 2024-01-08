package com.pc.greenbay.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.BidRepository;
import com.pc.greenbay.repositories.ItemRepository;
import com.pc.greenbay.repositories.PurchaseRepository;
import com.pc.greenbay.repositories.UserRepository;
import com.pc.greenbay.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseControllerITests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PurchaseService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BidService bidService;
    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setup() {
        purchaseRepository.deleteAll();
        bidRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration test for delete purchase method")
    public void givenValidPurchaseId_whenSHowDeletePurchase_thenDeletePurchase() throws Exception {
        // given - precondition or setup
        UUID adminId = UUID.randomUUID();
        User admin = User.builder()
                .id(adminId)
                .username("admin")
                .password(userService.encodePassword("A12345"))
                .balance(0.0)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("admin");

        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(51)
                .sellable(false)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID buyerId = UUID.randomUUID();
        User buyer = User.builder()
                .id(buyerId)
                .username("user2")
                .password(userService.encodePassword("u23456"))
                .balance(149.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(buyer);

        Purchase purchase = Purchase.builder()
                .id(1L)
                .item(item)
                .buyer(buyer)
                .purchaseAmount(51)
                .build();
        purchaseRepository.save(purchase);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(delete("/api/purchase/{id}/delete", purchase.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string("Purchase deleted successfully."));
    }

    @Test
    @DisplayName("Integration test for delete purchase method")
    public void givenInvalidPurchaseId_whenSHowDeletePurchase_thenException() throws Exception {
        // given - precondition or setup
        UUID adminId = UUID.randomUUID();
        User admin = User.builder()
                .id(adminId)
                .username("admin")
                .password(userService.encodePassword("A12345"))
                .balance(0.0)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("admin");

        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(51)
                .sellable(false)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID buyerId = UUID.randomUUID();
        User buyer = User.builder()
                .id(buyerId)
                .username("user2")
                .password(userService.encodePassword("u23456"))
                .balance(149.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(buyer);

        Purchase purchase = Purchase.builder()
                .id(1L)
                .item(item)
                .buyer(buyer)
                .purchaseAmount(51)
                .build();
        purchaseRepository.save(purchase);

        long purchaseId = 2L;

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(delete("/api/purchase/{id}/delete", purchaseId)
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("PurchaseID not found!"));
    }
}