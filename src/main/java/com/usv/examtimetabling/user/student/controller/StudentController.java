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
public class StudentController {

  private final StudentService studentService;

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/all")
  public List<StudentDto> getAllStudents() {
    return studentService.getAllStudents();
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/faculty")
  public List<StudentDto> getStudentsByFaculty(@RequestParam String faculty) {
    return studentService.getStudentsByFacultate(faculty);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/degree")
  public List<StudentDto> getStudentsByDegree(@RequestParam String degree) {
    return studentService.getStudentsByDegree(degree);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/group")
  public List<StudentDto> getStudentsByGroup(@RequestParam String group) {
    return studentService.getStudentsBySubGrupa(group);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PutMapping("/update")
  public StudentDto updateStudent(@RequestParam String email, @RequestBody SetStudent setStudent) {
    return studentService.updateStudent(email, setStudent);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PutMapping("/groupLeader")
  public StudentDto setGroupLeader(@RequestParam String email) {
    return studentService.setSubGrupaLeader(email);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @DeleteMapping("/delete")
  public void deleteStudent(@RequestParam String email) {
    studentService.deleteStudent(email);
  }
}
