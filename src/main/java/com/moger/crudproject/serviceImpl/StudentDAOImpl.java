package com.moger.crudproject.serviceImpl;

import com.moger.crudproject.dao.StudentDAO;
import com.moger.crudproject.entity.Student;
import com.moger.crudproject.exception.DataNotFoundException;
import com.moger.crudproject.exception.StudentNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentDAOImpl implements StudentDAO {

    //define field for entity manager
    private final EntityManager entityManager;

    //Inject entity manager using constructor injection
    public StudentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Long save(Student theStudent) {

        String id = String.valueOf(theStudent.getId());
        if (id != null) {
            theStudent = entityManager.merge(theStudent);
        }

        entityManager.persist(theStudent);
        entityManager.flush();  //to retrieve id
        return theStudent.getId();
    }

    @Override
    public Student findStudentById(Long id) {

        Student student = entityManager.find(Student.class, id);
        if(student == null) {
            throw new StudentNotFoundException(String.format("Student with id %d not found", id));
        }
        return student;
    }

    @Override
    public Student findStudentByLastName(String lastName) {

        TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student WHERE lastName=:theLastName", Student.class);

        //set query parameter
        theQuery.setParameter("theLastName", lastName);

        //return query results
        Student student = theQuery.getSingleResult();
        if(student == null)
           throw new StudentNotFoundException("Student not found");
        return student;
    }

    @Override
    //@Transactional
    public List<Student> getAllStudents() {

        //create query
        TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student", Student.class);

        //return query results
        if(theQuery.getResultList().isEmpty())
            throw new DataNotFoundException("Data is not added yet");
        return theQuery.getResultList();
    }

    @Override
    @Transactional
    public int updateStudent(Long id, Student theStudent) {

        //Ensure the ID in the path matches the entity's ID
        Student student = entityManager.find(Student.class, id);
        if(student == null)
            throw new DataNotFoundException(String.format("Student with id %d not found",id));

        Query theQuery = entityManager.createQuery("Update Student s set s.firstName =:fn, s.lastName=:ln, s.age=:age, s.email=:email, s.major=:major WHERE s.id=:id");
        theQuery.setParameter("fn", theStudent.getFirstName());
        theQuery.setParameter("ln", theStudent.getLastName());
        theQuery.setParameter("age", theStudent.getAge());
        theQuery.setParameter("email", theStudent.getEmail());
        theQuery.setParameter("major", theStudent.getMajor());
        theQuery.setParameter("id", id);
        return theQuery.executeUpdate();
    }

    @Override
    @Transactional
    public void deleteStudent(long theId) {

        //retrieve the student
        Student theStudent = entityManager.find(Student.class, theId);

        //delete the student
        if(theStudent == null)
            throw new StudentNotFoundException(String.format("Student with id %d does not exist", theId));
        entityManager.remove(theStudent);
    }

    @Override
    @Transactional
    public int deleteAllStudents() {

        return entityManager.createQuery("DELETE FROM Student").executeUpdate();
    }
}
