package com.budgettracker.services;

import com.budgettracker.dto.AuthResponse;
import com.budgettracker.dto.LoginRequest;
import com.budgettracker.dto.RegisterRequest;
import com.budgettracker.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
