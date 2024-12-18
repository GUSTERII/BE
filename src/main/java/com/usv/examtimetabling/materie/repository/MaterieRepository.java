package com.usv.examtimetabling.materie.repository;

import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.materie.model.Materie;
import com.usv.examtimetabling.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterieRepository extends JpaRepository<Materie, Long> {
  Optional<Materie> findByName(String subject);

  Optional<List<Materie>> findByTeacher(User teacherEntity);

  Optional<Materie> findByNameAndSemesterAndYearAndSpecialization(
      String name, Integer semester, Integer year, Specialization specialization);

  List<Materie> findBySpecialization(Specialization specialization);

  List<Materie> findBySpecializationAndYearAndSemester(
      Specialization specialization, Integer year, Integer semester);
}
