package com.usv.examtimetabling.materie.model;

import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.profesor.Profesor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "materie")
public class Materie {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materie_id_seq")
  @SequenceGenerator(name = "materie_id_seq", sequenceName = "materie_id_seq", allocationSize = 1)
  private Long id;

  private String name; // Maps to "topicLongName" from JSON

  private String shortName; // Maps to "topicShortName" from JSON

  private Integer semester;

  private Integer year;

  private String teacherPosition; // Maps to "positionShortName" from JSON

  private String teacherTitle; // Maps to "phdShortName" from JSON

  @ManyToOne
  @JoinColumn(name = "specialization_id")
  private Specialization specialization;

  @ManyToOne
  @JoinColumn(name = "profesor_id")
  private Profesor profesor;

  @ManyToOne
  @JoinColumn(name = "facultate_id")
  private Facultate facultate;

}
