package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity getStudentInfo(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findStudent(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllStudentsInfo() {
        return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudentsByAgeBetween(@RequestParam Integer min, @RequestParam Integer max) {
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(min, max));
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.editStudent(student));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
