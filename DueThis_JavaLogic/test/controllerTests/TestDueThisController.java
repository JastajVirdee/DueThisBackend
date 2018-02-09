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
}
