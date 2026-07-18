package com.carwashpro.backend.service;

import com.carwashpro.backend.request.LoginRequest;
import com.carwashpro.backend.request.RegisterRequest;
import com.carwashpro.backend.response.LoginResponse;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

}