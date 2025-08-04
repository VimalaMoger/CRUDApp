package com.moger.crudproject.DAO;

import com.moger.crudproject.entity.Student;

import java.util.List;

public interface StudentDAO {
    Long save(Student student);
    Student findStudentById(Long id);
    Student findStudentByLastName(String lastName);
    List<Student> getAllStudents();
    Student updateStudent(Long id, Student student);
    void deleteStudent(long id);
    int deleteAllStudents();

}
