package com.usv.examtimetabling.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

  @NotBlank
  private String name;

  @Email
  private String email;

  @NotBlank
  @Size(min = 8, max = 20)
  private String password;

  @NotBlank
  private String role;

}
