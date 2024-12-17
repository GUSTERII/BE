package com.usv.examtimetabling.user.model.dto;

import com.usv.examtimetabling.user.model.utils.Role;
import lombok.Data;

@Data
public class UpdateUserDto {

  private String name;

  private Role role;

}
