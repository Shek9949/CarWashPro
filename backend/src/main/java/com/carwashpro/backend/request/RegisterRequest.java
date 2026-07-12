package com.carwashpro.backend.request;

import com.carwashpro.backend.constant.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private Role role;

}