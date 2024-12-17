package com.usv.examtimetabling.exam.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usv.examtimetabling.exam.model.ExamsPeriod;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateExamPeriodDto {

  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  public ExamsPeriod toExamsPeriod() {
    return ExamsPeriod.builder()
        .description(description)
        .startDate(startDate)
        .endDate(endDate)
        .build();
  }

  @Override
  public String toString() {
    return "CreateExamPeriodDto{" +
        "description='" + description + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        '}';
  }
}
