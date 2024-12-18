package com.usv.examtimetabling.orar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orar_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrarEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "type_short_name")
  private String typeShortName;

  @Column(name = "teacher_id")
  private String teacherID;

  @Column(name = "teacher_last_name")
  private String teacherLastName;

  @Column(name = "teacher_first_name")
  private String teacherFirstName;

  @Column(name = "position_short_name")
  private String positionShortName;

  @Column(name = "phd_short_name")
  private String phdShortName;

  @Column(name = "other_title")
  private String otherTitle;

  @Column(name = "room_id")
  private String roomId;

  @Column(name = "room_building")
  private String roomBuilding;

  @Column(name = "room_long_name")
  private String roomLongName;

  @Column(name = "room_short_name")
  private String roomShortName;

  @Column(name = "topic_long_name")
  private String topicLongName;

  @Column(name = "topic_short_name")
  private String topicShortName;

  @Column(name = "week_day")
  private String weekDay;

  @Column(name = "start_hour")
  private String startHour;

  @Column(name = "duration")
  private String duration;

  @Column(name = "parity")
  private String parity;

  @Column(name = "other_info")
  private String otherInfo;

  @Column(name = "display_mode")
  private String displayMode;

  @Column(name = "type_long_name")
  private String typeLongName;

  @Column(name = "is_didactic")
  private String isDidactic;

  @Column(name = "document_id")
  private String documentID;

  @Column(name = "position_in_document")
  private String positionInDocument;

  @Column(name = "activity_document_type")
  private String activityDocumentType;
}
