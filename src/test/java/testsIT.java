import com.fasterxml.jackson.core.JsonProcessingException;
import com.moger.crudproject.dto.ErrorResponse;
import com.moger.crudproject.entity.Student;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import java.util.List;
import static org.junit.Assert.*;


@SpringBootTest
public class testsIT {

    @Test
    @Order(1)
    public void testGetStudentById() {

        TestRestTemplate testRestTemplate = new TestRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setBasicAuth("username", "password");
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = "http://localhost:8080/students/{id}";

        ResponseEntity<Student> res = testRestTemplate.exchange(url, HttpMethod.GET, request, Student.class, 2);// Path variable for {id}

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        System.out.println(res.getBody());
    }

    @Test
    @Order(2)
    public void testGetAllStudents() {

        TestRestTemplate testRestTemplate = new TestRestTemplate();

        String baseUrl = "http://localhost:8080/students";
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        //assertEquals(5, response.getBody().size());
    }

    @Test
    @Order(3)
    public void testSaveStudent() {

        Student student = buildStudent();
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Student> request = new HttpEntity<>(student, headers);
        String url = "http://localhost:8080/students";

        ResponseEntity<String> res = testRestTemplate.exchange(url, HttpMethod.POST, request, String.class);

        //ResponseEntity<String> res = testRestTemplate.postForEntity(url, student, String.class);//res type
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        System.out.println(res.getBody());
    }

    @Test
    @Order(4)
    public void testUpdateStudent() {

        TestRestTemplate restTemplate = new TestRestTemplate();
        String baseUrl = "http://localhost:8080/students/2";

        Student updatedStudent= buildStudent();
        restTemplate.put(baseUrl, updatedStudent);

        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl, Student.class);
        assertEquals("test", response.getBody().getFirstName());
    }

    @Test
    @Order(5)
    public void testDeleteStudent() throws JsonProcessingException {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String baseUrl = "http://localhost:8080/students/1";

        restTemplate.delete(baseUrl);

        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(baseUrl, ErrorResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Student with id 1 does not exist!", response.getBody().getMessage());
    }

    private Student buildStudent() {
        Student theStudent = new Student("test", "123",12,"test@comp.com", "stu_Chemistry");
        theStudent.setId(1);
        return theStudent;
    }
}
