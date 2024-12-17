package com.usv.examtimetabling.exam.controller;

import com.usv.examtimetabling.exam.model.dto.ConfirmExamDto;
import com.usv.examtimetabling.exam.model.dto.CreateExamDto;
import com.usv.examtimetabling.exam.model.dto.CreateExamPeriodDto;
import com.usv.examtimetabling.exam.model.dto.ExamDto;
import com.usv.examtimetabling.exam.model.dto.ExamsPeriodDto;
import com.usv.examtimetabling.exam.model.dto.UpdateExamDto;
import com.usv.examtimetabling.exam.model.dto.UpdateExamsPeriodDto;
import com.usv.examtimetabling.exam.service.ExamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ExamController {

    private static final Logger log = LoggerFactory.getLogger(ExamController.class);
    private final ExamService examService;

//    @PutMapping("/random")
//    public void assignExamRandomGroup(@RequestBody ExamAdd examAdd) {
//        examService.assignExamRandomGroup(examAdd);
//    }
    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/create")
    public ExamDto createExam(@RequestBody CreateExamDto examDto) {
        return examService.createExam(examDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/get")
    public List<ExamDto> getExams() {
        return examService.getExams();
    }

//    @PutMapping("/set")
//    public void setExam(@RequestBody ExamDto examDto, @RequestParam String name, @RequestParam String group) {
//        examService.setExam(examDto,name,group);
//    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/confirm")
    public ExamDto confirmExam(@RequestBody ConfirmExamDto confirmExamDto) {
        return examService.confirmExam(confirmExamDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PutMapping("/update")
    public ExamDto updateExam(@RequestBody UpdateExamDto updateExamDto) {
        return examService.updateExam(updateExamDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/get/user")
    public List<ExamDto> getExamsByUser() {
        return examService.getExamsByUser();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/get/degree/{degreeName}")
    public List<ExamDto> getExamsByFacultyAndDegree(@PathVariable String degreeName) {
        return examService.getExamsByFacultyAndDegree(degreeName);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/create/period")
    public ExamsPeriodDto createExamsPeriod(@RequestBody CreateExamPeriodDto createExamPeriodDto) {
      log.info("createExamsPeriod {}", createExamPeriodDto);
        return examService.createExamsPeriod(createExamPeriodDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/get/period")
    public ExamsPeriodDto getExamsPeriod() {
        return examService.getExamsPeriod();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PutMapping("/update/period")
    public ExamsPeriodDto updateExamsPeriod(@RequestBody UpdateExamsPeriodDto updateExamsPeriodDto) {
        return examService.updateExamsPeriod(updateExamsPeriodDto);
    }

}
