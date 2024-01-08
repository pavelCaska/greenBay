package com.pc.greenbay.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pc.greenbay.models.RequestDTOs.LoginRequestDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerITests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private BidService bidService;
    @Autowired
    private BidRepository bidRepository;
//    @Autowired
//    private PurchaseService purchaseService;
    @Autowired
    private PurchaseRepository purchaseRepository;
//    @Autowired
//    private PurchaseService itemService;
    @Autowired
    private ItemRepository itemRepository;


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
    @DisplayName("Integration test for user login with valid input")
    public void givenValidUserData_whenLogin_thenReturnJwtTokenAndBalance() throws Exception {
        // given - precondition or setup
        User user1 = new User.Builder()
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user1", "u12345");

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(loginRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").isNumber());
    }

    @Test
    @DisplayName("Integration test for user login with invalid password")
    public void givenInvalidPassword_whenLogin_thenReturnErrorDTO() throws Exception {
        // given - precondition or setup
        User user1 = new User.Builder()
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user1", "u54321");

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(loginRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Authentication failed. Incorrect username and/or password."));
    }

    @Test
    @DisplayName("Integration test for user login with missing username and password")
    public void givenInvalidPassword_whenLogin_thenReturnErrorResponseForLogin() throws Exception {
        // given - precondition or setup
        User user1 = new User.Builder()
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("", "");

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(loginRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Username is empty or missing."))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password is empty or missing."));
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password must consists of a minimum of 6 letters and/or digits."));
    }

    @Test
    @DisplayName("Integration test for helper isRunning method ")
    public void givenAuthenticatedUser_whenIsRunning_thenStringInBody() throws Exception {
        // given - precondition or setup
        UUID userId = UUID.randomUUID();
        User user1 = new User.Builder()
                .id(userId)
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("user1");


        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/isRunning")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));
//                .content(om.writeValueAsString(loginRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Service is running"));
    }

    @Test
    @DisplayName("Integration test for helper showUser method ")
    public void givenAuthenticatedAdmin_whenShowUser_thenUserList() throws Exception {
        // given - precondition or setup
        User admin = new User.Builder()
                .username("admin")
                .password(userService.encodePassword("A12345"))
                .balance(0.0)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("admin");

        User user1 = new User.Builder()
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);

        User user2 = new User.Builder()
                .username("user2")
                .password(userService.encodePassword("u23456"))
                .balance(200.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user2);


        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/user")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));
//                .content(om.writeValueAsString(loginRequestDTO)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Check the size of the array
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roles").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("user2"));
    }

    @Test
    @DisplayName("Integration test for helper updateBalance method ")
    public void givenAuthenticatedAdmin_whenUpdateBalance_thenJSON() throws Exception {
        // given - precondition or setup
        User admin = new User.Builder()
                .username("admin")
                .password(userService.encodePassword("A12345"))
                .balance(0.0)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("admin");

        User user1 = new User.Builder()
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(patch("/api/balance/{username}", "user1")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .param("newBalance", "33.3"));


        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user1").value(33.3));
    }

    @Test
    @DisplayName("Integration test for helper updateBalance method with missing user")
    public void givenAuthenticatedAdminAndMissingUser_whenUpdateBalance_thenErrorDTO() throws Exception {
        // given - precondition or setup
        User admin = new User.Builder()
                .username("admin")
                .password(userService.encodePassword("A12345"))
                .balance(0.0)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("admin");

        User user1 = new User.Builder()
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(patch("/api/balance/{username}", "user2")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .param("newBalance", "33.3"));


        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("error").value("Given username doesn't exit."));
    }

    @Test
    @DisplayName("Integration test for helper updateBalance method when negative newBalance")
    public void givenAuthenticatedAdminAndNegativeNewBalance_whenUpdateBalance_thenErrorDTO() throws Exception {
        // given - precondition or setup
        User admin = new User.Builder()
                .username("admin")
                .password(userService.encodePassword("A12345"))
                .balance(0.0)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("admin");

        User user1 = new User.Builder()
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(patch("/api/balance/{username}", "user2")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON)
                .param("newBalance", "-1"));


        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("error").value("New balance must be a positive number."));
    }

    @Test
    @DisplayName("Integration test for helper updateBalance method when parameter is missing")
    public void givenAuthenticatedAdminAndMissingParameter_whenUpdateBalance_thenErrorDTO() throws Exception {
        // given - precondition or setup
        User admin = new User.Builder()
                .username("admin")
                .password(userService.encodePassword("A12345"))
                .balance(0.0)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);
        String authorizedUser = "Bearer ";
        authorizedUser += jwtService.generateToken("admin");

        User user1 = new User.Builder()
                .username("user1")
                .password(userService.encodePassword("u12345"))
                .balance(100.0)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user1);

        // when - action or behaviour to be tested
        ResultActions response = mockMvc.perform(patch("/api/balance/{username}", "user2")
                .header("authorization", authorizedUser)
                .contentType(MediaType.APPLICATION_JSON));
//                .param("newBalance", "-1"));


        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("error").value("Parameter 'newBalance' is missing."));
    }

}
