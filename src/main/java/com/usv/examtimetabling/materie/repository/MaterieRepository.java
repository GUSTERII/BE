package com.usv.examtimetabling.materie.repository;

import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.materie.model.Materie;
import com.usv.examtimetabling.user.profesor.Profesor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterieRepository extends JpaRepository<Materie, Long> {
  Optional<Materie> findByName(String name);

  List<Materie> findAllByName(String name);

  Optional<List<Materie>> findByProfesor(Profesor profesor);

  Optional<Materie> findByNameAndSemesterAndYearAndSpecialization(
      String name, Integer semester, Integer year, Specialization specialization);

  List<Materie> findBySpecialization(Specialization specialization);

  List<Materie> findBySpecializationAndYearAndSemester(
      Specialization specialization, Integer year, Integer semester);

  boolean existsByName(String subjectName);
}
