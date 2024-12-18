package com.usv.examtimetabling.facultate.specialization.subgrupa.model.dto;

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

    private String specialization;

    private Integer year;

}
