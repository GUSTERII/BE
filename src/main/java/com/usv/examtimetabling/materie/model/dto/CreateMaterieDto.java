package com.usv.examtimetabling.materie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMaterieDto {

  private String name;

  private Integer semester;

  private Integer year;

  private String faculty;

  private String specialization;

  private String teacher;

  private Integer examDuration;

}
