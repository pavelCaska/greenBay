package com.pc.greenbay.repositories;

import com.pc.greenbay.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("JUnit test for save user operation")
    public void givenUserObject_whenSave_thenReturnSavedUser() {
        // given - precondition or setup
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();

        // when - action or behaviour to be tested
        User savedUser = userRepository.save(user);

        // then - verify the output
        Assertions.assertThat(savedUser).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for update user operation")
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUser() {
        // given - precondition or setup
        User user = User.builder()
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user);

        // when - action or behaviour to be tested
        User savedUser = userRepository.findByUsername(user.getUsername()).get();
        savedUser.setBalance(200);
        User updatedUser = userRepository.save(savedUser);

        // then - verify the output
        Assertions.assertThat(updatedUser.getBalance()).isEqualTo(200);
    }

    @Test
    @DisplayName("JUnit test for find user by username operation")
    public void givenUserObject_whenFindByUsername_thenReturnUserObject() {
        // given - precondition or setup
        User user = User.builder()
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user);

        // when - action or behaviour to be tested
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        // then - verify the output
        Assertions.assertThat(optionalUser).isNotEmpty();
    }

    @Test
    @DisplayName("JUnit test for find all users by roles operation")
    public void givenUserObjects_whenFindAllByRolesContains_thenReturnList() {
        // given - precondition or setup
        User user = User.builder()
                .username("user1")
                .password("u12345")
                .balance(100)
                .roles("ROLE_USER")
                .build();
        userRepository.save(user);

        User admin = User.builder()
                .username("admin")
                .password("A12345")
                .balance(100)
                .roles("ROLE_ADMIN")
                .build();
        userRepository.save(admin);

        // when - action or behaviour to be tested
        List<User> userList = userRepository.findAllByRolesContains("ROLE_USER");

        // then - verify the output
        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList).size().isEqualTo(1);
    }
}
