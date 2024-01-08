package com.pc.greenbay.repositories;

import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.Purchase;
import com.pc.greenbay.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PurchaseRepositoryTests {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("JUnit test for save purchase operation")
    public void givenPurchaseObject_whenSave_thenReturnSavedPurchase() {
        // given - precondition or setup
        User buyer = new User.Builder()
//                .id(UUID.fromString("fd814ac5-8ac9-40c5-9067-e0a52a8cbde6"))
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        User seller = new User.Builder()
//                .id(UUID.fromString("d0425f31-e063-4a82-b3fa-b1ab80fe07e6"))
                .username("user2")
                .password("u23456")
                .balance(10)
                .roles("ROLE_USER")
                .build();

        Item item = new Item.Builder()
//                .id(UUID.fromString("259587d6-1e4d-4e33-a9e8-3aed3492ddee"))
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .seller(seller)
                .build();

        Purchase purchase = new Purchase.Builder()
                .purchaseAmount(60)
                .buyer(buyer)
                .item(item)
                .build();

        // when - action or behaviour to be tested
        Purchase savedPurchase = purchaseRepository.save(purchase);

        // then - verify the output
        Assertions.assertThat(savedPurchase).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for delete purchase operation")
    public void givenPurchaseObject_whenDelete_thenRemovePurchase() {
        // given - precondition or setup
        User buyer = new User.Builder()
//                .id(UUID.fromString("fd814ac5-8ac9-40c5-9067-e0a52a8cbde6"))
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        User seller = new User.Builder()
//                .id(UUID.fromString("d0425f31-e063-4a82-b3fa-b1ab80fe07e6"))
                .username("user2")
                .password("u23456")
                .balance(10)
                .roles("ROLE_USER")
                .build();

        Item item = new Item.Builder()
//                .id(UUID.fromString("259587d6-1e4d-4e33-a9e8-3aed3492ddee"))
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .seller(seller)
                .build();

        Purchase purchase = new Purchase.Builder()
                .purchaseAmount(60)
                .buyer(buyer)
                .item(item)
                .build();

        purchaseRepository.save(purchase);

        // when - action or behaviour to be tested
        purchaseRepository.delete(purchase);
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchase.getId());

        // then - verify the output
        Assertions.assertThat(optionalPurchase).isEmpty();
    }
    @Test
    @DisplayName("JUnit test for findByItem operation")
    public void givenPurchaseObject_whenFindByItem_thenReturnPurchaseObject() {
        // given - precondition or setup
        User buyer = new User.Builder()
//                .id(UUID.fromString("fd814ac5-8ac9-40c5-9067-e0a52a8cbde6"))
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(buyer);

        User seller = new User.Builder()
//                .id(UUID.fromString("d0425f31-e063-4a82-b3fa-b1ab80fe07e6"))
                .username("user2")
                .password("u23456")
                .balance(10)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        Item item = new Item.Builder()
//                .id(UUID.fromString("259587d6-1e4d-4e33-a9e8-3aed3492ddee"))
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .seller(seller)
                .build();
        itemRepository.save(item);

        Purchase purchase = new Purchase.Builder()
                .purchaseAmount(60)
                .buyer(buyer)
                .item(item)
                .build();
        purchaseRepository.save(purchase);

        // when - action or behaviour to be tested
        Optional<Purchase> optionalPurchase = purchaseRepository.findByItem(item);

        // then - verify the output
        Assertions.assertThat(optionalPurchase).isNotEmpty();
    }
}
