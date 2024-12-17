package com.usv.examtimetabling.subject.controller;

import com.usv.examtimetabling.subject.model.dto.CreateSubjectDto;
import com.usv.examtimetabling.subject.model.dto.SubjectDto;
import com.usv.examtimetabling.subject.model.dto.UpdateSubjectDto;
import com.usv.examtimetabling.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private static final Logger log = LoggerFactory.getLogger(SubjectController.class);
    private final SubjectService subjectService;

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping
    public SubjectDto addSubject(@RequestBody CreateSubjectDto createSubjectDto) {
        return subjectService.addSubject(createSubjectDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PutMapping("/update")
    public SubjectDto updateSubject(@RequestBody UpdateSubjectDto updateSubjectDto) {
        return subjectService.updateSubject(updateSubjectDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PutMapping("/teacher")
    public SubjectDto addTeacher(@RequestParam String name,@RequestParam String teacher) {
        return subjectService.addTeacher(name,teacher);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping
    public List<SubjectDto> getAll() {
        return subjectService.getAll();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/teacher")
    public List<SubjectDto> getSubjectByTeacher(@RequestParam String teacher) {
        return subjectService.getSubjectByTeacher(teacher);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/name")
    public SubjectDto getSubjectByName(@RequestParam String name) {
        return subjectService.getSubjectByName(name);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @DeleteMapping("/delete")
    public void deleteSubject(@RequestParam String subject) {
        log.info("Deleting subject: " + subject);
        subjectService.deleteSubject(subject);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/ifSubjectThenExam")
    public void ifSubjectThenExam() {
        subjectService.ifSubjectThenExam();
    }
}
