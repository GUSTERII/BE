package com.usv.examtimetabling.exam.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateExamDto {
  private String oldGroupName;

  private String oldSubjectName;

  @JsonFormat(pattern = "HH:mm dd-MM-yyyy",timezone = "UTC")
  private Date date;

  private String classroom;

  private String group;

  private String materie;

  private String title;

  private String description;

  private int duration;

  @JsonFormat(pattern = "HH:mm")
  private LocalTime startTime;

  @JsonFormat(pattern = "HH:mm")
  private LocalTime endTime;
}
