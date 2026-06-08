package com.example.demo.dto;

import java.util.Date;

public record LoginResponseDTO(
        String token,
        Date expirationDate
) {
}
