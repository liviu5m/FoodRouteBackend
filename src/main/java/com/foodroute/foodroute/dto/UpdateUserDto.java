package com.foodroute.foodroute.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    private String fullName;
    private String username;
    private String currentUsername;
    private String address;
    private String phoneNumber;
    private String currentPassword;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;
    private String passwordConfirmation;
    private String type;
}
