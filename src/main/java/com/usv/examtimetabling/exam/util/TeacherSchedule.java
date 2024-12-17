package com.usv.examtimetabling.exam.util;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSchedule {
  private int totalHours;

  private List<String> intervals;
}
