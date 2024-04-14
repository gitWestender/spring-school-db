package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
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

    @GetMapping("/{id}")
    public ResponseEntity getStudentInfo(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.findStudent(id));
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudent(@PathVariable Long id) {
        Student tmpStudent = studentService.findStudent(id);
        if (tmpStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(studentService.getFacultyByStudent(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllStudentsInfo() {
        return ResponseEntity.ok().body(studentService.getAllStudent());
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudentsByAgeBetween(@RequestParam(required = true) Integer min,
                                                                       @RequestParam(required = true) Integer max) {
        if (min != null && max != null && min > max) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(min, max));
    }

    @GetMapping("/print-parallel")
    public void printNamesInParallelThreads() {
        studentService.printNamesInParallelThreads();
    }

    @GetMapping("/print-synchronized")
    public void printNamesInSyncThreads() {
        studentService.printNamesInSyncTreads();
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.editStudent(student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
