package com.pc.greenbay.services;

import com.pc.greenbay.models.Bid;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.models.RequestDTOs.ItemRequestDTO;
import com.pc.greenbay.models.ResponseDTOs.*;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.ItemRepository;
import com.pc.greenbay.repositories.PurchaseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private PurchaseService purchaseService;
    @Mock
    private BidService bidService;
    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private User seller;

    @BeforeEach
    public void setup() {
        UUID sellerId = UUID.randomUUID();
        seller = new User.Builder()
                .id(sellerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
//        userRepository.save(seller);

        UUID itemId = UUID.randomUUID();
        item = new Item.Builder()
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
    }

    @Test
    @DisplayName("JUnit test for save item")
    public void givenItemObject_whenSaveItem_thenReturnItemObject() {
        // given - precondition or setup
        given(itemRepository.save(item)).willReturn(item);

        // when - action or behaviour to be tested
        Item savedItem = itemService.saveItem(item);

        // then - verify the output
        Assertions.assertThat(savedItem).isNotNull();
        Assertions.assertThat(savedItem.getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("JUnit test for save lastBid method")
    public void givenItemObject_whenSaveLastBid_thenUpdateItemObject() {
        // given - precondition or setup
        int lastBid = 150;

        // when - action or behaviour to be tested
        itemService.saveLastBid(item, lastBid);

        // then - verify the output
        assertEquals(lastBid, item.getLastBid());
        verify(itemRepository, Mockito.times(1)).save(item);
    }
    @Test
    @DisplayName("JUnit test for return boolean value method")
    public void givenItemObject_whenIsSellable_thenReturnBoolean() {
        // given - precondition or setup

        // when - action or behaviour to be tested
        boolean result = itemService.isItemSellable(item);
        // then - verify the output
        assertTrue(result);
    }

    @Test
    @DisplayName("JUnit test for set sellable to false method")
    public void givenItemObject_whenMakeNotSellable_thenUpdateItemObject() {
        // given - precondition or setup

        // when - action or behaviour to be tested
        itemService.makeNotSellable(item);
        // then - verify the output
        assertFalse(item.isSellable());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    @DisplayName("JUnit test for get item by id method")
    public void givenItemId_whenGetItemById_thenReturnItemObject() {
        // given - precondition or setup
        UUID uuid = item.getId();
        given(itemRepository.findItemById(uuid)).willReturn(Optional.of(item));

        // when - action or behaviour to be tested
        Item savedItem = itemService.getItemById(uuid).get();

        // then - verify the output
        Assertions.assertThat(savedItem).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get sellable items paged method")
    public void givenItemList_whenGetItemBySellableAndPage_thenReturnItemPage() {
        // given - precondition or setup
        UUID itemId2 = UUID.randomUUID();
        Item item2 = new Item.Builder()
                .id(itemId2)
                .name("Asus")
                .description("notebook")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(150)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        UUID itemId3 = UUID.randomUUID();
        Item item3 = new Item.Builder()
                .id(itemId3)
                .name("Apple")
                .description("iPad")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(200)
                .purchasePrice(250)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();

        int page = 3;
        Pageable pageable = PageRequest.of(page, 3);
        given(itemRepository.findAllBySellableTrue(pageable)).willReturn(new PageImpl<>(List.of(item, item2, item3)));
        // when - action or behaviour to be tested
        Page<Item>  itemPage = itemService.getItemsBySellableTrueAndPage(page);

        // then - verify the output
        assertThat(itemPage).isNotNull();
        assertThat(itemPage.getContent()).containsExactly(item, item2, item3);
    }

    @Test
    @DisplayName("JUnit test for list items method")
    public void givenItemList_whenListItems_thenJsonList() {
        // given - precondition or setup
        UUID itemId2 = UUID.randomUUID();
        Item item2 = new Item.Builder()
                .id(itemId2)
                .name("Asus")
                .description("notebook")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(150)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        UUID itemId3 = UUID.randomUUID();
        Item item3 = new Item.Builder()
                .id(itemId3)
                .name("Apple")
                .description("iPad")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(200)
                .purchasePrice(250)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        given(itemRepository.findAll()).willReturn(List.of(item, item2, item3));

        // when - action or behaviour to be tested
        List<ItemListDTO> jsonList = itemService.listItems();

        // then - verify the output
        assertThat(jsonList).isNotNull();
        assertThat(jsonList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("JUnit test for create item method")
    public void givenValidItemRequest_whenCreateItem_thenItemResponseDTO() throws Exception {
        // given - precondition or setup
        ItemRequestDTO itemRequestDTO = new ItemRequestDTO("Apple", "iPad", "/img/green_fox_logo.png", 200, 250);
        UUID itemToSaveId = UUID.randomUUID();
        Item itemToSave = new Item.Builder()
                .id(itemToSaveId)
                .name(itemRequestDTO.getName())
                .description(itemRequestDTO.getDescription())
                .photoURL(itemRequestDTO.getPhotoURL())
                .sellable(true)
                .startingPrice(itemRequestDTO.getStartingPrice())
                .purchasePrice(itemRequestDTO.getPurchasePrice())
                .seller(seller)
                .build();

        UUID savedItemId = itemToSave.getId();
        given(itemRepository.save(any(Item.class))).willAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setId(savedItemId);
            return savedItem;
        });

        // when - action or behaviour to be tested
        ItemResponseDTO itemResponseDTO = null;
            itemResponseDTO = itemService.createItem(itemRequestDTO, seller);

        // then - verify the output
        assertThat(itemResponseDTO).isNotNull();
        assertThat(itemResponseDTO.getItemID()).isNotNull();
        assertThat(itemResponseDTO.getName()).isEqualTo(itemRequestDTO.getName());
        assertThat(itemResponseDTO.getDescription()).isEqualTo(itemRequestDTO.getDescription());
    }

    @Test
    @DisplayName("JUnit test for simulating database failure while saving item")
    public void givenDatabaseFailure_whenCreateItem_thenThrowException() throws Exception {
        // given - precondition or setup
        ItemRequestDTO itemRequestDTO = new ItemRequestDTO("Apple", "iPad", "/img/green_fox_logo.png", 200, 250);

        given(itemRepository.save(any(Item.class))).willThrow(new DataAccessException("Simulated database failure") {});

        // when - action or behaviour to be tested
        assertThrows(Exception.class, () -> {
            itemService.createItem(itemRequestDTO, seller);
        });

        // Additional verification
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("JUnit test for show item details method when item not sellable")
    public void givenValidItemIDAndNotSellable_whenShowItemDetails_thenReturnItemNotSellableResponseDTO() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(60)
                .sellable(false)
                .seller(seller)
                .build();

        given(itemRepository.findItemById(itemId)).willReturn(Optional.of(item));
        UUID buyerId = UUID.randomUUID();
        User buyer = new User.Builder()
                .id(buyerId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        Purchase purchase = new Purchase.Builder()
                .id(1L)
                .purchaseAmount(60)
                .buyer(buyer)
                .item(item)
                .build();

        given(purchaseService.getPurchaseByItem(item)).willReturn(Optional.of(purchase));

        // when - action or behaviour to be tested
        ItemNotSellableResponseDTO itemNotSellableResponseDTO = null;
        itemNotSellableResponseDTO = (ItemNotSellableResponseDTO) itemService.showItemDetails(item.getId());

        // then - verify the output
        assertThat(itemNotSellableResponseDTO).isNotNull();
        assertThat(itemNotSellableResponseDTO.getName()).isEqualTo(item.getName());
        assertThat(itemNotSellableResponseDTO.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemNotSellableResponseDTO.getSellerUsername()).isEqualTo(item.getSeller().getUsername());
        assertThat(itemNotSellableResponseDTO.getBuyerUsername()).isEqualTo(purchase.getBuyer().getUsername());
    }

    @Test
    @DisplayName("JUnit test for show item details method when itemId invalid")
    public void givenInvalidItemId_whenShowItemDetails_thenThrowItemNotFoundException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(60)
                .sellable(false)
                .seller(seller)
                .build();
        UUID invalidId = UUID.randomUUID();

        given(itemRepository.findItemById(invalidId)).willReturn(Optional.empty());

        // when - action or behaviour to be tested
        assertThrows(Exception.class, () -> {
            itemService.showItemDetails(invalidId);
        });

        // Additional verification
        verify(itemRepository, times(1)).findItemById(invalidId);

    }

    @Test
    @DisplayName("JUnit test for show item details method when purchase record not found")
    public void givenValidItemId_whenShowItemDetails_thenThrowPurchaseNotFoundException() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(60)
                .sellable(false)
                .seller(seller)
                .build();

        given(itemRepository.findItemById(item.getId())).willReturn(Optional.of(item));
        given(purchaseService.getPurchaseByItem(item)).willReturn(Optional.empty());


        // when - action or behaviour to be tested
        assertThrows(Exception.class, () -> {
            itemService.showItemDetails(item.getId());
        });

        // Additional verification
        verify(itemRepository, times(1)).findItemById(item.getId());
        verify(purchaseService, times(1)).getPurchaseByItem(item);
    }

    @Test
    @DisplayName("JUnit test for show item details method when item sellable")
    public void givenValidItemIDAndSellable_whenShowItemDetails_thenReturnItemSellableResponseDTO() throws Exception {
        // given - precondition or setup
        UUID itemId = UUID.randomUUID();
        Item item = new Item.Builder()
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
        User bidder = new User.Builder()
                .id(bidderId)
                .username("user2")
                .password("u23456")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        int bidAmount = 49;
        Bid bid = new Bid.Builder()
                .id(1L)
                .item(item)
                .bidder(bidder)
                .bidAmount(bidAmount)
                .build();

        given(itemRepository.findItemById(itemId)).willReturn(Optional.of(item));
        List<BidListDTO> bidList = List.of(new BidListDTO(bid.getId(), bid.getBidder().getUsername(), bid.getBidAmount()));
        given(bidService.findBidsByItem(item)).willReturn(bidList);

        // when - action or behaviour to be tested
        ItemSellableResponseDTO itemSellableResponseDTO = null;
        itemSellableResponseDTO = (ItemSellableResponseDTO) itemService.showItemDetails(item.getId());

        // then - verify the output
        assertThat(itemSellableResponseDTO).isNotNull();
        assertThat(itemSellableResponseDTO.getName()).isEqualTo(item.getName());
        assertThat(itemSellableResponseDTO.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemSellableResponseDTO.getSellerUsername()).isEqualTo(item.getSeller().getUsername());
        assertThat(itemSellableResponseDTO.getBidList()).isNotNull();
        assertThat(itemSellableResponseDTO.getBidList()).size().isEqualTo(1);
    }


}
