package controller;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.sql.Date;
import java.util.List;

import model.Assignment;
import model.Event;
import model.ExperiencedStudent;
import model.Student;

public class StudyAlgorithm {
	
	
	
	public void generateStudySchedule(Student aStudent) {
		
		java.util.Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = new java.util.Date();
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
		
		cal.add(Calendar.DAY_OF_WEEK, 7);
		java.sql.Date futureDate = new java.sql.Date(cal.getTime().getTime());
		
		ExperiencedStudent s = (ExperiencedStudent) aStudent.getStudentRole(0);
		int[] availability = getAvailability(s);
		
		//Get list of assignments
		List<Assignment> assignments = aStudent.getAssignments();
		
		ArrayList<Assignment> trackedAssignments = new ArrayList<Assignment>();
		
		//Check if student is experienced student
		if (aStudent.getStudentRole(0) instanceof model.ExperiencedStudent) {
			
			for (Assignment a : assignments) {
				boolean isCompleted = a.getIsCompleted();
				Date dueDate = a.getDueDate();
				
				if (!isCompleted && dueDate.after(sqlDate) && dueDate.before(futureDate)) {
					trackedAssignments.add(a);
					
				}
				
			}
						
			
		//Sort the assignments by due date	
		Collections.sort(trackedAssignments, new Comparator<Assignment>(){

		            @Override
		            public int compare(Assignment a1, Assignment a2) {
		                return a1.getDueDate().compareTo(a2.getDueDate());
		            }
		        });
		
		for (Assignment a : trackedAssignments) {
			
			Date dueDate = a.getDueDate();
			Duration compTime = a.getCompletionTime();
			
			int compTimeSeconds = (int) compTime.getSeconds();
			
			Calendar c = Calendar.getInstance();
			DueThisController dtc = new DueThisController();
			
			while (compTimeSeconds > 0) {
				//Returns 1 for sunday, 2 monday etc...
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				
				int availableToday = getAvailabilityForDay(availability, dayOfWeek);
				
				//Complete the entire assignment today
				if (compTimeSeconds <= availableToday) {
					createStudyEvent(dtc, aStudent, a, dueDate, compTimeSeconds);
					
					//Update availability
					setAvailabilityForDay(availability, dayOfWeek, availableToday-compTimeSeconds);
					
					compTimeSeconds = 0;
				}
				else {
					createStudyEvent(dtc, aStudent, a, dueDate, availableToday);
					
					//Update availability
					setAvailabilityForDay(availability, dayOfWeek, 0);
					
					c.add(Calendar.DAY_OF_WEEK, 1);
					
					compTimeSeconds = compTimeSeconds - availableToday;
					
				}
				
			}
			
			
			
		
		}
		
	}
}
	
	private int getAvailabilityForDay(int[] availability, int dayOfWeek) {
		return availability[dayOfWeek-1];
	}
	
	private void setAvailabilityForDay(int[] availability, int dayOfWeek, int newTime) {
		availability[dayOfWeek-1] = newTime;
	}
	
	//Creating an event with name study_<assignment_name>, date, start time=0, end time=duration (in milliseconds)
	private boolean createStudyEvent(DueThisController dtc, Student aStudent, Assignment assignment, Date date, int durationSeconds) {
		String name = "study_" + assignment.getName();
		Time startTime = new Time(0);
	
		
		//Obtain the duration in milliseconds
		int durationMS = durationSeconds*1000;
		
		Time endTime = new Time(durationMS);
		
		try {
			return dtc.createEvent(aStudent, name, date, startTime, endTime, false);
		}
		catch (InvalidInputException e) {
			return false;
		}

		
	}
	
	
	private int convertHoursToSeconds(int numHours) {
		
		return numHours*3600;
	
	}

	
	private int[] getAvailability(ExperiencedStudent aStudent) {
		int[] availability =  {aStudent.getSundayAvailability(),aStudent.getMondayAvailability(),aStudent.getTuesdayAvailability(),aStudent.getWednesdayAvailability(),
			aStudent.getThursdayAvailability(),aStudent.getFridayAvailability(),aStudent.getSaturdayAvailability()};
		
		for (int i=0; i<availability.length; i++) {
			availability[i] = convertHoursToSeconds(availability[i]);
		}
		
		return availability;
		}
	}

