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
public class StudentDto {

    private String name;

    @Email
    private String email;

    private Role role;

    private String faculty;

    private String specialization;

    private Integer year;

    private String group;

}
