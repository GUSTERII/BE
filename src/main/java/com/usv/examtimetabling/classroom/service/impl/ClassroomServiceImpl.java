package com.usv.examtimetabling.classroom.service.impl;

import com.usv.examtimetabling.classroom.model.Sala;
import com.usv.examtimetabling.classroom.repository.ClassroomRepository;
import com.usv.examtimetabling.classroom.service.ClassroomService;
import com.usv.examtimetabling.faculty.repository.FacultateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

  private final ClassroomRepository classroomRepository;

  private final FacultateRepository facultateRepository;

  @Override
  public List<Sala> getAllClassrooms() {
    return classroomRepository.findAll();
  }

  @Override
  public Sala updateClassroom(Sala updateClassroomDto) {
    return classroomRepository.save(updateClassroomDto);
  }

  @Override
  public void deleteClassroom(String name) {
      classroomRepository.deleteByName(name);
  }
}
