package com.usv.examtimetabling.faculty.degree.controller;

import com.usv.examtimetabling.faculty.degree.model.dto.CreateDegreeDto;
import com.usv.examtimetabling.faculty.degree.model.dto.DegreeDto;
import com.usv.examtimetabling.faculty.degree.model.dto.UpdateDegreeDto;
import com.usv.examtimetabling.faculty.degree.service.DegreeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/degree")
@RequiredArgsConstructor
public class DegreeController {

    private final DegreeService degreeService;

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/add")
    public DegreeDto addDegree(@RequestBody CreateDegreeDto createDegreeDto) {
        return degreeService.addDegree(createDegreeDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/all")
    public List<DegreeDto> getDegreeByFaculty(@RequestParam String facultyName) {
        return degreeService.getDegreeByFacultateName(facultyName);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/allDegrees")
    public List<DegreeDto> getAll() {
        return degreeService.getAll();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PutMapping("/update")
    public DegreeDto updateDegree(@RequestBody UpdateDegreeDto updateDegreeDto) {
        return degreeService.updateDegree(updateDegreeDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @DeleteMapping("/delete")
    public void deleteDegree(@RequestParam String name) {
        degreeService.deleteByDegreeName(name);
    }
}
