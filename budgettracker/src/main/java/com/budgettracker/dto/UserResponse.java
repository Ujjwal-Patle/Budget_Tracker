package com.budgettracker.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String currency,
        String timezone
) {}
