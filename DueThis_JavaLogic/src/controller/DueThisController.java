package controller;

import java.sql.Connection;
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
import persistence.SQLiteIntegration;

public class DueThisController {
    // - TODO Consider separate exceptions for the database
    // - TODO Handle the closing of the database on the front end.
    private SQLiteIntegration persistenceSQL = new SQLiteIntegration();

    public boolean createAssignment(String name, String course, Date dueDate, float gradeWeight,
            Duration compTime, Student aStudent) throws InvalidInputException {
        java.util.Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = new java.util.Date();
        cal.setTime(utilDate);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

        String error = "";

        if (aStudent == null) {
            error += "No student is associated to this assignment! ";
            throw new InvalidInputException(error);
        }

        if (name == null || name.trim().length() == 0)
            error += "Please enter a valid assignment name! ";

        if (course == null || course.trim().length() == 0)
            error += "Please enter a valid course name! ";

        if (dueDate == null)
            error += "Due data cannot be empty! ";

        else if (dueDate.before(sqlDate))
            error += "Due date must be in the future! ";

        if (!aStudent.getExperienced()) {
            if (gradeWeight <= 0)
                error += "Please enter a positive grade weight! ";
        } else {
            if (compTime == null)
                error += "Please enter an estimated completion time! ";
            else if (compTime.isNegative())
                error += "Please enter a positive estimated completion time! ";
        }

        if (error.trim().length() > 0)
            throw new InvalidInputException(error);

        String id = UUID.randomUUID().toString();
        Application app = Application.getInstance();

        Assignment a = new Assignment(id, name, course, dueDate, false, gradeWeight,
                aStudent.getExperienced() ? compTime : Duration.ZERO, aStudent, app);

        aStudent.addAssignment(a);

        // - Try to commit to the database
        Connection c = persistenceSQL.ensureConnection();
        if (c == null)
            throw new InvalidInputException("Failed to connect to database");

        boolean op = SQLiteIntegration.insertIntoAssignments(c, a);
        if (!op)
            throw new InvalidInputException("Failed to commit to database");

        return op;
    }

    public boolean editAssignment(Assignment anAssignment, String name, String course, Date dueDate,
            float gradeWeight, Duration compTime, Student aStudent) throws InvalidInputException {
        java.util.Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = new java.util.Date();
        cal.setTime(utilDate);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

        String error = "";
        if (aStudent == null) {
            error += "No student is associated to this assignment! ";
            throw new InvalidInputException(error);
        }

        if (name == null || name.trim().length() == 0)
            error += "Please enter a valid assignment name! ";

        if (course == null || course.trim().length() == 0)
            error += "Please enter a valid course name! ";

        if (dueDate == null)
            error += "Due data cannot be empty! ";
        else if (dueDate.before(sqlDate))
            error += "Due date must be in the future! ";

        if (!aStudent.getExperienced()) {
            if (gradeWeight <= 0)
                error += "Please enter a positive grade weight! ";
        } else {
            if (compTime == null)
                error += "Please enter an estimated completion time! ";
            else if (compTime.isNegative())
                error += "Please enter a positive estimated completion time! ";
        }
        if (error.trim().length() > 0)
            throw new InvalidInputException(error);

        anAssignment.setName(name);
        anAssignment.setCourse(course);
        anAssignment.setDueDate(dueDate);

        if (aStudent.getExperienced())
            anAssignment.setCompletionTime(compTime);
        else
            anAssignment.setGradeWeight(gradeWeight);

        // - Try to commit to the database
        Connection c = persistenceSQL.ensureConnection();
        if (c == null)
            throw new InvalidInputException("Failed to connect to database");

        boolean op = SQLiteIntegration.updateAssignments(c, anAssignment);
        if (!op)
            throw new InvalidInputException("Failed to commit to database");

        return op;
    }

    public boolean completeAssignment(Student aStudent, Assignment anAssignment)
            throws InvalidInputException {
        boolean legalUpdate = aStudent.equals(anAssignment.getStudent());
        String error = "";

        if (legalUpdate) {
            boolean currentState = anAssignment.getIsCompleted();
            anAssignment.setIsCompleted(!currentState);

            // - Try to commit to the database
            Connection c = persistenceSQL.ensureConnection();
            if (c == null)
                throw new InvalidInputException("Failed to connect to database");

            boolean op = SQLiteIntegration.updateAssignments(c, anAssignment);
            if (!op)
                throw new InvalidInputException("Failed to commit to database");

            legalUpdate &= op;
        } else {
            error += "This assignment does not belong to this student! ";
            throw new InvalidInputException(error);
        }

        return legalUpdate;
    }

    public boolean removeAssignment(Student aStudent, Assignment anAssignment)
            throws InvalidInputException {
        boolean legalRemove = aStudent.equals(anAssignment.getStudent());
        String error = "";

        if (legalRemove) {
            // - Try to commit to the database
            Connection c = persistenceSQL.ensureConnection();
            if (c == null)
                throw new InvalidInputException("Failed to connect to database");

            boolean op = SQLiteIntegration.deleteAssignments(c, anAssignment);
            if (!op)
                throw new InvalidInputException("Failed to commit to database");

            legalRemove &= op;

            anAssignment.delete();
        } else {
            error += "This assignment does not belong to this student! ";
            throw new InvalidInputException(error);
        }

        return legalRemove;
    }

    public boolean createEvent(Student aStudent, String name, Date date, Time startTime,
            Time endTime, boolean repeatWeekly) throws InvalidInputException {
        java.util.Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = new java.util.Date();
        cal.setTime(utilDate);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

        String error = "";

        if (aStudent == null) {
            error += "No student is associated to this event! ";
            throw new InvalidInputException(error);
        }

        if (name == null || name.trim().length() == 0) {
            error += "Please enter a valid name! ";
        }
        if (date == null) {
            error += "Date cannot be empty! ";
        } else if (date.before(sqlDate)) {
            error += "Date must be in the future! ";
        }

        if (startTime == null) {
            error += "Start time cannot be empty! ";
        } else if (endTime == null) {
            error += "End time cannot be empty! ";
        } else if (endTime.before(startTime)) {
            error += "Start time must be before end time! ";
        }

        if (error.trim().length() > 0) {
            throw new InvalidInputException(error);
        }

        String id = UUID.randomUUID().toString();
        Application app = Application.getInstance();
        Event e = new Event(id, name, date, startTime, endTime, repeatWeekly, aStudent, app);

        // - Try to commit to the database
        Connection c = persistenceSQL.ensureConnection();
        if (c == null)
            throw new InvalidInputException("Failed to connect to database");

        boolean op = SQLiteIntegration.insertIntoEvents(c, e);
        if (!op)
            throw new InvalidInputException("Failed to commit to database");

        return op;
    }

    public boolean editEvent(Event event, String name, Date date, Time startTime, Time endTime,
            boolean repeatWeekly) throws InvalidInputException {

        java.util.Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = new java.util.Date();
        cal.setTime(utilDate);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
        String error = "";

        if (name == null || name.trim().length() == 0) {
            error += "Please enter a valid name! ";
        }
        if (date == null) {
            error += "Date cannot be empty! ";
        } else if (date.before(sqlDate)) {
            error += "Date must be in the future! ";
        }

        if (startTime == null) {
            error += "Start time cannot be empty! ";
        } else if (endTime == null) {
            error += "End time cannot be empty! ";
        } else if (endTime.before(startTime)) {
            error += "Start time must be before end time! ";
        }

        if (error.trim().length() > 0) {
            throw new InvalidInputException(error);
        }

        event.setName(name);
        event.setDate(date);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setRepeatedWeekly(repeatWeekly);

        // - Try to commit to the database
        Connection c = persistenceSQL.ensureConnection();
        if (c == null)
            throw new InvalidInputException("Failed to connect to database");

        boolean op = SQLiteIntegration.updateEvents(c, event);
        if (!op)
            throw new InvalidInputException("Failed to commit to database");

        return op;
    }

    public boolean removeEvent(Student aStudent, Event anEvent) throws InvalidInputException {
        boolean legalRemove = aStudent.equals(anEvent.getStudent());
        String error = "";

        if (legalRemove) {
            // - Try to commit to the database
            Connection c = persistenceSQL.ensureConnection();
            if (c == null)
                throw new InvalidInputException("Failed to connect to database");

            boolean op = SQLiteIntegration.deleteEvents(c, anEvent);
            if (!op)
                throw new InvalidInputException("Failed to commit to database");

            legalRemove &= op;

            anEvent.delete();
        } else {
            error += "This event does not belong to this student! ";
            throw new InvalidInputException(error);
        }

        return legalRemove;
    }

    // when you click the save button on the availabilities page it runs this
    public boolean updateAvailabilities(Student aStudent, int sunday, int monday, int tuesday,
            int wednesday, int thursday, int friday, int saturday) throws InvalidInputException {
        String error = "";

        if (aStudent == null) {
            error += "No student was input into the updateAvailabilities method.";
            throw new InvalidInputException(error);
        }

        if (!aStudent.getExperienced()) {
            error += "Only experienced students can set availabilities.";
            throw new InvalidInputException(error);
        }

        if (sunday < 0 || sunday > 24) {
            error += "Sunday hours must be between 0 and 24! ";
        }

        if (monday < 0 || monday > 24) {
            error += "Monday hours must be between 0 and 24! ";
        }

        if (tuesday < 0 || tuesday > 24) {
            error += "Tuesday hours must be between 0 and 24! ";
        }

        if (wednesday < 0 || wednesday > 24) {
            error += "Wednesday hours must be between 0 and 24! ";
        }

        if (thursday < 0 || thursday > 24) {
            error += "Thursday hours must be between 0 and 24! ";
        }

        if (friday < 0 || friday > 24) {
            error += "Friday hours must be between 0 and 24! ";
        }

        if (saturday < 0 || saturday > 24) {
            error += "Saturday hours must be between 0 and 24! ";
        }

        if (error.trim().length() > 0) {
            throw new InvalidInputException(error);
        }

        aStudent.setSundayAvailability(sunday);
        aStudent.setMondayAvailability(monday);
        aStudent.setTuesdayAvailability(tuesday);
        aStudent.setWednesdayAvailability(wednesday);
        aStudent.setThursdayAvailability(thursday);
        aStudent.setFridayAvailability(friday);
        aStudent.setSaturdayAvailability(saturday);

        // - Try to commit changes to the database
        Connection c = persistenceSQL.ensureConnection();
        if (c == null)
            throw new InvalidInputException("Failed to connect to database");

        boolean op = SQLiteIntegration.updateStudents(c, aStudent);
        if (!op)
            throw new InvalidInputException("Failed to commit to database");

        return op;
    }

    public List<Time> showStudyTimeNovice(Student aStudent, Date dateSelected) {
        // - TODO Complete this. No values now but this will eventually work with the
        // - algorithm. Returns a list containing all the intervals of study time.
        return new ArrayList<>();
    }

    public Duration showStudyTimeExperienced(Student aStudent, Date dateSelected) {
        // - TODO Complete this. No values now but this will eventually work with the
        // - algorithm. Returns the time you need to study on a given day as `Duration`
        return null;
    }

    public List<Event> showEvent(Student aStudent, Date dateSelected) {
        // - Showing all events on a date (same year, month and day)
        List<Event> allEvents = new ArrayList<>();
        List<Event> eventsToday = new ArrayList<>();

        allEvents = aStudent.getEvents();

        for (Event event : allEvents) {
            if (event.getRepeatedWeekly()) {
                int diffInDays = Math
                        .round(getDateDiff(event.getDate(), dateSelected, TimeUnit.DAYS));
                if (event.getDate().compareTo(dateSelected) == 0 || (diffInDays % 7) == 6) {
                    eventsToday.add(event);
                }
            } else {
                if (event.getDate().compareTo(dateSelected) == 0) {
                    eventsToday.add(event);
                }
            }
        }
        return eventsToday;
    }

    public List<Assignment> showAssignment(Student aStudent, Date dateSelected) {
        // - Showing all assignments on a date (same year, month and day)
        List<Assignment> allAssignments = new ArrayList<>();
        List<Assignment> assignmentsToday = new ArrayList<>();

        allAssignments = aStudent.getAssignments();

        for (Assignment a : allAssignments) {
            if (a.getDueDate().compareTo(dateSelected) == 0) {
                assignmentsToday.add(a);
            }
        }
        return assignmentsToday;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public List<Assignment> showFilteredByDateAssignment(Student aStudent, Date filteredDate) {
        List<Assignment> assignments = new ArrayList<>();
        List<Assignment> filteredAssignments = new ArrayList<>();

        assignments = aStudent.getAssignments();

        for (Assignment a : assignments) {
            if (getDateDiff(filteredDate, a.getDueDate(), TimeUnit.MILLISECONDS) > 0) {
                filteredAssignments.add(a);
            }
        }

        return filteredAssignments;
    }

    public List<String> showCourses(Student aStudent) {
        List<Assignment> allAssignments = aStudent.getAssignments();
        List<String> courses = new ArrayList<>();

        for (Assignment a : allAssignments) {
            String course = a.getCourse();
            boolean addCourse = true;

            for (String s : courses) {
                if (s.equals(course)) {
                    addCourse = false;
                    break;
                }
            }

            if (addCourse) {
                courses.add(course);
            }
        }

        return courses;
    }

    public List<Assignment> showAssignmentsByCourse(Student aStudent, String course)
            throws InvalidInputException {
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

    public List<Assignment> showFilteredByCompleted(Student aStudent) throws InvalidInputException {
        if (aStudent == null) {
            // should never happen
            throw new InvalidInputException("Student in showFilteredByCompleted is null");
        }

        List<Assignment> allAssignments = new ArrayList<>();
        allAssignments = aStudent.getAssignments();

        if (allAssignments.isEmpty()) {
            throw new InvalidInputException("You have no assignments");
            // in UI this will make it so that clicking the button brings up an error
            // message, and the reason
        }

        List<Assignment> filteredCompletedAssignments = new ArrayList<>();

        for (Assignment a : allAssignments) {
            if (a.getIsCompleted()) {
                filteredCompletedAssignments.add(a);
            }
        }

        if (filteredCompletedAssignments.isEmpty()) {
            throw new InvalidInputException("No completed assignments");
            // in UI this will make it so that clicking the button brings up an error
            // message, and the reason
        }

        return filteredCompletedAssignments;
    }

    public List<Assignment> showFilteredByIncompleted(Student aStudent)
            throws InvalidInputException {
        if (aStudent == null) {
            // should never happen
            throw new InvalidInputException("Student in showFilteredByIncompleted is null");
        }

        List<Assignment> allAssignments = new ArrayList<>();
        allAssignments = aStudent.getAssignments();

        if (allAssignments.isEmpty()) {
            throw new InvalidInputException("You have no assignments");
            // in UI this will make it so that clicking the button brings up an error
            // message, and the reason
        }

        List<Assignment> filteredIncompletedAssignments = new ArrayList<>();
        for (Assignment a : allAssignments) {
            if (!(a.getIsCompleted())) {
                filteredIncompletedAssignments.add(a);
            }
        }

        if (filteredIncompletedAssignments.isEmpty()) {
            throw new InvalidInputException("No incompleted assignments");
            // in UI this will make it so that clicking the button brings up an error
            // message, and the reason
        }

        return filteredIncompletedAssignments;
    }
}
