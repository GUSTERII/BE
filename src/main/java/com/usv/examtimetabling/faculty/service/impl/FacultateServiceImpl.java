package com.usv.examtimetabling.faculty.service.impl;

import com.usv.examtimetabling.faculty.degree.repository.DegreeRepository;
import com.usv.examtimetabling.faculty.model.Facultate;
import com.usv.examtimetabling.faculty.model.dto.CreateFacultateDto;
import com.usv.examtimetabling.faculty.model.dto.FacultateDto;
import com.usv.examtimetabling.faculty.model.dto.UpdateFacultateDto;
import com.usv.examtimetabling.faculty.repository.FacultateRepository;
import com.usv.examtimetabling.faculty.service.FacultateService;
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

    private final DegreeRepository degreeRepository;

    public List<FacultateDto> getAllFaculties() {
        return facultateRepository.findAll().stream()
                .map(faculty -> FacultateDto.builder()
                        .name(faculty.getLongName())
                        .numberOfStudents(studentRepository.countByFacultate(faculty))
                        .numberOfDegrees(degreeRepository.countByFacultate(faculty))
                        .build())
                .collect(Collectors.toList());
    }

    public Facultate addFacultate(CreateFacultateDto facultyDto) {
       Optional<Facultate> existingFacultate = facultateRepository.findByLongName(facultyDto.getName());
        if (existingFacultate.isPresent()) {
            throw new IllegalArgumentException("Facultate name already exists");
        }
        Facultate faculty = new Facultate();
        faculty.setLongName(facultyDto.getName());

        return facultateRepository.save(faculty);
    }

    @Transactional
    public void deleteByName(String name) {
        facultateRepository.deleteByLongName(name);
    }

  @Override
  public FacultateDto updateFacultate(UpdateFacultateDto updateFacultateDto) {
    Facultate faculty = facultateRepository.findByLongName(updateFacultateDto.getOldName()).orElseThrow();
    faculty.setLongName(updateFacultateDto.getNewName());
    Facultate updatedFacultate = facultateRepository.save(faculty);

    return FacultateDto.builder()
        .name(updatedFacultate.getLongName())
        .numberOfStudents(studentRepository.countByFacultate(updatedFacultate))
        .numberOfDegrees(degreeRepository.countByFacultate(updatedFacultate))
        .build();
  }
}
