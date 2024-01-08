package com.pc.greenbay.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.RequestDTOs.BidRequestDTO;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.BidRepository;
import com.pc.greenbay.repositories.ItemRepository;
import com.pc.greenbay.repositories.PurchaseRepository;
import com.pc.greenbay.repositories.UserRepository;
import com.pc.greenbay.services.BidService;
import com.pc.greenbay.services.JwtService;
import com.pc.greenbay.services.PurchaseService;
import com.pc.greenbay.services.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class BidControllerITests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BidService bidService;
    @Autowired
    private BidRepository bidRepository;
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
    private ObjectMapper om;

    @BeforeEach
    public void setup() {
        purchaseRepository.deleteAll();
        bidRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration test for place bid method when bidder has no greenBay dollars")
    public void givenValidInput_whenPlaceBid_thenThrowNoMoneyException() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID bidderId = UUID.randomUUID();
        User bidder = new User.Builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user2");

        BidRequestDTO bidRequestDTO = new BidRequestDTO(49);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/bid/{itemId}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(bidRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("You have no greenBay dollars, you can't bid."));
    }

    @Test
    @DisplayName("Integration test for place bid method when item not found")
    public void givenValidInput_whenPlaceBid_thenItemNotFoundException() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);
        UUID invalidItemId = UUID.randomUUID();

        UUID bidderId = UUID.randomUUID();
        User bidder = new User.Builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user2");

        BidRequestDTO bidRequestDTO = new BidRequestDTO(49);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/bid/{itemId}", invalidItemId)
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(bidRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Item not found."));
    }

    @Test
    @DisplayName("Integration test for place bid method when bidder equals seller")
    public void givenValidInput_whenPlaceBid_thenThrowBidderIsSellerException() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        User bidder = seller;
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

        BidRequestDTO bidRequestDTO = new BidRequestDTO(49);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/bid/{itemId}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(bidRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("You cannot bid on your own item."));
    }

    @Test
    @DisplayName("Integration test for place bid method when item is not sellable")
    public void givenValidInput_whenPlaceBid_thenThrowNotSellableException() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .sellable(false)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID bidderId = UUID.randomUUID();
        User bidder = new User.Builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user2");

        BidRequestDTO bidRequestDTO = new BidRequestDTO(49);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/bid/{itemId}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(bidRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Item is not sellable."));
    }

    @Test
    @DisplayName("Integration test for place bid method when bidder has not enough money")
    public void givenValidInput_whenPlaceBid_thenThrowNotEnoughMoneyException() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID bidderId = UUID.randomUUID();
        User bidder = new User.Builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(9)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user2");

        BidRequestDTO bidRequestDTO = new BidRequestDTO(49);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/bid/{itemId}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(bidRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("You have not enough greenBay dollars on your account."));
    }

    @Test
    @DisplayName("Integration test for place bid method when bid is too low")
    public void givenValidInput_whenPlaceBid_thenThrowBitTooLowException() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(11)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID bidderId = UUID.randomUUID();
        User bidder = new User.Builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user2");

        BidRequestDTO bidRequestDTO = new BidRequestDTO(11);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/bid/{itemId}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(bidRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Your bid is too low."));
    }

    @Test
    @DisplayName("Integration test for place new bid method")
    public void givenValidInput_whenPlaceBid_thenSaveBidAndReturnDTO() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(20)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID bidderId = UUID.randomUUID();
        User bidder = new User.Builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user2");

        BidRequestDTO bidRequestDTO = new BidRequestDTO(25);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/bid/{itemId}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(bidRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lenovo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("tablet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.photoURL").value("/img/green_fox_logo.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seller").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bid_placed").value("25"));
    }

    @Test
    @DisplayName("Integration test for place when item is purchased")
    public void givenValidInput_whenPlaceBid_thenPurchaseItemAndReturnDTO() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(20)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID bidderId = UUID.randomUUID();
        User bidder = new User.Builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user2");

        BidRequestDTO bidRequestDTO = new BidRequestDTO(51);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/bid/{itemId}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(bidRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lenovo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("tablet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.photoURL").value("/img/green_fox_logo.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seller").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer").value("user2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bought_at").value("51"));
    }
}
