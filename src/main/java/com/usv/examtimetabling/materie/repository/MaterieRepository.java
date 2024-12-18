package com.usv.examtimetabling.materie.repository;
import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.materie.model.Materie;
import com.usv.examtimetabling.user.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterieRepository extends JpaRepository<Materie, Long> {
    Optional<Materie> findByName(String subject);

    Optional<List<Materie>> findByTeacher(User teacherEntity);

    Optional<Materie> findByNameAndSemesterAndYearAndDegree(String name, Integer semester, Integer year, Specialization specialization);

  List<Materie> findByDegree(Specialization specialization);

  List<Materie> findByDegreeAndYearAndSemester(Specialization specialization, Integer year, Integer semester);
}
