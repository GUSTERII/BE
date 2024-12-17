package com.usv.examtimetabling.faculty.controller;


import com.usv.examtimetabling.faculty.model.Facultate;
import com.usv.examtimetabling.faculty.model.dto.CreateFacultateDto;
import com.usv.examtimetabling.faculty.model.dto.FacultateDto;
import com.usv.examtimetabling.faculty.model.dto.UpdateFacultateDto;
import com.usv.examtimetabling.faculty.service.FacultateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultateController {

    private final FacultateService facultyService;

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/add")
    public Facultate addFacultate(@RequestBody CreateFacultateDto createFacultateDto) {
        return facultyService.addFacultate(createFacultateDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/all")
    public List<FacultateDto> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PutMapping("/update")
    public FacultateDto updateFacultate(@RequestBody UpdateFacultateDto updateFacultateDto) {
        return facultyService.updateFacultate(updateFacultateDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @DeleteMapping("/delete")
    public void deleteFacultate(@RequestParam String name) {
        facultyService.deleteByName(name);
    }
}
