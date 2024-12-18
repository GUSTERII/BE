package com.usv.examtimetabling.facultate.service;

import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.model.dto.CreateFacultateDto;
import com.usv.examtimetabling.facultate.model.dto.FacultateDto;
import com.usv.examtimetabling.facultate.model.dto.UpdateFacultateDto;
import java.util.List;

public interface FacultateService {
  List<FacultateDto> getAllFaculties();

  Facultate addFacultate(CreateFacultateDto facultyDto);

  void deleteByName(String name);

  FacultateDto updateFacultate(UpdateFacultateDto updateFacultateDto);
}
