package com.usv.examtimetabling.faculty.degree.subgrupa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SubGrupaDto {

    private String name;

    private String degree;

    private Integer year;

}
