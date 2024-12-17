package com.usv.examtimetabling.subject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSubjectDto {

  private String oldName;

  private String name;

  private Integer semester;

  private Integer year;

  private String degree;

  private String teacher;

  private Integer examDuration;
}
