package com.usv.examtimetabling.subject.service.impl;

import com.usv.examtimetabling.exam.model.Exam;
import com.usv.examtimetabling.exam.repository.ExamRepository;
import com.usv.examtimetabling.faculty.degree.model.Degree;
import com.usv.examtimetabling.faculty.degree.repository.DegreeRepository;
import com.usv.examtimetabling.faculty.degree.subgrupa.model.SubGrupa;
import com.usv.examtimetabling.faculty.degree.subgrupa.repository.SubGrupaRepository;
import com.usv.examtimetabling.faculty.repository.FacultateRepository;
import com.usv.examtimetabling.subject.model.Subject;
import com.usv.examtimetabling.subject.model.dto.CreateSubjectDto;
import com.usv.examtimetabling.subject.model.dto.SubjectDto;
import com.usv.examtimetabling.subject.model.dto.UpdateSubjectDto;
import com.usv.examtimetabling.subject.repository.SubjectRepository;
import com.usv.examtimetabling.subject.service.SubjectService;
import com.usv.examtimetabling.user.model.User;
import com.usv.examtimetabling.user.model.utils.Role;
import com.usv.examtimetabling.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final UserRepository userRepository;

    private final ExamRepository examRepository;

    private final SubGrupaRepository groupRepository;

    private final DegreeRepository degreeRepository;

    private final FacultateRepository facultateRepository;

    public SubjectDto addSubject(CreateSubjectDto createSubjectDto) {

        Degree degree = degreeRepository.findByName(createSubjectDto.getDegree())
                .orElseThrow(() -> new IllegalArgumentException("Degree not found"));

        User teacher = userRepository.findByName(createSubjectDto.getTeacher())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if (subjectRepository.findByNameAndSemesterAndYearAndDegree(createSubjectDto.getName(), createSubjectDto.getSemester(), createSubjectDto.getYear(), degree).isPresent()) {
            throw new IllegalArgumentException("Subject already exists");
        }

        Subject subject = subjectRepository.save(Subject.builder()
                .name(createSubjectDto.getName())
                .semester(createSubjectDto.getSemester())
                .year(createSubjectDto.getYear())
                .degree(degree)
                .facultate(createSubjectDto.getFaculty() != null ?
                    facultateRepository.findByLongName(createSubjectDto.getFaculty()).orElseThrow(() -> new IllegalArgumentException("Faculty not found")) : null)
                .teacher(teacher)
                .examDuration(createSubjectDto.getExamDuration())
                .build());

        return SubjectDto.builder()
                .name(subject.getName())
                .semester(subject.getSemester())
                .year(subject.getYear())
                .degree(subject.getDegree().getName())
            .faculty(subject.getFacultate() != null ? subject.getFacultate().getLongName() : null)
                .teacher(subject.getTeacher().getEmail())
                .examDuration(subject.getExamDuration())
                .build();
    }

    public SubjectDto addTeacher(String name, String teacher) {
        Subject subject = subjectRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        User teacherEntity = userRepository.findByEmail(teacher)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if (teacherEntity.getRole() != Role.PROFESSOR) {
            throw new IllegalArgumentException("User is not a teacher");
        }

        subject.setTeacher(teacherEntity);

        List<SubGrupa> groups = groupRepository.findBySpecializationShortNameAndStudyYear(subject.getDegree().getName(), String.valueOf(subject.getYear()));

        for (SubGrupa group : groups) {
            Exam exam = Exam.builder()
                    .name(subject.getName())
                    .subGrupa(group)
                    .subject(subject)
                    .build();

            examRepository.save(exam);
        }

        return SubjectDto.builder()
                .name(subjectRepository.save(subject).getName())
                .semester(subject.getSemester())
                .teacher(subject.getTeacher().getEmail())
                .year(subject.getYear())
                .degree(subject.getDegree().getName())
                .faculty(subject.getFacultate() != null ? subject.getFacultate().getLongName() : null)
                .build();
    }

    public void ifSubjectThenExam(){
        List<Subject> subjects = subjectRepository.findAll();
        for(Subject subject : subjects){
            List<SubGrupa> groups = groupRepository.findBySpecializationShortNameAndStudyYear(subject.getDegree().getName(), String.valueOf(subject.getYear()));
            for (SubGrupa group : groups) {
                Exam exam = Exam.builder()
                        .name(subject.getName())
                        .subGrupa(group)
                        .subject(subject)
                        .build();

                examRepository.save(exam);
            }
        }
    }

    @Override
    public SubjectDto getSubjectByName(String name) {
        return subjectRepository.findByName(name)
                .map(subject -> SubjectDto.builder()
                        .name(subject.getName())
                        .semester(subject.getSemester())
                        .teacher(subject.getTeacher() != null ? subject.getTeacher().getName() : null)
                        .year(subject.getYear())
                        .degree(subject.getDegree().getName()!= null ? subject.getTeacher().getEmail() : null)
                        .faculty(subject.getFacultate() != null ? subject.getFacultate().getLongName() : null)
                        .build())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
    }

    public SubjectDto updateSubject(UpdateSubjectDto updateSubjectDto){
        Subject subject = subjectRepository.findByName(updateSubjectDto.getOldName())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        subject.setName(updateSubjectDto.getName());
        subject.setSemester(updateSubjectDto.getSemester());
        subject.setYear(updateSubjectDto.getYear());
        subject.setDegree(degreeRepository.findByName(updateSubjectDto.getDegree())
                .orElseThrow(() -> new IllegalArgumentException("Degree not found")));
        subject.setTeacher(userRepository.findByName(updateSubjectDto.getTeacher())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found")));
        subject.setExamDuration(updateSubjectDto.getExamDuration());

        return SubjectDto.builder()
                .name(subjectRepository.save(subject).getName())
                .semester(subject.getSemester())
                .teacher(subject.getTeacher() != null ? subject.getTeacher().getEmail() : null)
                .year(subject.getYear())
                .degree(subject.getDegree().getName()!= null ? subject.getTeacher().getEmail() : null)
                .examDuration(subject.getExamDuration())
                .build();
    }

    public List<SubjectDto> getAll() {
        return subjectRepository.findAll().stream()
                .map(toSubjectDtoList())
                .toList();
    }

    public List<SubjectDto> getSubjectByTeacher(String teacher) {
        User teacherEntity = userRepository.findByEmail(teacher)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        List<Subject> subjects = subjectRepository.findByTeacher(teacherEntity)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        return subjects.stream()
                .map(toSubjectDtoList())
                .toList();
    }

    private static Function<Subject, SubjectDto> toSubjectDtoList() {
        return subject -> SubjectDto.builder()
            .name(subject.getName())
            .semester(subject.getSemester())
            .teacher(subject.getTeacher() != null ? subject.getTeacher().getName() : null)
            .year(subject.getYear())
            .degree(subject.getDegree() != null ? subject.getDegree().getName() : null)
            .faculty(subject.getFacultate() != null ? subject.getFacultate().getLongName() : null)
            .examDuration(subject.getExamDuration())
            .build();
    }

    @Transactional
    public void deleteSubject(String subject) {
        Optional<Subject> subjectEntityOptional = subjectRepository.findByName(subject);
        if (subjectEntityOptional.isPresent()) {
            subjectRepository.delete(subjectEntityOptional.get());
        } else {
            throw new IllegalArgumentException("Subject not found");
        }
    }
}
