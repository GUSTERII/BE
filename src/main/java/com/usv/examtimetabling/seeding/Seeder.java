package com.usv.examtimetabling.seeding;

import com.usv.examtimetabling.config.DataConfiguration;
import com.usv.examtimetabling.facultate.model.Facultate;
import com.usv.examtimetabling.facultate.repository.FacultateRepository;
import com.usv.examtimetabling.facultate.specialization.model.Specialization;
import com.usv.examtimetabling.facultate.specialization.repository.SpecializationRepository;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.materie.repository.MaterieRepository;
import com.usv.examtimetabling.orar.OrarEntryRepository;
import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.sali.repository.SaliRepository;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
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
      populateFaculties();
      populateRooms();
      populateSpecializations();
      populateUsers();
      populateSubGroups();
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

  //  private void populateOrarEntries() {
  //    String url = "https://orar.usv.ro/orar/vizualizare/data/orarSPG.php?ID=723&mod=prof&json";
  //    String jsonResponse = apiService.fetchRawApiResponse(url);
  //
  //    ObjectMapper objectMapper = new ObjectMapper();
  //    try {
  //      // Parse the response as a generic list
  //      List<Object> rawResponse = objectMapper.readValue(jsonResponse, new
  // TypeReference<List<Object>>() {});
  //
  //      // Extract the first part of the response (the nested lists of OrarEntry)
  //      List<List<OrarEntry>> entries = objectMapper.convertValue(rawResponse.get(0), new
  // TypeReference<List<List<OrarEntry>>>() {});
  //
  //      // Flatten the nested lists
  //      List<OrarEntry> flatEntries = entries.stream()
  //          .flatMap(List::stream)
  //          .collect(Collectors.toList());
  //
  //      // Save the flattened entries to the repository
  //      orarEntryRepository.saveAll(flatEntries);
  //
  //    } catch (JsonProcessingException e) {
  //      log.error(e.getMessage());
  //    } catch (IndexOutOfBoundsException e) {
  //      log.error(e.getMessage());
  //    }
  //  }

  private void populateSpecializations() {
    String url = "https://orar.usv.ro/orar/vizualizare/data/subgrupe.php?json";
    List<Specialization> specializations = apiService.fetchApiData(url, Specialization[].class);
    specializationRepository.saveAll(specializations);
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
