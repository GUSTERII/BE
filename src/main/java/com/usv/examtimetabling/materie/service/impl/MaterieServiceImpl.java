package com.usv.examtimetabling.materie.service.impl;

import com.usv.examtimetabling.exam.model.Exam;
import com.usv.examtimetabling.exam.repository.ExamRepository;
import com.usv.examtimetabling.facultate.repository.FacultateRepository;
import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.facultate.specialization.repository.SpecializationRepository;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.materie.model.Materie;
import com.usv.examtimetabling.materie.model.dto.CreateMaterieDto;
import com.usv.examtimetabling.materie.model.dto.MaterieDto;
import com.usv.examtimetabling.materie.model.dto.UpdateMaterieDto;
import com.usv.examtimetabling.materie.repository.MaterieRepository;
import com.usv.examtimetabling.materie.service.MaterieService;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaterieServiceImpl implements MaterieService {

  private final MaterieRepository materieRepository;

  private final UserRepository userRepository;

  private final ExamRepository examRepository;

  private final SubGrupaRepository groupRepository;

  private final SpecializationRepository specializationRepository;

  private final FacultateRepository facultateRepository;

  public MaterieDto addMaterie(CreateMaterieDto createMaterieDto) {

    Specialization specialization =
        specializationRepository
            .findByName(createMaterieDto.getSpecialization())
            .orElseThrow(() -> new IllegalArgumentException("Specialization not found"));

    User teacher =
        userRepository
            .findByName(createMaterieDto.getTeacher())
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

    if (materieRepository
        .findByNameAndSemesterAndYearAndSpecialization(
            createMaterieDto.getName(),
            createMaterieDto.getSemester(),
            createMaterieDto.getYear(),
            specialization)
        .isPresent()) {
      throw new IllegalArgumentException("Materie already exists");
    }

    Materie materie =
        materieRepository.save(
            Materie.builder()
                .name(createMaterieDto.getName())
                .semester(createMaterieDto.getSemester())
                .year(createMaterieDto.getYear())
                .specialization(specialization)
                .facultate(
                    createMaterieDto.getFaculty() != null
                        ? facultateRepository
                            .findByLongName(createMaterieDto.getFaculty())
                            .orElseThrow(() -> new IllegalArgumentException("Faculty not found"))
                        : null)
                .teacher(teacher)
                .examDuration(createMaterieDto.getExamDuration())
                .build());

    return MaterieDto.builder()
        .name(materie.getName())
        .semester(materie.getSemester())
        .year(materie.getYear())
        .specialization(materie.getSpecialization().getName())
        .faculty(materie.getFacultate() != null ? materie.getFacultate().getLongName() : null)
        .teacher(materie.getTeacher().getEmail())
        .examDuration(materie.getExamDuration())
        .build();
  }

  public MaterieDto addTeacher(String name, String teacher) {
    Materie materie =
        materieRepository
            .findByName(name)
            .orElseThrow(() -> new IllegalArgumentException("Materie not found"));

    User teacherEntity =
        userRepository
            .findByEmail(teacher)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

    if (teacherEntity.getRole() != Role.PROFESSOR) {
      throw new IllegalArgumentException("User is not a teacher");
    }

    materie.setTeacher(teacherEntity);

    List<SubGrupa> groups =
        groupRepository.findBySpecializationShortNameAndStudyYear(
            materie.getSpecialization().getName(), String.valueOf(materie.getYear()));

    for (SubGrupa group : groups) {
      Exam exam = Exam.builder().name(materie.getName()).subGrupa(group).materie(materie).build();

      examRepository.save(exam);
    }

    return MaterieDto.builder()
        .name(materieRepository.save(materie).getName())
        .semester(materie.getSemester())
        .teacher(materie.getTeacher().getEmail())
        .year(materie.getYear())
        .specialization(materie.getSpecialization().getName())
        .faculty(materie.getFacultate() != null ? materie.getFacultate().getLongName() : null)
        .build();
  }

  public void ifMaterieThenExam() {
    List<Materie> materies = materieRepository.findAll();
    for (Materie materie : materies) {
      List<SubGrupa> groups =
          groupRepository.findBySpecializationShortNameAndStudyYear(
              materie.getSpecialization().getName(), String.valueOf(materie.getYear()));
      for (SubGrupa group : groups) {
        Exam exam = Exam.builder().name(materie.getName()).subGrupa(group).materie(materie).build();

        examRepository.save(exam);
      }
    }
  }

  @Override
  public MaterieDto getMaterieByName(String name) {
    return materieRepository
        .findByName(name)
        .map(
            materie ->
                MaterieDto.builder()
                    .name(materie.getName())
                    .semester(materie.getSemester())
                    .teacher(materie.getTeacher() != null ? materie.getTeacher().getName() : null)
                    .year(materie.getYear())
                    .specialization(
                        materie.getSpecialization().getName() != null
                            ? materie.getTeacher().getEmail()
                            : null)
                    .faculty(
                        materie.getFacultate() != null
                            ? materie.getFacultate().getLongName()
                            : null)
                    .build())
        .orElseThrow(() -> new IllegalArgumentException("Materie not found"));
  }

  public MaterieDto updateMaterie(UpdateMaterieDto updateMaterieDto) {
    Materie materie =
        materieRepository
            .findByName(updateMaterieDto.getOldName())
            .orElseThrow(() -> new IllegalArgumentException("Materie not found"));

    materie.setName(updateMaterieDto.getName());
    materie.setSemester(updateMaterieDto.getSemester());
    materie.setYear(updateMaterieDto.getYear());
    materie.setSpecialization(
        specializationRepository
            .findByName(updateMaterieDto.getSpecialization())
            .orElseThrow(() -> new IllegalArgumentException("Specialization not found")));
    materie.setTeacher(
        userRepository
            .findByName(updateMaterieDto.getTeacher())
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found")));
    materie.setExamDuration(updateMaterieDto.getExamDuration());

    return MaterieDto.builder()
        .name(materieRepository.save(materie).getName())
        .semester(materie.getSemester())
        .teacher(materie.getTeacher() != null ? materie.getTeacher().getEmail() : null)
        .year(materie.getYear())
        .specialization(
            materie.getSpecialization().getName() != null ? materie.getTeacher().getEmail() : null)
        .examDuration(materie.getExamDuration())
        .build();
  }

  public List<MaterieDto> getAll() {
    return materieRepository.findAll().stream().map(toMaterieDtoList()).toList();
  }

  public List<MaterieDto> getMaterieByTeacher(String teacher) {
    User teacherEntity =
        userRepository
            .findByEmail(teacher)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

    List<Materie> materies =
        materieRepository
            .findByTeacher(teacherEntity)
            .orElseThrow(() -> new IllegalArgumentException("Materie not found"));

    return materies.stream().map(toMaterieDtoList()).toList();
  }

  private static Function<Materie, MaterieDto> toMaterieDtoList() {
    return materie ->
        MaterieDto.builder()
            .name(materie.getName())
            .semester(materie.getSemester())
            .teacher(materie.getTeacher() != null ? materie.getTeacher().getName() : null)
            .year(materie.getYear())
            .specialization(
                materie.getSpecialization() != null ? materie.getSpecialization().getName() : null)
            .faculty(materie.getFacultate() != null ? materie.getFacultate().getLongName() : null)
            .examDuration(materie.getExamDuration())
            .build();
  }

  @Transactional
  public void deleteMaterie(String materie) {
    Optional<Materie> materieEntityOptional = materieRepository.findByName(materie);
    if (materieEntityOptional.isPresent()) {
      materieRepository.delete(materieEntityOptional.get());
    } else {
      throw new IllegalArgumentException("Materie not found");
    }
  }
}
