package ru.hogwarts.school.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        logger.info("Was invoked method for PUT student");
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.info("Was invoked method for GET student");
        Supplier<StudentNotFoundException> sup = new Supplier<StudentNotFoundException>() {
            @Override
            public StudentNotFoundException get() {
                return null;
            }
        };
        return studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException(""));
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method to PATCH student");
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method to DELETE student");
        studentRepository.deleteById(id);
    }

    public List<Student> getAllStudent() {
        logger.info("Was invoked method to GET_ALL student");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAgeBetween(Integer min, Integer max) {
        logger.info("Was invoked method to GET_ALL_BETWEEN_AGE");
        return studentRepository.findStudentsByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudent(Long id){
        logger.info("Was invoked method to GET_STUDENT_FACULTY");
        return studentRepository.getReferenceById(id).getFaculty();
    }

    public Integer getSchoolStudentsCount() {
        logger.info("Was invoked method to GET_NUMBER_OF_STUDENTS");
        return studentRepository.getNumbersOfAllStudents();
    }

    public Integer getAvgAgeOfAllStudents() {
        logger.info("Was invoked method to GET_AVG_AGE");
        return studentRepository.getAvgAgeOfStudents();
    }

    public List<Student> getLastFiveStudents(Integer count) {
        logger.info("Was invoked method to GET_LAST_STUDENTS");
        return studentRepository.getLastStudents(count);
    }
}
