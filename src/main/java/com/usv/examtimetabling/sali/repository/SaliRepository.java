package com.usv.examtimetabling.sali.repository;


import com.usv.examtimetabling.sali.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaliRepository extends JpaRepository<Sala, Integer> {
  void deleteByName(String name);
  Sala findByName(String name);
}
