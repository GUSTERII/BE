package com.usv.examtimetabling.faculty.degree.service.impl;

import com.usv.examtimetabling.faculty.degree.model.Degree;
import com.usv.examtimetabling.faculty.degree.model.dto.CreateDegreeDto;
import com.usv.examtimetabling.faculty.degree.model.dto.DegreeDto;
import com.usv.examtimetabling.faculty.degree.model.dto.UpdateDegreeDto;
import com.usv.examtimetabling.faculty.degree.repository.DegreeRepository;
import com.usv.examtimetabling.faculty.degree.service.DegreeService;
import com.usv.examtimetabling.faculty.model.Facultate;
import com.usv.examtimetabling.faculty.repository.FacultateRepository;
import com.usv.examtimetabling.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;

    private final FacultateRepository facultyRepository;

    private final StudentRepository studentRepository;

    public DegreeDto addDegree(CreateDegreeDto createDegreeDto) {
        Optional<Degree> existingDegree = degreeRepository.findByName(createDegreeDto.getName());
        if (existingDegree.isPresent()) {
            throw new IllegalArgumentException("Degree name already exists");
        }
        Facultate faculty = facultyRepository.findByLongName(createDegreeDto.getFacultyName())
                .orElseThrow(() -> new IllegalArgumentException("Facultate not found"));

        Degree degree = Degree.builder()
                .name(createDegreeDto.getName())
                .facultate(faculty)
                .build();

        Degree savedDegree = degreeRepository.save(degree);

        return DegreeDto.builder()
                .name(savedDegree.getName())
                .facultyName(savedDegree.getFacultate().getLongName())
                .numberOfStudents(studentRepository.countByDegree(savedDegree))
                .build();
    }

    public List<DegreeDto> getDegreeByFacultateName(String facultyName) {
        List<Degree> degrees = degreeRepository.findByFacultate(facultyRepository.findByLongName(facultyName).orElseThrow())
                .orElseThrow(() -> new IllegalArgumentException("Degree not found"));

        return degrees.stream()
                .map(degree -> DegreeDto.builder()
                        .name(degree.getName())
                        .facultyName(degree.getFacultate().getLongName())
                        .numberOfStudents(studentRepository.countByDegree(degree))
                        .build())
                .toList();
    }

    @Transactional
    public void deleteByDegreeName(String degreeName) {

        degreeRepository.deleteByName(degreeName);
    }

    @Override
    public List<DegreeDto> getAll() {
        List<Degree> degrees = degreeRepository.findAll();
        return degrees.stream()
                .map(degree -> DegreeDto.builder()
                        .name(degree.getName())
                        .facultyName(degree.getFacultate().getLongName())
                        .numberOfStudents(studentRepository.countByDegree(degree))
                        .build())
                .toList();
    }

    @Override
    public DegreeDto updateDegree(UpdateDegreeDto updateDegreeDto) {
        Degree degree = degreeRepository.findByName(updateDegreeDto.getOldName())
                .orElseThrow(() -> new IllegalArgumentException("Degree not found"));
        Facultate faculty = facultyRepository.findByLongName(updateDegreeDto.getFacultyName())
                .orElseThrow(() -> new IllegalArgumentException("Facultate not found"));

        degree.setName(updateDegreeDto.getNewName());
        degree.setFacultate(faculty);

        Degree updatedDegree = degreeRepository.save(degree);

        return DegreeDto.builder()
                .name(updatedDegree.getName())
                .facultyName(updatedDegree.getFacultate().getLongName())
                .numberOfStudents(studentRepository.countByDegree(updatedDegree))
                .build();
    }

}
