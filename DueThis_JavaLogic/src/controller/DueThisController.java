package controller;

import java.util.*;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;

import model.Assignment;
import model.Event;
import model.Student;
import model.ExperiencedStudent;

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
			Assignment a = new Assignment(id, name, course, dueDate, gradeWeight, false, null, aStudent);
			aStudent.addAssignment(a);
			return true;
		} else if (aStudent.getStudentRole(0) instanceof model.ExperiencedStudent)
		{
			// Cannot put null for the grade weight. Will leave as a 0 for now.
			Assignment a = new Assignment(id, name, course, dueDate, 0, false, compTime, aStudent);
			aStudent.addAssignment(a);
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
	
	public boolean createEvent(Student aStudent, String name, Date date, Time startTime, Time endTime, boolean repeatWeekly) throws InvalidInputException {
		java.util.Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = new java.util.Date();
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
		
		String error = "";

		//Verify that there is a student provided
		if (aStudent == null)
		{
			error += "No student is associated to this event! ";
			throw new InvalidInputException(error);
		}
		
		// Check for input errors
		if (name == null || name.trim().length() == 0) {
			error += "Please enter a valid name! ";
		}
		if (date == null) {
			error += "Date cannot be empty! ";
		}
		else if (date.before(sqlDate) == true) {
			error += "Date must be in the future! ";
		}
		if (startTime == null) {
			error += "Start time cannot be empty! ";
		}
		else if (endTime == null) {
			error += "End time cannot be empty! ";
		}
		else if (endTime.before(startTime) == true) {
			error += "Start time must be before end time! ";
		}
		if (error.trim().length() > 0) {
			throw new InvalidInputException(error);
		}
		
		String id = UUID.randomUUID().toString();

		Event e = new Event(id, name, date, startTime, endTime, repeatWeekly, aStudent);
		return true;
		
	}
	
	public boolean editEvent(Event event, String name, Date date, Time startTime, Time endTime, boolean repeatWeekly) throws InvalidInputException {		
		
		java.util.Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = new java.util.Date();
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
		
		// Check for input errors
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error += "Please enter a valid name! ";
		}
		if (date == null) {
			error += "Date cannot be empty! ";
		}
		else if (date.before(sqlDate) == true) {
			error += "Date must be in the future! ";
		}
		if (startTime == null) {
			error += "Start time cannot be empty! ";
		}
		else if (endTime == null) {
			error += "End time cannot be empty! ";
		}
		else if (endTime.before(startTime) == true) {
			error += "Start time must be before end time! ";
		}
		if (error.trim().length() > 0) {
			throw new InvalidInputException(error);
		}
		
		// Edit the event
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		event.setRepeatedWeekly(repeatWeekly);
		
		return true;
	}
	
	
	
	//when you click the save button on the availabilities page it runs this
	public boolean updateAvailabilities(Student aStudent, int sunday, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday) throws InvalidInputException{ 
		
		String error = "";
		
		if(aStudent == null) {
			error += "No student was input into the updateAvailabilities method";
			throw new InvalidInputException(error);
		}
		
		
		// Check if the student is an experienced student
		boolean legalRemove = aStudent.getStudentRole(0) instanceof model.ExperiencedStudent;
				// true if student is an experienced student
		
		if (!legalRemove) {
			error += "Only experienced students can set availabilities.";
			throw new InvalidInputException(error);	
		}
		
		
		// Make sure hours between 0 and 24 inclusive
		if(sunday<0 || sunday>24) {
			error += "Sunday hours must be between 0 and 24! ";
		}
		if(monday<0 || monday>24) {
			error += "Monday hours must be between 0 and 24! ";
		}
		if(tuesday<0 || tuesday>24) {
			error += "Tuesday hours must be between 0 and 24! ";
		}
		if(wednesday<0 || wednesday>24) {
			error += "Wednesday hours must be between 0 and 24! ";
		}
		if(thursday<0 || thursday>24) {
			error += "Thursday hours must be between 0 and 24! ";
		}
		if(friday<0 || friday>24) {
			error += "Friday hours must be between 0 and 24! ";
		}
		if(saturday<0 || saturday>24) {
			error += "Saturday hours must be between 0 and 24! ";
		}
		if (error.trim().length() > 0) { //if errors exist
			throw new InvalidInputException(error);
		}
		
		
		// Get the role and get experiencedStudent, allows the method to access experiencedStudent object
		ExperiencedStudent anExperiencedStudent = (ExperiencedStudent)aStudent.getStudentRole(0);
		
		// Set the availabilities
		anExperiencedStudent.setSundayAvailability(sunday);
		anExperiencedStudent.setMondayAvailability(monday);
		anExperiencedStudent.setTuesdayAvailability(tuesday);
		anExperiencedStudent.setWednesdayAvailability(wednesday);
		anExperiencedStudent.setThursdayAvailability(thursday);
		anExperiencedStudent.setFridayAvailability(friday);
		anExperiencedStudent.setSaturdayAvailability(saturday);
		
		
		return true;
	}

}
