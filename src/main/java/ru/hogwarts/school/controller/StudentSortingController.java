package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student-sorting")
public class StudentSortingController {
    private final StudentService studentService;

    public StudentSortingController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getSchoolStudentsCount() {
        return ResponseEntity.ok().body(studentService.getSchoolStudentsCount());
    }

    @GetMapping("/avg-age")
    public ResponseEntity<Integer> getAvgAgeOfAllStudents() {
        return ResponseEntity.ok().body(studentService.getAvgAgeOfAllStudents());
    }

    @GetMapping("/last")
    public ResponseEntity<List<Student>> getLastFiveStudents(@RequestParam(name = "count") Integer count) {
        return ResponseEntity.ok().body(studentService.getLastFiveStudents(count));
    }
}
