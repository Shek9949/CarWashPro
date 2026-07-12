package com.carwashpro.backend.service;

import com.carwashpro.backend.request.RegisterRequest;
import com.carwashpro.backend.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

}