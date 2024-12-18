package com.usv.examtimetabling.facultate.service.impl;

import com.usv.examtimetabling.facultate.specialization.repository.SpecializationRepository;
import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.model.dto.CreateFacultateDto;
import com.usv.examtimetabling.facultate.model.dto.FacultateDto;
import com.usv.examtimetabling.facultate.model.dto.UpdateFacultateDto;
import com.usv.examtimetabling.facultate.repository.FacultateRepository;
import com.usv.examtimetabling.facultate.service.FacultateService;
import com.usv.examtimetabling.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacultateServiceImpl implements FacultateService {

    private final FacultateRepository facultateRepository;

    private final StudentRepository studentRepository;

    private final SpecializationRepository specializationRepository;

    public List<FacultateDto> getAllFaculties() {
        return facultateRepository.findAll().stream()
                .map(facultate -> FacultateDto.builder()
                        .name(facultate.getLongName())
                        .numberOfStudents(studentRepository.countByFacultate(facultate))
                        .numberOfDegrees(specializationRepository.countByFacultate(facultate))
                        .build())
                .collect(Collectors.toList());
    }

    public Facultate addFacultate(CreateFacultateDto facultateDto) {
       Optional<Facultate> existingFacultate = facultateRepository.findByLongName(facultateDto.getName());
        if (existingFacultate.isPresent()) {
            throw new IllegalArgumentException("Facultate name already exists");
        }
        Facultate facultate = new Facultate();
        facultate.setLongName(facultateDto.getName());

        return facultateRepository.save(facultate);
    }

    @Transactional
    public void deleteByName(String name) {
        facultateRepository.deleteByLongName(name);
    }

  @Override
  public FacultateDto updateFacultate(UpdateFacultateDto updateFacultateDto) {
    Facultate facultate = facultateRepository.findByLongName(updateFacultateDto.getOldName()).orElseThrow();
    facultate.setLongName(updateFacultateDto.getNewName());
    Facultate updatedFacultate = facultateRepository.save(facultate);

    return FacultateDto.builder()
        .name(updatedFacultate.getLongName())
        .numberOfStudents(studentRepository.countByFacultate(updatedFacultate))
        .numberOfDegrees(specializationRepository.countByFacultate(updatedFacultate))
        .build();
  }
}
