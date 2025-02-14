package com.usv.examtimetabling.facultate.specialization.subgrupa.repository;

import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubGrupaRepository extends JpaRepository<SubGrupa, Long> {

  Optional<SubGrupa> findByGroupNameAndStudyYearAndSpecializationShortName(String name, String studyYear, String specializationShortName);

  Optional<List<SubGrupa>> findByGroupName(String name);

  @Query("SELECT g FROM SubGrupa g WHERE g.groupName = ?1")
  SubGrupa findByGroupNameNotOptional(String groupName);

  Optional<SubGrupa> findBySpecializationShortName(String specializationShortName);

//  Optional<SubGrupa> findBySpecializationShortNameAndStudyYear(String specializationShortName, String studyYear);

  List<SubGrupa> findBySpecializationShortNameAndStudyYear(String specializationShortName, String studyYear);

  boolean existsByGroupNameAndSubgroupIndex(String groupName, String subgroupIndex);
}
