package com.usv.examtimetabling.facultate.specialization.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.user.student.model.Student;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Builder
@Table(name = "degree")
public class Specialization {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true)
  private String name;

  @ManyToOne()
  @JoinColumn(name = "facultate_id")
  @JsonBackReference
  private Facultate facultate;

  @OneToMany(mappedBy = "degree")
  @JsonManagedReference
  private List<Student> students;
}
