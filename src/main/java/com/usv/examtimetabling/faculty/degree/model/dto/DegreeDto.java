package com.usv.examtimetabling.faculty.degree.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DegreeDto {

    private String name;

    private String facultyName;

    private int numberOfStudents;

}
