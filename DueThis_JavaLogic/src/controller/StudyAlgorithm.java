package controller;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.sql.Date;
import java.util.List;

import model.Assignment;
import model.Event;
import model.ExperiencedStudent;
import model.Student;

public class StudyAlgorithm {
	
	public static class MyObject implements Comparable<MyObject> {

		  private Date dateTime;

		  public Date getDateTime() {
		    return dateTime;
		  }

		  public void setDateTime(Date datetime) {
		    this.dateTime = datetime;
		  }

		  @Override
		  public int compareTo(MyObject o) {
		    return getDateTime().compareTo(o.getDateTime());
		  }
		}
	
	
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
						
			
			
			
		}
		
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
	
	private void setAvailableHoursFromWeekday(ExperiencedStudent aStudent, int dayOfWeek, int newHours) {
		
		if (dayOfWeek == Calendar.SUNDAY) {
			aStudent.setSundayAvailability(newHours);
		}
		
		else if (dayOfWeek == Calendar.MONDAY) {
			aStudent.setMondayAvailability(newHours);
		}
		
		else if (dayOfWeek == Calendar.TUESDAY) {
			aStudent.setTuesdayAvailability(newHours);
		}
		
		else if (dayOfWeek == Calendar.WEDNESDAY) {
			aStudent.setWednesdayAvailability(newHours);
		}
		
		else if (dayOfWeek == Calendar.THURSDAY) {
			aStudent.setThursdayAvailability(newHours);
		}
		
		else if (dayOfWeek == Calendar.FRIDAY) {
			aStudent.setFridayAvailability(newHours);
		}
		
		else {
			aStudent.setSaturdayAvailability(newHours);
		}
	}
	
	private int getAvailableHoursFromWeekday(ExperiencedStudent aStudent, int dayOfWeek) {
		
		if (dayOfWeek == Calendar.SUNDAY) {
			return aStudent.getSundayAvailability();
		}
		
		else if (dayOfWeek == Calendar.MONDAY) {
			return aStudent.getMondayAvailability();
		}
		
		else if (dayOfWeek == Calendar.TUESDAY) {
			return aStudent.getTuesdayAvailability();
		}
		
		else if (dayOfWeek == Calendar.WEDNESDAY) {
			return aStudent.getWednesdayAvailability();
		}
		
		else if (dayOfWeek == Calendar.THURSDAY) {
			return aStudent.getThursdayAvailability();
		}
		
		else if (dayOfWeek == Calendar.FRIDAY) {
			return aStudent.getFridayAvailability();
		}
		
		else {
			return aStudent.getSaturdayAvailability();
		}
	}
	
	private int[] getAvailability(ExperiencedStudent aStudent) {
		int[] availability =  {aStudent.getSundayAvailability(),aStudent.getMondayAvailability(),aStudent.getTuesdayAvailability(),aStudent.getWednesdayAvailability(),
			aStudent.getThursdayAvailability(),aStudent.getFridayAvailability(),aStudent.getSaturdayAvailability()};
		
		return availability;
		}
	}

}
