package com.usv.examtimetabling.exam.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateExamDto {

    private String sala;

    private String subGrupa;

    private String materie;

    private String title;

    private String description;

    private int duration;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Override
    public String toString() {
        return "CreateExamDto{" + "classroom=" + sala + ", group=" + subGrupa + ", materie=" + materie + ", title=" + title + ", description=" + description + ", duration=" + duration + ", startTime=" + startTime + ", date=" + date + '}';
    }

}
