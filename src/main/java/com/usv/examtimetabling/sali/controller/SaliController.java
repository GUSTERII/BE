package com.usv.examtimetabling.sali.controller;

import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.sali.service.SaliService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sali")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {
      "https://gusteriife-azgvfvgdg3fmfth7.canadacentral-01.azurewebsites.net",
      "http://localhost:3000"
    },
    allowCredentials = "true")
public class SaliController {

  private final SaliService saliService;

  @GetMapping("/all")
  public List<Sala> getAllSali() {
    return saliService.getAllClassrooms();
  }

  @PutMapping("/update")
  public Sala updateSala(@RequestBody Sala updateClassroomDto) {
    return saliService.updateClassroom(updateClassroomDto);
  }

  @DeleteMapping("/delete")
  public void deleteSala(@RequestParam String name) {
    saliService.deleteClassroom(name);
  }
}
