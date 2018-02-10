package controller;

import java.util.*;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;

import model.Assignment;
import model.Student;

public class DueThisController
{

	public boolean createAssignment(String name, String course, Date dueDate, float gradeWeight, Duration compTime,
			Student aStudent) throws InvalidInputException
	{
		java.util.Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = new java.util.Date();
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

		String error = "";
		if (aStudent == null)
		{
			error += "No student is associated to this assignment! ";
			throw new InvalidInputException(error);
		}
		if (name == null || name.trim().length() == 0)
			error += "Please enter a valid assignment name! ";
		if (course == null || course.trim().length() == 0)
			error += "Please enter a valid course name! ";
		if (dueDate == null)
			error += "Due data cannot be empty! ";
		else if (dueDate.before(sqlDate) == true)
			error += "Due date must be in the future! ";
		if (aStudent.getStudentRoles().size() == 0)
			error += "Student must have a role! ";
		else if (aStudent.getStudentRole(0) instanceof model.NoviceStudent)
		{
			if (gradeWeight <= 0)
				error += "Please enter a positive grade weight! ";

		} else
		{
			if (compTime == null)
				error += "Please enter an estimated completion time! ";
			else if (compTime.isNegative())
				error += "Please enter a positive estimated completion time! ";
		}
		if (error.trim().length() > 0)
			throw new InvalidInputException(error);

		String id = UUID.randomUUID().toString();
		if (aStudent.getStudentRole(0) instanceof model.NoviceStudent)
		{
			Assignment a = new Assignment(id, name, course, dueDate, gradeWeight, null, aStudent);
			return true;
		} else if (aStudent.getStudentRole(0) instanceof model.ExperiencedStudent)
		{
			// Cannot put null for the grade weight. Will leave as a 0 for now.
			Assignment a = new Assignment(id, name, course, dueDate, 0, compTime, aStudent);
			return true;
		} else
		{
			return false;
		}
	}
	
	public boolean removeAssignment(Student aStudent, Assignment anAssignment) throws InvalidInputException {
		
		//Verify that the assignment belongs to the student
		boolean legalRemove = aStudent.equals(anAssignment.getStudent());
		
		String error = "";
		
		if (legalRemove) {
			anAssignment.delete();
		}
		else {
			error += "This assignment does not belong to this student! ";
			throw new InvalidInputException(error);
		}
		
		return legalRemove;
	}

}
