package com.usv.examtimetabling.subject.service;

import com.usv.examtimetabling.subject.model.dto.CreateSubjectDto;
import com.usv.examtimetabling.subject.model.dto.SubjectDto;

import com.usv.examtimetabling.subject.model.dto.UpdateSubjectDto;
import java.util.List;

public interface SubjectService {
    SubjectDto addTeacher(String name,String teacher);
    SubjectDto addSubject(CreateSubjectDto createSubjectDto);
    List<SubjectDto> getAll();
    List<SubjectDto> getSubjectByTeacher(String teacher);
    SubjectDto updateSubject(UpdateSubjectDto updateSubjectDto);
    void deleteSubject(String subject);

    void ifSubjectThenExam();

  SubjectDto getSubjectByName(String name);
}
