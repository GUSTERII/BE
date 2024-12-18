package com.usv.examtimetabling.materie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterieDto {

    private String name;

    private Integer semester;

    private Integer year;

    private String degree;

    private String teacher;

    private String faculty;

    private Integer examDuration;

}
