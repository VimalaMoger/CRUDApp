package com.moger.crudproject.helperUtilities;

//import com.moger.crudproject.DAO.StudentDAO;
import com.moger.crudproject.DAO.StudentDAO;
import com.moger.crudproject.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Dataprovider {

    StudentDAO studentDAO;

    public Dataprovider(StudentDAO studentDAO){
        this.studentDAO = studentDAO;
    }
    
    // createStudent();
    public void saveStudent(){
        Student tempStudent = new Student("Pansy", "Bran", "pansy@comp.com");
        studentDAO.save(tempStudent);
    }

    // createMultipleStudents();
    public void saveMultipleStudents(){
        Student tempStudent1 = new Student("Lily", "Public", "lily@comp.com");
        Student tempStudent2 = new Student("Bonita", "Applebum", "bonita@comp.com");
        studentDAO.save(tempStudent1);
        studentDAO.save(tempStudent2);
    }

    // findStudentByID();
    public void findStudentById(){
        long studentId = 1;
        Student student = studentDAO.findStudentById(studentId);
        System.out.println(student);
    }

    // queryForStudentsByLastName();
    public void findStudentByLastName(){
        Student student = studentDAO.findStudentByLastName("Applebum");
        System.out.println(student);
    }

    // getAllStudents();
    public void findAllStudents(){
        List<Student> students = studentDAO.getAllStudents();
        System.out.println(Arrays.toString(new List[]{students}));
    }

    // updateStudent();
    public void updateStudent(){
        Long studentId = 1L;
        Student tempStudent1 = new Student("Daisy", "Dona", "daisy@comp.com");
        //Student student = studentDAO.findStudentById(studentId);
        studentDAO.updateStudent(studentId, tempStudent1);
    }

    // deleteStudent();
    public void deleteStudent(){
        long studentId = 1;
        studentDAO.deleteStudent(studentId);
    }

    //deleteAllStudents();
    public void deleteALLStudent(){
        long studentId = 1;
        studentDAO.deleteAllStudents();
    }
}
