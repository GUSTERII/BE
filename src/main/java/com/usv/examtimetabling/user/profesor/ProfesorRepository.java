package com.usv.examtimetabling.user.profesor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
  Optional<Profesor> findByFirstNameAndLastName(String firstName, String lastName);

  Optional<Profesor> findByEmailAddress(String teacher);
}
