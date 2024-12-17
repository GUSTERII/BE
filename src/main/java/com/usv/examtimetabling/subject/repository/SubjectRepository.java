package com.usv.examtimetabling.subject.repository;
import com.usv.examtimetabling.faculty.degree.model.Degree;
import com.usv.examtimetabling.subject.model.Subject;
import com.usv.examtimetabling.user.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String subject);

    Optional<List<Subject>> findByTeacher(User teacherEntity);

    Optional<Subject> findByNameAndSemesterAndYearAndDegree(String name, Integer semester, Integer year, Degree degree);

  List<Subject> findByDegree(Degree degree);

  List<Subject> findByDegreeAndYearAndSemester(Degree degree, Integer year, Integer semester);
}
