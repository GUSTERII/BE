package com.usv.examtimetabling.classroom.repository;


import com.usv.examtimetabling.classroom.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Sala, Integer> {
  void deleteByName(String name);
  Sala findByName(String name);
}
