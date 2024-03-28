package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerITests {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty createTestFaculty(String name) {
        return new Faculty(100L, name, "TestColor");
    }

    private void deleteLastAdded() {
        List<Faculty> list = facultyRepository.findAll();
        Faculty lastAddedFaculty = list.stream()
                .max(Comparator.comparing(Faculty::getId))
                .orElseThrow(NoSuchElementException::new);
        facultyRepository.deleteById(lastAddedFaculty.getId());
    }

    @Test
    @DisplayName(value = "GET_StatusIsOK_WhenGetFacultyByID")
    void getFacultyByIdTest() {
        long id = 1L;
        Faculty tmp = restTemplate.getForObject(
                "/faculty/{id}",
                Faculty.class,
                id);

        assertThat(tmp.getColor(), is("Red"));
    }

    @Test
    @DisplayName(value = "POST_StatusIs201_WhenFacultyIsCreated")
    void createFacultyTest() {
        Faculty tmp = createTestFaculty("TestFaculty");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "/faculty",
                tmp,
                Faculty.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("TestFaculty"));

        deleteLastAdded();
    }

    @Test
    @DisplayName(value = "PUT_StatusIs200_WhenFacultyIsUpdated")
    void editFacultyTest() {
        Faculty tmp = createTestFaculty("TestFaculty");
        HttpEntity<Faculty> entity = new HttpEntity<>(tmp);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty",
                HttpMethod.PUT,
                entity,
                Faculty.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("TestFaculty"));

        deleteLastAdded();
    }

    @Test
    @DisplayName(value = "DELETE_StatusIs200_WhenDeleteFacultyByID")
    void deleteFacultyTest() {
        long id = createTestFaculty("TestFaculty").getId();

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty/{id}",
                HttpMethod.DELETE,
                null,
                Faculty.class, id);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    @DisplayName(value = "GET_StatusIs200_WhenReturnStudentsFromFacultyByID")
    void getStudentsByFacultyIdTest() {
        long id = 1L;

        ResponseEntity<List<Student>> response = restTemplate
                .exchange("/faculty/" + id + "/students",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Student>>() {
                        });

        List<Student> list = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(list, hasSize(2));
    }

    @Test
    @DisplayName(value = "GET_StatusIs200_WhenFindFacultyByNameOrColor")
    void findFacultyByNameOrColor() {
        String name = "Gryffindor",
                color = null;

        String url = "http://localhost:" + port;
        URI uri = UriComponentsBuilder.fromHttpUrl(url).path("/faculty")
                .queryParam("name", name)
                .queryParam("color", color).build().toUri();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(uri, Faculty.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getName(), is("Gryffindor"));
    }
}
