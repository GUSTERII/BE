package com.usv.examtimetabling.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

  @NotBlank
  @Size(min = 8, max = 20)
  private String currentPassword;

  @NotBlank
  @Size(min = 8, max = 20)
  private String newPassword;

  @NotBlank
  @Size(min = 8, max = 20)
  private String confirmPassword;

}
