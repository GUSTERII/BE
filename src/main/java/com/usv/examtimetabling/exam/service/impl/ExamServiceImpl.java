package com.usv.examtimetabling.exam.service.impl;

import com.usv.examtimetabling.exam.model.Exam;
import com.usv.examtimetabling.exam.model.ExamsPeriod;
import com.usv.examtimetabling.exam.model.dto.ConfirmExamDto;
import com.usv.examtimetabling.exam.model.dto.CreateExamDto;
import com.usv.examtimetabling.exam.model.dto.CreateExamPeriodDto;
import com.usv.examtimetabling.exam.model.dto.ExamDto;
import com.usv.examtimetabling.exam.model.dto.ExamsPeriodDto;
import com.usv.examtimetabling.exam.model.dto.UpdateExamDto;
import com.usv.examtimetabling.exam.model.dto.UpdateExamsPeriodDto;
import com.usv.examtimetabling.exam.repository.ExamRepository;
import com.usv.examtimetabling.exam.repository.ExamsPeriodRepository;
import com.usv.examtimetabling.exam.service.ExamService;
import com.usv.examtimetabling.exam.util.DateUtils;
import com.usv.examtimetabling.exam.util.ExamStatus;
import com.usv.examtimetabling.exam.util.TeacherSchedule;
import com.usv.examtimetabling.facultate.specialization.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.facultate.specialization.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.sali.model.Sala;
import com.usv.examtimetabling.sali.repository.SaliRepository;
import com.usv.examtimetabling.materie.model.Materie;
import com.usv.examtimetabling.materie.repository.MaterieRepository;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.service.UserService;
import com.usv.examtimetabling.user.student.model.Student;
import com.usv.examtimetabling.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

  private static final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);
  private final ExamRepository examRepository;

  private final SaliRepository saliRepository;

  private final SubGrupaRepository groupRepository;

  private final MaterieRepository materieRepository;

  private final StudentRepository studentRepository;

  private final UserService userService;

  private final ExamsPeriodRepository examsPeriodRepository;

  // New method to create an exam
  @Transactional
  public ExamDto createExam(CreateExamDto createExamDto) {
    log.info("createExam {}", createExamDto);
    ExamsPeriod examsPeriod = examsPeriodRepository.findNextPeriod(LocalDate.now());
    if (examsPeriod == null) {
      throw new RuntimeException("Exams period not yet defined, please contact the administrator");
    } else if (createExamDto.getDate().before(DateUtils.convertToDate(examsPeriod.getStartDate()))
        || createExamDto.getDate().after(DateUtils.convertToDate(examsPeriod.getEndDate()))) {
      throw new RuntimeException("Exam is outside the  next exams period");
    }

    List<Sala> classroom = saliRepository.findAllByName(createExamDto.getSala());
    if (classroom == null) {
      throw new RuntimeException("Sala not found");
    }

    List<SubGrupa> group =
        groupRepository
            .findByGroupName(createExamDto.getSubGrupa())
            .orElseThrow(() -> new RuntimeException("SubGrupa not found"));

    List<Materie> materie =
        materieRepository
            .findAllByName(createExamDto.getMaterie());

    Optional<Exam> existingExam = examRepository.findBySubGrupaAndMaterie(group.get(0), materie.get(0));
    if (existingExam.isPresent()) {
      throw new RuntimeException("The group already has an exam scheduled for this materie");
    }

    LocalDate examDate =
        createExamDto.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalTime startTime = createExamDto.getStartTime();
    Integer studentsInGroup = 0;
    for (SubGrupa subGrupa : group) {
      studentsInGroup += studentRepository.countBySubGrupa(subGrupa);
    }
    if (!isSalaAvailable(classroom.get(0), startTime, createExamDto.getDuration(), null)
        || studentsInGroup > classroom.get(0).getCapacity()) {
      throw new RuntimeException(
          "Sala is not available at the specified time or does not have enough capacity");
    }

    LocalTime startHour = LocalTime.of(8, 0);
    LocalTime endHour = LocalTime.of(20, 0);
    if (startTime.isBefore(startHour) || startTime.isAfter(endHour)) {
      throw new RuntimeException("Exams cannot be scheduled after 8 PM until 8 AM");
    }

    TeacherSchedule teacherSchedule = getTeacherSchedule(examDate, materie.get(0).getTeacher().getId());
    if (teacherSchedule.getTotalHours() + createExamDto.getDuration() > 8) {
      throw new RuntimeException("The teacher's total work hours would exceed 8 hours");
    }

    // Check if the new exam's time interval would collide with any of the existing intervals
    String newInterval =
        String.format("%s - %s", startTime, startTime.plusMinutes(createExamDto.getDuration()));
    if (teacherSchedule.getIntervals().stream()
        .anyMatch(interval -> intervalsOverlap(interval, newInterval))) {
      throw new RuntimeException(
          "The new exam's time interval would collide with the teacher's existing schedule");
    }

    User currentUser = userService.getCurrentUser();
    ExamStatus status =
        (currentUser.getRole().name().equals("PROFESSOR")
                || currentUser.getRole().name().equals("ADMIN"))
            ? ExamStatus.CONFIRMED
            : ExamStatus.PENDING_CONFIRMATION;

    // Create the new exam
    for (SubGrupa subGrupa : group) {
      Exam newExam =
          Exam.builder()
              .name(createExamDto.getTitle())
              .description(createExamDto.getDescription())
              .date(examDate)
              .start(startTime)
              .duration(createExamDto.getDuration())
              .sala(classroom.get(0))
              .subGrupa(subGrupa)
              .materie(materie.get(0))
              .status(status)
              .build();

      // Save the new exam
      examRepository.save(newExam);
    }

    // Map the new exam to ExamDto and return it
    return null;
  }

  @Override
  public List<ExamDto> getExams() {
    List<Exam> exams = examRepository.findAll();
    return exams.stream().map(this::mapToExamDto).toList();
  }

  @Transactional
  public ExamDto confirmExam(ConfirmExamDto confirmExamDto) {
    // Get the current user
    User currentUser = userService.getCurrentUser();

    // Check if the current user is a professor or admin
    if (!(currentUser.getRole().name().equals("PROFESSOR")
        || currentUser.getRole().name().equals("ADMIN"))) {
      throw new RuntimeException("Only professors and admins can confirm exams");
    }

    // Find the exam by name and group
    Exam exam = findExamByNameAndSubGrupa(confirmExamDto.getName(), confirmExamDto.getSubGrupa());
    if (exam == null) {
      throw new RuntimeException("Exam not found");
    }

    // Check if the exam is assigned to the current professor
    if (!currentUser.getRole().name().equals("ADMIN")
        && !exam.getMaterie().getTeacher().equals(currentUser)) {
      throw new RuntimeException("Professors can only confirm their own exams");
    }

    // Update the exam status to CONFIRMED
    exam.setStatus(ExamStatus.CONFIRMED);

    // Save the updated exam to the database
    examRepository.save(exam);

    // Map the updated exam to ExamDto and return it
    return mapToExamDto(exam);
  }

  @Override
  @Transactional
  public ExamDto updateExam(UpdateExamDto updateExamDto) {
    List<SubGrupa> group =
        groupRepository
            .findByGroupName(updateExamDto.getOldSubGrupaName())
            .orElseThrow(() -> new RuntimeException("SubGrupa not found"));

    Materie materie =
        materieRepository
            .findByName(updateExamDto.getOldMaterieName())
            .orElseThrow(() -> new RuntimeException("Materie not found"));

    // Check if the group already has an exam with the same materie
    Optional<Exam> existingExam = examRepository.findBySubGrupaAndMaterie(group.get(0), materie);
    if (existingExam.isEmpty()) {
      throw new RuntimeException("The group does not have an exam scheduled for this materie");
    }

    // Find the classroom by name
    Sala classroom = saliRepository.findByName(updateExamDto.getSala());
    if (classroom == null) {
      throw new RuntimeException("Sala not found");
    }

    // Convert date and set start time
    LocalDate examDate = convertToLocalDate(updateExamDto.getDate(), existingExam.get().getDate());
    LocalTime startTime =
        updateExamDto.getStartTime() != null
            ? updateExamDto.getStartTime()
            : existingExam.get().getStart();

    try {
      // Check if the classroom is available at the specified time
      if (isSalaAvailable(
          classroom, startTime, existingExam.get().getDuration(), existingExam.get())) {
        // Update the exam details with the new information

        existingExam.get().setDate(examDate);
        existingExam.get().setStart(startTime);
        existingExam.get().setSala(classroom);
        List<SubGrupa> subGrupa =
            groupRepository
                .findByGroupName(updateExamDto.getSubGrupa())
                .orElseThrow(() -> new RuntimeException("SubGrupa not found"));
        existingExam
            .get()
            .setSubGrupa(subGrupa.get(0)); // TODO: check if the group is the same
        existingExam
            .get()
            .setMaterie(
                materieRepository
                    .findByName(updateExamDto.getMaterie())
                    .orElseThrow(() -> new RuntimeException("Materie not found")));
        existingExam.get().setName(updateExamDto.getTitle());
        existingExam.get().setDescription(updateExamDto.getDescription());
        existingExam.get().setDuration(updateExamDto.getDuration());
        existingExam.get().setStatus(ExamStatus.PENDING_CONFIRMATION);

        // Save the modified exam to the database
        examRepository.save(existingExam.get());
      } else {
        throw new RuntimeException("Sala is not available at the specified time or the next hours");
      }
    } catch (Exception e) {
      log.error("Error updating exam: {}", e.getMessage());
      throw new RuntimeException("Error updating exam");
    }
    return mapToExamDto(existingExam.get());
  }

  @Override
  public List<ExamDto> getExamsByUser() {
    User currentUser = userService.getCurrentUser();
    if (currentUser.getRole().name().equals("PROFESSOR")) {
      return getExamsByTeacher(currentUser).stream()
          .map(this::mapToExamDto)
          .collect(Collectors.toList());
    } else if (currentUser.getRole().name().equals("STUDENT")
        || currentUser.getRole().name().equals("GROUP_LEADER")) {
      return getExamsByStudent(currentUser);
    } else {
      return List.of();
    }
  }

  @Override
  public List<ExamDto> getExamsByFacultyAndSpecialization(String specializationName) {
    return examRepository.findExamsBySpecialization(specializationName).stream()
        .map(this::mapToExamDto)
        .collect(Collectors.toList());
  }

  public ExamsPeriodDto createExamsPeriod(CreateExamPeriodDto createExamPeriodDto) {
    log.info("createExamsPeriod {}", createExamPeriodDto);
    LocalDate currentDate = LocalDate.now();

    // Check if we are in an exam period
    ExamsPeriod currentExamsPeriod = examsPeriodRepository.findCurrentPeriod(currentDate);
    if (currentExamsPeriod != null) {
      String message =
          "Cannot create a new exams period during an existing exams period: "
              + currentExamsPeriod.getStartDate()
              + " - "
              + currentExamsPeriod.getEndDate();
      throw new RuntimeException(message);
    }

    // Check if the start date of the new exam period is before the current date
    if (createExamPeriodDto.getStartDate().isBefore(currentDate)) {
      throw new RuntimeException(
          "The start date of the new exams period cannot be before the current date");
    }

    // Check if there is an exam period already created for a date after the current date
    ExamsPeriod nextExamsPeriod = examsPeriodRepository.findNextPeriod(currentDate);
    if (nextExamsPeriod != null) {
      String message =
          "Next exams period already defined: "
              + nextExamsPeriod.getStartDate()
              + " - "
              + nextExamsPeriod.getEndDate();
      throw new RuntimeException(message);
    }

    ExamsPeriod examsPeriod = createExamPeriodDto.toExamsPeriod();
    examsPeriod = examsPeriodRepository.save(examsPeriod);

    ExamsPeriodDto examsPeriodDto = new ExamsPeriodDto();
    examsPeriodDto.setExamsPeriod(examsPeriod);
    return examsPeriodDto;
  }

  @Override
  public ExamsPeriodDto getExamsPeriod() {
    LocalDate date = LocalDate.now();

    ExamsPeriod examsPeriod = examsPeriodRepository.findNextPeriod(date);
    return examsPeriod != null ? examsPeriod.toExamsPeriodDto() : ExamsPeriodDto.builder().build();
  }

  @Override
  public ExamsPeriodDto updateExamsPeriod(UpdateExamsPeriodDto updateExamsPeriodDto) {
    if (updateExamsPeriodDto == null) {
      throw new IllegalArgumentException("UpdateExamsPeriodDto must not be null");
    }

    LocalDate oldStartDate = updateExamsPeriodDto.getOldStartDate();
    LocalDate oldEndDate = updateExamsPeriodDto.getOldEndDate();
    LocalDate newStartDate = updateExamsPeriodDto.getStartDate();
    LocalDate newEndDate = updateExamsPeriodDto.getEndDate();
    LocalDate currentDate = LocalDate.now();

    // Check if we are in an exam period
    ExamsPeriod currentExamsPeriod = examsPeriodRepository.findCurrentPeriod(currentDate);
    if (currentExamsPeriod != null) {
      String message =
          "Cannot update the exams period during an existing exams period: "
              + currentExamsPeriod.getStartDate()
              + " - "
              + currentExamsPeriod.getEndDate();
      throw new RuntimeException(message);
    }

    // Check if the start date of the new exam period is before the current date
    if (newStartDate.isBefore(currentDate)) {
      throw new RuntimeException(
          "The start date of the new exams period cannot be before the current date");
    }

    // Check if there is an exam period already created for a date after the current date
    ExamsPeriod nextExamsPeriod = examsPeriodRepository.findNextPeriod(currentDate);
    if (nextExamsPeriod != null && !nextExamsPeriod.getStartDate().equals(oldStartDate)) {
      String message =
          "Next exams period already defined: "
              + nextExamsPeriod.getStartDate()
              + " - "
              + nextExamsPeriod.getEndDate();
      throw new RuntimeException(message);
    }

    if (newStartDate.isAfter(newEndDate)) {
      throw new IllegalArgumentException("New start date must be before new end date");
    }

    ExamsPeriod examsPeriod =
        examsPeriodRepository.findByStartDateAndEndDate(oldStartDate, oldEndDate);
    if (examsPeriod == null) {
      return examsPeriodRepository.save(updateExamsPeriodDto.toExamsPeriod()).toExamsPeriodDto();
    }
    examsPeriod.setStartDate(updateExamsPeriodDto.getStartDate());
    examsPeriod.setEndDate(updateExamsPeriodDto.getEndDate());
    examsPeriod.setDescription(updateExamsPeriodDto.getDescription());

    ExamsPeriod updatedExamsPeriod = examsPeriodRepository.save(examsPeriod);
    return updatedExamsPeriod.toExamsPeriodDto();
  }

  private List<ExamDto> getExamsByStudent(User user) {
    Student student =
        studentRepository
            .findByEmail(user.getEmail())
            .orElseThrow(() -> new RuntimeException("Student not found"));
    List<Exam> exams = examRepository.findBySubGrupa(student.getSubGrupa());
    return exams.stream().map(this::mapToExamDto).collect(Collectors.toList());
  }

  private List<Exam> getExamsByTeacher(User user) {
    List<Materie> materies =
        materieRepository
            .findByTeacher(user)
            .orElseThrow(() -> new RuntimeException("Teacher not found"));

    List<Exam> exams = new ArrayList<>();
    for (Materie materie : materies) {
      exams.addAll(
          examRepository.findByMaterie(materie).isPresent()
              ? examRepository.findByMaterie(materie).get()
              : List.of());
    }
    return exams;
  }

  private boolean intervalsOverlap(String interval1, String interval2) {
    LocalTime start1 = LocalTime.parse(interval1.split(" - ")[0]);
    LocalTime end1 = LocalTime.parse(interval1.split(" - ")[1]);
    LocalTime start2 = LocalTime.parse(interval2.split(" - ")[0]);
    LocalTime end2 = LocalTime.parse(interval2.split(" - ")[1]);

    return start1.isBefore(end2) && start2.isBefore(end1);
  }

  public TeacherSchedule getTeacherSchedule(LocalDate date, Integer teacherId) {
    List<Exam> exams = examRepository.findByDateAndTeacher(date, teacherId);

    int totalHours = exams.stream().mapToInt(Exam::getDuration).sum();

    List<String> intervals =
        exams.stream()
            .map(
                exam ->
                    String.format(
                        "%s - %s",
                        exam.getStart(), exam.getStart().plusMinutes(exam.getDuration())))
            .collect(Collectors.toList());

    return new TeacherSchedule(totalHours, intervals);
  }

  @Transactional
  public ExamDto setExam(CreateExamDto createExamDto) {
    // Find existing exam by name and group
    Exam exam = findExamByNameAndSubGrupa(createExamDto.getTitle(), createExamDto.getSubGrupa());
    if (exam == null) {
      throw new RuntimeException("Exam not found");
    }

    // Find the classroom by name
    Sala classroom = saliRepository.findByName(createExamDto.getSala());
    if (classroom == null) {
      throw new RuntimeException("Sala not found");
    }

    // Convert date and set start time
    LocalDate examDate = convertToLocalDate(createExamDto.getDate(), exam.getDate());
    LocalTime startTime =
        createExamDto.getStartTime() != null ? createExamDto.getStartTime() : exam.getStart();

    // Check if the classroom is available at the specified time
    if (isSalaAvailable(classroom, startTime, exam.getDuration(), null)) {
      // Update the exam details with the new information
      updateExamDetails(exam, createExamDto, classroom, examDate, startTime);
      // Save the modified exam to the database
      examRepository.save(exam);
    } else {
      throw new RuntimeException("Sala is not available at the specified time or the next hours");
    }

    // Map the modified exam to ExamDto and return it
    return mapToExamDto(exam);
  }

  private Exam findExamByNameAndSubGrupa(String name, String groupName) {
    Optional<List<SubGrupa>> groupOptional = groupRepository.findByGroupName(groupName);
    return groupOptional
        .map(group -> examRepository.findByNameAndSubGrupa_GroupName(name, group.get(0).getGroupName()))
        .orElse(null);
  }

  private LocalDate convertToLocalDate(Date date, LocalDate defaultDate) {
    return date != null
        ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        : defaultDate;
  }

  private boolean isSalaAvailable(
      Sala classroom, LocalTime startTime, int duration, Exam examToUpdate) {
    List<Exam> examsInSala = examRepository.findBySala(classroom);
    for (Exam exam : examsInSala) {
      if (exam.equals(examToUpdate)) {
        continue;
      }
      LocalTime examStartTime = exam.getStart();
      LocalTime examEndTime = examStartTime.plusMinutes(exam.getDuration());

      LocalTime requestEndTime = startTime.plusMinutes(duration);
      if (startTime.isBefore(examEndTime) && requestEndTime.isAfter(examStartTime)) {
        return false;
      }
    }
    return true;
  }

  private void updateExamDetails(
      Exam exam,
      CreateExamDto createExamDto,
      Sala classroom,
      LocalDate examDate,
      LocalTime startTime) {
    exam.setDate(examDate);
    exam.setStart(startTime);
    exam.setSala(classroom);
    exam.setName(createExamDto.getTitle());
    exam.setDescription(createExamDto.getDescription());
    exam.setDuration(createExamDto.getDuration());
    exam.setStatus(ExamStatus.PENDING_CONFIRMATION);

    Materie materie =
        materieRepository
            .findByName(createExamDto.getMaterie())
            .orElseThrow(() -> new RuntimeException("Materie not found"));
    if (materie == null) {
      throw new RuntimeException("Materie not found");
    }
    exam.setMaterie(materie);
  }

  private ExamDto mapToExamDto(Exam exam) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    String date =
        exam.getStart().format(timeFormatter) + " " + exam.getDate().format(dateFormatter);

    return ExamDto.builder()
        .date(date)
        .classroom(exam.getSala().getName())
        .group(exam.getSubGrupa().getGroupName())
        .materie(exam.getMaterie().getName())
        .name(exam.getName())
        .description(exam.getDescription())
        .duration(exam.getDuration())
        .start(exam.getStart().format(timeFormatter))
        .status(exam.getStatus())
        .build();
  }
}
