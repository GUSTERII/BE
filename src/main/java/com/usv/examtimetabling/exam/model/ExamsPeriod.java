package com.usv.examtimetabling.exam.model;

import com.usv.examtimetabling.exam.model.dto.ExamsPeriodDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "exams_period")
public class ExamsPeriod {

  @Id
  @GeneratedValue
  private Integer id;

  private String description;

  private LocalDate startDate;

  private LocalDate endDate;

  public ExamsPeriodDto toExamsPeriodDto() {
    return ExamsPeriodDto.builder()
        .startDate(startDate)
        .endDate(endDate)
        .build();
  }
}
