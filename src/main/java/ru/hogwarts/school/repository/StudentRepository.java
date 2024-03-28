package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findStudentsByAgeBetween(Integer min, Integer max);

    @Query (value = "DELETE FROM students WHERE id = (SELECT id FROM students ORDER BY id DESC LIMIT 1)", nativeQuery = true)
    Collection<Student> deleteLastAdded();
}
