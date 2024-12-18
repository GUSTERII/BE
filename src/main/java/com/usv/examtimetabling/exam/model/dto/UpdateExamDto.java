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
  private String oldSubGrupaName;

  private String oldMaterieName;

  @JsonFormat(pattern = "HH:mm dd-MM-yyyy",timezone = "UTC")
  private Date date;

  private String sala;

  private String subGrupa;

  private String materie;

  private String title;

  private String description;

  private int duration;

  @JsonFormat(pattern = "HH:mm")
  private LocalTime startTime;
}
