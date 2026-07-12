package com.carwashpro.backend.service;

import com.carwashpro.backend.request.LoginRequest;
import com.carwashpro.backend.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}