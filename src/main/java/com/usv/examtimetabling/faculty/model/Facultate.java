package com.usv.examtimetabling.faculty.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "facultate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facultate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "long_name")
    private String longName;
}

