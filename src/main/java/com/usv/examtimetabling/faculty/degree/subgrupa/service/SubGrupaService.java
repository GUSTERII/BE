package com.usv.examtimetabling.faculty.degree.subgrupa.service;

import com.usv.examtimetabling.faculty.degree.subgrupa.model.dto.SubGrupaDto;
import java.util.List;

public interface SubGrupaService {
    SubGrupaDto createSubGrupa(SubGrupaDto subGrupaDto);

//    SubGrupaDto getSubGrupaByDegreeAndYear(String degreeName, Integer year);

    List<SubGrupaDto> getAllSubGrupas();
}
