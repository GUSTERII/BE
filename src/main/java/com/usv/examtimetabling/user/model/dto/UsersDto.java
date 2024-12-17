package com.usv.examtimetabling.user.model.dto;

import com.usv.examtimetabling.user.model.utils.Role;
import jakarta.validation.constraints.Email;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UsersDto {

    private String name;

    @Email
    private String email;

    private Role role;

    private Date created_at;

    private Date updated_at;

}
