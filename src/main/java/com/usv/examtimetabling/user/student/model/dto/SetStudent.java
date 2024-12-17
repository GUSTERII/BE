package com.usv.examtimetabling.user.student.model.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SetStudent {

  @Email private String email;

  private String name;

  private String facultate;

  private String degree;

  private String subGrupa;

  private Integer year;
}
