package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findFacultyByColorIgnoreCase(String color);
    Faculty findFacultyByNameIgnoreCase(String name);

    @Query(value = "select name, age from students join faculties on students.faculty_id = :id", nativeQuery = true)
    List<Student> findStudentsByFacultyId(@Param("id") Long id);

}
