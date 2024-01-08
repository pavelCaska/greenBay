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
        User seller = User.builder()
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        Item item = Item.builder()
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
        User seller = User.builder()
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        Item item = Item.builder()
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
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
        User seller = User.builder()
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(seller);

        Item item = Item.builder()
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

        Item item2 = Item.builder()
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

        Item item3 = Item.builder()
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
