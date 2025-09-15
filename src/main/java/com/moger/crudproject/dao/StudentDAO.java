package com.moger.crudproject.dao;

import com.moger.crudproject.entity.Student;
import java.util.List;

public interface StudentDAO {

    //students data
    Long save(Student student);

    Student findStudentById(Long id);

    Student findStudentByLastName(String lastName);

    List<Student> getAllStudents();

    void updateStudent(Long id, Student student);

    void deleteStudent(long id);

    int deleteAllStudents();

}
