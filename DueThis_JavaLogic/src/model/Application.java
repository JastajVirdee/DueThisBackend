/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3802.c2696fa modeling language!*/


import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 41 "model.ump"
// line 66 "model.ump"
public class Application
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Application theInstance = null;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Application Associations
  private List<Student> students;
  private List<Event> events;
  private List<Assignment> assignments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  private Application()
  {
    students = new ArrayList<Student>();
    events = new ArrayList<Event>();
    assignments = new ArrayList<Assignment>();
  }

  public static Application getInstance()
  {
    if(theInstance == null)
    {
      theInstance = new Application();
    }
    return theInstance;
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Student getStudent(int index)
  {
    Student aStudent = students.get(index);
    return aStudent;
  }

  public List<Student> getStudents()
  {
    List<Student> newStudents = Collections.unmodifiableList(students);
    return newStudents;
  }

  public int numberOfStudents()
  {
    int number = students.size();
    return number;
  }

  public boolean hasStudents()
  {
    boolean has = students.size() > 0;
    return has;
  }

  public int indexOfStudent(Student aStudent)
  {
    int index = students.indexOf(aStudent);
    return index;
  }
  /* Code from template association_GetMany */
  public Event getEvent(int index)
  {
    Event aEvent = events.get(index);
    return aEvent;
  }

  public List<Event> getEvents()
  {
    List<Event> newEvents = Collections.unmodifiableList(events);
    return newEvents;
  }

  public int numberOfEvents()
  {
    int number = events.size();
    return number;
  }

  public boolean hasEvents()
  {
    boolean has = events.size() > 0;
    return has;
  }

  public int indexOfEvent(Event aEvent)
  {
    int index = events.indexOf(aEvent);
    return index;
  }
  /* Code from template association_GetMany */
  public Assignment getAssignment(int index)
  {
    Assignment aAssignment = assignments.get(index);
    return aAssignment;
  }

  public List<Assignment> getAssignments()
  {
    List<Assignment> newAssignments = Collections.unmodifiableList(assignments);
    return newAssignments;
  }

  public int numberOfAssignments()
  {
    int number = assignments.size();
    return number;
  }

  public boolean hasAssignments()
  {
    boolean has = assignments.size() > 0;
    return has;
  }

  public int indexOfAssignment(Assignment aAssignment)
  {
    int index = assignments.indexOf(aAssignment);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfStudents()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Student addStudent(String aId, String aName, boolean aExperienced, int aSundayAvailability, int aMondayAvailability, int aTuesdayAvailability, int aWednesdayAvailability, int aThursdayAvailability, int aFridayAvailability, int aSaturdayAvailability)
  {
    return new Student(aId, aName, aExperienced, aSundayAvailability, aMondayAvailability, aTuesdayAvailability, aWednesdayAvailability, aThursdayAvailability, aFridayAvailability, aSaturdayAvailability, this);
  }

  public boolean addStudent(Student aStudent)
  {
    boolean wasAdded = false;
    if (students.contains(aStudent)) { return false; }
    Application existingApplication = aStudent.getApplication();
    boolean isNewApplication = existingApplication != null && !this.equals(existingApplication);
    if (isNewApplication)
    {
      aStudent.setApplication(this);
    }
    else
    {
      students.add(aStudent);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeStudent(Student aStudent)
  {
    boolean wasRemoved = false;
    //Unable to remove aStudent, as it must always have a application
    if (!this.equals(aStudent.getApplication()))
    {
      students.remove(aStudent);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addStudentAt(Student aStudent, int index)
  {  
    boolean wasAdded = false;
    if(addStudent(aStudent))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStudents()) { index = numberOfStudents() - 1; }
      students.remove(aStudent);
      students.add(index, aStudent);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStudentAt(Student aStudent, int index)
  {
    boolean wasAdded = false;
    if(students.contains(aStudent))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStudents()) { index = numberOfStudents() - 1; }
      students.remove(aStudent);
      students.add(index, aStudent);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStudentAt(aStudent, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEvents()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Event addEvent(String aId, String aName, Date aDate, Time aStartTime, Time aEndTime, boolean aRepeatedWeekly, Student aStudent)
  {
    return new Event(aId, aName, aDate, aStartTime, aEndTime, aRepeatedWeekly, aStudent, this);
  }

  public boolean addEvent(Event aEvent)
  {
    boolean wasAdded = false;
    if (events.contains(aEvent)) { return false; }
    Application existingApplication = aEvent.getApplication();
    boolean isNewApplication = existingApplication != null && !this.equals(existingApplication);
    if (isNewApplication)
    {
      aEvent.setApplication(this);
    }
    else
    {
      events.add(aEvent);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEvent(Event aEvent)
  {
    boolean wasRemoved = false;
    //Unable to remove aEvent, as it must always have a application
    if (!this.equals(aEvent.getApplication()))
    {
      events.remove(aEvent);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addEventAt(Event aEvent, int index)
  {  
    boolean wasAdded = false;
    if(addEvent(aEvent))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEvents()) { index = numberOfEvents() - 1; }
      events.remove(aEvent);
      events.add(index, aEvent);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEventAt(Event aEvent, int index)
  {
    boolean wasAdded = false;
    if(events.contains(aEvent))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEvents()) { index = numberOfEvents() - 1; }
      events.remove(aEvent);
      events.add(index, aEvent);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEventAt(aEvent, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAssignments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Assignment addAssignment(String aId, String aName, String aCourse, Date aDueDate, boolean aIsCompleted, float aGradeWeight, Duration aCompletionTime, Student aStudent)
  {
    return new Assignment(aId, aName, aCourse, aDueDate, aIsCompleted, aGradeWeight, aCompletionTime, aStudent, this);
  }

  public boolean addAssignment(Assignment aAssignment)
  {
    boolean wasAdded = false;
    if (assignments.contains(aAssignment)) { return false; }
    Application existingApplication = aAssignment.getApplication();
    boolean isNewApplication = existingApplication != null && !this.equals(existingApplication);
    if (isNewApplication)
    {
      aAssignment.setApplication(this);
    }
    else
    {
      assignments.add(aAssignment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAssignment(Assignment aAssignment)
  {
    boolean wasRemoved = false;
    //Unable to remove aAssignment, as it must always have a application
    if (!this.equals(aAssignment.getApplication()))
    {
      assignments.remove(aAssignment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAssignmentAt(Assignment aAssignment, int index)
  {  
    boolean wasAdded = false;
    if(addAssignment(aAssignment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssignments()) { index = numberOfAssignments() - 1; }
      assignments.remove(aAssignment);
      assignments.add(index, aAssignment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAssignmentAt(Assignment aAssignment, int index)
  {
    boolean wasAdded = false;
    if(assignments.contains(aAssignment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssignments()) { index = numberOfAssignments() - 1; }
      assignments.remove(aAssignment);
      assignments.add(index, aAssignment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAssignmentAt(aAssignment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=students.size(); i > 0; i--)
    {
      Student aStudent = students.get(i - 1);
      aStudent.delete();
    }
    for(int i=events.size(); i > 0; i--)
    {
      Event aEvent = events.get(i - 1);
      aEvent.delete();
    }
    for(int i=assignments.size(); i > 0; i--)
    {
      Assignment aAssignment = assignments.get(i - 1);
      aAssignment.delete();
    }
  }

}