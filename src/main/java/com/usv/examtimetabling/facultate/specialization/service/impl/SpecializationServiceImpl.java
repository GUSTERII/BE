package com.usv.examtimetabling.facultate.specialization.service.impl;

import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.facultate.specialization.model.dto.CreateSpecializationDto;
import com.usv.examtimetabling.facultate.specialization.model.dto.SpecializationDto;
import com.usv.examtimetabling.facultate.specialization.model.dto.UpdateSpecializationDto;
import com.usv.examtimetabling.facultate.specialization.repository.SpecializationRepository;
import com.usv.examtimetabling.facultate.specialization.service.SpecializationService;
import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.repository.FacultateRepository;
import com.usv.examtimetabling.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;

    private final FacultateRepository facultyRepository;

    private final StudentRepository studentRepository;

    public SpecializationDto addSpecialization(CreateSpecializationDto createSpecializationDto) {
        Optional<Specialization> existingSpecialization = specializationRepository.findByName(createSpecializationDto.getName());
        if (existingSpecialization.isPresent()) {
            throw new IllegalArgumentException("Specialization name already exists");
        }
        Facultate faculty = facultyRepository.findByLongName(createSpecializationDto.getFacultateName())
                .orElseThrow(() -> new IllegalArgumentException("Facultate not found"));

        Specialization specialization = Specialization.builder()
                .name(createSpecializationDto.getName())
                .facultate(faculty)
                .build();

        Specialization savedSpecialization = specializationRepository.save(specialization);

        return SpecializationDto.builder()
                .name(savedSpecialization.getName())
                .facultateName(savedSpecialization.getFacultate().getLongName())
                .numberOfStudents(studentRepository.countBySpecialization(savedSpecialization))
                .build();
    }

    public List<SpecializationDto> getSpecializationByFacultateName(String facultyName) {
        List<Specialization> specializations = specializationRepository.findByFacultate(facultyRepository.findByLongName(facultyName).orElseThrow())
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found"));

        return specializations.stream()
                .map(degree -> SpecializationDto.builder()
                        .name(degree.getName())
                        .facultateName(degree.getFacultate().getLongName())
                        .numberOfStudents(studentRepository.countBySpecialization(degree))
                        .build())
                .toList();
    }

    @Transactional
    public void deleteBySpecializationName(String degreeName) {

        specializationRepository.deleteByName(degreeName);
    }

    @Override
    public List<SpecializationDto> getAll() {
        List<Specialization> specializations = specializationRepository.findAll();
        return specializations.stream()
                .map(specialization -> SpecializationDto.builder()
                        .name(specialization.getName())
                        .facultateName(specialization.getFacultate().getLongName())
                        .numberOfStudents(studentRepository.countBySpecialization(specialization))
                        .build())
                .toList();
    }

    @Override
    public SpecializationDto updateSpecialization(UpdateSpecializationDto updateSpecializationDto) {
        Specialization specialization = specializationRepository.findByName(updateSpecializationDto.getOldName())
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found"));
        Facultate faculty = facultyRepository.findByLongName(updateSpecializationDto.getFacultateName())
                .orElseThrow(() -> new IllegalArgumentException("Facultate not found"));

        specialization.setName(updateSpecializationDto.getNewName());
        specialization.setFacultate(faculty);

        Specialization updatedSpecialization = specializationRepository.save(specialization);

        return SpecializationDto.builder()
                .name(updatedSpecialization.getName())
                .facultateName(updatedSpecialization.getFacultate().getLongName())
                .numberOfStudents(studentRepository.countBySpecialization(updatedSpecialization))
                .build();
    }

}
