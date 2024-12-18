package com.usv.examtimetabling.facultate.controller;


import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.model.dto.CreateFacultateDto;
import com.usv.examtimetabling.facultate.model.dto.FacultateDto;
import com.usv.examtimetabling.facultate.model.dto.UpdateFacultateDto;
import com.usv.examtimetabling.facultate.service.FacultateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facultate")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {
        "https://gusteriife-azgvfvgdg3fmfth7.canadacentral-01.azurewebsites.net",
        "http://localhost:3000"
    },
    allowCredentials = "true")
public class FacultateController {

    private final FacultateService facultateService;

    @PostMapping("/add")
    public Facultate addFacultate(@RequestBody CreateFacultateDto createFacultateDto) {
        return facultateService.addFacultate(createFacultateDto);
    }

    @GetMapping("/all")
    public List<FacultateDto> getAllFaculties() {
        return facultateService.getAllFaculties();
    }

    @PutMapping("/update")
    public FacultateDto updateFacultate(@RequestBody UpdateFacultateDto updateFacultateDto) {
        return facultateService.updateFacultate(updateFacultateDto);
    }

    @DeleteMapping("/delete")
    public void deleteFacultate(@RequestParam String name) {
        facultateService.deleteByName(name);
    }
}
