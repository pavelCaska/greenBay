package com.pc.greenbay.services;

import com.pc.greenbay.models.ResponseDTOs.UserListDTO;
import com.pc.greenbay.models.User;
import com.pc.greenbay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public String encodePassword(String pwd) {
        return passwordEncoder.encode(pwd);
    }

    @Override
    public Map<String, String> buildErrorResponseForLogin(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }

    @Override
    public double showGreenBayDollarsBalance(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return user.getBalance();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Map<String, Double> updateBalance(String username, double balance) throws Exception {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) {
            throw new Exception("Given username doesn't exit.");
        }
        User user = optionalUser.get();
        user.setBalance(balance);
        userRepository.save(user);
        Map<String, Double> response = new HashMap<>();
        response.put(user.getUsername(), user.getBalance());
        return response;
    }
//    @Override
//    public List<User> listAllUsers() {
//        return userRepository.findAllByRolesContains("ROLE_USER");
//    }
    @Override
    public List<UserListDTO> listAllUsers() {
        return userRepository.findAllByRolesContains("ROLE_USER").stream()
                .map(o -> new UserListDTO(o.getId(), o.getUsername(), o.getPassword(), o.getBalance(), o.getRoles())).collect(Collectors.toList());
    }
}
