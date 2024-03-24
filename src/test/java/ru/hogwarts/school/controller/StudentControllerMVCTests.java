package ru.hogwarts.school.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(StudentController.class)
public class StudentControllerMVCTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    Student testStudent;
    Collection<Student> testStudentsList;
    Faculty testFaculty;
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {

        testStudent = new Student(1L, "TestName", 18);
        objectMapper = new ObjectMapper();
    }

    public ResultMatcher rmStudent(String prefix, Student student) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(student.getId()),
                jsonPath(prefix + ".name").value(student.getName()),
                jsonPath(prefix + ".age").value(student.getAge())
        );
    }

    public static String asJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName(value = "GET_StatusIsOK_WhenGetStudentByID")
    void getStudentByIdTest() throws Exception {
        final Long ID = 1L;

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testStudent));

        mockMvc.perform(get("/student/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(rmStudent("$", testStudent));
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName(value = "GET_StatusIs4xx_WhenNotFoundStudentByID")
    void getStudentById404Test() throws Exception {
        final Long ID = 1L;

        when(studentRepository.findById(ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/student/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
        verify(studentRepository, times(1)).findById(ID);
    }

    @Test
    @DisplayName(value = "POST_StatusIsOk_WhenStudentIsCreated")
    void createStudentTest() throws Exception {

        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/student")
                        .content(asJson(testStudent))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(rmStudent("$", testStudent))
                .andDo(print());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName(value = "PUT_StatusIsOk_WhenStudentIsUpdate")
    void editStudentTest() throws Exception {

        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(put("/student")
                        .content(asJson(testStudent))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()))
                .andExpect(rmStudent("$", testStudent))
                .andDo(print());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName(value = "DELETE_StatusIsOk_WhenStudentWasDeleted")
    void deleteStudentTest() throws Exception {
        final Long ID = 1L;

        doNothing().when(studentRepository).deleteById(ID);

        mockMvc.perform(delete("/student/{id}", ID))
                .andExpect(status().isOk())
                .andDo(print());
        verify(studentRepository, times(1)).deleteById(ID);
        assertThat(studentRepository.findById(ID)).isEmpty();
    }

    @Test
    @DisplayName(value = "GET_StatusIsOk_WhenGetFacultyByStudentID")
    void getFacultyByStudentIdTest() throws Exception {
        final Long ID = 1L;

        testFaculty = new Faculty(5L, "TestName", "TestColor");
        testStudent.setFaculty(testFaculty);

        when(studentRepository.findById(ID)).thenReturn(Optional.of(testStudent));
        when(studentRepository.getReferenceById(ID)).thenReturn(testStudent);

        mockMvc.perform(get("/student/{id}/faculty", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testFaculty.getId()))
                .andExpect(jsonPath("$.name").value(testFaculty.getName()))
                .andExpect(jsonPath("$.color").value(testFaculty.getColor()))
                .andDo(print());
        verify(studentRepository, times(1)).getReferenceById(ID);
        verify(studentRepository, times(1)).findById(ID);

    }

    @Test
    @DisplayName(value = "GET_StatusIsOk_WhenGetAllStudentsInfo")
    void getAllStudentsInfoTest() throws Exception {
        testStudentsList = new ArrayList<>(List.of(testStudent));

        when(studentRepository.findAll()).thenReturn((List<Student>) testStudentsList);

        mockMvc.perform(get("/student/all"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName(value = "GET_StatusIsOk_WhenGetStudentsByAgeBetween")
    void getStudentsByAgeBetweenTest() throws Exception {
        testStudentsList = new ArrayList<>(List.of(testStudent));

        when(studentRepository.findStudentsByAgeBetween(any(Integer.class), any(Integer.class)))
                .thenReturn(testStudentsList);

        mockMvc.perform(get("/student?min=1&max=20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
        verify(studentRepository, times(1)).findStudentsByAgeBetween(1,20);

    }

}
