package com.usv.examtimetabling.facultate.specialization.controller;

import com.usv.examtimetabling.facultate.specialization.model.dto.CreateSpecializationDto;
import com.usv.examtimetabling.facultate.specialization.model.dto.SpecializationDto;
import com.usv.examtimetabling.facultate.specialization.model.dto.UpdateSpecializationDto;
import com.usv.examtimetabling.facultate.specialization.service.SpecializationService;
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
@RequestMapping("/specialization")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {
      "https://gusteriife-azgvfvgdg3fmfth7.canadacentral-01.azurewebsites.net",
      "http://localhost:3000"
    },
    allowCredentials = "true")
public class SpecializationController {

  private final SpecializationService specializationService;

  @PostMapping("/add")
  public SpecializationDto addSpecialization(
      @RequestBody CreateSpecializationDto createSpecializationDto) {
    return specializationService.addSpecialization(createSpecializationDto);
  }

  @GetMapping("/all")
  public List<SpecializationDto> getSpecializationByFaculty(@RequestParam String facultyName) {
    return specializationService.getSpecializationByFacultateName(facultyName);
  }

  @GetMapping("/allSpecializations")
  public List<SpecializationDto> getAll() {
    return specializationService.getAll();
  }

  @PutMapping("/update")
  public SpecializationDto updateSpecialization(
      @RequestBody UpdateSpecializationDto updateSpecializationDto) {
    return specializationService.updateSpecialization(updateSpecializationDto);
  }

  @DeleteMapping("/delete")
  public void deleteSpecialization(@RequestParam String name) {
    specializationService.deleteBySpecializationName(name);
  }
}
