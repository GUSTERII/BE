package com.usv.examtimetabling.facultate.specialization.subgrupa.service.impl;

import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.facultate.specialization.repository.SpecializationRepository;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.dto.SubGrupaDto;
import com.usv.examtimetabling.facultate.specialization.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.facultate.specialization.subgrupa.service.SubGrupaService;
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

  private final SpecializationRepository specializationRepository;

  private final StudentRepository studentRepository;
  private static final int MAX_GROUP_SIZE = 30;

  public SubGrupaDto createSubGrupa(SubGrupaDto subGrupaDto) {
    Specialization specialization =
        specializationRepository
            .findByName(subGrupaDto.getSpecialization())
            .orElseThrow(() -> new IllegalArgumentException("Specialization not found"));

    Optional<SubGrupa> existingSubGrupa =
        subGrupaRepository.findByGroupNameAndStudyYearAndSpecializationShortName(
            subGrupaDto.getName(), String.valueOf(subGrupaDto.getYear()), specialization.getName());
    if (existingSubGrupa.isPresent()) {
      throw new IllegalArgumentException(
          "SubGrupa with the same name, year, and specialization already exists");
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
    Specialization specialization =
        specializationRepository
            .findByName(subGrupaDto.getSpecialization())
            .orElseThrow(() -> new IllegalArgumentException("Specialization not found"));

    Optional<SubGrupa> existingSubGrupa =
        subGrupaRepository.findByGroupNameAndStudyYearAndSpecializationShortName(
            subGrupaDto.getName(), String.valueOf(subGrupaDto.getYear()), specialization.getName());
    if (existingSubGrupa.isPresent()) {
      return existingSubGrupa
          .map(
              subGrupa ->
                  SubGrupaDto.builder()
                      .name(subGrupa.getGroupName())
                      .specialization(subGrupa.getSpecializationShortName())
                      .year(Integer.valueOf(subGrupa.getStudyYear()))
                      .build())
          .orElseThrow(() -> new IllegalArgumentException("SubGrupa not found"));
    }

    SubGrupa subGrupa =
        SubGrupa.builder()
            .groupName(subGrupaDto.getName())
            .specializationShortName(specialization.getName())
            .studyYear(String.valueOf(subGrupaDto.getYear()))
            .build();
    subGrupaRepository.save(subGrupa);

    return subGrupaDto;
  }

  public SubGrupaDto getSubGrupaBySpecializationName(String specializationName) {
    SubGrupa subGrupa =
        subGrupaRepository
            .findBySpecializationShortName(specializationName)
            .orElseThrow(() -> new IllegalArgumentException("SubGrupa not found"));

    return SubGrupaDto.builder()
        .name(subGrupa.getGroupName())
        .specialization(subGrupa.getSpecializationShortName())
        .year(Integer.valueOf(subGrupa.getStudyYear()))
        .build();
  }

//  public SubGrupaDto getSubGrupaBySpecializationAndYear(String specializationName, Integer year) {
//    SubGrupa subGrupa =
//        subGrupaRepository
//            .findBySpecializationShortNameAndStudyYear(specializationName, String.valueOf(year))
//            .orElseThrow(() -> new IllegalArgumentException("SubGrupa not found"));
//
//    return SubGrupaDto.builder()
//        .name(subGrupa.getGroupName())
//        .specialization(subGrupa.getSpecializationShortName() )
//        .year(Integer.valueOf(subGrupa.getStudyYear()))
//        .build();
//  }

  public void assignStudentsToSubGrupa() {
    List<Student> studentsWithoutSubGrupa =
        studentRepository.findAllBySpecializationIsNotNullAndYearIsNotNullAndSubGrupaIsNull();

    Map<Pair<Specialization, Integer>, List<Student>> subGrupaedStudents =
        studentsWithoutSubGrupa.stream()
            .collect(
                Collectors.groupingBy(
                    student -> Pair.of(student.getSpecialization(), student.getYear())));

    for (Map.Entry<Pair<Specialization, Integer>, List<Student>> entry : subGrupaedStudents.entrySet()) {
      Pair<Specialization, Integer> specializationYearPair = entry.getKey();
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
            specializationYearPair.getFirst().getName() + specializationYearPair.getSecond() + subGrupaNameSuffix;
        SubGrupaDto subGrupaDto =
            SubGrupaDto.builder()
                .name(subGrupaName)
                .specialization(specializationYearPair.getFirst().getName())
                .year(specializationYearPair.getSecond())
                .build();
        subGrupaDto = createSubGrupaForAutoAssignment(subGrupaDto);
        SubGrupa subGrupa =
            subGrupaRepository
                .findByGroupNameAndStudyYearAndSpecializationShortName(
                    subGrupaDto.getName(), String.valueOf(subGrupaDto.getYear()), specializationYearPair.getFirst().getName())
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
                    .specialization(subGrupa.getSpecializationShortName())
                    .year(Integer.valueOf(subGrupa.getStudyYear()))
                    .build())
        .collect(Collectors.toList());
  }
}
