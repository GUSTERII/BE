package com.usv.examtimetabling.materie.service;

import com.usv.examtimetabling.materie.model.dto.CreateMaterieDto;
import com.usv.examtimetabling.materie.model.dto.MaterieDto;
import com.usv.examtimetabling.materie.model.dto.UpdateMaterieDto;
import java.util.List;

public interface MaterieService {
  MaterieDto addTeacher(String name, String teacher);

  MaterieDto addMaterie(CreateMaterieDto createMaterieDto);

  List<MaterieDto> getAll();

  List<MaterieDto> getMaterieByTeacher(String teacher);

  MaterieDto updateMaterie(UpdateMaterieDto updateMaterieDto);

  void deleteMaterie(String materie);

  void ifMaterieThenExam();

  MaterieDto getMaterieByName(String name);
}
