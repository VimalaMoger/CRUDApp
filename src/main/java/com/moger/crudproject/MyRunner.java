
package com.moger.crudproject;

import com.moger.crudproject.helperUtilities.Dataprovider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyRunner.class);

    private final Dataprovider dataprovider;;

    @Autowired
    public MyRunner(Dataprovider dataprovider){
        this.dataprovider = dataprovider;
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("Add a student");
        dataprovider.saveStudent();

        logger.info("Adding multiple student");
        dataprovider.saveMultipleStudents();

        logger.info("Find student by id 1");
        dataprovider.findStudentById();

        logger.info("Find student by lastname");
		dataprovider.findStudentByLastName();

        logger.info("Find all students from student table");
		//dataprovider.findAllStudents();

		logger.info("Update a student");
		//dataprovider.updateStudent();

		logger.info("Delete a student");
		//dataprovider.deleteStudent();

		logger.info("Delete all");
		//dataprovider.deleteALLStudent();

    }
}

