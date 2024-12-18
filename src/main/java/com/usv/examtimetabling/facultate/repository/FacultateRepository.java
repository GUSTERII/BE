package com.usv.examtimetabling.facultate.repository;

import com.usv.examtimetabling.facultate.model.Facultate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultateRepository extends JpaRepository<Facultate, Long> {
  Optional<Facultate> findByLongName(String name);

  void deleteByLongName(String name);
}
