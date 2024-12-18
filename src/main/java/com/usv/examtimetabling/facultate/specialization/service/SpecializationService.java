package com.usv.examtimetabling.facultate.specialization.service;

import com.usv.examtimetabling.facultate.specialization.model.dto.CreateSpecializationDto;
import com.usv.examtimetabling.facultate.specialization.model.dto.SpecializationDto;
import com.usv.examtimetabling.facultate.specialization.model.dto.UpdateSpecializationDto;
import java.util.List;

public interface SpecializationService {

    SpecializationDto addSpecialization(CreateSpecializationDto createSpecializationDto);

    List<SpecializationDto> getSpecializationByFacultateName(String facultyName);

    void deleteBySpecializationName(String specializationName);

    List<SpecializationDto> getAll();

    SpecializationDto updateSpecialization(UpdateSpecializationDto updateSpecializationDto);
}
