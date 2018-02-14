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
	public void testCreateEvent() {
		DueThisController dtc = new DueThisController();
		
		Student s = createNoviceStudent();
		
		assertEquals(s.numberOfEvents(), 0);
		
		try {
			dtc.createEvent(s, name, dueDate, startTime, endTime, true);
		}
		catch (InvalidInputException e) {
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
	public void testCreateEventNoStudent() {
		DueThisController dtc = new DueThisController();
		String error = "";
		
		try {
			dtc.createEvent(null, name, dueDate, startTime, endTime, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "No student is associated to this event! ");
	}
	
	@Test
	public void testCreateEventNoName() {
		DueThisController dtc = new DueThisController();
		Student s = createNoviceStudent();
		String error = "";
		
		assertEquals(s.numberOfEvents(), 0);
		
		//Test for name being null
		try {
			dtc.createEvent(s, null, dueDate, startTime, endTime, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Please enter a valid name! ");
		assertEquals(s.numberOfEvents(), 0);
		
		error = "";
		assertEquals(error, "");
		
		//Test for name being empty
		try {
			dtc.createEvent(s, "", dueDate, startTime, endTime, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Please enter a valid name! ");
		assertEquals(s.numberOfEvents(), 0);
		
		error = "";
		assertEquals(error, "");
		
		//Test for name being empty spaces
		try {
			dtc.createEvent(s, "    ", dueDate, startTime, endTime, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Please enter a valid name! ");
		assertEquals(s.numberOfEvents(), 0);
	}
	
	@Test
	public void testCreateEventNoDate() {
		DueThisController dtc = new DueThisController();
		Student s = createNoviceStudent();
		String error = "";
		
		assertEquals(s.numberOfEvents(), 0);
		
		try {
			dtc.createEvent(s, name, null, startTime, endTime, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Date cannot be empty! ");
		assertEquals(s.numberOfEvents(), 0);
	}
	
	@Test
	public void testCreatePastEvent() {
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(s.numberOfEvents(), 0);

		@SuppressWarnings("deprecation")
		Date d1 = new Date(0, 1, 1);
		
		try {
			dtc.createEvent(s, name, d1, startTime, endTime, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Date must be in the future! ");
		assertEquals(s.numberOfEvents(), 0);
	}
	
	@Test
	public void testCreateEventNoStartTime() {
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(s.numberOfEvents(), 0);

		
		try {
			dtc.createEvent(s, name, dueDate, null, endTime, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Start time cannot be empty! ");
		assertEquals(s.numberOfEvents(), 0);
	}
	
	@Test
	public void testCreateEventNoEndTime() {
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(s.numberOfEvents(), 0);

		
		try {
			dtc.createEvent(s, name, dueDate, startTime, null, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "End time cannot be empty! ");
		assertEquals(s.numberOfEvents(), 0);
	}
	
	@Test
	public void testCreateEventEndBeforeStart() {
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		assertEquals(s.numberOfEvents(), 0);

		
		try {
			dtc.createEvent(s, name, dueDate, endTime, startTime, true);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Start time must be before end time! ");
		assertEquals(s.numberOfEvents(), 0);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////
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
	
	@Test
	public void testEditEventNullStartTime() {
		
		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();
		
		String error ="";
		try {
			dtc.editEvent(event, name, dueDate, null, endTime, repeatedWeekly);
		} catch (InvalidInputException e) {
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
		
		assertEquals("", error);
		//assertEquals(sun, ns.); //want to -->  getSundayHours(); but idk how to access experienced student object
		//assertEquals(mon, ns.);
		//assertEquals(tue, ns.);
		//assertEquals(wed, ns.);
		//assertEquals(thu, ns.);
		//assertEquals(fri, ns.);
		//assertEquals(sat, ns.);
		
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
	private Student createNoviceStudent()
	{
		Student s = new Student("testId", "Richard Potato");
		new NoviceStudent(s);
		return s;
	}

	private Student createExperiencedStudent()
	{
		Student s = new Student("testId", "Richard Potato");
		new ExperiencedStudent(s, 0, 0, 0, 0, 0, 0, 0);
		return s;
	}
	
	private Assignment createAssignment(Student s) {
		return new Assignment("testId", name, course, dueDate, gradeWeight, isCompleted, compTime, s);
	}
	
	private Event createEvent(Student s) {
		return new Event("testId", name, dueDate, startTime, endTime, repeatedWeekly, s);
	}
	
}
