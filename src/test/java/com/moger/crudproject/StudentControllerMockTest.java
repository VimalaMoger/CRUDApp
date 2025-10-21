package com.moger.crudproject;


import com.moger.crudproject.controller.StudentController;
import com.moger.crudproject.entity.Student;
import com.moger.crudproject.serviceImpl.StudentDAOImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 * Mockito framework to mock the dependencies
 */

@ExtendWith(MockitoExtension.class)
public class StudentControllerMockTest {

    @Mock
    private StudentDAOImpl studentDAO;

    @InjectMocks
    StudentController controller;

    @Test
    public void addStudentTest() {
        Student student = new Student("Pansy", "Bran",12,"pansy@comp.com", "stu_Chemistry");
        when(studentDAO.save(any())).thenReturn(1L);
        ResponseEntity<String> responseEntity = controller.createStudent(student);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void getAllStudentsTest() {
    }
}
