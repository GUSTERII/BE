package com.usv.examtimetabling.exam.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.exam.util.ExamStatus;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.materie.model.Materie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "exam")
public class Exam {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

    @NotNull
  @Column(nullable = false)
  private String name;

  private String description;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "dd-MM-yyyy", timezone = "UTC")
  private LocalDate date;

  @NotNull
  @Column(nullable = false)
  @JsonFormat(pattern = "HH:mm", timezone = "UTC")
  private LocalTime start;

  @NotNull
  @Column(nullable = false)
  private int duration;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "classroom_id", nullable = false)
  private Sala sala;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "group_id", nullable = false)
  private SubGrupa subGrupa;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "subject_id", nullable = false)
  private Materie materie;

  private ExamStatus status;
}
