package com.budgettracker.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {}
