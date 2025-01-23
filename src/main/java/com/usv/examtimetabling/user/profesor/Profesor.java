package com.usv.examtimetabling.user.profesor;

import com.usv.examtimetabling.exam.model.Exam;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profesor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "email_address")
  private String emailAddress;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "faculty_name")
  private String facultyName;

  @Column(name = "department_name")
  private String departmentName = "Exterior"; // Default value

  @ManyToMany(mappedBy = "profesors")
  private List<Exam> exams;
}
