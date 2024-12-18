package com.usv.examtimetabling.orar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrarEntryRepository extends JpaRepository<OrarEntry, Long> {}
