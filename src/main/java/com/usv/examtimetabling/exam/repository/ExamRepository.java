package com.usv.examtimetabling.exam.repository;

import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.exam.model.Exam;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.materie.model.Materie;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

  Exam findByNameAndSubGrupa_GroupName(String name, String groupName);

  @Query("SELECT e FROM Exam e WHERE e.date = :date AND e.materie.teacher.id = :teacherId")
  List<Exam> findByDateAndTeacher(
      @Param("date") LocalDate date, @Param("teacherId") Integer teacherId);

  List<Exam> findBySubGrupa(SubGrupa group);

  Optional<List<Exam>> findByMaterie(Materie materie);

  @Query("SELECT e FROM Exam e JOIN e.materie s JOIN s.specialization d WHERE d.name = :specializationName")
  List<Exam> findExamsBySpecialization(@Param("specializationName") String specializationName);

  List<Exam> findBySala(Sala classroom);

  Optional<Exam> findBySubGrupaAndMaterie(SubGrupa group, Materie materie);
}
