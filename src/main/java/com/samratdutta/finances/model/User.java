package com.samratdutta.finances.model;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private UUID uuid;
    private String firstname;
    private String lastname;
    private String email;
    private String passwordHash;
    private String rememberToken;
}
