package com.moger.crudproject;

import com.moger.crudproject.dao.StudentDAO;
import com.moger.crudproject.entity.Student;
import com.moger.crudproject.serviceImpl.StudentDAOImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


//Using DataJpaTest annotation
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentDAOImplTests {

    @Autowired
    private EntityManager entityManager;

    private StudentDAO studentDao;

    @BeforeEach
    void beginEachTestWith(){
        studentDao = new StudentDAOImpl(entityManager);
    }

    @Test
    @Order(1)
    void injectedComponentsAreNotNull(){

        assertThat(entityManager).isNotNull();
        assertThat(studentDao).isNotNull();
    }

    @Test
    @Order(2)
    void save() {

        entityManager.persist(new Student("Sunny", "Flask",12, "sunny@comp.com", "stu_Chemistry"));
        entityManager.persist(new Student("Josh", "Molly",12, "josh@comp.com", "stu_Chemistry"));
        List<Student> students = studentDao.getAllStudents();

        assertEquals(2, students.size());
        assertThat(students).extracting(Student::getFirstName).containsOnly("Sunny", "Josh");
    }

    @Test
    @Order(3)
    @Sql(scripts={"classpath:createStudent.sql"})
    void getAllStudents() {

        TypedQuery<Student> students = entityManager.createQuery("From Student", Student.class);

        assertEquals(2, students.getResultList().size());
        assertThat(students.getResultList()).extracting(Student::getFirstName).containsOnly("Sunny", "Josh");
    }

    @Test
    @Order(4)
    @Sql(scripts={"classpath:createStudent.sql"})
    void findStudentById() {
        Long id = 1L;

        Student student= entityManager.find(Student.class, id);

        Student student1 = studentDao.findStudentById(id);

        assertEquals(student, student1);
    }

    @Test
    @Order(5)
    @Sql(scripts={"classpath:createStudent.sql"})
    void findStudentByLastName() {

        TypedQuery<Student> student = entityManager.createQuery("From Student where lastName=:name", Student.class);
        // name is the named parameter
        student.setParameter("name", "Flask");
        Student theStudent = student.getSingleResult();
        Student student1 = studentDao.findStudentByLastName("Flask");

        assertEquals(theStudent.getFirstName(), student1.getFirstName());
    }

    @Test
    @Order(6)
    @Sql(scripts={"classpath:createStudent.sql"})
    void updateStudent() {

        Student student = new Student("Joshua", "Mosh", 12, "joshua@comp.com", "stu_Chemistry");
        Student student1 = entityManager.merge(student);

        assertEquals(student.getFirstName(), student1.getFirstName());
    }

    @Test
    @Order(7)
    @Sql(scripts={"classpath:createStudent.sql"})
    void deleteStudent() {

        Long id = 1L;
        Student student = entityManager.find(Student.class, id);
        if(student != null)
            entityManager.remove(student);

        assertEquals(false, entityManager.contains(student));
    }

    @Test
    @Order(8)
    @Sql(scripts={"classpath:createStudent.sql"})
    void deleteAllStudents() {

        int rowsAffected = entityManager.createQuery("DELETE FROM Student").executeUpdate();

        assertEquals(2, rowsAffected);
    }
}