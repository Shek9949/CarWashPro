package com.carwashpro.backend.service.impl;

import com.carwashpro.backend.entity.User;
import com.carwashpro.backend.exception.ResourceNotFoundException;
import com.carwashpro.backend.repository.UserRepository;
import com.carwashpro.backend.request.LoginRequest;
import com.carwashpro.backend.response.LoginResponse;
import com.carwashpro.backend.security.JwtService;
import com.carwashpro.backend.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .build();
    }
}