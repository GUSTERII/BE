package com.usv.examtimetabling.user;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
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
}

