package com.pc.greenbay.services;

import com.pc.greenbay.models.ResponseDTOs.UserListDTO;
import com.pc.greenbay.models.User;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    String encodePassword(String pwd);

    Map<String, String> buildErrorResponseForLogin(BindingResult bindingResult);

    double showGreenBayDollarsBalance(String username);

    Optional<User> findByUsername(String username);

    Map<String, Double> updateBalance(String username, double balance) throws Exception;

//    List<User> listAllUsers();
    List<UserListDTO> listAllUsers();
}
