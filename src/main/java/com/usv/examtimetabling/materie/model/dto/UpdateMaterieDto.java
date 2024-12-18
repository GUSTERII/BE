package com.usv.examtimetabling.materie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMaterieDto {

  private String oldName;

  private String name;

  private Integer semester;

  private Integer year;

  private String specialization;

  private String teacher;

  private Integer examDuration;
}
