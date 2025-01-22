package com.usv.examtimetabling.exam.repository;

import com.usv.examtimetabling.exam.model.ExamsPeriod;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamsPeriodRepository extends JpaRepository<ExamsPeriod, Integer> {

  ExamsPeriod findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

  @Query("SELECT ep FROM ExamsPeriod ep WHERE ep.startDate >= :date ORDER BY ep.startDate ASC LIMIT 1")
  ExamsPeriod findNextPeriod(LocalDate date);

  @Query("SELECT ep FROM ExamsPeriod ep WHERE ep.startDate <= :currentDate AND ep.endDate >= :currentDate")
  ExamsPeriod findCurrentPeriod(LocalDate currentDate);
}
