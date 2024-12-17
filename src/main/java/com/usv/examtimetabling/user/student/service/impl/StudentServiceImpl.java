package com.usv.examtimetabling.user.student.service.impl;

import com.usv.examtimetabling.faculty.degree.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.faculty.degree.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.faculty.degree.repository.DegreeRepository;
import com.usv.examtimetabling.faculty.repository.FacultateRepository;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.repository.UserRepository;
import com.usv.examtimetabling.user.student.model.Student;
import com.usv.examtimetabling.user.student.model.dto.SetStudent;
import com.usv.examtimetabling.user.student.model.dto.StudentDto;
import com.usv.examtimetabling.user.student.repository.StudentRepository;
import com.usv.examtimetabling.user.student.service.StudentService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;

  private final UserRepository userRepository;

  private final DegreeRepository degreeRepository;

  private final SubGrupaRepository groupRepository;

  private final FacultateRepository facultateRepository;

  private final PasswordEncoder passwordEncoder;

  public StudentDto updateStudent(String email, SetStudent setStudent) {
    return studentRepository
        .findByEmail(email)
        .map(
            student -> {
              student.setEmail(setStudent.getEmail());
              student.setName(setStudent.getName());
              student.setFacultate(
                  facultateRepository.findByLongName(setStudent.getFacultate()).orElseThrow());
              student.setDegree(degreeRepository.findByName(setStudent.getDegree()).orElseThrow());
              student.setSubGrupa(
                  groupRepository.findByGroupName(setStudent.getSubGrupa()).orElse(null));
              student.setYear(setStudent.getYear());
              Student updatedStudent = studentRepository.save(student);

              userRepository
                  .findByEmail(email)
                  .ifPresent(
                      user -> {
                        user.setEmail(setStudent.getEmail());
                        user.setName(setStudent.getName());
                        user.setPassword(passwordEncoder.encode(setStudent.getEmail()));
                        userRepository.save(user);
                      });

              return StudentDto.builder()
                  .name(updatedStudent.getName())
                  .email(updatedStudent.getEmail())
                  .role(updatedStudent.getRole())
                  .faculty(updatedStudent.getFacultate().getLongName())
                  .degree(updatedStudent.getDegree().getName())
                  .group(updatedStudent.getSubGrupa().getGroupName())
                  .year(updatedStudent.getYear())
                  .build();
            })
        .orElseThrow(() -> new RuntimeException("Student not found"));
  }

  public StudentDto setSubGrupaLeader(String email) {

    return studentRepository
        .findByEmail(email)
        .map(
            student -> {
              if (student.getRole() == Role.GROUP_LEADER) {
                throw new RuntimeException("The student is already a group leader");
              }

              SubGrupa group = student.getSubGrupa();
              Student currentSubGrupaLeader =
                  studentRepository.findBySubGrupaAndRole(group, Role.GROUP_LEADER);

              if (currentSubGrupaLeader != null) {
                currentSubGrupaLeader.setRole(Role.STUDENT);
                studentRepository.save(currentSubGrupaLeader);
              }

              student.setRole(Role.GROUP_LEADER);
              Student savedStudent = studentRepository.save(student);

              return StudentDto.builder()
                  .group(
                      savedStudent.getSubGrupa() != null
                          ? savedStudent.getSubGrupa().getGroupName()
                          : null)
                  .build();
            })
        .orElseThrow(() -> new RuntimeException("Student not found"));
  }

  public List<StudentDto> getAllStudents() {
    return studentRepository.findAll().stream()
        .map(
            student ->
                StudentDto.builder()
                    .name(student.getName())
                    .email(student.getEmail())
                    .role(student.getRole())
                    .faculty(
                        student.getFacultate() != null
                            ? student.getFacultate().getLongName()
                            : null)
                    .degree(student.getDegree() != null ? student.getDegree().getName() : null)
                    .group(
                        student.getSubGrupa() != null ? student.getSubGrupa().getGroupName() : null)
                    .year(student.getYear())
                    .build())
        .collect(Collectors.toList());
  }

  public List<StudentDto> getStudentsByFacultate(String faculty) {
    return studentRepository.findAllByFacultateLongName(faculty).stream()
        .map(
            student ->
                StudentDto.builder()
                    .name(student.getName())
                    .email(student.getEmail())
                    .role(student.getRole())
                    .faculty(student.getFacultate().getLongName())
                    .degree(student.getDegree().getName())
                    .group(student.getSubGrupa().getGroupName())
                    .build())
        .collect(Collectors.toList());
  }

  public List<StudentDto> getStudentsByDegree(String degree) {
    return studentRepository.findAllByDegreeName(degree).stream()
        .map(
            student ->
                StudentDto.builder()
                    .name(student.getName())
                    .email(student.getEmail())
                    .role(student.getRole())
                    .faculty(student.getFacultate().getLongName())
                    .degree(student.getDegree().getName())
                    .group(student.getSubGrupa().getGroupName())
                    .build())
        .collect(Collectors.toList());
  }

  public List<StudentDto> getStudentsBySubGrupa(String group) {
    return studentRepository.findAllBySubGrupaGroupName(group).stream()
        .map(
            student ->
                StudentDto.builder()
                    .name(student.getName())
                    .email(student.getEmail())
                    .role(student.getRole())
                    .faculty(student.getFacultate().getLongName())
                    .degree(student.getDegree().getName())
                    .group(student.getSubGrupa().getGroupName())
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void deleteStudent(String email) {
    studentRepository.deleteByEmail(email);
  }
}
