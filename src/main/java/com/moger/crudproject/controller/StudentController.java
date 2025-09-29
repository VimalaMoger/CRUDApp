package com.moger.crudproject.controller;

import com.moger.crudproject.dao.StudentDAO;
import com.moger.crudproject.entity.Student;
import com.moger.crudproject.exception.DataNotFoundException;
import com.moger.crudproject.exception.StudentNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
public class StudentController {
    private final StudentDAO studentDAO;

    public StudentController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @PostMapping(value = "/students", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createStudent(@RequestBody @Valid Student student) {

        Long studentId = studentDAO.save(student);
        return new ResponseEntity<>(String.format("Created student with id %d", studentId), HttpStatus.CREATED);
    }

    @GetMapping(value = "/students/{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) {

        List<Student> students = studentDAO.getAllStudents().stream().filter(n -> n.getId()==id).toList();
        if (students.isEmpty())
            throw new StudentNotFoundException(String.format("Student with id %d does not exist!", id));
        return new ResponseEntity<>(studentDAO.findStudentById(id),HttpStatus.OK);
    }

    @GetMapping(value = "/students")
    public List<Student> findAll() {

        return studentDAO.getAllStudents();
    }

    @PutMapping(value = "/students/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Long id, @RequestBody @Valid Student student) {

        List<Student> students = studentDAO.getAllStudents().stream().filter(n -> n.getId()==id).toList();
        if (students.isEmpty())
            throw new DataNotFoundException(String.format("Student with id %d does not exist!", id));

        Student beforeUpdateStudent = studentDAO.findStudentById(id);
        if (studentDAO.updateStudent(id, student) > 0) {
            student.setId(id);
        }
        return new ResponseEntity<>(beforeUpdateStudent + String.format(" now changed to " + student),HttpStatus.OK);
    }

    @DeleteMapping(value = "/students/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {

        List<Student> students = studentDAO.getAllStudents().stream().filter(n -> n.getId() == id).toList();
        if (students.isEmpty())
            throw new StudentNotFoundException(String.format("Student with id %d does not exist!", id));
        studentDAO.deleteStudent(id);

        String message = String.format("Student with id %d deleted", id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/students")
    public String deleteAll() {

        int rowsAffected = studentDAO.deleteAllStudents();
        return String.format("%d records removed", rowsAffected);
    }
}
