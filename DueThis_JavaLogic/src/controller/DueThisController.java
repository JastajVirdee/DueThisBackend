package controller;

import java.util.*;
import java.sql.Date;
import java.sql.Time;

import model.Assignment;
import model.Student;

public class DueThisController
{

	public void createAssignment(String name, String course, Date dueDate, float gradeWeight, Time compTime, Student aStudent)
			throws InvalidInputException
	{
		java.util.Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = new java.util.Date();
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

		String error = "";

		if (aStudent == null)
			error += "No student is associated to this assignment! ";
		if (name == null || name.trim().length() == 0)
			error += "Please enter a valid assignment name! ";
		if (course == null || course.trim().length() == 0)
			error += "Please enter a valid course name! ";
		if (dueDate == null)
			error += "Due data cannot be empty! ";
		else if (dueDate.before(sqlDate) == true)
			error += "Due date must be in the future! ";
		if (aStudent.getStudentRole(0) instanceof model.NoviceStudent)
		{
			if (gradeWeight <= 0)
				error += "Please enter a positive grade weight! ";
		}
		else
		{
			//Currently have no way to check if the value is positive. Issue opened on the repo.
			if (compTime == null)
				error += "Please enter a positive time amount! ";
		}
		
		if (error.trim().length() > 0)
			throw new InvalidInputException(error);
		
		String id = "random";
		
		if (aStudent.getStudentRole(0) instanceof model.NoviceStudent)
		{
			Assignment a = new Assignment(id, name, course, dueDate, gradeWeight, null, aStudent);
			aStudent.addAssignment(a);
		}
		else
		{
			//Cannot put null for the grade weight. Will leave as a 0 for now.
			Assignment a = new Assignment(id, name, course, dueDate, 0, compTime, aStudent);
			aStudent.addAssignment(a);
		}
	}

}
