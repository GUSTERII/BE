package com.usv.examtimetabling.sali.controller;

import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.sali.service.SaliService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sali")
@RequiredArgsConstructor
public class SaliController {

  private final SaliService saliService;

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/all")
  public List<Sala> getAllSali() {
    return saliService.getAllClassrooms();
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PutMapping("/update")
  public Sala updateSala(@RequestBody Sala updateClassroomDto) {
    return saliService.updateClassroom(updateClassroomDto);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @DeleteMapping("/delete")
  public void deleteSala(@RequestParam String name) {
    saliService.deleteClassroom(name);
  }
}
