# Project Contents

Basic CRUD actions on Student table and created MyRunner that implements CommandLineRunner to run sql insert queries when
application starts. 


### Dependencies needed to run the project
    - spring-boot (starter-web, data-jpa, starter-test, starter-security, starter-thymeleaf), 
    - springdoc-openapi-starter-webmvc-ui, lombok, spring-boot-starter-validation, hibernate-validator, mysql-connector, h2 database

#### Tasks

* [x] Creation of Entity named Student, User, and Role 
* [x] DAO, service and controller implementation
* [x] Accessing data with MySQL 
* [x] Accessing Data with JPA - EntityManager to interact with the persistence context(crud actions)
* [x] Testing JPA and service layer
* [x] REST API calls over HTTP
* [x] Exception handlers
* [x] Externalized the jdbc configuration using environment variables in Properties file
* [x] Used @Value annotation to inject environment variable(user password) into SecurityConfig class 
* [x] RESTAPI Security-restrict endpoints based on user roles
* [x] Spring MVC Validation - Required fields, @InitBinder, Number range, Regex, Custom messages, Custom
        Java Annotation class, Custom Validation rule

### Used cURL to send HTTP requests

*Included security credentials in cURL request* examples

Get

```
curl -u lily:test localhost:8080/students/2

{"id":2,"firstName":"Lily","lastName":"Public","age":12,"email":"lily@comp.com","major":"stu_Chemistry"}
```
Get request: error response

```
curl -u lily:test localhost:8080/students/10

{"status":404,"message":"Student with id 10 does not exist!","time":1759173171360}
```

Post

```
curl -u lily:test localhost:8080/students --json "{\"firstName\":\"Sidney\", \"lastName\":\"Royal\", \"age\":\"12\", \"email\":\"sid@comp.com\", \"major\":\"stu_Physics\"}"

Created student with id 4
```

Put

```
curl -u lily:test localhost:8080/students/4   -X PUT --json "{\"firstName\":\"Sidney\", \"lastName\":\"Roy\", \"age\":\"19\", \"email\":\"sid@comp.com\", \"major\":\"stu_Social science\"}"

Student{id=4, firstName='Sidney', lastName='Royal', age=12, email='sid@comp.com', major='stu_Physics'} now changed to Student{id=4, firstName='Sidney', lastName='Roy', age=19, email='sid@comp.com', major='stu_Social science'}
```

Delete

```
curl -u pansy:test localhost:8080/students/1 -X DELETE
 
 Student with id 1 deleted
 ```

curl -u pansy:test localhost:8080/students -X DELETE
```
3 records removed
```

### Tests - service logic is tested in conjunction with the repository layer
- @DataJpaTest

    ```
        Manually instantiated StudentDAO and student objects in test class
        Added H2 database to test classpath
        Ran SQL script using @Sql 
  
    ```   
  ![test](assets/testResult.png)


<br>
<br>
<br>

#### Lessons learned

> In Windows cmd, double quotes are used to wrap json data, backslash(\\) is used to escape double quotes in  **cURL** request(if key or value contains special characters (like quotes or spaces))

> Difference between 401 and 403
```
    401 Unauthorized: The client is not authenticated. The server expects the client to provide valid credentials.(client uses authentication header)
    403 Forbidden: The client is authenticated but does not have permission to access the resource. 
``` 

> TypedQuery are designed for SELECT queries which return results
```
    UPDATE queries do not return results. Instead of using a TypedQuery, we can use a standard Query and execute it with executeUpdate().
```

> @DataJpaTest doesn’t load service bean, if needed, can manually instantiate the service class in the test setup.

> @DataJpaTest includes @Transactional by default


> Difference between spring.jpa.hibernate.ddl-auto: create and create-drop
```
    create: It drops all the tables first then creates new schema when the application starts. The schema saved after the application stops.
    create-drop: Similar to create, it drops all the tables first then creates new schema when the application starts and drops again when application ends. The schema is removed when the application stops.
```  

> Update and Validate:
```
    Update: It does not create or drop any table, but uses the existing tables to add columns or constraints. The schema saved after the application stops.
    Validate - Hibernate checks the database schema against the entity mappings but does not modify or create the schema
```

> Difference between persist() and merge() in Persistence Context
```
    Merge returns the managed instance that the state was merged with. Any changes made after the merge is not part of transaction unless we call merge again
    Persist takes an entity instance, adds it to the context and makes that instance managed (i.e. future updates to the entity will be tracked)
```  

> @ControllerAdvice annotation was first introduced in Spring 3.2 

> The difference between using ResponseEntity and a simple class object as a return value
```
    ResponseEntity allows us to customize the HTTP Responses like error message, staus code and so on. It gives us complete control over what is sent back to the client. 
    A simple class object return suffices to default HTTP behavior, limited to default error handling, no customizable
```  

> How to reset shared state between tests - error tests pass individually but not when run together
```
    Add the @DirtiesContext annotation, but provide it with the AFTER_EACH_TEST_METHOD classMode
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
```

> How to retrieve id from row saved in em.persist(entity)  EntityManager em
```
    The ID is only guaranteed to be generated at flush time. Persisting an entity only makes it "attached" to the persistence context. So, either flush the entity manager explicitly:
    Ex:
          em.persist(student);
          em.flush();
          return student.getId();
          or return the entity itself rather than its ID
```
  


  
