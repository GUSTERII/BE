package com.usv.examtimetabling.user.student.service;

import com.usv.examtimetabling.user.student.model.dto.SetStudent;
import com.usv.examtimetabling.user.student.model.dto.StudentDto;
import java.util.List;

public interface StudentService {

    StudentDto updateStudent(String email, SetStudent setStudent);

    StudentDto setSubGrupaLeader(String email);

    List<StudentDto> getAllStudents();

    List<StudentDto> getStudentsByFacultate(String faculty);

    List<StudentDto> getStudentsBySpecialization(String specialization);

    List<StudentDto> getStudentsBySubGrupa(String group);

    void deleteStudent(String email);
}
