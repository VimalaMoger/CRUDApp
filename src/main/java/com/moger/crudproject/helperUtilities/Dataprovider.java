package com.moger.crudproject.helperUtilities;

import com.moger.crudproject.dao.StudentDAO;
import com.moger.crudproject.dao.UserDAO;
import com.moger.crudproject.config.SecurityConfig;
import com.moger.crudproject.entity.Role;
import com.moger.crudproject.entity.Student;
import com.moger.crudproject.entity.User;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class Dataprovider {

    private StudentDAO studentDAO;
    private UserDAO usersDAO;
    private SecurityConfig config;
    String encodedPassword;

    public Dataprovider(StudentDAO studentDAO, UserDAO usersDAO, SecurityConfig config){
        this.studentDAO = studentDAO;
        this.usersDAO = usersDAO;
        encodedPassword = config.passwordEncoder().encode(config.user_password);
    }
    
    // createStudent();
    public void saveStudent(){
        Student tempStudent = new Student("Pansy", "Bran", "pansy@comp.com");
        User tempUser = new User("Pansy", encodedPassword, true);
        Role role = new Role("ROLE_ADMIN");

        studentDAO.save(tempStudent);

        usersDAO.save(tempUser);
        usersDAO.save(role);

        usersDAO.saveUsersRoles(tempUser, role);
    }

    // createMultipleStudents();
    public void saveMultipleStudents(){
        Student tempStudent2 = new Student("Lily", "Public", "lily@comp.com");
        Student tempStudent3 = new Student("Bonita", "Applebum", "bonita@comp.com");

        User tempUser2 = new User("Lily", encodedPassword, true);
        Role role2 = new Role("ROLE_STU");

        User tempUser3 = new User("Bonita", encodedPassword, true);
        Role role3 = new Role("ROLE_STU");

        studentDAO.save(tempStudent2);
        studentDAO.save(tempStudent3);

        usersDAO.save(tempUser2);
        usersDAO.save(tempUser3);

        usersDAO.save(role2);
        usersDAO.save(role3);

        usersDAO.saveUsersRoles(tempUser2, role2);
        usersDAO.saveUsersRoles(tempUser3, role3);
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
