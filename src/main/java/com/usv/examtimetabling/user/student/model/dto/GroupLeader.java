package com.usv.examtimetabling.user.student.model.dto;

import com.usv.examtimetabling.user.model.utils.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class GroupLeader {

    @Email
    private String email;

    private Role role;

    private String facultate;

    private String specialization;

    private Integer year;

    private String group;

}
