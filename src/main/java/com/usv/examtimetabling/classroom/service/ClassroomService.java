package com.usv.examtimetabling.classroom.service;

import com.usv.examtimetabling.classroom.model.Sala;
import java.util.List;

public interface ClassroomService {

  List<Sala> getAllClassrooms();

  Sala updateClassroom(Sala updateClassroomDto);

  void deleteClassroom(String name);
}
