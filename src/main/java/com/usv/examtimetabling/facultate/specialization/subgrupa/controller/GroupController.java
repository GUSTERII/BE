package com.usv.examtimetabling.facultate.specialization.subgrupa.controller;

import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.dto.SubGrupaDto;
import com.usv.examtimetabling.facultate.specialization.subgrupa.service.SubGrupaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grupa")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {
      "https://gusteriife-azgvfvgdg3fmfth7.canadacentral-01.azurewebsites.net",
      "http://localhost:3000"
    },
    allowCredentials = "true")
public class GroupController {

  private final SubGrupaService subGrupaService;

  @GetMapping("/all")
  public List<SubGrupa> getAllGroups() {
    return subGrupaService.getAllSubGrupas();
  }

  @PostMapping("/create")
  public SubGrupaDto createGroup(@RequestBody SubGrupaDto subGrupaDto) {
    return subGrupaService.createSubGrupa(subGrupaDto);
  }

  //
  //    @GetMapping("/getByDegreeAndYear")
  //    public SubGrupaDto getGroupByDegreeAndYear(@RequestParam String degreeName, @RequestParam
  // Integer year) {
  //        return subGrupaService.getSubGrupaByDegreeAndYear(degreeName, year);
  //    }
}
