package com.usv.examtimetabling.user.profesor;

import lombok.Data;

@Data
public class ProfesorDto {
  private String id;
  private String lastName;
  private String firstName;
  private String emailAddress;
  private String phoneNumber;
  private String facultyName;
  private String departmentName;
}
