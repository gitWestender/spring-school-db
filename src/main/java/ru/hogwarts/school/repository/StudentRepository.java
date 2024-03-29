package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findStudentsByAgeBetween(Integer min, Integer max);

    @Query(
            value = "select count(distinct (name)) from students",
            nativeQuery = true)
    Integer getNumbersOfAllStudents();

    @Query(
            value = "select avg(students.age) as age from students",
            nativeQuery = true)
    Integer getAvgAgeOfStudents();

    @Query(
            value = "select * from students order by id desc limit ?",
            nativeQuery = true)
    List<Student> getLastStudents(Integer count);
}
