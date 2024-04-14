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
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.*;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        Supplier<StudentNotFoundException> sup = new Supplier<StudentNotFoundException>() {
            @Override
            public StudentNotFoundException get() {
                return null;
            }
        };
        return studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException(""));
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public List<Student> getAllStudentsByFirstLetter(String letter) {
        return studentRepository.findAll()
                .stream()
                .filter(student -> startsWithIgnoreCase(student.getName(), letter))
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    public Double getAverageAgeOfAllStudents() {
        return studentRepository.findAll()
                .stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0);
    }

    public Collection<Student> getStudentsByAgeBetween(Integer min, Integer max) {
        return studentRepository.findStudentsByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudent(Long id){
        return studentRepository.getReferenceById(id).getFaculty();
    }

    public Integer getSchoolStudentsCount() {
        return studentRepository.getNumbersOfAllStudents();
    }

    public Integer getAvgAgeOfAllStudents() {
        return studentRepository.getAvgAgeOfStudents();
    }

    public List<Student> getLastFiveStudents(Integer count) {
        return studentRepository.getLastStudents(count);
    }


    public int defaultSum() {

        Long time = System.currentTimeMillis();

        int sum = Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        logger.info("Time spend (for Default Sum): " + (System.currentTimeMillis() - time) + " ms");
        return sum;
    }

    public int parallelSum() {

        Long time = System.currentTimeMillis();

        int sum = IntStream
                .iterate(1, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .sum();
        logger.info("Time spend (for Parallel Sum): " + (System.currentTimeMillis() - time) + " ms");
        return sum;
    }
}
