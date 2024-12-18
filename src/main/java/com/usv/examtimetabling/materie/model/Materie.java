package com.usv.examtimetabling.materie.model;

import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.user.model.User;
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
@Table(name = "subject")
public class Materie {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_id_seq")
  @SequenceGenerator(name = "subject_id_seq", sequenceName = "subject_id_seq", allocationSize = 1)
  private Long id;

  private String name;

  private Integer semester;

  private Integer year;

  @ManyToOne
  @JoinColumn(name = "degree_id")
  private Specialization specialization;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private User teacher;

  @ManyToOne
  @JoinColumn(name = "facultate_id")
  private Facultate facultate;

  private Integer examDuration;
}
