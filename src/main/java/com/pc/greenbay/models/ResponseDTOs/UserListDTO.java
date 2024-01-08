package com.pc.greenbay.models.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UserListDTO {
    private UUID id;
    private String username;
    private String password;
    private double balance;
    private String roles;

}
