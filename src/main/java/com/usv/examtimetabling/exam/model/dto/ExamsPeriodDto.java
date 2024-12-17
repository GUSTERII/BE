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
public class ExamsPeriodDto {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  public void setExamsPeriod(ExamsPeriod save) {
    this.startDate = save.getStartDate();
    this.endDate = save.getEndDate();
  }
}
