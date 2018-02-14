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
	public void testEditAssignmentNoviceStudent()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
		Assignment a1 = createAssignment(s);
		String id1 = s.getAssignment(0).getId();
		
		try
		{
			dtc.editAssignment(s.getAssignment(0), "DueThis Controller", course, dueDate, gradeWeight, compTime, s);
		} catch (InvalidInputException e)
		{
			fail();
		}
		
		assertEquals(1, s.getAssignments().size());
		assertEquals("DueThis Controller", s.getAssignment(0).getName());
		assertEquals(course, s.getAssignment(0).getCourse());
		assertEquals(dueDate, s.getAssignment(0).getDueDate());
		assertEquals(gradeWeight, s.getAssignment(0).getGradeWeight(),0.0f);
		assertEquals(false, s.getAssignment(0).isIsCompleted());
		assertEquals(id1, s.getAssignment(0).getId());
		
		s.delete();
	}
	
	@Test
	public void testEditAssignmentExperiencedStudent()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createExperiencedStudent();
		Assignment a1 = createAssignment(s);
		String id1 = s.getAssignment(0).getId();

		try
		{
			dtc.editAssignment(s.getAssignment(0), "DueThis Controller", course, dueDate, gradeWeight, compTime, s);
		} catch (InvalidInputException e)
		{
			fail();
		}
		
		assertEquals(1, s.getAssignments().size());
		assertEquals("DueThis Controller", s.getAssignment(0).getName());
		assertEquals(course, s.getAssignment(0).getCourse());
		assertEquals(dueDate, s.getAssignment(0).getDueDate());
		assertEquals(compTime, s.getAssignment(0).getCompletionTime());
		assertEquals(false, s.getAssignment(0).isIsCompleted());
		assertEquals(id1, s.getAssignment(0).getId());
		
		s.delete();
	}
	
	@Test
	public void testCompleteAssignmentFalseToTrue()
	{
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = new Student("testId", "Richard Potato");
		Assignment a1 = createAssignment(s);
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
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = new Student("testId", "Richard Potato");
		Assignment a1 = createAssignment(s);
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
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = new Student("testId", "Richard Potato");
		Student s2 = new Student("ID", "Ichard Potato");
		Assignment a1 = createAssignment(s);
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
		Student ns = createNoviceStudent();
		Student es = createExperiencedStudent();

		assertEquals(0, ns.numberOfAssignments());
		assertEquals(0, es.numberOfAssignments());

		Assignment a1 = createAssignment(ns);
		Assignment a2 = createAssignment(es);

		assertEquals(1, ns.numberOfAssignments());
		assertEquals(1, es.numberOfAssignments());

		DueThisController dtc = new DueThisController();

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
		Student ns = createNoviceStudent();
		Student es = createExperiencedStudent();

		assertEquals(0, ns.numberOfAssignments());
		assertEquals(0, es.numberOfAssignments());

		Assignment a1 = createAssignment(ns);
		Assignment a2 = createAssignment(es);

		assertEquals(1, ns.numberOfAssignments());
		assertEquals(1, es.numberOfAssignments());

		DueThisController dtc = new DueThisController();

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
		DueThisController dtc = new DueThisController();

		Student s = createNoviceStudent();

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
		DueThisController dtc = new DueThisController();
		Student s = createNoviceStudent();
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
		DueThisController dtc = new DueThisController();
		Student s = createNoviceStudent();
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
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
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
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
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
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
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
		DueThisController dtc = new DueThisController();
		String error = "";

		Student s = createNoviceStudent();
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

	@Test
	public void testEditEvent()
	{

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

		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();
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

		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();

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

		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();

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
	
	@Test
	public void testRemoveEventSuccess(){
		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();
		
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
		String error = "";
		Student ns = createNoviceStudent();
		Student es = createExperiencedStudent();
		Event event = createEvent(ns);
		DueThisController dtc = new DueThisController();
		
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
		
		@SuppressWarnings("deprecation")
		Date testDate = new Date(120, 5, 25);
		
		Student ns = createNoviceStudent();
		Event event = createEvent(ns);
		Event event2 = createEvent(ns);
		Event event3 = createEvent(ns);
		Event event4 = createEvent(ns);
		Event event5 = createEventDetailed(ns, testDate);
		
		DueThisController dtc = new DueThisController();
		
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
		
		@SuppressWarnings("deprecation")
		Date testDate = new Date(120, 5, 25);
		
		Student ns = createNoviceStudent();
		Assignment assignment = createAssignment(ns);
		Assignment assignment2 = createAssignment(ns);
		Assignment assignment3 = createAssignment(ns);
		Assignment assignment4 = createAssignment(ns);
		Assignment assignment5 = createAssignmentDetailed(ns, testDate);
		DueThisController dtc = new DueThisController();
		
		List<Assignment> list = dtc.showAssignment(ns, dueDate);
		
		assertEquals(4, list.size());
		assertEquals(assignment, list.get(0));
		assertEquals(assignment2, list.get(1));
		assertEquals(assignment3, list.get(2));
		assertEquals(assignment4, list.get(3));
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
		new ExperiencedStudent(s, 0, 0, 0, 0, 0, 0, 0);
		return s;
	}

	private Assignment createAssignment(Student s)
	{
		return new Assignment("testId", name, course, dueDate, gradeWeight, isCompleted, compTime, s);
	}

	private Event createEvent(Student s)
	{
		return new Event("testId", name, dueDate, startTime, endTime, repeatedWeekly, s);
	}
	
	private Event createEventDetailed(Student s, Date date) {
		return new Event("testId", name, date, startTime, endTime, repeatedWeekly, s);
	}
	
	private Assignment createAssignmentDetailed(Student s, Date date) {
		return new Assignment("testId", name, course, date, gradeWeight, isCompleted, compTime, s);
	}
	
}
