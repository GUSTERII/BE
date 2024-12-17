package com.usv.examtimetabling.faculty.degree.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateDegreeDto {

    private String oldName;

    private String newName;

    private String facultyName;

}
