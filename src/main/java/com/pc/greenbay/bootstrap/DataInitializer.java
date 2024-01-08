package com.pc.greenbay.bootstrap;

import com.pc.greenbay.models.Item;
import com.pc.greenbay.models.User;
import com.pc.greenbay.services.ItemService;
import com.pc.greenbay.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final ItemService itemService;

    public DataInitializer(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public void run(String... args) throws Exception {

        User admin = new User("admin", userService.encodePassword("A12345"), 0.0, "ROLE_ADMIN");
        User user1 = new User("user1", userService.encodePassword("u12345"), 100.0, "ROLE_USER");
        User user2 = new User("user2", userService.encodePassword("u23456"), 200.0, "ROLE_USER");
        User user3 = new User("user3", userService.encodePassword("u34567"), 300.0, "ROLE_USER");
        User user4 = new User("user4", userService.encodePassword("u45678"), 400.0, "ROLE_USER");

        userService.saveUser(admin);
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);
        userService.saveUser(user4);

        Item item01 = new Item("Lenovo", "tablet", "/img/green_fox_logo.png", 10, 50, user4);
        Item item02 = new Item("HP ELite", "notebook", "/img/green_fox_logo.png", 100, 150, user3);
        Item item03 = new Item("MacBook", "notebook", "/img/green_fox_logo.png", 200, 300, user2);
        Item item04 = new Item("HP Pavillon", "notebook", "/img/green_fox_logo.png", 200, 350, user1);
        Item item05 = new Item("Asus", "notebook", "/img/green_fox_logo.png", 100, 150, user1);
        Item item06 = new Item("Asus", "notebook", "/img/green_fox_logo.png", 150, 250, user2);
        Item item07 = new Item("Acer", "notebook", "/img/green_fox_logo.png", 75, 150, user3);
        Item item08 = new Item("iPad", "tablet", "/img/green_fox_logo.png", 100, 150, user4);
        Item item09 = new Item("iPad", "tablet", "/img/green_fox_logo.png", 150, 250, user1);
        Item item10 = new Item("Lenovo", "tablet", "/img/green_fox_logo.png", 100, 150, user2);
        Item item11 = new Item("Lenovo", "tablet", "/img/green_fox_logo.png", 50, 100, user3);
        Item item12 = new Item("iPhone", "mobile phone", "/img/green_fox_logo.png", 100, 150, user4);
        Item item13 = new Item("iPhone", "mobile phone", "/img/green_fox_logo.png", 125, 200, user1);
        Item item14 = new Item("Samsung", "mobile phone", "/img/green_fox_logo.png", 50, 150, user2);
        Item item15 = new Item("Motorola", "mobile phone", "/img/green_fox_logo.png", 75, 150, user3);
        Item item16 = new Item("Lenovo", "mobile phone", "/img/green_fox_logo.png", 50, 100, user4);
        Item item17 = new Item("Samsung", "mobile phone", "/img/green_fox_logo.png", 100, 150, user1);
        Item item18 = new Item("Samsung", "mobile phone", "/img/green_fox_logo.png", 75, 125, user2);
        Item item19 = new Item("iPhone", "mobile phone", "/img/green_fox_logo.png", 150, 250, user3);

        itemService.saveItem(item01);
        itemService.saveItem(item02);
        itemService.saveItem(item03);
        itemService.saveItem(item04);
        itemService.saveItem(item05);
        itemService.saveItem(item06);
        itemService.saveItem(item07);
        itemService.saveItem(item08);
        itemService.saveItem(item09);
        itemService.saveItem(item10);
        itemService.saveItem(item11);
        itemService.saveItem(item12);
        itemService.saveItem(item13);
        itemService.saveItem(item14);
        itemService.saveItem(item15);
        itemService.saveItem(item16);
        itemService.saveItem(item17);
        itemService.saveItem(item18);
        itemService.saveItem(item19);

    }
}
