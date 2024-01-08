package com.pc.greenbay.services;

import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.UserRepository;
//import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.BDDMockito;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setup(){
        UUID userId = UUID.randomUUID();
        user = new User.Builder()
                .id(userId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
    }

    @Test
    @DisplayName("JUnit test for saveUser method")
    public void givenUserObject_whenSaveUser_thenSaveReturnUserObject() {
        // given - precondition or setup
        given(userRepository.save(user))
                .willReturn(user);

        // when - action or behaviour to be tested
        User savedUser = userService.saveUser(user);

        // then - verify the output
        assertThat(savedUser).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for find user by username method")
    public void givenUsername_whenFindByUsername_thenReturnUserObject() {
        // given - precondition or setup
        userRepository.save(user);
        given(userRepository.findByUsername("user1")).willReturn(Optional.of(user));

        // when - action or behaviour to be tested
        User savedUser = userService.findByUsername(user.getUsername()).get();

        // then - verify the output
        assertThat(savedUser).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for update user method")
    public void givenUserObject_whenUpdateBalance_thenReturnUpdatedUser () throws Exception {
        // given - precondition or setup
        given(userRepository.findByUsername("user1")).willReturn(Optional.of(user));
        user.setBalance(200);

        // when - action or behaviour to be tested
        userService.updateBalance("user1", 200);

        // then - verify the output
        assertThat(user.getBalance()).isEqualTo(200);
    }

    @Test
    @DisplayName("JUnit test for update user method throws Exception")
    public void givenUserObject_whenUpdateBalance_thenReturnUpdatedUser2 () throws Exception {
        // given - precondition or setup
        given(userRepository.findByUsername("user2")).willReturn(Optional.empty());

        // when - action or behaviour to be tested
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> userService.updateBalance("user2", 200));

        // then - verify the output
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    @DisplayName("JUnit test for showing GreenBay dollar balance")
    public void givenUserObject_whenShowBalance_thenReturnBalance() {
        // given - precondition or setup
        given(userRepository.findByUsername("user1")).willReturn(Optional.of(user));

        // when - action or behaviour to be tested
        userService.showGreenBayDollarsBalance("user1");

        // then - verify the output
        assertThat(user.getBalance()).isEqualTo(100);
    }

    @Test
    @DisplayName("JUnit test for list all users with role user")
    public void givenUserList_whenListAllUsers_thenReturnUserList() {
        // given - precondition or setup
        userRepository.save(user);

        UUID adminId = UUID.randomUUID();
        User admin = new User.Builder()
                .id(adminId)
                .username("admin")
                .password("A12345")
                .balance(100)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);

        given(userRepository.findAllByRolesContains("ROLE_USER")).willReturn(List.of(user));

        // when - action or behaviour to be tested
        List<User> userList = userService.listAllUsers();

        // then - verify the output
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("JUnit test for password encoder")
    public void givenStringPassword_whenEncodePassword_thenReturnEncodedPassword() {
        // given - precondition or setup
        String inputPassword = "u12345";
        String encodedPassword = "$2a$10$fN8O3qGYHs0AXYCtVGzXl.KCuNy4tsDlOtZAzv1JVBEe8L2eui.iy";

        Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn(encodedPassword);
        // when - action or behaviour to be tested
        String result = userService.encodePassword(inputPassword);

        // then - verify the output
        verify(passwordEncoder).encode(inputPassword);

        org.junit.jupiter.api.Assertions.assertEquals(encodedPassword, result);
    }

}
