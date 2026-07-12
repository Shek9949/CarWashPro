package com.carwashpro.backend.response;

import com.carwashpro.backend.constant.Role;
import com.carwashpro.backend.constant.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Role role;

    private UserStatus status;

}