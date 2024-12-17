package com.usv.examtimetabling.faculty.degree.repository;

import com.usv.examtimetabling.faculty.degree.model.Degree;
import com.usv.examtimetabling.faculty.model.Facultate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {

  void deleteByName(String degreeName);

  Optional<Degree> findByName(String degree);

  Optional<List<Degree>> findByFacultate(Facultate facultate);

  Integer countByFacultate(Facultate faculty);
}
