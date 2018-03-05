package controllerTests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.DueThisController;
import controller.InvalidInputException;
import model.Application;
import model.Assignment;
import model.Event;
import model.Student;

public class TestDueThisController
{
	String name = "Assignment1";
	String newName = "AssignmentChanged";
	String course = "ECSE428";
	String newCourse = "ECSE429";
	float gradeWeight = (float) 0.4;
	float newGradeWeight = (float) 0.5;

	// Temporary value until the model has been updated
	Duration compTime = Duration.ofHours(5);
	Duration newCompTime = Duration.ofHours(6);

	// This is suppressed since it is easier to create a specific date
	@SuppressWarnings("deprecation")
	Date dueDate = new Date(118, 4, 30);
	@SuppressWarnings("deprecation")
	Date newDueDate = new Date(118, 4, 40);

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
		Application a = Application.getInstance();
		a.delete();
	}

	@Test
	public void testCreateAssignmentNoviceSucceeds()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();

		Student s = createNoviceStudent(app);

		assertEquals(false, s.getExperienced());

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
	}

	@Test
	public void testCreateAssignmentExperiencedSucceeds()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();

		Student s = createExperiencedStudent(app);
		assertEquals(true, s.getExperienced());

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
	}

	@Test
	public void testCreateAssignmentNoName()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(false, s.getExperienced());

		try
		{
			dtc.createAssignment(null, course, dueDate, gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a valid assignment name! ", error);
		assertEquals(0, s.getAssignments().size());
	}

	@Test
	public void testCreateAssignmentNoCourse()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(false, s.getExperienced());

		try
		{
			dtc.createAssignment(name, null, dueDate, gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a valid course name! ", error);
		assertEquals(0, s.getAssignments().size());
	}

	@Test
	public void testCreateAssignmentNoDueDate()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(false, s.getExperienced());

		try
		{
			dtc.createAssignment(name, course, null, gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Due data cannot be empty! ", error);
		assertEquals(0, s.getAssignments().size());
	}

	@Test
	public void testCreateAssignmentPastDueDate()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(false, s.getExperienced());

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
	}

	@Test
	public void testCreateAssignmentNoviceNegativeFloat()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(false, s.getExperienced());

		try
		{
			dtc.createAssignment(name, course, dueDate, -gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a positive grade weight! ", error);
		assertEquals(0, s.getAssignments().size());
	}

	@Test
	public void testCreateAssignmentNoStudent()
	{
		Application app = Application.getInstance();
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
	public void testCreateAssignmentExperiencedStudentNoCompTime()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent(app);

		try
		{
			dtc.createAssignment(name, course, dueDate, 0, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter an estimated completion time! ", error);
	}

	@Test
	public void testCreateAssignmentExperiencedStudentNegCompTime()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent(app);

		try
		{
			dtc.createAssignment(name, course, dueDate, 0, compTime.negated(), s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a positive estimated completion time! ", error);
	}

	@Test
	public void testEditAssignmentNoviceStudent()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		Assignment a1 = createAssignment(s, app);
		String id1 = s.getAssignment(0).getId();
		
		try
		{
			dtc.editAssignment(s.getAssignment(0), newName, newCourse, newDueDate, newGradeWeight, newCompTime, s);
		} catch (InvalidInputException e)
		{
			fail();
		}
		
		assertEquals(1, s.getAssignments().size());
		assertEquals(newName, s.getAssignment(0).getName());
		assertEquals(newCourse, s.getAssignment(0).getCourse());
		assertEquals(newDueDate, s.getAssignment(0).getDueDate());
		assertEquals(newGradeWeight, s.getAssignment(0).getGradeWeight(),0.0f);
		assertEquals(false, s.getAssignment(0).isIsCompleted());
		assertEquals(id1, s.getAssignment(0).getId());
	}
	
	@Test
	public void testEditAssignmentExperiencedStudent()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent(app);
		Assignment a1 = createAssignment(s, app);
		String id1 = s.getAssignment(0).getId();

		try
		{
			dtc.editAssignment(s.getAssignment(0), newName, newCourse, newDueDate, newGradeWeight, newCompTime, s);
		} catch (InvalidInputException e)
		{
			fail();
		}
		
		assertEquals(1, s.getAssignments().size());
		assertEquals(newName, s.getAssignment(0).getName());
		assertEquals(newCourse, s.getAssignment(0).getCourse());
		assertEquals(newDueDate, s.getAssignment(0).getDueDate());
		assertEquals(newCompTime, s.getAssignment(0).getCompletionTime());
		assertEquals(false, s.getAssignment(0).isIsCompleted());
		assertEquals(id1, s.getAssignment(0).getId());
	}
	
	@Test
	public void testEditAssignmentNoviceInvalidInputs()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		Assignment a1 = createAssignment(s, app);
		String id1 = s.getAssignment(0).getId();
		
		@SuppressWarnings("deprecation")
		Date d1 = new Date(0, 1, 1);
		
		try
		{
			dtc.editAssignment(s.getAssignment(0), null, "", d1, -gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}
		
		assertEquals("Please enter a valid assignment name! Please enter a valid course name! Due date must be in the future! Please enter a positive grade weight! ", error);
	}
	
	@Test
	public void testEditAssignmentExperiencedInvalidInputs()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent(app);
		Assignment a1 = createAssignment(s, app);
		String id1 = s.getAssignment(0).getId();
		
		@SuppressWarnings("deprecation")
		Date d1 = new Date(0, 1, 1);
		
		try
		{
			dtc.editAssignment(s.getAssignment(0), null, "", d1, -gradeWeight, null, s);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}
		
		assertEquals("Please enter a valid assignment name! Please enter a valid course name! Due date must be in the future! Please enter an estimated completion time! ", error);
	}
	
	@Test
	public void testCompleteAssignmentFalseToTrue()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent(app);
		Assignment a1 = createAssignment(s, app);
		a1.setIsCompleted(false);
		
		try{
			dtc.completeAssignment(s, a1);
		}
		catch(InvalidInputException e)
		{
			fail();
		}
		
		assertEquals(true, a1.getIsCompleted());
	}
	
	@Test
	public void testCompleteAssignmentTrueToFalse()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent(app);
		Assignment a1 = createAssignment(s, app);
		a1.setIsCompleted(true);
		
		try{
			dtc.completeAssignment(s, a1);
		}
		catch(InvalidInputException e)
		{
			fail();
		}
		
		assertEquals(false, a1.getIsCompleted());
	}
	
	@Test
	public void testCompleteAssignmentError()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent(app);
		Student s2 = createExperiencedStudent2(app);
		Assignment a1 = createAssignment(s, app);
		a1.setIsCompleted(true);
		
		try{
			dtc.completeAssignment(s2, a1);
		}
		catch(InvalidInputException e)
		{
			error = e.getMessage();
		}
		
		assertEquals("This assignment does not belong to this student! ", error);
	}

	@Test
	public void testRemoveAssignment()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		
		Student ns = createNoviceStudent(app);
		Student es = createExperiencedStudent(app);

		assertEquals(0, ns.numberOfAssignments());
		assertEquals(0, es.numberOfAssignments());

		Assignment a1 = createAssignment(ns, app);
		Assignment a2 = createAssignment(es, app);

		assertEquals(1, ns.numberOfAssignments());
		assertEquals(1, es.numberOfAssignments());

		try
		{
			dtc.removeAssignment(ns, a1);
			dtc.removeAssignment(es, a2);

		} catch (InvalidInputException e)
		{
			fail();
		}

		assertEquals(0, ns.numberOfAssignments());
		assertEquals(0, es.numberOfAssignments());
	}

	@Test
	public void testRemoveUnownedAssignment()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		
		Student ns = createNoviceStudent(app);
		Student es = createExperiencedStudent(app);

		assertEquals(0, ns.numberOfAssignments());
		assertEquals(0, es.numberOfAssignments());

		Assignment a1 = createAssignment(ns, app);
		Assignment a2 = createAssignment(es, app);

		assertEquals(1, ns.numberOfAssignments());
		assertEquals(1, es.numberOfAssignments());


		try
		{
			dtc.removeAssignment(ns, a2);
		} catch (InvalidInputException e)
		{
			assertEquals("This assignment does not belong to this student! ", e.getMessage());
		}

		try
		{
			dtc.removeAssignment(es, a1);
		} catch (InvalidInputException e)
		{
			assertEquals("This assignment does not belong to this student! ", e.getMessage());
		}

		assertEquals(1, ns.numberOfAssignments());
		assertEquals(1, es.numberOfAssignments());

	}

	@Test
	public void testCreateEvent()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();

		Student s = createNoviceStudent(app);

		assertEquals(s.numberOfEvents(), 0);

		try
		{
			dtc.createEvent(s, name, dueDate, startTime, endTime, true);
		} catch (InvalidInputException e)
		{
			fail();
		}

		assertEquals(s.numberOfEvents(), 1);
		assertEquals(s.getEvent(0).getName(), name);
		assertEquals(s.getEvent(0).getDate().toString(), dueDate.toString());
		assertEquals(s.getEvent(0).getStartTime().toString(), startTime.toString());
		assertEquals(s.getEvent(0).getEndTime().toString(), endTime.toString());
		assertEquals(s.getEvent(0).getRepeatedWeekly(), true);
	}

	@Test
	public void testCreateEventNoStudent()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		try
		{
			dtc.createEvent(null, name, dueDate, startTime, endTime, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "No student is associated to this event! ");
	}

	@Test
	public void testCreateEventNoName()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		Student s = createNoviceStudent(app);
		String error = "";

		assertEquals(s.numberOfEvents(), 0);

		// Test for name being null
		try
		{
			dtc.createEvent(s, null, dueDate, startTime, endTime, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "Please enter a valid name! ");
		assertEquals(s.numberOfEvents(), 0);

		error = "";
		assertEquals(error, "");

		// Test for name being empty
		try
		{
			dtc.createEvent(s, "", dueDate, startTime, endTime, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "Please enter a valid name! ");
		assertEquals(s.numberOfEvents(), 0);

		error = "";
		assertEquals(error, "");

		// Test for name being empty spaces
		try
		{
			dtc.createEvent(s, "    ", dueDate, startTime, endTime, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "Please enter a valid name! ");
		assertEquals(s.numberOfEvents(), 0);
	}

	@Test
	public void testCreateEventNoDate()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		Student s = createNoviceStudent(app);
		String error = "";

		assertEquals(s.numberOfEvents(), 0);

		try
		{
			dtc.createEvent(s, name, null, startTime, endTime, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "Date cannot be empty! ");
		assertEquals(s.numberOfEvents(), 0);
	}

	@Test
	public void testCreatePastEvent()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(s.numberOfEvents(), 0);

		@SuppressWarnings("deprecation")
		Date d1 = new Date(0, 1, 1);

		try
		{
			dtc.createEvent(s, name, d1, startTime, endTime, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "Date must be in the future! ");
		assertEquals(s.numberOfEvents(), 0);
	}

	@Test
	public void testCreateEventNoStartTime()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(s.numberOfEvents(), 0);

		try
		{
			dtc.createEvent(s, name, dueDate, null, endTime, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "Start time cannot be empty! ");
		assertEquals(s.numberOfEvents(), 0);
	}

	@Test
	public void testCreateEventNoEndTime()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(s.numberOfEvents(), 0);

		try
		{
			dtc.createEvent(s, name, dueDate, startTime, null, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "End time cannot be empty! ");
		assertEquals(s.numberOfEvents(), 0);
	}

	@Test
	public void testCreateEventEndBeforeStart()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent(app);
		assertEquals(s.numberOfEvents(), 0);

		try
		{
			dtc.createEvent(s, name, dueDate, endTime, startTime, true);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals(error, "Start time must be before end time! ");
		assertEquals(s.numberOfEvents(), 0);
	}

	////////////////////////////////////////////////////////////////////////////////////////
	@Test
	public void testEditEvent()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();

		Student ns = createNoviceStudent(app);
		Event event = createEvent(ns, app);
		
		@SuppressWarnings("deprecation")
		Date date = new Date(119, 4, 30);
		@SuppressWarnings("deprecation")
		Time starttime = new Time(10, 30, 0);
		@SuppressWarnings("deprecation")
		Time endtime = new Time(11, 30, 0);

		String error = "";
		try
		{
			dtc.editEvent(event, "class", date, starttime, endtime, true);
		} catch (InvalidInputException e)
		{
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
	public void testEditEventEndBeforeStart()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		Student ns = createNoviceStudent(app);
		Event event = createEvent(ns, app);
		
		@SuppressWarnings("deprecation")
		Time endtime = new Time(7, 30, 0);

		String error = "";
		try
		{
			dtc.editEvent(event, name, dueDate, startTime, endtime, repeatedWeekly);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Start time must be before end time! ", error);
	}

	@Test
	public void testEditEventInvalidName()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		Student ns = createNoviceStudent(app);
		Event event = createEvent(ns, app);

		String error = "";
		try
		{
			dtc.editEvent(event, "", dueDate, startTime, endTime, repeatedWeekly);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Please enter a valid name! ", error);
	}

	@Test
	public void testEditEventNullStartTime()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		Student ns = createNoviceStudent(app);
		Event event = createEvent(ns, app);

		String error = "";
		try
		{
			dtc.editEvent(event, name, dueDate, null, endTime, repeatedWeekly);
		} catch (InvalidInputException e)
		{
			error = e.getMessage();
		}

		assertEquals("Start time cannot be empty! ", error);
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	//Testing the updateAvailabilitiesMethod
	
	@Test
	public void testUpdateAvailabilitiesGoodInput() {
		//*******UNDER CONSTRUCTION STILL******
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		int sun=1;
		int mon=0;
		int tue=24;
		int wed=23;
		int thu=2;
		int fri=7;
		int sat=9;	
		
		try{
			dtc.updateAvailabilities(ns, sun, mon, tue, wed, thu, fri, sat);
		} catch(InvalidInputException e){
			error = e.getMessage();
		}
		
		// To access the methods in the ExperiencedStudent class
		ExperiencedStudent aExpStud = (ExperiencedStudent)ns.getStudentRole(0);
		int sunActual =  aExpStud.getSundayAvailability();
		int monActual =  aExpStud.getMondayAvailability();
		int tueActual =  aExpStud.getTuesdayAvailability();
		int wedActual =  aExpStud.getWednesdayAvailability();
		int thuActual =  aExpStud.getThursdayAvailability();
		int friActual =  aExpStud.getFridayAvailability();
		int satActual =  aExpStud.getSaturdayAvailability();
		
		
		assertEquals("", error);
		assertEquals(sun, sunActual);
		assertEquals(mon, monActual);
		assertEquals(tue, tueActual);
		assertEquals(wed, wedActual);
		assertEquals(thu, thuActual);
		assertEquals(fri, friActual);
		assertEquals(sat, satActual);
		
	}
	
	
	@Test
	public void testUpdateAvailabilitiesNoStudent() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(null, 2, 2, 2, 2, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("No student was input into the updateAvailabilities method.", error);
	}
	
	@Test
	public void testUpdateAvailabilitiesWrongRole() {
		Student ns = createNoviceStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, 2, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Only experienced students can set availabilities.", error);
		
	}
	
	// Sunday
	@Test
	public void testUpdateAvailabilitiesSundayHigh() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 25, 2, 2, 2, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Sunday hours must be between 0 and 24! ", error);
	}
	@Test
	public void testUpdateAvailabilitiesSundayLow() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, -1, 2, 2, 2, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Sunday hours must be between 0 and 24! ", error);
	}
	
	// Monday
	@Test
	public void testUpdateAvailabilitiesMondayHigh() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 25, 2, 2, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Monday hours must be between 0 and 24! ", error);
	}
	@Test
	public void testUpdateAvailabilitiesMondayLow() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, -1, 2, 2, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Monday hours must be between 0 and 24! ", error);
	}
	
	// Tuesday
	@Test
	public void testUpdateAvailabilitiesTuesdayHigh() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 25, 2, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Tuesday hours must be between 0 and 24! ", error);
	}
	@Test
	public void testUpdateAvailabilitiesTuesdayLow() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, -1, 2, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Tuesday hours must be between 0 and 24! ", error);
	}
	
	// Wednesday
	@Test
	public void testUpdateAvailabilitiesWednesdayHigh() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, 25, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Wednesday hours must be between 0 and 24! ", error);
	}
	@Test
	public void testUpdateAvailabilitiesWednesdayLow() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, -1, 2, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Wednesday hours must be between 0 and 24! ", error);
	}
	
	// Thursday
	@Test
	public void testUpdateAvailabilitiesThursdayHigh() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, 2, 25, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Thursday hours must be between 0 and 24! ", error);
	}
	@Test
	public void testUpdateAvailabilitiesThursdayLow() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, 2, -1, 2, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Thursday hours must be between 0 and 24! ", error);
	}
	
	// Friday
	@Test
	public void testUpdateAvailabilitiesFridayHigh() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, 2, 2, 25, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Friday hours must be between 0 and 24! ", error);
	}
	@Test
	public void testUpdateAvailabilitiesFridayLow() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, 2, 2, -1, 2);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Friday hours must be between 0 and 24! ", error);
	}
	
	// Saturday
	@Test
	public void testUpdateAvailabilitiesSaturdayHigh() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, 2, 2, 2, 25);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Saturday hours must be between 0 and 24! ", error);
	}
	@Test
	public void testUpdateAvailabilitiesSaturdayLow() {
		Student ns = createExperiencedStudent();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		try {
			dtc.updateAvailabilities(ns, 2, 2, 2, 2, 2, 2, -1);
		} catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Saturday hours must be between 0 and 24! ", error);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////

	@Test
	public void testRemoveEventSuccess(){
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		
		Student ns = createNoviceStudent(app);
		Event event = createEvent(ns, app);
		
		assertEquals(event, ns.getEvent(0));
		assertEquals(1, ns.getEvents().size());
		
		try{
			dtc.removeEvent(ns, event);
		}catch(InvalidInputException e)
		{
			fail();
		}
		
		assertEquals(0, ns.getEvents().size());	
	}
	
	@Test
	public void testRemoveEventNotOwned(){
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		
		String error = "";
		Student ns = createNoviceStudent(app);
		Student es = createExperiencedStudent(app);
		Event event = createEvent(ns, app);
		
		assertEquals(event, ns.getEvent(0));
		assertEquals(1, ns.getEvents().size());
		
		try{
			dtc.removeEvent(es, event);
		}catch(InvalidInputException e)
		{
			error = e.getMessage();
		}	
		assertEquals("This event does not belong to this student! ", error);
	}
	
	@Test
	public void testShowEvent()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		
		@SuppressWarnings("deprecation")
		Date testDate = new Date(120, 5, 25);
		
		Student ns = createNoviceStudent(app);
		Event event = createEvent(ns, app);
		Event event2 = createEvent(ns, app);
		Event event3 = createEvent(ns, app);
		Event event4 = createEvent(ns, app);
		Event event5 = createEventDetailed(ns, testDate, app);
		
		List<Event> list = dtc.showEvent(ns, dueDate);
		
		assertEquals(4, list.size());
		assertEquals(event, list.get(0));
		assertEquals(event2, list.get(1));
		assertEquals(event3, list.get(2));
		assertEquals(event4, list.get(3));
		//assertEquals(event5, list.get(4));
	}
	
	@Test
	public void testShowAssignment()
	{
		Application app = Application.getInstance();
		DueThisController dtc = new DueThisController();
		
		@SuppressWarnings("deprecation")
		Date testDate = new Date(120, 5, 25);
		
		Student ns = createNoviceStudent(app);
		Assignment assignment = createAssignment(ns, app);
		Assignment assignment2 = createAssignment(ns, app);
		Assignment assignment3 = createAssignment(ns, app);
		Assignment assignment4 = createAssignment(ns, app);
		Assignment assignment5 = createAssignmentDetailed(ns, testDate, app);
		
		List<Assignment> list = dtc.showAssignment(ns, dueDate);
		
		assertEquals(4, list.size());
		assertEquals(assignment, list.get(0));
		assertEquals(assignment2, list.get(1));
		assertEquals(assignment3, list.get(2));
		assertEquals(assignment4, list.get(3));
	}

	private Student createNoviceStudent(Application app)
	{
		Student s = new Student("testId", "Richard Potato", "food", "a@b.c", false, 0, 0, 0, 0, 0, 0, 0, app);
		return s;
	}

	private Student createExperiencedStudent(Application app)
	{
		Student s = new Student("testId", "Richard Potato", "food", "a@b.c", true, 5, 5, 5, 5, 5, 5, 5, app);
		return s;
	}
	
	private Student createExperiencedStudent2(Application app)
	{
		Student s = new Student("UID", "Ichard Potato", "nope", "a@b.c", true, 5, 5, 5, 5, 5, 5, 5, app);
		return s;
	}

	private Assignment createAssignment(Student s, Application app)
	{
		return new Assignment("testId", name, course, dueDate, isCompleted, gradeWeight, compTime, s, app);
	}

	private Event createEvent(Student s, Application app)
	{
		return new Event("testId", name, dueDate, startTime, endTime, repeatedWeekly, s, app);
	}
	
	private Event createEventDetailed(Student s, Date date, Application app) {
		return new Event("testId", name, date, startTime, endTime, repeatedWeekly, s, app);
	}
	
	private Assignment createAssignmentDetailed(Student s, Date date, Application app) {
		return new Assignment("testId", name, course, date, isCompleted, gradeWeight, compTime, s, app);
	}
	
}
