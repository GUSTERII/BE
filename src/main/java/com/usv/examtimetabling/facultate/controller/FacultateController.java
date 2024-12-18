package com.usv.examtimetabling.facultate.controller;


import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.model.dto.CreateFacultateDto;
import com.usv.examtimetabling.facultate.model.dto.FacultateDto;
import com.usv.examtimetabling.facultate.model.dto.UpdateFacultateDto;
import com.usv.examtimetabling.facultate.service.FacultateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facultate")
@RequiredArgsConstructor
public class FacultateController {

    private final FacultateService facultateService;

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/add")
    public Facultate addFacultate(@RequestBody CreateFacultateDto createFacultateDto) {
        return facultateService.addFacultate(createFacultateDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/all")
    public List<FacultateDto> getAllFaculties() {
        return facultateService.getAllFaculties();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PutMapping("/update")
    public FacultateDto updateFacultate(@RequestBody UpdateFacultateDto updateFacultateDto) {
        return facultateService.updateFacultate(updateFacultateDto);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @DeleteMapping("/delete")
    public void deleteFacultate(@RequestParam String name) {
        facultateService.deleteByName(name);
    }
}
