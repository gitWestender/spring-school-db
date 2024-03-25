package ru.hogwarts.school.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerITests {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    private Student createTestStudent(String name) {
        Student tmp = new Student(100L, name, 20);
        return studentRepository.save(tmp);
    }


    void cleanUP() {
        List<Student> tmp = studentRepository.findAll();
        Student last = tmp.stream()
                .max(Comparator.comparing(Student::getId))
                .orElseThrow(NoSuchElementException::new);
        studentRepository.deleteById(last.getId());
    }


    @Test
    @DisplayName(value = "GET_StatusIs200_WhenGetStudentByID")
    void getStudentByIdTest() {
        long id = createTestStudent("TestName1").getId();
        Student tmp = restTemplate.getForObject("/student/{id}", Student.class, id);

        assertThat(tmp.getName(), is("TestName1"));

        cleanUP();
    }

    @Test
    @DisplayName(value = "POST_StatusIs201_WhenStudentIsCreated")
    void createStudentTest() {
        Student tmp = createTestStudent("CreateTest");

        ResponseEntity<Student> response = restTemplate.postForEntity("/student", tmp, Student.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("CreateTest"));

        cleanUP();
    }

    @Test
    @DisplayName(value = "PUT_StatusIs200_WhenStudentIsUpdated")
    void editStudentTest() {
        Student tmp = createTestStudent("UpdateTest");
        HttpEntity<Student> entity = new HttpEntity<>(tmp);

        ResponseEntity<Student> response = restTemplate.exchange("/student", HttpMethod.PUT, entity, Student.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("UpdateTest"));
        cleanUP();

    }

    @Test
    @DisplayName(value = "DELETE_StatusIs200_WhenDeleteStudentByID")
    void deleteStudentTest() {
        long id = createTestStudent("DeleteTest").getId();

        ResponseEntity<Student> response = restTemplate
                .exchange("/student/{id}", HttpMethod.DELETE, null, Student.class, id);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    @DisplayName(value = "GET_StatusIs200_WhenFindStudentsByAgeBetween")
    void getStudentsByAgeBetweenIsOKTest() {
        Integer min = 16, max = 18;
        String url = "http://localhost:"+port;
        URI uri = UriComponentsBuilder.fromHttpUrl(url).path("/student")
                .queryParam("min", min)
                .queryParam("max", max).build().toUri();

        ResponseEntity<Collection<Student>> response = restTemplate.getForEntity(uri, null);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    @DisplayName(value = "GET_StatusIs400_WhenBadRequestForFindStudentsByAgeBetween")
    void getStudentsByAgeBetweenIsBADREQUESTTest() {
        int min = 20, max = 10;

        String url = "http://localhost:"+port;
        URI uri = UriComponentsBuilder.fromHttpUrl(url).path("/student")
                .queryParam("min", min)
                .queryParam("max", max).build().toUri();

        ResponseEntity<Collection<Student>> response = restTemplate.getForEntity(uri, null);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));

    }

    @Test
    @DisplayName(value = "GET_StatusIs200_WhenGetAllStudents")
    void getAllStudentsTest() {
        ResponseEntity<List<Student>> response = restTemplate.exchange("/student/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });
        List<Student> students = response.getBody();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(students, hasSize(4));
    }

}
