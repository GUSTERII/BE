package com.usv.examtimetabling.facultate.specialization.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateSpecializationDto {

    private String oldName;

    private String newName;

    private String facultateName;

}
