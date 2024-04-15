package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for PUT faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaclty(Long id) {
        logger.info("Was invoked method for GET faculty");
        Supplier<FacultyNotFoundException> sup = new Supplier<>() {
            @Override
            public FacultyNotFoundException get() {
                return new FacultyNotFoundException("");
            }
        };
//        Supplier<FacultyNotFoundException> sup = () -> new FacultyNotFoundException("");
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(""));
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for PATCH faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for DELETE faculty");
        facultyRepository.deleteById(id);
    }

    public Faculty findFacultyByName(String name) {
        logger.info("Was invoked method for GET_FACULTY_BY_NAME");
        return facultyRepository.findFacultyByNameIgnoreCase(name);
    }

    public Faculty findFacultyByColor(String color) {
        logger.info("Was invoked method for GET_FACULTY_BY_COLOR");
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }

    public List<Student> getStudentsByFaculty(Long id) {
        logger.info("Was invoked method for GET_ALL_STUDENTS_BY_FACULTY_ID");
        return facultyRepository.findById(id)
                .map(Faculty::getStudents)
                .orElse(Collections.emptyList());
    }

    public String getLongestFacultyName() {
        return facultyRepository.findAll()
                .stream()
                .max(Comparator.comparing(faculty -> faculty.getName().length()))
                .get()
                .getName();
    }

}
