package com.usv.examtimetabling.materie.controller;

import com.usv.examtimetabling.materie.model.dto.CreateMaterieDto;
import com.usv.examtimetabling.materie.model.dto.MaterieDto;
import com.usv.examtimetabling.materie.model.dto.UpdateMaterieDto;
import com.usv.examtimetabling.materie.service.MaterieService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/materie")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {
      "https://gusteriife-azgvfvgdg3fmfth7.canadacentral-01.azurewebsites.net",
      "http://localhost:3000"
    },
    allowCredentials = "true")
public class MaterieController {

  private static final Logger log = LoggerFactory.getLogger(MaterieController.class);
  private final MaterieService materieService;

  @GetMapping
  public List<MaterieDto> getAll() {
    return materieService.getAll();
  }

  @GetMapping("/teacher")
  public List<MaterieDto> getMaterieByTeacher(@RequestParam String teacher) {
    return materieService.getMaterieByTeacher(teacher);
  }

  @GetMapping("/name")
  public MaterieDto getMaterieByName(@RequestParam String name) {
    return materieService.getMaterieByName(name);
  }

  @DeleteMapping("/delete")
  public void deleteMaterie(@RequestParam String materie) {
    log.info("Deleting materie: " + materie);
    materieService.deleteMaterie(materie);
  }

  @PostMapping("/ifMaterieThenExam")
  public void ifMaterieThenExam() {
    materieService.ifMaterieThenExam();
  }
}
