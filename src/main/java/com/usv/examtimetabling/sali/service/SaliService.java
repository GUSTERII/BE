package com.usv.examtimetabling.sali.service;

import com.usv.examtimetabling.sali.model.Sala;
import java.util.List;

public interface SaliService {

  List<Sala> getAllClassrooms();

  Sala updateClassroom(Sala updateClassroomDto);

  void deleteClassroom(String name);
}
