package com.usv.examtimetabling.exam.service;

import com.usv.examtimetabling.exam.model.dto.ConfirmExamDto;
import com.usv.examtimetabling.exam.model.dto.CreateExamDto;
import com.usv.examtimetabling.exam.model.dto.CreateExamPeriodDto;
import com.usv.examtimetabling.exam.model.dto.ExamDto;
import com.usv.examtimetabling.exam.model.dto.ExamsPeriodDto;
import com.usv.examtimetabling.exam.model.dto.UpdateExamDto;
import com.usv.examtimetabling.exam.model.dto.UpdateExamsPeriodDto;
import com.usv.examtimetabling.exam.util.TeacherSchedule;
import java.time.LocalDate;
import java.util.List;

public interface ExamService {
//    void assignExamRandomGroup(ExamAdd examAdd);
//    List<ClassroomSchedule> getAvailableHoursAndClassrooms(ExamsPeriod examsPeriod);
//    ExamDto setExam(ExamDto examDto, String name, String group);

    ExamDto createExam(CreateExamDto examDto);

    List<ExamDto> getExams();

    TeacherSchedule getTeacherSchedule(LocalDate date, Integer teacherId);

    ExamDto confirmExam(ConfirmExamDto confirmExamDto);

    ExamDto updateExam(UpdateExamDto updateExamDto);

    List<ExamDto> getExamsByUser();

  List<ExamDto> getExamsByFacultyAndSpecialization(String specializationName);

    ExamsPeriodDto createExamsPeriod(CreateExamPeriodDto createExamPeriodDto);

  ExamsPeriodDto getExamsPeriod();

  ExamsPeriodDto updateExamsPeriod(UpdateExamsPeriodDto updateExamsPeriodDto);
}
