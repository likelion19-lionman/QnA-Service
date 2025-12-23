package com.example.springqnaapp.dto;

import lombok.Data;

@Data
public class UserRequestDto {

    private String username;
    private String password;
    private String email;
}
