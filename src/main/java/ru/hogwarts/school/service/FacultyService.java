package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaclty(Long id) {
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
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Faculty findFacultyByName(String name) {
        return facultyRepository.findFacultyByNameIgnoreCase(name);
    }

    public Faculty findFacultyByColor(String color) {
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }

    public List<Student> getStudentsByFaculty(Long id) {
        return facultyRepository.findStudentsByFacultyId(id);
    }

}
