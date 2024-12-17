package com.usv.examtimetabling.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @Email
    private String email;

}
