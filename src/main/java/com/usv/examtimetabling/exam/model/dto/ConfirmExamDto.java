package com.usv.examtimetabling.exam.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmExamDto {
  @JsonFormat(pattern = "HH:mm dd-MM-yyyy",timezone = "UTC")
  private String date;

  private String sala;

  private String group;

  private String materie;

  private String name;

  private String description;

  private int duration;

  private String startTime;

  private String endTime;

  private String classroom;
}
