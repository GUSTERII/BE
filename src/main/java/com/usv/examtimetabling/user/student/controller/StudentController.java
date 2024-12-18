package com.usv.examtimetabling.user.student.controller;

import com.usv.examtimetabling.user.student.model.dto.SetStudent;
import com.usv.examtimetabling.user.student.model.dto.StudentDto;
import com.usv.examtimetabling.user.student.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {
      "https://gusteriife-azgvfvgdg3fmfth7.canadacentral-01.azurewebsites.net",
      "http://localhost:3000"
    },
    allowCredentials = "true")
public class StudentController {

  private final StudentService studentService;

  @GetMapping("/all")
  public List<StudentDto> getAllStudents() {
    return studentService.getAllStudents();
  }

  @GetMapping("/faculty")
  public List<StudentDto> getStudentsByFaculty(@RequestParam String faculty) {
    return studentService.getStudentsByFacultate(faculty);
  }

  @GetMapping("/specialization")
  public List<StudentDto> getStudentsBySpecialization(@RequestParam String specialization) {
    return studentService.getStudentsBySpecialization(specialization);
  }

  @GetMapping("/group")
  public List<StudentDto> getStudentsByGroup(@RequestParam String group) {
    return studentService.getStudentsBySubGrupa(group);
  }

  @PutMapping("/update")
  public StudentDto updateStudent(@RequestParam String email, @RequestBody SetStudent setStudent) {
    return studentService.updateStudent(email, setStudent);
  }

  @PutMapping("/groupLeader")
  public StudentDto setGroupLeader(@RequestParam String email) {
    return studentService.setSubGrupaLeader(email);
  }

  @DeleteMapping("/delete")
  public void deleteStudent(@RequestParam String email) {
    studentService.deleteStudent(email);
  }
}
