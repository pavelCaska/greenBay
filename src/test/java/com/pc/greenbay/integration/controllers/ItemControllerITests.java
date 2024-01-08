package com.pc.greenbay.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pc.greenbay.models.Bid;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.models.RequestDTOs.ItemRequestDTO;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.BidRepository;
import com.pc.greenbay.repositories.ItemRepository;
import com.pc.greenbay.repositories.PurchaseRepository;
import com.pc.greenbay.repositories.UserRepository;
import com.pc.greenbay.services.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemControllerITests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ItemService itemService;
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
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setup(){
        purchaseRepository.deleteAll();
        bidRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

    }

@Test
@DisplayName("Integration test for create item method when valid input given")
public void givenValidInput_whenCreateItem_thenReturnItemDTO() throws Exception {
    // given - precondition or setup
    UUID userId = UUID.randomUUID();
    User seller = User.builder()
            .id(userId)
            .username("user1")
            .password(userService.encodePassword("u12345"))
            .balance(100.0)
            .roles("ROLE_USER")
            .build();
    userRepository.save(seller);
    String authorizedUser = "Bearer ";
    authorizedUser += jwtService.generateToken("user1");

    ItemRequestDTO itemRequestDTO = new ItemRequestDTO("Lenovo", "tablet", "/img/green_fox_logo.png", 10, 50);

    // when - action or behaviour to be tested
    ResultActions response = mockMvc.perform(post("/api/item")
            .header("authorization", authorizedUser)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(itemRequestDTO)));

    // then - verify the output
    response.andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lenovo"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("tablet"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.photoURL").value("/img/green_fox_logo.png"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.starting_price").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.purchase_price").value(50));

    }

@Test
@DisplayName("Integration test for create item method when invalid price given")
public void givenInvalidPrice_whenCreateItem_thenReturnError() throws Exception {
    // given - precondition or setup
    UUID userId = UUID.randomUUID();
    User seller = User.builder()
            .id(userId)
            .username("user1")
            .password(userService.encodePassword("u12345"))
            .balance(100.0)
            .roles("ROLE_USER")
            .build();
    userRepository.save(seller);
    String authorizedUser = "Bearer ";
    authorizedUser += jwtService.generateToken("user1");

    ItemRequestDTO itemRequestDTO = new ItemRequestDTO("Lenovo", "tablet", "/img/green_fox_logo.png", 0, 0);

    // when - action or behaviour to be tested
    ResultActions response = mockMvc.perform(post("/api/item")
            .header("authorization", authorizedUser)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(itemRequestDTO)));

    // then - verify the output
    response.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.startingPrice").value("Starting price must be greater than or equal to 1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.purchasePrice").value("Purchase price must be greater than or equal to 1"));
    }

@Test
@DisplayName("Integration test for create item method when missing name & description")
public void givenMissingNameAndDescription_whenCreateItem_thenReturnError() throws Exception {
    // given - precondition or setup
    UUID userId = UUID.randomUUID();
    User seller = User.builder()
            .id(userId)
            .username("user1")
            .password(userService.encodePassword("u12345"))
            .balance(100.0)
            .roles("ROLE_USER")
            .build();
    userRepository.save(seller);
    String authorizedUser = "Bearer ";
    authorizedUser += jwtService.generateToken("user1");

    ItemRequestDTO itemRequestDTO = new ItemRequestDTO("", "", "/img/green_fox_logo.png", 0, 0);

    // when - action or behaviour to be tested
    ResultActions response = mockMvc.perform(post("/api/item")
            .header("authorization", authorizedUser)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(itemRequestDTO)));

    // then - verify the output
    response.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Item name is empty or missing."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Item description is empty or missing."));
    }

    @Test
    @DisplayName("Integration test for show item details method when valid input given")
    public void givenValidInput_whenShowItemDetails_thenReturnItemDetails() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
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
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password(userService.encodePassword("u23456"))
                .balance(200.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);

        Bid bid1 = Bid.builder()
                .id(1L)
                .item(item)
                .bidder(bidder)
                .bidAmount(20)
                .build();
        bidRepository.save(bid1);

        Bid bid2 = Bid.builder()
                .id(2L)
                .item(item)
                .bidder(bidder)
                .bidAmount(30)
                .build();
        bidRepository.save(bid2);

        Bid bid3 = Bid.builder()
                .id(3L)
                .item(item)
                .bidder(bidder)
                .bidAmount(40)
                .build();
        bidRepository.save(bid3);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/item/{id}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lenovo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("tablet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.photoURL").value("/img/green_fox_logo.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seller").value(seller.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bidList", Matchers.hasSize(3)));
    }
    @Test
    @DisplayName("Integration test for show item details method when invalid input given")
    public void givenInvalidInput_whenSHowItemDetails_thenReturnErrorDTO() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
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

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/item/{id}", UUID.randomUUID())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Item not found."));
    }

    @Test
    @DisplayName("Integration test for show item details method when valid input & not sellable")
    public void givenValidInputNotSellable_whenSHowItemDetails_thenReturnItemDetails() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

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
        ResultActions response = mockMvc.perform(get("/api/item/{id}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lenovo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("tablet"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.photoURL").value("/img/green_fox_logo.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seller").value(seller.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer").value(buyer.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buying_price").value(51));
    }

    @Test
    @DisplayName("Integration test for show item details method when valid input & not sellable & no purchase")
    public void givenItemNotSellableButNoPurchase_whenShowItemDetails_thenReturnErrorDTO() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

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

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/item/{id}", item.getId())
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Purchase record not found."));
    }

    @Test
    @DisplayName("Integration test for pagination method")
    public void givenValidInput_whenListItemsPages_thenReturnPageList() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(41)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID item2Id = UUID.randomUUID();
        Item item2 = Item.builder()
                .id(item2Id)
                .name("iPhone")
                .description("mobile phone")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(150)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item2);

        UUID item3Id = UUID.randomUUID();
        Item item3 = Item.builder()
                .id(item3Id)
                .name("iPad")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(250)
                .lastBid(151)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item3);

        UUID item4Id = UUID.randomUUID();
        Item item4 = Item.builder()
                .id(item4Id)
                .name("MacBook")
                .description("notebook")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(1000)
                .purchasePrice(1150)
                .lastBid(50)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item4);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/item")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()").value(3));
    }

    @Test
    @DisplayName("Integration test for pagination method when requesting page 2")
    public void givenValidInputPageTwo_whenListItemsPages_thenReturnPageList() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(41)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID item2Id = UUID.randomUUID();
        Item item2 = Item.builder()
                .id(item2Id)
                .name("iPhone")
                .description("mobile phone")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(150)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item2);

        UUID item3Id = UUID.randomUUID();
        Item item3 = Item.builder()
                .id(item3Id)
                .name("iPad")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(250)
                .lastBid(151)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item3);

        UUID item4Id = UUID.randomUUID();
        Item item4 = Item.builder()
                .id(item4Id)
                .name("MacBook")
                .description("notebook")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(1000)
                .purchasePrice(1150)
                .lastBid(50)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item4);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/item")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "2"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()").value(1));
    }

    @Test
    @DisplayName("Integration test for pagination method when invalid parameter")
    public void givenInvalidParameter_whenListItemsPages_thenReturnErrorDTO() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(41)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID item2Id = UUID.randomUUID();
        Item item2 = Item.builder()
                .id(item2Id)
                .name("iPhone")
                .description("mobile phone")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(150)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item2);

        UUID item3Id = UUID.randomUUID();
        Item item3 = Item.builder()
                .id(item3Id)
                .name("iPad")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(250)
                .lastBid(151)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item3);

        UUID item4Id = UUID.randomUUID();
        Item item4 = Item.builder()
                .id(item4Id)
                .name("MacBook")
                .description("notebook")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(1000)
                .purchasePrice(1150)
                .lastBid(50)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item4);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/item")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "4"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("There is no page: 4"));
    }

    @Test
    @DisplayName("Integration test for helper show method")
    public void givenValidInput_whenShow_thenReturnItemList() throws Exception {
        // given - precondition or setup
        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");

        UUID itemId = UUID.randomUUID();
        Item item = Item.builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(41)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        UUID item2Id = UUID.randomUUID();
        Item item2 = Item.builder()
                .id(item2Id)
                .name("iPhone")
                .description("mobile phone")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(150)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item2);

        UUID item3Id = UUID.randomUUID();
        Item item3 = Item.builder()
                .id(item3Id)
                .name("iPad")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(250)
                .lastBid(151)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item3);

        UUID item4Id = UUID.randomUUID();
        Item item4 = Item.builder()
                .id(item4Id)
                .name("MacBook")
                .description("notebook")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(1000)
                .purchasePrice(1150)
                .lastBid(50)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item4);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/show")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4));
    }
}
