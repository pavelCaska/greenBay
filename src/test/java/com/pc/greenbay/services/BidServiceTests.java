package com.pc.greenbay.services;

import com.pc.greenbay.models.Bid;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.models.ResponseDTOs.BidListDTO;
import com.pc.greenbay.models.ResponseDTOs.BidPlacedResponseDTO;
import com.pc.greenbay.models.ResponseDTOs.ItemBoughtResponseDTO;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.BidRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BidServiceTests {

    @Mock
    private BidRepository bidRepository;
    @Mock
    private ItemService itemService;
    @Mock
    private PurchaseService purchaseService;
    @Mock
    private UserService userService;
    @InjectMocks
    private BidServiceImpl bidService;

    private Item item;

    private User seller;

    @BeforeEach
    public void setup() {
        UUID sellerId = UUID.randomUUID();
        seller = User.builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();

    }

    @Test
    @DisplayName("JUnit test for place bid method when bidder has no greenBay dollars")
    public void givenValidInput_whenPlaceBid_thenThrowNoMoneyException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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


        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(0)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 49;

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));

        // when - action or behaviour to be tested
        Exception exception = assertThrows(Exception.class, () -> {
            bidService.placeBid(item.getId(), bidder, bidAmount);
        });

        // then - assert and verify
        assertEquals("You have no greenBay dollars, you can't bid.", exception.getMessage());

        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
    }

    @Test
    @DisplayName("JUnit test for place bid method when item not found")
    public void givenValidInput_whenPlaceBid_thenItemNotFoundException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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
        UUID invalidItemId = UUID.randomUUID();

        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 49;
        given(itemService.getItemById(invalidItemId)).willReturn(Optional.empty());

        // when - action or behaviour to be tested
        Exception exception = assertThrows(Exception.class, () -> {
            bidService.placeBid(invalidItemId, bidder, bidAmount);
        });

        // then - assert and verify
        assertEquals("Item not found.", exception.getMessage());

        // Additional verification
        verify(itemService, times(1)).getItemById(invalidItemId);
    }

    @Test
    @DisplayName("JUnit test for place bid method when bidder equals seller")
    public void givenValidInput_whenPlaceBid_thenThrowBidderIsSellerException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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

        User bidder = seller;
        int bidAmount = 49;

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));

        // when - action or behaviour to be tested
        Exception exception = assertThrows(Exception.class, () -> {
            bidService.placeBid(item.getId(), bidder, bidAmount);
        });

        // then - assert and verify
        assertEquals("You cannot bid on your own item.", exception.getMessage());

        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
    }

    @Test
    @DisplayName("JUnit test for place bid method when item is not sellable")
    public void givenValidInput_whenPlaceBid_thenThrowNotSellableException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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


        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 49;

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));

        // when - action or behaviour to be tested
        Exception exception = assertThrows(Exception.class, () -> {
            bidService.placeBid(item.getId(), bidder, bidAmount);
        });

        // then - assert and verify
        assertEquals("Item is not sellable.", exception.getMessage());

        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
    }

    @Test
    @DisplayName("JUnit test for place bid method when bidder has not enough money")
    public void givenValidInput_whenPlaceBid_thenThrowNotEnoughMoneyException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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


        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(9)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 49;

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));

        // when - action or behaviour to be tested
        Exception exception = assertThrows(Exception.class, () -> {
            bidService.placeBid(item.getId(), bidder, bidAmount);
        });

        // then - assert and verify
        assertEquals("You have not enough greenBay dollars on your account.", exception.getMessage());

        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
    }

    @Test
    @DisplayName("JUnit test for place bid method when bid is too low")
    public void givenValidInput_whenPlaceBid_thenThrowBitTooLowException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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


        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 11;

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));

        // when - action or behaviour to be tested
        Exception exception = assertThrows(Exception.class, () -> {
            bidService.placeBid(item.getId(), bidder, bidAmount);
        });

        // then - assert and verify
        assertEquals("Your bid is too low.", exception.getMessage());

        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
    }

    @Test
    @DisplayName("JUnit test for place new bid method")
    public void givenValidInput_whenPlaceBid_thenSaveBidObjectAndReturnDTO() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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


        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 25;

        Bid bid = Bid.builder()
                .id(1L)
                .item(item)
                .bidder(bidder)
                .bidAmount(bidAmount)
                .build();

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));

        // when - action or behaviour to be tested
        BidPlacedResponseDTO bidPlacedResponseDTO = null;
        bidPlacedResponseDTO = (BidPlacedResponseDTO) bidService.placeBid(item.getId(), bidder, bidAmount);

        // then - assert and verify
        Assertions.assertThat(bidPlacedResponseDTO).isNotNull();
        Assertions.assertThat(bidPlacedResponseDTO.getBidAmount()).isEqualTo(bidAmount);
        Assertions.assertThat(bidPlacedResponseDTO.getName()).isEqualTo(item.getName());
        Assertions.assertThat(bidPlacedResponseDTO.getSellerUsername()).isEqualTo(item.getSeller().getUsername());


        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
        verify(itemService, times(1)).saveLastBid(item, bidAmount);
        verify(bidRepository, times(1)).save(any(Bid.class));
    }

    @Test
    @DisplayName("JUnit test for simulating database failure while saving new bid")
    public void givenValidInput_whenPlaceBid_thenSavingBidThrowException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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


        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 25;

        Bid bid = Bid.builder()
                .id(1L)
                .item(item)
                .bidder(bidder)
                .bidAmount(bidAmount)
                .build();

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));
        given(bidRepository.save(any(Bid.class))).willThrow(new DataAccessException("Simulated database failure") {
        });

        // when - action or behaviour to be tested
        Exception exception = assertThrows(Exception.class, () -> {
            bidService.placeBid(item.getId(), bidder, bidAmount);
        });
        // then - assert and verify
        assertEquals("Placing your bid has failed.", exception.getMessage());

        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
        verify(bidRepository, times(1)).save(any(Bid.class));
    }

    @Test
    @DisplayName("JUnit test for place bid method while item is purchased")
    public void givenValidInput_whenPlaceBid_thenPurchaseItemAndReturnDTO() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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


        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 51;

        Bid bid = Bid.builder()
                .id(1L)
                .item(item)
                .bidder(bidder)
                .bidAmount(bidAmount)
                .build();

        User buyer = bidder;

        Purchase purchase = Purchase.builder()
                .id(1L)
                .item(item)
                .buyer(buyer)
                .purchaseAmount(bidAmount)
                .build();

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));

        // when - action or behaviour to be tested
        ItemBoughtResponseDTO itemBoughtResponseDTO = null;
        itemBoughtResponseDTO = (ItemBoughtResponseDTO) bidService.placeBid(item.getId(), bidder, bidAmount);

        // then - assert and verify
        Assertions.assertThat(itemBoughtResponseDTO).isNotNull();
        Assertions.assertThat(itemBoughtResponseDTO.getBuyingPrice()).isEqualTo(bidAmount);
        Assertions.assertThat(itemBoughtResponseDTO.getName()).isEqualTo(item.getName());
        Assertions.assertThat(itemBoughtResponseDTO.getSellerUsername()).isEqualTo(item.getSeller().getUsername());
        Assertions.assertThat(itemBoughtResponseDTO.getBuyerUsername()).isEqualTo(bidder.getUsername());


        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
        verify(itemService, times(1)).saveLastBid(item, bidAmount);
        verify(itemService, times(1)).makeNotSellable(item);
        verify(bidRepository, times(1)).save(any(Bid.class));
        verify(purchaseService, times(1)).savePurchase(any(Purchase.class));
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("JUnit test for place bid method while purchase has failed")
    public void givenValidInput_whenPlaceBid_thenThrowPurchaseFailedException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
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


        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 51;

        Bid bid = Bid.builder()
                .id(1L)
                .item(item)
                .bidder(bidder)
                .bidAmount(bidAmount)
                .build();

        User buyer = bidder;

        Purchase purchase = Purchase.builder()
                .id(1L)
                .item(item)
                .buyer(buyer)
                .purchaseAmount(bidAmount)
                .build();

        given(itemService.getItemById(item.getId())).willReturn(Optional.of(item));
        given(purchaseService.savePurchase(any(Purchase.class))).willThrow(new DataAccessException("Simulated database failure") {
        });

        // when - action or behaviour to be tested
        Exception exception = assertThrows(Exception.class, () -> {
            bidService.placeBid(item.getId(), bidder, bidAmount);
        });
        // then - assert and verify
        assertEquals("Purchase has failed.", exception.getMessage());

        // Additional verification
        verify(itemService, times(1)).getItemById(item.getId());
        verify(itemService, times(1)).saveLastBid(item, bidAmount);
        verify(itemService, times(1)).makeNotSellable(item);
        verify(bidRepository, times(1)).save(any(Bid.class));
        verify(purchaseService, times(1)).savePurchase(any(Purchase.class));
        verify(userService, times(0)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("JUnit test for find bids by item method")
    public void givenItemObject_whenFindBidsByItem_thenReturnDTOList() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        item = Item.builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(40)
                .sellable(true)
                .seller(seller)
                .build();

        UUID bidderId = UUID.randomUUID();
        User bidder = User.builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        Bid bid1 = Bid.builder()
                .id(1L)
                .item(item)
                .bidder(bidder)
                .bidAmount(20)
                .build();

        Bid bid2 = Bid.builder()
                .id(2L)
                .item(item)
                .bidder(bidder)
                .bidAmount(30)
                .build();

        Bid bid3 = Bid.builder()
                .id(3L)
                .item(item)
                .bidder(bidder)
                .bidAmount(40)
                .build();

        List<Bid> bidList = List.of(bid1, bid2, bid3);

        given(bidRepository.findAllByItem(item)).willReturn(bidList);

        // when - action or behaviour to be tested
        List<BidListDTO> bidListDTOList = null;
        bidListDTOList = bidService.findBidsByItem(item);

        // then - assert and verify
        Assertions.assertThat(bidListDTOList).isNotNull();
        Assertions.assertThat(bidListDTOList.size()).isEqualTo(3);
    }
}