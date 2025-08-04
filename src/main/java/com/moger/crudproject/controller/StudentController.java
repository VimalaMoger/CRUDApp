package com.moger.crudproject.controller;

import com.moger.crudproject.DAO.StudentDAO;
import com.moger.crudproject.entity.Student;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {
    private final StudentDAO studentDAO;

    public StudentController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @PostMapping(value = "/students", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String createStudent(@RequestBody @Valid Student student) {
        Long studentId = studentDAO.save(student);
        return String.format("Created student with id %d", studentId);
    }

    @GetMapping(value = "/students/{id}")
    public Student findStudent(@PathVariable Long id) {
        return studentDAO.findStudentById(id);
    }

    @GetMapping(value = "/students")
    public List<Student> findAll() {
        return studentDAO.getAllStudents();
    }

    @PutMapping(value = "/students/{id}")
    public String updateStudent(@PathVariable Long id, @RequestBody @Valid Student student) {
        Student beforeUpdateStudent = studentDAO.updateStudent(id, student);
        student.setId((int) beforeUpdateStudent.getId());
        return beforeUpdateStudent + String.format(" now changed to " + student);
    }

    @DeleteMapping(value = "/students/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentDAO.deleteStudent(id);
        return String.format("Student with id %d deleted",id);
    }

    @DeleteMapping(value = "/students")
    public String deleteAll() {
        int rowsAffected = studentDAO.deleteAllStudents();
        return String.format("%d records removed", rowsAffected);
    }
}
