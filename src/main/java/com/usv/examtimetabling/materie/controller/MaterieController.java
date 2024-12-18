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
public class MaterieController {

  private static final Logger log = LoggerFactory.getLogger(MaterieController.class);
  private final MaterieService materieService;

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PostMapping
  public MaterieDto addMaterie(@RequestBody CreateMaterieDto createMaterieDto) {
    return materieService.addMaterie(createMaterieDto);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PutMapping("/update")
  public MaterieDto updateMaterie(@RequestBody UpdateMaterieDto updateMaterieDto) {
    return materieService.updateMaterie(updateMaterieDto);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PutMapping("/teacher")
  public MaterieDto addTeacher(@RequestParam String name, @RequestParam String teacher) {
    return materieService.addTeacher(name, teacher);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping
  public List<MaterieDto> getAll() {
    return materieService.getAll();
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/teacher")
  public List<MaterieDto> getMaterieByTeacher(@RequestParam String teacher) {
    return materieService.getMaterieByTeacher(teacher);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/name")
  public MaterieDto getMaterieByName(@RequestParam String name) {
    return materieService.getMaterieByName(name);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @DeleteMapping("/delete")
  public void deleteMaterie(@RequestParam String materie) {
    log.info("Deleting materie: " + materie);
    materieService.deleteMaterie(materie);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PostMapping("/ifMaterieThenExam")
  public void ifMaterieThenExam() {
    materieService.ifMaterieThenExam();
  }
}
