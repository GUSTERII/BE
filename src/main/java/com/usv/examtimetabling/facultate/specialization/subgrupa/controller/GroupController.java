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
public class GroupController {

    private final SubGrupaService subGrupaService;

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/all")
    public List<SubGrupa> getAllGroups() {
        return subGrupaService.getAllSubGrupas();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/create")
    public SubGrupaDto createGroup(@RequestBody SubGrupaDto subGrupaDto) {
        return subGrupaService.createSubGrupa(subGrupaDto);
    }

//    @CrossOrigin(origins = "http://127.0.0.1:5173")
//    @GetMapping("/getByDegreeAndYear")
//    public SubGrupaDto getGroupByDegreeAndYear(@RequestParam String degreeName, @RequestParam Integer year) {
//        return subGrupaService.getSubGrupaByDegreeAndYear(degreeName, year);
//    }
}
