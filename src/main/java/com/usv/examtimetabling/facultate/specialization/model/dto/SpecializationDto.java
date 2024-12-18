package com.usv.examtimetabling.facultate.specialization.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SpecializationDto {

    private String name;

    private String facultateName;

    private int numberOfStudents;

}
