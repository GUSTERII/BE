package com.usv.examtimetabling.materie.service;

import com.usv.examtimetabling.materie.model.dto.MaterieDto;
import java.util.List;

public interface MaterieService {

  List<MaterieDto> getAll();

  List<MaterieDto> getMaterieByTeacher(String teacher);

  void deleteMaterie(String materie);

  void ifMaterieThenExam();

  MaterieDto getMaterieByName(String name);
}
