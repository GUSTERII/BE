package com.usv.examtimetabling.materie.service.impl;

import com.usv.examtimetabling.exam.model.Exam;
import com.usv.examtimetabling.exam.repository.ExamRepository;
import com.usv.examtimetabling.facultate.repository.FacultateRepository;
import com.usv.examtimetabling.facultate.specialization.repository.SpecializationRepository;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.materie.model.Materie;
import com.usv.examtimetabling.materie.model.dto.MaterieDto;
import com.usv.examtimetabling.materie.repository.MaterieRepository;
import com.usv.examtimetabling.materie.service.MaterieService;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.profesor.Profesor;
import com.usv.examtimetabling.user.profesor.ProfesorRepository;
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

  private final ProfesorRepository pr;

  private final ExamRepository examRepository;

  private final SubGrupaRepository groupRepository;

  private final SpecializationRepository specializationRepository;

  private final FacultateRepository facultateRepository;

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
                    .teacher(
                        materie.getProfesor() != null
                            ? materie.getProfesor().getLastName()
                                + " "
                                + materie.getProfesor().getFirstName()
                            : null)
                    .year(materie.getYear())
                    .specialization(
                        materie.getSpecialization().getName() != null
                            ? materie.getProfesor().getEmailAddress()
                            : null)
                    .faculty(
                        materie.getFacultate() != null
                            ? materie.getFacultate().getLongName()
                            : null)
                    .build())
        .orElseThrow(() -> new IllegalArgumentException("Materie not found"));
  }

  public List<MaterieDto> getAll() {
    return materieRepository.findAll().stream().map(toMaterieDtoList()).toList();
  }

  public List<MaterieDto> getMaterieByTeacher(String teacher) {
    Profesor teacherEntity =
        pr
            .findByEmailAddress(teacher)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

    List<Materie> materies =
        materieRepository
            .findByProfesor(teacherEntity)
            .orElseThrow(() -> new IllegalArgumentException("Materie not found"));

    return materies.stream().map(toMaterieDtoList()).toList();
  }

  private static Function<Materie, MaterieDto> toMaterieDtoList() {
    return materie ->
        MaterieDto.builder()
            .name(materie.getName())
            .semester(materie.getSemester())
            .teacher(
                materie.getProfesor() != null
                    ? materie.getProfesor().getLastName()
                        + " "
                        + materie.getProfesor().getFirstName()
                    : null)
            .year(materie.getYear())
            .specialization(
                materie.getSpecialization() != null ? materie.getSpecialization().getName() : null)
            .faculty(materie.getFacultate() != null ? materie.getFacultate().getLongName() : null)
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
