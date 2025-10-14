package com.moger.crudproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moger.crudproject.controller.StudentController;
import com.moger.crudproject.entity.Student;
import com.moger.crudproject.serviceImpl.StudentDAOImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    //status":404,
    // "message":"Student with id 1 does not exist!"

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

    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldGetCustomErrorIfStudentNotExists() throws Exception {

        when(studentDAO.findStudentById(1L)).thenReturn(buildStudent());
        this.mockMvc.perform(get("/students/{id}", 1L)
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

/*  .andExpect(status().isOk())
.andExpect(jsonPath("$", hasSize(3)))
.andExpect(jsonPath("$[0]", is("The Book of CSS3")))
.andExpect(jsonPath("$[1]", is("Core HTML5 Canvas")))
.andExpect(jsonPath("$[2]", is("Pro JavaScript for Web Apps")))
.andDo(print());

*/

    Student buildStudent() {
        Student theStudent = new Student("Pansy", "Bran",12,"pansy@comp.com", "stu_Chemistry");
        theStudent.setId(1);
        return theStudent;
    }
}
