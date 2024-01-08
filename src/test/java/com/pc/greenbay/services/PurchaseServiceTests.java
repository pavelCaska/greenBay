package com.pc.greenbay.services;

import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.ItemRepository;
import com.pc.greenbay.repositories.PurchaseRepository;
import com.pc.greenbay.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTests {

    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    private Item item;
    private Purchase purchase;

    @BeforeEach
    public void setup(){

        UUID buyerId = UUID.randomUUID();
        User buyer = User.builder()
                .id(buyerId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        UUID sellerId = UUID.randomUUID();
        User seller = User.builder()
                .id(sellerId)
                .username("user2")
                .password("u23456")
                .balance(10)
                .roles("ROLE_USER")
                .build();

        UUID itemId = UUID.randomUUID();
        item = Item.builder()
                .id(itemId)
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .seller(seller)
                .build();

        purchase = Purchase.builder()
                .id(1L)
                .purchaseAmount(60)
                .buyer(buyer)
                .item(item)
                .build();

    }

    @Test
    @DisplayName("JUnit test for save purchase method")
    public void givenPurchaseObject_whenSavePurchase_thenVerifyMethodCall() {
        // given - precondition or setup
        given(purchaseRepository.save(purchase)).willReturn(purchase);
        // when - action or behaviour to be tested
        Purchase savedPurchase = purchaseService.savePurchase(purchase);

        // then - verify the output
        Assertions.assertThat(savedPurchase).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for get purchase by item method")
    public void givenPurchaseObject_whenFindByItem_thenReturnPurchaseObject() {
        // given - precondition or setup
        given(purchaseRepository.findByItem(item)).willReturn(Optional.of(purchase));

        // when - action or behaviour to be tested
        Purchase savedPurchase = purchaseService.getPurchaseByItem(item).get();

        // then - verify the output
        assertThat(savedPurchase).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for delete purchase method")
    public void givenPurchaseId_whenDeletePurchase_thenNothing() throws Exception {
        // given - precondition or setup
        Long purchaseId = 1L;
        given(purchaseRepository.findById(purchaseId)).willReturn(Optional.of(purchase));
        willDoNothing().given(purchaseRepository).delete(purchase);

        // when - action or behaviour to be tested
        purchaseService.deletePurchase(purchaseId);

        // then - verify the output
        verify(purchaseRepository, times(1)).delete(purchase);
    }

    @Test
    @DisplayName("JUnit test for delete purchase method when database failure")
    public void givenPurchaseId_whenDeletePurchase_thenException() {
        // given - precondition or setup
        Long purchaseId = 1L;
        given(purchaseRepository.findById(purchaseId)).willReturn(Optional.of(purchase));
        doThrow(new DataIntegrityViolationException("Simulated database failure")).when(purchaseRepository).delete(any(Purchase.class));

        // when - action or behaviour to be tested
        assertThrows(Exception.class, () -> {
            purchaseService.deletePurchase(purchaseId);
        });

        // then - verify the output
        verify(purchaseRepository).delete(any(Purchase.class));
    }
}
