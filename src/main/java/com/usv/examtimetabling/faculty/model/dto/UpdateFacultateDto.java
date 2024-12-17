package com.usv.examtimetabling.faculty.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateFacultateDto {

      private String oldName;

      private String newName;

}
