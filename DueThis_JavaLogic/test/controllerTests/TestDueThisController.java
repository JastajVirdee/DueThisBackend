package controllerTests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.DueThisController;
import controller.InvalidInputException;
import model.Assignment;
import model.Event;
import model.ExperiencedStudent;
import model.Student;
import model.NoviceStudent;

public class TestDueThisController
{
	String name = "Assignment1";
	String course = "ECSE428";
	float gradeWeight = (float) 0.4;

	// Temporary value until the model has been updated
	Duration compTime = Duration.ofHours(5);

	// This is suppressed since it is easier to create a specific date
	@SuppressWarnings("deprecation")
	Date dueDate = new Date(118, 4, 30);
	
	boolean isCompleted = false;
	boolean repeatedWeekly = false;
	
	@SuppressWarnings("deprecation")
	Time startTime = new Time(8, 30, 0);
	@SuppressWarnings("deprecation")
	Time endTime = new Time(9, 30, 0);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{

	}

	@Test
	public void testCreateAssignmentNoviceSucceeds()
	{
		DueThisController dtc = new DueThisController();

		Student s = createNoviceStudent();

		assertEquals(model.NoviceStudent.class, s.getStudentRole(0).getClass());

		try
		{
			dtc.createAssignment(name, course, dueDate, gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			fail();
		}

		assertEquals(1, s.getAssignments().size());
		assertEquals(name, s.getAssignment(0).getName());
		assertEquals(course, s.getAssignment(0).getCourse());
		assertEquals(dueDate, s.getAssignment(0).getDueDate());
		assertEquals(gradeWeight, s.getAssignment(0).getGradeWeight(), 0.0f);
		assertEquals(false, s.getAssignment(0).isIsCompleted());
		if (s.getAssignment(0).getId() == null)
		{
			fail();
		}
		s.delete();
	}

	@Test
	public void testCreateAssignmentExperiencedSucceeds()
	{
		DueThisController dtc = new DueThisController();

		Student s = createExperiencedStudent();
		assertEquals(model.ExperiencedStudent.class, s.getStudentRole(0).getClass());

		try
		{
			dtc.createAssignment(name, course, dueDate, 0, compTime, s);
		} catch (InvalidInputException e)
		{
			fail();
		}

		assertEquals(1, s.getAssignments().size());
		assertEquals(name, s.getAssignment(0).getName());
		assertEquals(course, s.getAssignment(0).getCourse());
		assertEquals(dueDate, s.getAssignment(0).getDueDate());
		assertEquals(compTime, s.getAssignment(0).getCompletionTime());
		assertEquals(false, s.getAssignment(0).isIsCompleted());
		if (s.getAssignment(0).getId() == null)
		{
			fail();
		}
		s.delete();
	}

	@Test
	public void testCreateAssignmentNoName()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(model.NoviceStudent.class, s.getStudentRole(0).getClass());

		try
		{
			dtc.createAssignment(null, course, dueDate, gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a valid assignment name! ", error);
		assertEquals(0, s.getAssignments().size());
		s.delete();
	}

	@Test
	public void testCreateAssignmentNoCourse()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(model.NoviceStudent.class, s.getStudentRole(0).getClass());

		try
		{
			dtc.createAssignment(name, null, dueDate, gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a valid course name! ", error);
		assertEquals(0, s.getAssignments().size());
		s.delete();
	}

	@Test
	public void testCreateAssignmentNoDueDate()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(model.NoviceStudent.class, s.getStudentRole(0).getClass());

		try
		{
			dtc.createAssignment(name, course, null, gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Due data cannot be empty! ", error);
		assertEquals(0, s.getAssignments().size());
		s.delete();
	}

	@Test
	public void testCreateAssignmentPastDueDate()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(model.NoviceStudent.class, s.getStudentRole(0).getClass());

		@SuppressWarnings("deprecation")
		Date d1 = new Date(0, 1, 1);

		try
		{
			dtc.createAssignment(name, course, d1, gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Due date must be in the future! ", error);
		assertEquals(0, s.getAssignments().size());
		s.delete();
	}

	@Test
	public void testCreateAssignmentNoviceNegativeFloat()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(model.NoviceStudent.class, s.getStudentRole(0).getClass());

		try
		{
			dtc.createAssignment(name, course, dueDate, -gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a positive grade weight! ", error);
		assertEquals(0, s.getAssignments().size());
		s.delete();
	}

	@Test
	public void testCreateAssignmentNoStudent()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		try
		{
			dtc.createAssignment(name, course, dueDate, -gradeWeight, null, null);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("No student is associated to this assignment! ", error);
	}

	@Test
	public void testCreateAssignmentNoStudentRole()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = new Student("testId", "Richard Potato");
		boolean result = true;

		try
		{
			result = dtc.createAssignment(name, course, dueDate, -gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Student must have a role! ", error);
		s.delete();
	}

	@Test
	public void testCreateAssignmentExperiencedStudentNoCompTime()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent();

		try
		{
			dtc.createAssignment(name, course, dueDate, 0, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter an estimated completion time! ", error);
		s.delete();
	}

	@Test
	public void testCreateAssignmentExperiencedStudentNegCompTime()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent();

		try
		{
			dtc.createAssignment(name, course, dueDate, 0, compTime.negated(), s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a positive estimated completion time! ", error);
		s.delete();
	}
	
	@Test
	public void testRemoveAssignment() {
		Student ns = createNoviceStudent();
		Student es = createExperiencedStudent();
		
		assertEquals(0, ns.numberOfAssignments());
		assertEquals(0, es.numberOfAssignments());
		
		Assignment a1 = createAssignment(ns);
		Assignment a2 = createAssignment(es);
		
		assertEquals(1, ns.numberOfAssignments());
		assertEquals(1, es.numberOfAssignments());
		
		DueThisController dtc = new DueThisController();
		
		try {
			dtc.removeAssignment(ns, a1);
			dtc.removeAssignment(es, a2);
			
		} catch (InvalidInputException e) {
			fail();
		}
		
		assertEquals(0, ns.numberOfAssignments());
		assertEquals(0, es.numberOfAssignments());	
	}
	
	@Test
	public void testRemoveUnownedAssignment() {
		Student ns = createNoviceStudent();
		Student es = createExperiencedStudent();
		
		assertEquals(0, ns.numberOfAssignments());
		assertEquals(0, es.numberOfAssignments());
		
		Assignment a1 = createAssignment(ns);
		Assignment a2 = createAssignment(es);
		
		assertEquals(1, ns.numberOfAssignments());
		assertEquals(1, es.numberOfAssignments());
		
		DueThisController dtc = new DueThisController();
		
		try {
			dtc.removeAssignment(ns, a2);
		} catch (InvalidInputException e) {
			assertEquals("This assignment does not belong to this student! ", e.getMessage());
		}
		
		try {
			dtc.removeAssignment(es, a1);
		} catch (InvalidInputException e) {
			assertEquals("This assignment does not belong to this student! ", e.getMessage());
		}
		
		assertEquals(1, ns.numberOfAssignments());
		assertEquals(1, es.numberOfAssignments());
		
	}
	
	@Test
	public void testEditEvent() {
		
		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();
		@SuppressWarnings("deprecation")
		Date date = new Date(119, 4, 30);
		@SuppressWarnings("deprecation")
		Time starttime = new Time(10, 30, 0);
		@SuppressWarnings("deprecation")
		Time endtime = new Time(11, 30, 0);
		
		String error = "";
		try {
			dtc.editEvent(event, "class", date, starttime, endtime, true);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("", error);
		assertEquals("class", event.getName());
		assertEquals(date, event.getDate());
		assertEquals(starttime, event.getStartTime());
		assertEquals(endtime, event.getEndTime());
		assertEquals(true, event.getRepeatedWeekly());
	}
	
	@Test
	public void testEditEventEndBeforeStart () {
		
		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();
		@SuppressWarnings("deprecation")
		Time endtime = new Time(7, 30, 0);
		
		String error ="";
		try {
			dtc.editEvent(event, name, dueDate, startTime, endtime, repeatedWeekly);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Start time must be before end time! ", error);
	}
	
	@Test
	public void testEditEventInvalidName() {
		
		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();
		
		String error ="";
		try {
			dtc.editEvent(event, "", dueDate, startTime, endTime, repeatedWeekly);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Please enter a valid name! ", error);
	}

	private Student createNoviceStudent()
	{
		Student s = new Student("testId", "Richard Potato");
		new NoviceStudent(s);
		return s;
	}

	private Student createExperiencedStudent()
	{
		Student s = new Student("testId", "Richard Potato");
		new ExperiencedStudent(s);
		return s;
	}
	
	private Assignment createAssignment(Student s) {
		return new Assignment("testId", name, course, dueDate, gradeWeight, isCompleted, compTime, s);
	}
	
	private Event createEvent(Student s) {
		return new Event("testId", name, dueDate, startTime, endTime, repeatedWeekly, s);
	}
	
}
