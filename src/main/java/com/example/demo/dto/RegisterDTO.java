package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotNull
        @Email
        String username,
        @NotNull
        String password
) {
}
