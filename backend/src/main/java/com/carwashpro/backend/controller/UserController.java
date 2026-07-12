package com.carwashpro.backend.controller;

import com.carwashpro.backend.request.RegisterRequest;
import com.carwashpro.backend.response.UserResponse;
import com.carwashpro.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}