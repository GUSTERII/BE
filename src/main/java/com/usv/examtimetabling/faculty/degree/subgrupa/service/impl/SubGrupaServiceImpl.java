package com.usv.examtimetabling.faculty.degree.subgrupa.service.impl;

import com.usv.examtimetabling.faculty.degree.model.Degree;
import com.usv.examtimetabling.faculty.degree.repository.DegreeRepository;
import com.usv.examtimetabling.faculty.degree.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.faculty.degree.subgrupa.model.dto.SubGrupaDto;
import com.usv.examtimetabling.faculty.degree.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.faculty.degree.subgrupa.service.SubGrupaService;
import com.usv.examtimetabling.user.student.model.Student;
import com.usv.examtimetabling.user.student.repository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubGrupaServiceImpl implements SubGrupaService {

  private final SubGrupaRepository subGrupaRepository;

  private final DegreeRepository degreeRepository;

  private final StudentRepository studentRepository;
  private static final int MAX_GROUP_SIZE = 30;

  public SubGrupaDto createSubGrupa(SubGrupaDto subGrupaDto) {
    Degree degree =
        degreeRepository
            .findByName(subGrupaDto.getDegree())
            .orElseThrow(() -> new IllegalArgumentException("Degree not found"));

    Optional<SubGrupa> existingSubGrupa =
        subGrupaRepository.findByGroupNameAndStudyYearAndSpecializationShortName(
            subGrupaDto.getName(), String.valueOf(subGrupaDto.getYear()), degree.getName());
    if (existingSubGrupa.isPresent()) {
      throw new IllegalArgumentException(
          "SubGrupa with the same name, year, and degree already exists");
    }

    SubGrupa subGrupa =
        SubGrupa.builder()
            .specializationShortName(subGrupaDto.getName())
            .subgroupIndex(String.valueOf(1))
            .studyYear(String.valueOf(subGrupaDto.getYear()))
            .build();
    subGrupaRepository.save(subGrupa);

    return subGrupaDto;
  }

  public SubGrupaDto createSubGrupaForAutoAssignment(SubGrupaDto subGrupaDto) {
    Degree degree =
        degreeRepository
            .findByName(subGrupaDto.getDegree())
            .orElseThrow(() -> new IllegalArgumentException("Degree not found"));

    Optional<SubGrupa> existingSubGrupa =
        subGrupaRepository.findByGroupNameAndStudyYearAndSpecializationShortName(
            subGrupaDto.getName(), String.valueOf(subGrupaDto.getYear()), degree.getName());
    if (existingSubGrupa.isPresent()) {
      return existingSubGrupa
          .map(
              subGrupa ->
                  SubGrupaDto.builder()
                      .name(subGrupa.getGroupName())
                      .degree(subGrupa.getSpecializationShortName())
                      .year(Integer.valueOf(subGrupa.getStudyYear()))
                      .build())
          .orElseThrow(() -> new IllegalArgumentException("SubGrupa not found"));
    }

    SubGrupa subGrupa =
        SubGrupa.builder()
            .groupName(subGrupaDto.getName())
            .specializationShortName(degree.getName())
            .studyYear(String.valueOf(subGrupaDto.getYear()))
            .build();
    subGrupaRepository.save(subGrupa);

    return subGrupaDto;
  }

  public SubGrupaDto getSubGrupaByDegreeName(String degreeName) {
    SubGrupa subGrupa =
        subGrupaRepository
            .findBySpecializationShortName(degreeName)
            .orElseThrow(() -> new IllegalArgumentException("SubGrupa not found"));

    return SubGrupaDto.builder()
        .name(subGrupa.getGroupName())
        .degree(subGrupa.getSpecializationShortName())
        .year(Integer.valueOf(subGrupa.getStudyYear()))
        .build();
  }

//  public SubGrupaDto getSubGrupaByDegreeAndYear(String degreeName, Integer year) {
//    SubGrupa subGrupa =
//        subGrupaRepository
//            .findBySpecializationShortNameAndStudyYear(degreeName, String.valueOf(year))
//            .orElseThrow(() -> new IllegalArgumentException("SubGrupa not found"));
//
//    return SubGrupaDto.builder()
//        .name(subGrupa.getGroupName())
//        .degree(subGrupa.getSpecializationShortName() )
//        .year(Integer.valueOf(subGrupa.getStudyYear()))
//        .build();
//  }

  public void assignStudentsToSubGrupa() {
    List<Student> studentsWithoutSubGrupa =
        studentRepository.findAllByDegreeIsNotNullAndYearIsNotNullAndSubGrupaIsNull();

    Map<Pair<Degree, Integer>, List<Student>> subGrupaedStudents =
        studentsWithoutSubGrupa.stream()
            .collect(
                Collectors.groupingBy(
                    student -> Pair.of(student.getDegree(), student.getYear())));

    for (Map.Entry<Pair<Degree, Integer>, List<Student>> entry : subGrupaedStudents.entrySet()) {
      Pair<Degree, Integer> degreeYearPair = entry.getKey();
      List<Student> students = entry.getValue();

      int subGrupaSize = students.size() <= MAX_GROUP_SIZE ? students.size() : students.size() / 2;

      if (students.size() > MAX_GROUP_SIZE && students.size() % 2 != 0) {
        subGrupaSize++;
      }

      List<List<Student>> subSubGrupas = new ArrayList<>();
      for (int i = 0; i < students.size(); i += subGrupaSize) {
        subSubGrupas.add(students.subList(i, Math.min(i + subGrupaSize, students.size())));
      }
      char subGrupaNameSuffix = 'a';
      for (List<Student> subSubGrupa : subSubGrupas) {
        String subGrupaName =
            degreeYearPair.getFirst().getName() + degreeYearPair.getSecond() + subGrupaNameSuffix;
        SubGrupaDto subGrupaDto =
            SubGrupaDto.builder()
                .name(subGrupaName)
                .degree(degreeYearPair.getFirst().getName())
                .year(degreeYearPair.getSecond())
                .build();
        subGrupaDto = createSubGrupaForAutoAssignment(subGrupaDto);
        SubGrupa subGrupa =
            subGrupaRepository
                .findByGroupNameAndStudyYearAndSpecializationShortName(
                    subGrupaDto.getName(), String.valueOf(subGrupaDto.getYear()), degreeYearPair.getFirst().getName())
                .orElseThrow(() -> new IllegalArgumentException("SubGrupa not found"));

        for (Student student : subSubGrupa) {
          student.setSubGrupa(subGrupa);
          studentRepository.save(student);
        }

        subGrupaNameSuffix++;
      }
    }
  }

  @Override
  public List<SubGrupaDto> getAllSubGrupas() {
    return subGrupaRepository.findAll().stream()
        .map(
            subGrupa ->
                SubGrupaDto.builder()
                    .name(subGrupa.getSpecializationShortName())
                    .degree(subGrupa.getSpecializationShortName())
                    .year(Integer.valueOf(subGrupa.getStudyYear()))
                    .build())
        .collect(Collectors.toList());
  }
}
