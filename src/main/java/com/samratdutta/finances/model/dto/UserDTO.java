package com.samratdutta.finances.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {
    private UUID uuid;
    private String firstname;
    private String lastname;
    private String email;
    private String clearPassword;
}
