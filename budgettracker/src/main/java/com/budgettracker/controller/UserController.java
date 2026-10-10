package com.budgettracker.controller;

import com.budgettracker.dto.UserResponse;
import com.budgettracker.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public UserResponse getCurrentUser(
            @AuthenticationPrincipal User user) {

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getCurrency(),
                user.getTimezone()
        );
    }
}
