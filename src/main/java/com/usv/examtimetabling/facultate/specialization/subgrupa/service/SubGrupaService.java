package com.usv.examtimetabling.facultate.specialization.subgrupa.service;

import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.dto.SubGrupaDto;
import java.util.List;

public interface SubGrupaService {
    SubGrupaDto createSubGrupa(SubGrupaDto subGrupaDto);

//    SubGrupaDto getSubGrupaByDegreeAndYear(String degreeName, Integer year);

    List<SubGrupa> getAllSubGrupas();
}
