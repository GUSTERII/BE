package com.usv.examtimetabling.user.student.repository;

import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.student.model.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
  Optional<Student> findAllByFacultateLongName(String facultateLongName);

  Optional<Student> findAllBySpecializationName(String degree);

  Optional<Student> findAllBySubGrupaGroupName(String subGrupaGroupName);

  Optional<Student> findByEmail(String email);

  Student findBySubGrupaAndRole(SubGrupa group, Role role);

  void deleteByEmail(String email);

  List<Student> findAllBySpecializationIsNotNullAndYearIsNotNullAndSubGrupaIsNull();

  Integer countByFacultate(Facultate facultate);

  Integer countBySpecialization(Specialization savedSpecialization);

  Integer countBySubGrupa(SubGrupa subGrupa);

}
