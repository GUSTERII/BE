package com.usv.examtimetabling.security.dto;

import com.usv.examtimetabling.user.model.utils.Role;
import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;

    private String refreshToken;

    private Role role;

    private String name;

    private String email;

}
