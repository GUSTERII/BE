package com.usv.examtimetabling.exam.model.dto;

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
public class UpdateExamsPeriodDto {

  private LocalDate oldStartDate;

  private LocalDate oldEndDate;

  private String description;

  private LocalDate startDate;

  private LocalDate endDate;

  public ExamsPeriod toExamsPeriod() {
    return ExamsPeriod.builder()
        .startDate(startDate)
        .endDate(endDate)
        .description(description)
        .build();
  }
}
