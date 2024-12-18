package com.usv.examtimetabling.facultate.specialization.repository;

import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

  void deleteByName(String specializationName);

  Optional<Specialization> findByName(String specializationName);

  Optional<List<Specialization>> findByFacultate(Facultate facultate);

  Integer countByFacultate(Facultate faculty);
}
