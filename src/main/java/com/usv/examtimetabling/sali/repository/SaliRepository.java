package com.usv.examtimetabling.sali.repository;


import com.usv.examtimetabling.sali.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaliRepository extends JpaRepository<Sala, Integer> {
  void deleteByName(String name);
  Sala findByName(String name);
  List<Sala> findAllByName(String name);

  boolean existsByName(String name);
}
