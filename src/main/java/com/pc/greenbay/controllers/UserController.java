package com.pc.greenbay.controllers;

import com.pc.greenbay.models.DTOs.ErrorDTO;
import com.pc.greenbay.models.RequestDTOs.LoginRequestDTO;
import com.pc.greenbay.services.JwtService;
import com.pc.greenbay.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(userService.buildErrorResponseForLogin(bindingResult));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()));
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("access_token", jwtService.generateToken(loginRequestDTO.getUsername()));
            response.put("balance", userService.showGreenBayDollarsBalance(loginRequestDTO.getUsername()));

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException b) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorDTO("Authentication failed." +
                            " Incorrect username and/or password."));
        }
    }

    @GetMapping("/isRunning")
    public String isRunning() {
        return "Service is running";
    }

    @GetMapping("/user")
    public ResponseEntity<?> showUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    @PatchMapping("/balance/{username}")
    public ResponseEntity<?> updateBalance(@PathVariable String username, @RequestParam(name = "newBalance", required = false) Double newBalance) {
        if(newBalance == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("Parameter 'newBalance' is missing."));
        }
        if(newBalance < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("New balance must be a positive number."));
        }
        try {
            return ResponseEntity.ok().body(userService.updateBalance(username, newBalance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(e.getMessage()));
        }
    }
}
