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
import com.usv.examtimetabling.orar.OrarEntryRepository;
import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.sali.repository.SaliRepository;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.profesor.Profesor;
import com.usv.examtimetabling.user.profesor.ProfesorDto;
import com.usv.examtimetabling.user.profesor.ProfesorRepository;
import com.usv.examtimetabling.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class Seeder {

  private final OrarApiService apiService;
  private final FacultateRepository facultateRepository;
  private final SaliRepository salaRepository;
  private final SpecializationRepository specializationRepository;
  private final MaterieRepository materieRepository;
  private final UserRepository userRepository;
  private final SubGrupaRepository subGrupaRepository;
  private final DataConfiguration dataConfiguration;
  private final ProfesorRepository profesorRepository;

  @PostConstruct
  public void seedDatabase() {
    if (dataConfiguration.getSeeding()) {
      populateFaculties();
      populateRooms();
      populateSpecializations();
      createSpecializationsBasedOnGroupsData();
      populateSubGroups();
      fetchAndPopulateProfessors();
      fetchAndPopulateSubjects();
      populateUsers();
    }
  }

  private void populateFaculties() {
    String url = "https://orar.usv.ro/orar/vizualizare/data/facultati.php?json";
    List<Facultate> faculties = apiService.fetchApiData(url, Facultate[].class);
    facultateRepository.saveAll(faculties);
  }

  private void populateRooms() {
    String url = "https://orar.usv.ro/orar/vizualizare/data/sali.php?json";
    List<Sala> rooms = apiService.fetchApiData(url, Sala[].class);
    for (Sala room : rooms) {
      if (!salaRepository.existsByName(room.getName())) {
        salaRepository.save(room);
      }
    }
  }

  public void fetchAndPopulateProfessors() {
    String API_URL = "https://orar.usv.ro/orar/vizualizare/data/cadre.php?json";
    RestTemplate restTemplate = new RestTemplate();

    // Fetch data from API
    ProfesorDto[] profesorDtos = restTemplate.getForObject(API_URL, ProfesorDto[].class);
    if (profesorDtos == null) {
      throw new RuntimeException("No data returned from API");
    }

    // Map DTOs to Profesor entities
    List<Profesor> profesores =
        List.of(profesorDtos).stream()
            .map(
                dto -> {
                  Profesor profesor = new Profesor();
                  profesor.setId(dto.getId() != null ? Long.parseLong(dto.getId()) : null);
                  profesor.setLastName(dto.getLastName());
                  profesor.setFirstName(dto.getFirstName());
                  profesor.setEmailAddress(dto.getEmailAddress());
                  profesor.setPhoneNumber(dto.getPhoneNumber());
                  profesor.setFacultyName(dto.getFacultyName());
                  profesor.setDepartmentName(
                      dto.getDepartmentName() != null ? dto.getDepartmentName() : "Exterior");
                  return profesor;
                })
            .collect(Collectors.toList());

    // Save to database
    profesorRepository.saveAll(profesores);
  }

  public void fetchAndPopulateSubjects() {
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    for (int teacherId = 1; teacherId <= 5000; teacherId++) {
      String url =
          "https://orar.usv.ro/orar/vizualizare/data/orarSPG.php?ID="
              + teacherId
              + "&mod=prof&json";
      try {
        String response = restTemplate.getForObject(url, String.class);

        // Parse the JSON response
        List<Object> parsedResponse = objectMapper.readValue(response, new TypeReference<>() {});

        if (parsedResponse.isEmpty()) continue;

        // First part: Subjects
        List<Map<String, Object>> subjects = (List<Map<String, Object>>) parsedResponse.get(0);

        // Second part: Course Info
        Map<String, List<String>> courseInfoMap = null;
        if (parsedResponse.size() > 1 && parsedResponse.get(1) instanceof Map) {
          courseInfoMap = (Map<String, List<String>>) parsedResponse.get(1);
        }

        for (Map<String, Object> subjectData : subjects) {
          String subjectName = (String) subjectData.get("topicLongName");

          // Avoid duplicates: Check if Materie with the same name already exists
          if (materieRepository.existsByName(subjectName)) {
            log.info("Materie with name '{}' already exists, skipping.", subjectName);
            continue;
          }

          Materie materie = new Materie();
          materie.setName(subjectName);
          materie.setShortName((String) subjectData.get("topicShortName"));

          // Get course info based on subject ID
          List<String> courseInfo =
              courseInfoMap != null ? courseInfoMap.get(subjectData.get("id")) : null;

          // Validate course info before using
          if (courseInfo == null || courseInfo.stream().allMatch(String::isEmpty)) {
            log.warn("Course info for subject '{}' is null or empty, skipping.", subjectName);
            continue;
          }

          // Parse semester and year
          materie.setSemester(parseSemester());
          materie.setYear(parseYear(courseInfo));

          // Set teacher details
          materie.setTeacherPosition((String) subjectData.get("positionShortName"));
          materie.setTeacherTitle((String) subjectData.get("phdShortName"));

          // Find associated Profesor
          String firstName = (String) subjectData.get("teacherFirstName");
          String lastName = (String) subjectData.get("teacherLastName");
          Profesor profesor =
              profesorRepository.findByFirstNameAndLastName(firstName, lastName).orElse(null);
          materie.setProfesor(profesor);

          // Find associated Specialization
          Specialization specialization = findSpecialization(courseInfo);
          if (specialization == null) {
            log.warn("No specialization found for course info '{}', skipping.", courseInfo);
            continue;
          }
          materie.setSpecialization(specialization);

          // Find associated Facultate
          Facultate facultate = specialization.getFacultate();
          materie.setFacultate(facultate);

          // Save Materie
          materieRepository.save(materie);
        }
      } catch (JsonProcessingException e) {
        log.error("Failed to parse JSON for teacher ID {}: {}", teacherId, e.getMessage());
      } catch (Exception e) {
        log.warn("Failed to process teacher ID {}: {}", teacherId, e.getMessage());
      }
    }
  }

  private void createSpecializationsBasedOnGroupsData() {
    List<SubGrupa> subGrupas = subGrupaRepository.findAll();
    for (SubGrupa s : subGrupas) {
      if (specializationRepository.findByName(s.getSpecializationShortName()).isEmpty()) {
        Specialization specialization = new Specialization();
        specialization.setFacultate(
            facultateRepository.findById(Long.valueOf(s.getFacultyId())).orElse(null));
        specialization.setName(s.getSpecializationShortName());
        specializationRepository.save(specialization);
      }
    }
  }

  private Specialization findSpecialization(List<String> courseInfo) {
    if (courseInfo == null || courseInfo.isEmpty() || courseInfo.size() < 2) {
      log.warn("Invalid course info '{}', skipping.", courseInfo);
      return null;
    }

    for (String part : courseInfo) {
      if (part.contains("an")) {
        int index = part.lastIndexOf("an");
        if (index != -1) {
          StringBuilder specializationName = new StringBuilder();

          // Extract characters right to left from the word "an"
          for (int i = index - 1; i >= 0; i--) {
            char currentChar = part.charAt(i);
            if (currentChar == ',') {
              break; // Stop at the first comma
            }
            specializationName.insert(0, currentChar); // Build the string in reverse
          }

          String specialization = specializationName.toString().trim();
          if (!specialization.isEmpty()) {
            return specializationRepository.findByName(specialization).orElse(null);
          }
        }
      }
    }

    log.warn("No specialization found for course info '{}', skipping.", courseInfo);
    return null;
  }

  private Integer parseYear(List<String> courseInfo) {
    if (courseInfo != null && !courseInfo.isEmpty()) {
      for (String info : courseInfo) {
        if (info.contains("an")) {
          String[] parts = info.split(" ");
          for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals("an")) {
              try {
                return Integer.parseInt(parts[i + 1]);
              } catch (NumberFormatException e) {
                // Ignore and continue
              }
            }
          }
        }
      }
    }
    return null;
  }

  private Integer parseSemester() {
    String url = "https://orar.usv.ro/orar/vizualizare/data/anSemSapt.php?json";
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      String response = restTemplate.getForObject(url, String.class);
      List<Map<String, String>> data =
          objectMapper.readValue(response, new TypeReference<List<Map<String, String>>>() {});

      if (!data.isEmpty()) {
        String semValue = data.get(0).get("sem");
        return Integer.parseInt(semValue);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

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

                    String specializationName = data.get("specializationShortName");

                    // Check if the specialization already exists in the database
                    if (specializationRepository.existsByName(specializationName)) {
                      log.info(
                          "Specialization with name '{}' already exists in the database, skipping.",
                          specializationName);
                      return null;
                    }
                    try {
                      Specialization specialization = new Specialization();
                      specialization.setName(specializationName);
                      specialization.setFacultate(facultate);
                      specializationRepository.save(specialization);
                    } catch (Exception e) {
                      log.error("Failed to save specialization: {}", e.getMessage());
                      return null;
                    }

                    return Specialization.builder()
                        .name(specializationName)
                        .facultate(facultate)
                        .build();
                  })
              .filter(Objects::nonNull)
              .collect(Collectors.toList());
      specializations.forEach(
          specialization -> {
            if (!specializationRepository.existsByName(specialization.getName())) {
              specializationRepository.save(specialization);
            }
          });

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
    List<Profesor> professors = profesorRepository.findAll();
    List<User> users =
        professors.stream()
            .map(
                profesor -> {
                  User user = new User();
                  user.setName(profesor.getFirstName() + " " + profesor.getLastName());
                  user.setEmail(profesor.getEmailAddress());
                  user.setPassword("$2a$10$oMnnDxXJpv83aZA5vF4ZfOaa75ENafiujgiE9.EpgVFqNUhQhUCbe");
                  user.setRole(Role.PROFESSOR);
                  return user;
                })
            .collect(Collectors.toList());

    users.forEach(
        user -> {
          if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
          }
        });
  }
}
