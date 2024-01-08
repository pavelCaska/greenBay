package com.pc.greenbay.repositories;

import com.pc.greenbay.models.Bid;
import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

@DataJpaTest
public class BidRepositoryTests {

    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("JUnit test save bid operation")
    public void givenBidObject_whenSave_thenReturnSavedBid() {
        // given - precondition or setup
        User bidder = User.builder()
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        User seller = User.builder()
                .username("user2")
                .password("u23456")
                .balance(10)
                .roles("ROLE_USER")
                .build();

        Item item = Item.builder()
                .name("Lenovo")
                .description("tablet")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(10)
                .purchasePrice(50)
                .lastBid(0)
                .seller(seller)
                .build();

        Bid bid = Bid.builder()
                .bidAmount(20)
                .bidder(bidder)
                .item(item)
                .build();

        // when - action or behaviour to be tested
        Bid savedBid = bidRepository.save(bid);

        // then - verify the output
        Assertions.assertThat(savedBid).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for findAllByItem operation")
    public void givenBidList_whenFindAllByItem_thenBidList() {
        // given - precondition or setup
        User bidder = User.builder()
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder);

        User seller = User.builder()
                .username("user2")
                .password("u23456")
                .balance(10)
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

        Bid bid = Bid.builder()
                .bidAmount(20)
                .bidder(bidder)
                .item(item)
                .build();
        bidRepository.save(bid);


        User bidder2 = User.builder()
                .username("user3")
                .password("u34567")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder2);

        Bid bid2 = Bid.builder()
                .bidAmount(30)
                .bidder(bidder2)
                .item(item)
                .build();
        bidRepository.save(bid2);

        User bidder3 = User.builder()
                .username("user4")
                .password("u45678")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(bidder3);

        Item item2 = Item.builder()
                .name("Asus")
                .description("notebook")
                .photoURL("/img/green_fox_logo.png")
                .startingPrice(75)
                .purchasePrice(99)
                .lastBid(0)
                .seller(seller)
                .build();
        itemRepository.save(item2);

        Bid bid3 = Bid.builder()
                .bidAmount(76)
                .bidder(bidder3)
                .item(item2)
                .build();
        bidRepository.save(bid3);

        // when - action or behaviour to be tested
        List<Bid> bidList = bidRepository.findAllByItem(item);

        // then - verify the output
        Assertions.assertThat(bidList).isNotNull();
        Assertions.assertThat(bidList.size()).isEqualTo(2);
    }
}
