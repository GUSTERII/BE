package com.usv.examtimetabling.sali.service.impl;

import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.sali.repository.SaliRepository;
import com.usv.examtimetabling.sali.service.SaliService;
import com.usv.examtimetabling.facultate.repository.FacultateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaliServiceImpl implements SaliService {

  private final SaliRepository saliRepository;

  private final FacultateRepository facultateRepository;

  @Override
  public List<Sala> getAllClassrooms() {
    return saliRepository.findAll();
  }

  @Override
  public Sala updateClassroom(Sala updateClassroomDto) {
    return saliRepository.save(updateClassroomDto);
  }

  @Override
  public void deleteClassroom(String name) {
      saliRepository.deleteByName(name);
  }
}
