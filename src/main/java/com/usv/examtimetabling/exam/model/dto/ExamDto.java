package com.usv.examtimetabling.exam.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usv.examtimetabling.exam.util.ExamStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamDto {

    @JsonFormat(pattern = "HH:mm dd-MM-yyyy",timezone = "UTC")
    private String date;

    private String classroom;

    private String group;

    private String materie;

    private String name;

    private String description;

    private int duration;

    private String start;

    private ExamStatus status;

    public CreateExamDto toCreateExamDto() {
        String[] dateTimeParts = date.split(" ", 2);
        String dateString = dateTimeParts[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);

        return CreateExamDto.builder()
            .sala(classroom)
            .subGrupa(group)
            .materie(materie)
            .title(name)
            .description(description)
            .duration(duration)
            .startTime(LocalTime.parse(start))
            .date(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
            .build();
    }
}
