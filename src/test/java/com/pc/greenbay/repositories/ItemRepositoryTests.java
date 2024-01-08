package com.pc.greenbay.repositories;

import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
public class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("JUnit test save item operation")
    public void givenItemObject_whenSave_thenReturnSavedItem() {
        // given - precondition or setup
        User seller = new User.Builder()
//                .id(UUID.fromString("fd814ac5-8ac9-40c5-9067-e0a52a8cbde6"))
                .username("user1")
                .password("u12345")
                .balance(100)
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
                .sellable(true)
                .seller(seller)
                .build();

        // when - action or behaviour to be tested
        Item savedItem = itemRepository.save(item);

        // then - verify the output
        Assertions.assertThat(savedItem).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for findItemById operation")
    public void givenItemObject_whenFindItemById_thenReturnItemObject() {
        // given - precondition or setup
        User seller = new User.Builder()
//                .id(UUID.fromString("fd814ac5-8ac9-40c5-9067-e0a52a8cbde6"))
                .username("user1")
                .password("u12345")
                .balance(100)
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
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        // when - action or behaviour to be tested
        Optional<Item> optionalItem = itemRepository.findItemById(item.getId());

        // then - verify the output
        Assertions.assertThat(optionalItem).isNotEmpty();
    }
    @Test
    @DisplayName("JUnit test for getting all sellable items ")
    public void givenItemObjects_whenFindAllBySellableTrue_thenReturnPage() {
        // given - precondition or setup
        User seller = new User.Builder()
//                .id(UUID.fromString("fd814ac5-8ac9-40c5-9067-e0a52a8cbde6"))
                .username("user1")
                .password("u12345")
                .balance(100)
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
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item);

        Item item2 = new Item.Builder()
//                .id(UUID.fromString("556c8778-3c04-4a01-83ea-459e37c01e33")) // uuid
                .name("Samsung")
                .description("mobile phone")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(100)
                .purchasePrice(150)
                .lastBid(0)
                .sellable(true)
                .seller(seller)
                .build();
        itemRepository.save(item2);

        Item item3 = new Item.Builder()
//                .id(UUID.fromString("a59a33dd-d40e-4500-bff2-e566146b0bb2")) // uuid
                .name("Lenovo")
                .description("mobile phone")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(50)
                .purchasePrice(100)
                .lastBid(0)
                .seller(seller)
                .sellable(false)
                .build();
        itemRepository.save(item3);

        // when - action or behaviour to be tested
        Page<Item> itemPage = itemRepository.findAllBySellableTrue(Pageable.unpaged());

        // then - verify the output
        Assertions.assertThat(itemPage).isNotNull();
        Assertions.assertThat(itemPage).size().isEqualTo(2);
    }

}
