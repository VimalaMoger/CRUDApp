package com.moger.crudproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moger.crudproject.controller.StudentController;
import com.moger.crudproject.entity.Student;
import com.moger.crudproject.exception.DataNotFoundException;
import com.moger.crudproject.serviceImpl.StudentDAOImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Mock MVC Framework to run REST API Tests with serverless mode
 *  with Mockito  and JUnit
 */

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EntityManager entityManager;

    @MockitoBean
    private StudentDAOImpl studentDAO;

    @Autowired
    private StudentController studentController;

    @Test
    @WithMockUser(roles = {"USER"})  //role = "USER"
    public void shouldSaveStudent() throws Exception {

        objectMapper= new ObjectMapper();
        Student stu = buildStudent();
        String jsonString = objectMapper.writeValueAsString(stu);
        when(studentDAO.save(any())).thenReturn(1L);
        this.mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(jsonString)).andDo(print()).andExpect(status().isCreated());
        verify(studentDAO, times(1)).save(any());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldGetStudentIfExists() throws Exception {

        when(studentDAO.findStudentById(1L)).thenReturn(buildStudent());
        when(studentDAO.getAllStudents()).thenReturn(List.of(buildStudent()));
        MvcResult result = this.mockMvc.perform(get("/students/{id}", 1L)
                .with(csrf()))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();

	// or using objectmapper
	//  Student student = objectMapper.readValue(json, Student.class);

        Student student = new Gson().fromJson(json, Student.class);

        assertNotNull(student);
        assertEquals(1L, student.getId());
        assertEquals("stu_Chemistry", student.getMajor());
    }

    //status":404,
    // "message":"Student with id 1 does not exist!"

    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldGetCustomErrorIfStudentNotExists() throws Exception {

        when(studentDAO.findStudentById(1L)).thenReturn(buildStudent());
        this.mockMvc.perform(get("/students/{id}", 1L)/// "students/"+id
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student with id 1 does not exist!"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldGetAllStudents() throws Exception {

        List<Student> studentsList = new ArrayList();
        Student theStudent = buildStudent();
        Student student = new Student("Lily", "Public",12,"lily@comp.com", "stu_Physics");
        studentsList.add(theStudent);
        studentsList.add(student);

        when(studentDAO.getAllStudents()).thenReturn(studentsList);

        MvcResult result = this.mockMvc.perform(get("/students")
                        .with(csrf()))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        TypeToken<List<Student>> typeToken = new TypeToken<>(){};  //set type of object
        List<Student> students = new Gson().fromJson(json, typeToken.getType()); //Gson uses reflection to convert object type

        assertNotNull(students);
        assertEquals(2, students.size());

    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldGetStudentUpdated() throws Exception {

        Student student = buildStudent();
        when(studentDAO.findStudentById(1L)).thenReturn(student);
        when(studentDAO.getAllStudents()).thenReturn(List.of(buildStudent()));
        when(studentDAO.updateStudent(anyLong(), any())).thenReturn(1);

        Student updateStudent = new Student("Daisy", "Bran",12,"pansy@comp.com", "stu_Chemistry");
        updateStudent.setId(student.getId());
        String jsonString = objectMapper.writeValueAsString(updateStudent);

        this.mockMvc.perform(put("/students/"+student.getId()).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(jsonString)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(student + String.format(" now changed to " + updateStudent)));

        verify(studentDAO, times(1)).updateStudent(anyLong(), any());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldGetUpdateErrorMessage() throws Exception {

        Student student = buildStudent();

        when(studentDAO.getAllStudents().isEmpty()).thenThrow(new DataNotFoundException("Student with id 1 does not exist!"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            studentDAO.getAllStudents().isEmpty();
        });

        Student updateStudent = new Student("Daisy", "Bran",12,"pansy@comp.com", "stu_Chemistry");
        String jsonString = objectMapper.writeValueAsString(updateStudent);

        this.mockMvc.perform(put("/students/"+student.getId()).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(jsonString)).andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldDeleteStudent() throws Exception {

        Student student = buildStudent();
        when(studentDAO.getAllStudents()).thenReturn(List.of(buildStudent()));
        //when(entityManager.find(eq(Student.class), anyLong())).thenReturn(student);

        doNothing().when(studentDAO).deleteStudent(anyLong());
        this.mockMvc.perform(delete("/students/"+student.getId())
                        .with(csrf()))
                        .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Student with id 1 deleted"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldDeleteAllStudents() throws Exception {

        when(studentDAO.deleteAllStudents()).thenReturn(2);

        this.mockMvc.perform(delete("/students")
                        .with(csrf()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("2 records removed"));
    }

    private Student buildStudent() {
        Student theStudent = new Student("Pansy", "Bran",12,"pansy@comp.com", "stu_Chemistry");
        theStudent.setId(1);
        return theStudent;
    }
}
