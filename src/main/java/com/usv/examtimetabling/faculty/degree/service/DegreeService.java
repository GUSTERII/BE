package com.usv.examtimetabling.faculty.degree.service;

import com.usv.examtimetabling.faculty.degree.model.dto.CreateDegreeDto;
import com.usv.examtimetabling.faculty.degree.model.dto.DegreeDto;
import com.usv.examtimetabling.faculty.degree.model.dto.UpdateDegreeDto;
import java.util.List;

public interface DegreeService {

    DegreeDto addDegree(CreateDegreeDto createDegreeDto);

    List<DegreeDto> getDegreeByFacultateName(String facultyName);

    void deleteByDegreeName(String degreeName);

    List<DegreeDto> getAll();

    DegreeDto updateDegree(UpdateDegreeDto updateDegreeDto);
}
