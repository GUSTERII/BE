package com.usv.examtimetabling.sali.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sala")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "short_name")
  private String shortName;

  @Column(name = "building_name")
  private String buildingName = "0";

  @Column(name = "capacity")
  private int capacity = 10;
}
