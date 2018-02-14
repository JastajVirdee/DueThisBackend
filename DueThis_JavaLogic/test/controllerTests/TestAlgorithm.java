package controllerTests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.util.Calendar;

import model.Assignment;
import model.ExperiencedStudent;
import model.Student;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAlgorithm {
	
	String name = "Assignment1";
	String course = "ECSE428";
	float gradeWeight = (float) 0.4;

	// Temporary value until the model has been updated
	Duration compTime = Duration.ofHours(5);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		

		
		
		
		
	}

	@After
	public void tearDown() throws Exception {
	}


	
	private Student createExperiencedStudent()
	{
		Student s = new Student("testId", "Richard Potato");
		new ExperiencedStudent(s, 0, 0, 0, 0, 0, 0, 0);
		return s;
	}

}
