package controller;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import model.Application;
import model.Assignment;
import model.Event;
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
		if (aStudent.getExperienced() == false)
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
		Application app = Application.getInstance();
		if (aStudent.getExperienced() == false)
		{
			Assignment a = new Assignment(id, name, course, dueDate, false, gradeWeight, null, aStudent, app);
			aStudent.addAssignment(a); 
			//TODO:Persistence needed here
			
			return true;
		} else if (aStudent.getExperienced())
		{
			Assignment a = new Assignment(id, name, course, dueDate, false, gradeWeight, compTime, aStudent, app);
			aStudent.addAssignment(a);
			//TODO: Persistence needed here
			return true;
		}
		
		return false;
	}

	public boolean editAssignment(Assignment anAssignment, String name, String course, Date dueDate, float gradeWeight,
			Duration compTime, Student aStudent) throws InvalidInputException
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
		if (aStudent.getExperienced() == false)
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

		if (aStudent.getExperienced() == false)
		{
			anAssignment.setName(name);
			anAssignment.setCourse(course);
			anAssignment.setDueDate(dueDate);
			anAssignment.setGradeWeight(gradeWeight);
			//TODO: Persistence needed here
			return true;
		} else if (aStudent.getExperienced())
		{
			anAssignment.setName(name);
			anAssignment.setCourse(course);
			anAssignment.setDueDate(dueDate);
			anAssignment.setCompletionTime(compTime);
			//TODO: Persistence needed here
			return true;
		}
		
		return false;
	}

	public boolean completeAssignment(Student aStudent, Assignment anAssignment) throws InvalidInputException
	{
		boolean legalRemove = aStudent.equals(anAssignment.getStudent());

		String error = "";
		if (legalRemove)
		{
			boolean currentState = anAssignment.getIsCompleted();
			anAssignment.setIsCompleted(!currentState);
		} else
		{
			error += "This assignment does not belong to this student! ";
			throw new InvalidInputException(error);
		}
		
		//TODO: Persistence needed here

		return true;
	}

	public boolean removeAssignment(Student aStudent, Assignment anAssignment) throws InvalidInputException
	{

		// Verify that the assignment belongs to the student
		boolean legalRemove = aStudent.equals(anAssignment.getStudent());

		String error = "";

		if (legalRemove)
		{
			anAssignment.delete();
		} else
		{
			error += "This assignment does not belong to this student! ";
			throw new InvalidInputException(error);
		}

		//TODO: Persistence needed here
		return legalRemove;
	}

	public boolean createEvent(Student aStudent, String name, Date date, Time startTime, Time endTime,
			boolean repeatWeekly) throws InvalidInputException
	{
		java.util.Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = new java.util.Date();
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

		String error = "";

		// Verify that there is a student provided
		if (aStudent == null)
		{
			error += "No student is associated to this event! ";
			throw new InvalidInputException(error);
		}

		// Check for input errors
		if (name == null || name.trim().length() == 0)
		{
			error += "Please enter a valid name! ";
		}
		if (date == null)
		{
			error += "Date cannot be empty! ";
		} else if (date.before(sqlDate) == true)
		{
			error += "Date must be in the future! ";
		}
		if (startTime == null)
		{
			error += "Start time cannot be empty! ";
		} else if (endTime == null)
		{
			error += "End time cannot be empty! ";
		} else if (endTime.before(startTime) == true)
		{
			error += "Start time must be before end time! ";
		}
		if (error.trim().length() > 0)
		{
			throw new InvalidInputException(error);
		}

		String id = UUID.randomUUID().toString();
		Application app = Application.getInstance();

		Event e = new Event(id, name, date, startTime, endTime, repeatWeekly, aStudent, app);
		//TODO: Persistence needed here
		return true;

	}

	public boolean editEvent(Event event, String name, Date date, Time startTime, Time endTime, boolean repeatWeekly)
			throws InvalidInputException
	{

		java.util.Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = new java.util.Date();
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

		// Check for input errors
		String error = "";
		if (name == null || name.trim().length() == 0)
		{
			error += "Please enter a valid name! ";
		}
		if (date == null)
		{
			error += "Date cannot be empty! ";
		} else if (date.before(sqlDate) == true)
		{
			error += "Date must be in the future! ";
		}
		if (startTime == null)
		{
			error += "Start time cannot be empty! ";
		} else if (endTime == null)
		{
			error += "End time cannot be empty! ";
		} else if (endTime.before(startTime) == true)
		{
			error += "Start time must be before end time! ";
		}
		if (error.trim().length() > 0)
		{
			throw new InvalidInputException(error);
		}

		// Edit the event
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		event.setRepeatedWeekly(repeatWeekly);
		//TODO: Persistence needed here
		
		return true;
	}

	public boolean removeEvent(Student aStudent, Event anEvent) throws InvalidInputException
	{
		boolean legalRemove = aStudent.equals(anEvent.getStudent());

		String error = "";

		if (legalRemove)
		{
			anEvent.delete();
		} else
		{
			error += "This event does not belong to this student! ";
			throw new InvalidInputException(error);
		}

		//TODO: Persistence needed here
		return legalRemove;
	}

	// when you click the save button on the availabilities page it runs this
	public boolean updateAvailabilities(Student aStudent, int sunday, int monday, int tuesday, int wednesday,
			int thursday, int friday, int saturday) throws InvalidInputException
	{

		String error = "";

		
		if(aStudent == null) {
			error += "No student was input into the updateAvailabilities method.";
			throw new InvalidInputException(error);
		}

		// Check if the student is an experienced student
		boolean legalRemove = aStudent.getExperienced();
		// true if student is an experienced student

		if (!legalRemove)
		{
			error += "Only experienced students can set availabilities.";
			throw new InvalidInputException(error);	
		}

		// Make sure hours between 0 and 24 inclusive
		if (sunday < 0 || sunday > 24)
		{
			error += "Sunday hours must be between 0 and 24! ";
		}
		if (monday < 0 || monday > 24)
		{
			error += "Monday hours must be between 0 and 24! ";
		}
		if (tuesday < 0 || tuesday > 24)
		{
			error += "Tuesday hours must be between 0 and 24! ";
		}
		if (wednesday < 0 || wednesday > 24)
		{
			error += "Wednesday hours must be between 0 and 24! ";
		}
		if (thursday < 0 || thursday > 24)
		{
			error += "Thursday hours must be between 0 and 24! ";
		}
		if (friday < 0 || friday > 24)
		{
			error += "Friday hours must be between 0 and 24! ";
		}
		if (saturday < 0 || saturday > 24)
		{
			error += "Saturday hours must be between 0 and 24! ";
		}
		if (error.trim().length() > 0)
		{ // if errors exist
			throw new InvalidInputException(error);
		}

		// Set the availabilities
		aStudent.setSundayAvailability(sunday);
		aStudent.setMondayAvailability(monday);
		aStudent.setTuesdayAvailability(tuesday);
		aStudent.setWednesdayAvailability(wednesday);
		aStudent.setThursdayAvailability(thursday);
		aStudent.setFridayAvailability(friday);
		aStudent.setSaturdayAvailability(saturday);

		//TODO: Persistence needed here
		
		return true;
	}
	
	public List<Time> showStudyTimeNovice(Student aStudent, Date dateSelected) { //No values now but this will eventually work with the algorithm
		List<Time> listOfIntervals = new ArrayList<>(); //List containing all the intervals of study time
		return listOfIntervals;
	}
	
	public Duration showStudyTimeExperienced(Student aStudent, Date dateSelected) { //No values now but this will eventually work with the algorithm
		Duration timeSpent = null; //The time you need to study on a given day will be returned as type Duration
		return timeSpent;
	}
	
	@SuppressWarnings("deprecation")
	public List<Event> showEvent(Student aStudent, Date dateSelected) { //Showing all events on a date (same year, month and day)
		List<Event> allEvents = new ArrayList<>();
		allEvents = aStudent.getEvents();
		
		List<Event> eventsToday = new ArrayList<>();
		
		for(Event event: allEvents)
		{
			if (event.getRepeatedWeekly()){
				int diffInDays = (int) Math.round(getDateDiff(event.getDate(),dateSelected,TimeUnit.DAYS));
				if (event.getDate().getDate() == dateSelected.getDate() && event.getDate().getMonth() == dateSelected.getMonth() && event.getDate().getYear() == dateSelected.getYear() || (diffInDays % 7) == 6) {
					eventsToday.add(event);
				}
			} else {
				if(event.getDate().getDate() == dateSelected.getDate() && event.getDate().getMonth() == dateSelected.getMonth() && event.getDate().getYear() == dateSelected.getYear()) {
			                eventsToday.add(event);
			    }
			}
		}
		return eventsToday;
	}
	
	@SuppressWarnings("deprecation")
	public List<Assignment> showAssignment(Student aStudent, Date dateSelected) { //Showing all assignments on a date (same year, month and day)
		List<Assignment> allAssignments = new ArrayList<>();
		allAssignments = aStudent.getAssignments();
		
		List<Assignment> assignmentsToday = new ArrayList<>();
		
		for(Assignment a : allAssignments)
		{
			if ( (a.getDueDate().getDate() == dateSelected.getDate()) && (a.getDueDate().getMonth() == dateSelected.getMonth()) && (a.getDueDate().getYear() == dateSelected.getYear()) ) {
				assignmentsToday.add(a);
			}	
		}
		return assignmentsToday;
	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	
	public List<Assignment> showFilteredByDateAssignment(Student aStudent, Date filteredDate)
	{
		List<Assignment> assignments = new ArrayList<>();
		assignments = aStudent.getAssignments();
		
		List<Assignment> filteredAssignments = new ArrayList<>();
		for (Assignment a : assignments)
		{
			if (getDateDiff(filteredDate ,a.getDueDate(), TimeUnit.MILLISECONDS) > 0)
			{
				filteredAssignments.add(a);
			}
		}
		
		return filteredAssignments;
	}
	
	public List<String> showCourses(Student aStudent) {
		List<Assignment> allAssignments = aStudent.getAssignments();
		
		List<String> courses = new ArrayList<>();
		
		for(Assignment a : allAssignments) {
			
			String course = a.getCourse();
			boolean addCourse = true;
			
			for (String s : courses) {
				if (s.equals(course)) {
					addCourse = false;
					break;
				}
			}
			
			if(addCourse) {
				courses.add(course);
			}
			
		}
		
		return courses;
		
	}
	
	public List<Assignment> showAssignmentsByCourse(Student aStudent, String course) throws InvalidInputException {
		
		if (course == null || course.trim().equals("")) {
			throw new InvalidInputException("Course is Required");
		}
		
		List<Assignment> allAssignments = aStudent.getAssignments();

		List<Assignment> courseAssignments = new ArrayList<>();
		
		for (Assignment a : allAssignments) {
			
			if (course.equals(a.getCourse())) {
				courseAssignments.add(a);
			}
		}
		
		return courseAssignments;

	}
	
	public List<Assignment> showFilteredByCompleted(Student aStudent) throws InvalidInputException
	{
		if (aStudent == null){
			throw new InvalidInputException("Student in showFilteredByCompleted is null"); //should never happen
		}
		
		List<Assignment> allAssignments = new ArrayList<>();
		allAssignments = aStudent.getAssignments();
		
		if (allAssignments.isEmpty()){
			throw new InvalidInputException("You have no assignments");
			//in UI this will make it so that clicking the button brings up an error message, and the reason
		}
		
		List<Assignment> filteredCompletedAssignments = new ArrayList<>();
		
		for(Assignment a : allAssignments)
		{
			if(a.getIsCompleted())
			{
				filteredCompletedAssignments.add(a);
			}
		}
		
		if (filteredCompletedAssignments.isEmpty()){
			throw new InvalidInputException("No completed assignments");
			//in UI this will make it so that clicking the button brings up an error message, and the reason
		}
		
		return filteredCompletedAssignments;
	}
	
	
}
