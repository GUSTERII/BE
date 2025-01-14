package com.usv.examtimetabling.seeding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usv.examtimetabling.config.DataConfiguration;
import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.repository.FacultateRepository;
import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.facultate.specialization.repository.SpecializationRepository;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.materie.model.Materie;
import com.usv.examtimetabling.materie.repository.MaterieRepository;
import com.usv.examtimetabling.orar.OrarEntry;
import com.usv.examtimetabling.orar.OrarEntryRepository;
import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.sali.repository.SaliRepository;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Seeder {

  private final OrarApiService apiService;
  private final OrarEntryRepository orarEntryRepository;
  private final FacultateRepository facultateRepository;
  private final SaliRepository salaRepository;
  private final SpecializationRepository specializationRepository;
  private final MaterieRepository materieRepository;
  private final UserRepository userRepository;
  private final SubGrupaRepository subGrupaRepository;
  private final DataConfiguration dataConfiguration;

  @PostConstruct
  public void seedDatabase() {
    if (dataConfiguration.getSeeding()) {
      //      populateFaculties();
      //      populateRooms();
      //      populateSpecializations();
      //      populateUsers();
//      populateMaterieEntries();
//      createSpecializationsBasedOnGroupsData();
      //      populateSubGroups();
    }
    // Add methods for other entities as needed
  }

  private void populateFaculties() {
    String url = "https://orar.usv.ro/orar/vizualizare/data/facultati.php?json";
    List<Facultate> faculties = apiService.fetchApiData(url, Facultate[].class);
    facultateRepository.saveAll(faculties);
  }

  private void populateRooms() {
    String url = "https://orar.usv.ro/orar/vizualizare/data/sali.php?json";
    List<Sala> rooms = apiService.fetchApiData(url, Sala[].class);
    salaRepository.saveAll(rooms);
  }

  private void populateMaterieEntries() {
    ObjectMapper objectMapper = new ObjectMapper();

    for (int id = 1; id <= 1000; id++) {
      String url = "https://orar.usv.ro/orar/vizualizare/data/orarSPG.php?ID=" + id + "&mod=prof&json";
      String jsonResponse = apiService.fetchRawApiResponse(url);

      try {
        List<Object> rawResponse = objectMapper.readValue(jsonResponse, new TypeReference<List<Object>>() {});

        // Deserialize the first element to a list of OrarEntry objects
        List<OrarEntry> orarEntries = objectMapper.convertValue(rawResponse.get(0), new TypeReference<List<OrarEntry>>() {});

        // Deserialize the second element to a map of subjects
        Map<String, List<String>> subjectMappings = objectMapper.convertValue(rawResponse.get(1), new TypeReference<Map<String, List<String>>>() {});

        for (OrarEntry entry : orarEntries) {
          if (entry.getTopicLongName() == null || entry.getTopicLongName().isEmpty()) {
            // Skip entries with no topic name
            continue;
          }
          List<String> subjectInfo = subjectMappings.getOrDefault(entry.getId(), List.of(""));
          Materie materie = Materie.builder()
              .name(entry.getTopicLongName())
              .year(parseYear(subjectInfo))
              .semester(parseSemester(subjectInfo))
              .specialization(findSpecialization(subjectInfo))
              .teacher(findOrCreateTeacher(entry.getTeacherFirstName(), entry.getTeacherLastName()))
              .facultate(findFacultate(entry.getRoomBuilding()))
              .examDuration(Integer.parseInt(entry.getDuration()))
              .build();

          materieRepository.save(materie);
        }
      } catch (JsonProcessingException e) {
        log.error("Failed to parse materie entries for ID {}: {}", id, e.getMessage());
      } catch (IndexOutOfBoundsException e) {
        log.error("Unexpected response format for ID {}: {}", id, e.getMessage());
      }
    }
  }



  private Specialization findSpecialization(List<String> courseInfo) {
    if (courseInfo != null && !courseInfo.isEmpty()) {
      String specializationName = courseInfo.get(0).split(",")[1].trim();
      return specializationRepository.findByName(specializationName).orElse(null);
    }
    return null;
  }

  private void createSpecializationsBasedOnGroupsData()
  {
    List<SubGrupa> subGrupas = subGrupaRepository.findAll();
    for (SubGrupa s:subGrupas)
    {
      if (specializationRepository.findByName(s.getSpecializationShortName()).isEmpty()) {
        Specialization specialization = new Specialization();
        specialization.setFacultate(
            facultateRepository.findById(Long.valueOf(s.getFacultyId())).orElse(null));
        specialization.setName(s.getSpecializationShortName());
        specializationRepository.save(specialization);
      }
    }
  }


  private Integer parseYear(List<String> courseInfo) {
    if (courseInfo != null && !courseInfo.isEmpty()) {
      String[] parts = courseInfo.get(0).split(",| ");
      return Arrays.stream(parts)
          .filter(part -> part.matches(".*\\d+.*"))
          .mapToInt(part -> Integer.parseInt(part.replaceAll("\\D", "")))
          .findFirst()
          .orElse(0);
    }
    return null;
  }

  private Integer parseSemester(List<String> courseInfo) {
    if (courseInfo != null && !courseInfo.isEmpty()) {
      String info = courseInfo.get(0).toLowerCase();
      if (info.contains("an 1")) {
        return 1;
      } else if (info.contains("an 2")) {
        return 2;
      } else if (info.contains("an 3")) {
        return 3;
      }
    }
    return null;
  }


  private User findOrCreateTeacher(String firstName, String lastName) {
    return userRepository
        .findByName(firstName + " " + lastName)
        .orElseGet(
            () -> {
              User newUser =
                  User.builder()
                      .name(firstName + " " + lastName)
                      .email(generateEmail(firstName, lastName))
                      .password("defaultPassword") // Replace with appropriate password generation
                      .role(Role.PROFESSOR)
                      .created_at(new Date())
                      .updated_at(new Date())
                      .build();
              return userRepository.save(newUser);
            });
  }

  private String generateEmail(String firstName, String lastName) {
    return (firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.com")
        .replaceAll(" ", "");
  }

  private Facultate findFacultate(String roomBuilding) {
    // Implement a lookup or mapping based on roomBuilding
    return null;
  }

  private void populateSpecializations() {
    String url = "https://orar.usv.ro/orar/vizualizare/data/subgrupe.php?json";
    String jsonResponse = apiService.fetchRawApiResponse(url);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // Parse the response into a list of raw data maps
      List<Map<String, String>> rawResponse =
          objectMapper.readValue(jsonResponse, new TypeReference<List<Map<String, String>>>() {});

      List<Specialization> specializations =
          rawResponse.stream()
              .map(
                  data -> {
                    Facultate facultate =
                        facultateRepository
                            .findById(Long.parseLong(data.get("facultyId")))
                            .orElse(null);
                    if (facultate == null) {
                      log.warn(
                          "Facultate with ID {} not found, skipping specialization.",
                          data.get("facultyId"));
                      return null;
                    }
                    return Specialization.builder()
                        .name(data.get("specializationShortName"))
                        .facultate(facultate)
                        .build();
                  })
              .filter(Objects::nonNull)
              .collect(Collectors.toList());

      specializationRepository.saveAll(specializations);
    } catch (JsonProcessingException e) {
      log.error("Failed to parse specializations: {}", e.getMessage());
    }
  }

  private void populateSubGroups() {
    String url = "https://orar.usv.ro/orar/vizualizare/data/subgrupe.php?json";
    List<SubGrupa> subGroups = apiService.fetchApiData(url, SubGrupa[].class);
    subGrupaRepository.saveAll(subGroups);
  }

  private void populateUsers() {
    // Example: Populate users from a custom source
    List<User> users =
        List.of(new User(null, "admin", "admin@usv.ro", "password", Role.ADMIN, null, null));

    userRepository.saveAll(users);
  }
}
