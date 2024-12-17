package com.usv.examtimetabling.classroom.controller;

import com.usv.examtimetabling.classroom.model.Sala;
import com.usv.examtimetabling.classroom.service.ClassroomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
public class ClassroomController {

  private final ClassroomService classroomService;

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @GetMapping("/all")
  public List<Sala> getAllClassrooms() {
    return classroomService.getAllClassrooms();
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @PutMapping("/update")
  public Sala updateClassroom(@RequestBody Sala updateClassroomDto) {
    return classroomService.updateClassroom(updateClassroomDto);
  }

  @CrossOrigin(origins = "http://127.0.0.1:5173")
  @DeleteMapping("/delete")
  public void deleteClassroom(@RequestParam String name) {
    classroomService.deleteClassroom(name);
  }
}
