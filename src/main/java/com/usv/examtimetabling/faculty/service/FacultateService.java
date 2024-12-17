package com.usv.examtimetabling.faculty.service;

import com.usv.examtimetabling.faculty.model.Facultate;
import com.usv.examtimetabling.faculty.model.dto.CreateFacultateDto;
import com.usv.examtimetabling.faculty.model.dto.FacultateDto;
import com.usv.examtimetabling.faculty.model.dto.UpdateFacultateDto;
import java.util.List;

public interface FacultateService {
  List<FacultateDto> getAllFaculties();

  Facultate addFacultate(CreateFacultateDto facultyDto);

  void deleteByName(String name);

  FacultateDto updateFacultate(UpdateFacultateDto updateFacultateDto);
}
