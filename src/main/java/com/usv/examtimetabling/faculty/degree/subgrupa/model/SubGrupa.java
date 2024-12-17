package com.usv.examtimetabling.faculty.degree.subgrupa.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subgrupa")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubGrupa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "faculty_id")
    private String facultyId;

    @Column(name = "specialization_short_name")
    private String specializationShortName;

    @Column(name = "study_year")
    private String studyYear;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "subgroup_index")
    private String subgroupIndex;

    @Column(name = "is_modular")
    private String isModular;

    @Column(name = "orar_id")
    private String orarId;
}

