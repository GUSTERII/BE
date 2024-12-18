package com.usv.examtimetabling.user.student.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.user.model.utils.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "student")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  private String name;

  @Column(name = "email", unique = true)
  private String email;

  private String password;

  private Role role;

  private Integer year;

  @ManyToOne
  @JoinColumn(name = "facultate_id")
  @JsonBackReference
  private Facultate facultate;

  @ManyToOne
  @JoinColumn(name = "specialization_id")
  @JsonBackReference
  private Specialization specialization;

  @ManyToOne
  @JoinColumn(name = "sub_grupa_id")
  @JsonBackReference
  private SubGrupa subGrupa;
}
