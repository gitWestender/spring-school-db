package ru.hogwarts.school.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerMVCTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private Faculty testFaculty;
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        testFaculty = new Faculty(1L, "TestName", "TestColor");
        objectMapper = new ObjectMapper();
    }

    private ResultMatcher rmFaculty(String prefix, Faculty faculty) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(faculty.getId()),
                jsonPath(prefix + ".name").value(faculty.getName()),
                jsonPath(prefix + ".color").value(faculty.getColor())
        );
    }

    private MockHttpServletRequestBuilder postJson(String uri, Object body) {
        try {
            String json = new ObjectMapper().writeValueAsString(body);
            return post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName(value = "GET_StatusIsOK_WhenGetFacultyByID")
    void getFacultyByIdTest() throws Exception {
        final Long ID = 1L;
        when(facultyRepository.findById(ID)).thenReturn(Optional.of(testFaculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(rmFaculty("$", testFaculty));
    }

    @Test
    @DisplayName(value = "GET_StatusIs4xx_WhenNotFoundFacultyByID")
    void getFacultyById404Test() throws Exception {
        final Long ID = 1L;
        when(facultyRepository.findById(ID)).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void postFacultyTest() throws Exception {

        when(facultyRepository.save(any())).thenReturn(testFaculty);

        mockMvc.perform(postJson("/faculty", testFaculty))
                .andExpect(status().isCreated())
                .andExpect(rmFaculty("$", testFaculty))
                .andDo(print());

    }

    @Test
    void findFacultyByNameOrColorTest() throws Exception {

        when(facultyRepository.findFacultyByNameIgnoreCase(any(String.class))).thenReturn(testFaculty);
        when(facultyRepository.findFacultyByColorIgnoreCase(any(String.class))).thenReturn(testFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?name=" + testFaculty.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(rmFaculty("$", testFaculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + testFaculty.getColor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(rmFaculty("$", testFaculty));
    }

    @Test
    @DisplayName(value = "ShouldReturn_StudentsByFacultyID_When_Found")
    void getStudentsByFacultyIdTest() throws Exception {
        List<Student> testStudentList = new ArrayList<>(
                List.of(new Student(1L, "TS1", 18),
                        new Student(2L, "TS2", 20),
                        new Student(3L, "TS3", 22)));
        testFaculty.setStudents(testStudentList);

        when(facultyRepository.getReferenceById(1L)).thenReturn(testFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}/students", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(facultyRepository, times(1)).getReferenceById(1L);
    }

    @Test
    @DisplayName(value = "ShouldReturn_OK_WhenReplaceFaculty")
    void editFacultyTest() throws Exception {

        when(facultyRepository.save(any())).thenReturn(testFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isOk());
        verify(facultyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName(value = "ShouldReturn_OK_WhenDeleteFoundFacultyByID")
    void deleteFacultyBuIdTest() throws Exception {

        doNothing().when(facultyRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(print());
        verify(facultyRepository, times(1)).deleteById(1L);
        assertThat(facultyRepository.findById(1L)).isEmpty();
    }

}
