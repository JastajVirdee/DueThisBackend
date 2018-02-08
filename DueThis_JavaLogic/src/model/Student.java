/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3785.4f7512d modeling language!*/


import java.util.*;
import java.sql.Date;

// line 16 "model.ump"
// line 59 "model.ump"
public class Student
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Student Attributes
  private String id;
  private String name;

  //Student Associations
  private List<StudentRole> studentRoles;
  private List<Assignment> assignments;
  private List<Event> events;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Student(String aId, String aName)
  {
    id = aId;
    name = aName;
    studentRoles = new ArrayList<StudentRole>();
    assignments = new ArrayList<Assignment>();
    events = new ArrayList<Event>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(String aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template association_GetMany */
  public StudentRole getStudentRole(int index)
  {
    StudentRole aStudentRole = studentRoles.get(index);
    return aStudentRole;
  }

  public List<StudentRole> getStudentRoles()
  {
    List<StudentRole> newStudentRoles = Collections.unmodifiableList(studentRoles);
    return newStudentRoles;
  }

  public int numberOfStudentRoles()
  {
    int number = studentRoles.size();
    return number;
  }

  public boolean hasStudentRoles()
  {
    boolean has = studentRoles.size() > 0;
    return has;
  }

  public int indexOfStudentRole(StudentRole aStudentRole)
  {
    int index = studentRoles.indexOf(aStudentRole);
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfStudentRoles()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public StudentRole addStudentRole()
  {
    return new StudentRole(this);
  }

  public boolean addStudentRole(StudentRole aStudentRole)
  {
    boolean wasAdded = false;
    if (studentRoles.contains(aStudentRole)) { return false; }
    Student existingStudent = aStudentRole.getStudent();
    boolean isNewStudent = existingStudent != null && !this.equals(existingStudent);
    if (isNewStudent)
    {
      aStudentRole.setStudent(this);
    }
    else
    {
      studentRoles.add(aStudentRole);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeStudentRole(StudentRole aStudentRole)
  {
    boolean wasRemoved = false;
    //Unable to remove aStudentRole, as it must always have a student
    if (!this.equals(aStudentRole.getStudent()))
    {
      studentRoles.remove(aStudentRole);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addStudentRoleAt(StudentRole aStudentRole, int index)
  {  
    boolean wasAdded = false;
    if(addStudentRole(aStudentRole))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStudentRoles()) { index = numberOfStudentRoles() - 1; }
      studentRoles.remove(aStudentRole);
      studentRoles.add(index, aStudentRole);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStudentRoleAt(StudentRole aStudentRole, int index)
  {
    boolean wasAdded = false;
    if(studentRoles.contains(aStudentRole))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStudentRoles()) { index = numberOfStudentRoles() - 1; }
      studentRoles.remove(aStudentRole);
      studentRoles.add(index, aStudentRole);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStudentRoleAt(aStudentRole, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAssignments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Assignment addAssignment(String aId, String aName, String aCourse, Date aDueDate, float aGradeWeight, Duration aCompletionTime)
  {
    return new Assignment(aId, aName, aCourse, aDueDate, aGradeWeight, aCompletionTime, this);
  }

  public boolean addAssignment(Assignment aAssignment)
  {
    boolean wasAdded = false;
    if (assignments.contains(aAssignment)) { return false; }
    Student existingStudent = aAssignment.getStudent();
    boolean isNewStudent = existingStudent != null && !this.equals(existingStudent);
    if (isNewStudent)
    {
      aAssignment.setStudent(this);
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
    //Unable to remove aAssignment, as it must always have a student
    if (!this.equals(aAssignment.getStudent()))
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEvents()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Event addEvent(String aId, String aName, Datetime aStartTime, Datetime aEndTime)
  {
    return new Event(aId, aName, aStartTime, aEndTime, this);
  }

  public boolean addEvent(Event aEvent)
  {
    boolean wasAdded = false;
    if (events.contains(aEvent)) { return false; }
    Student existingStudent = aEvent.getStudent();
    boolean isNewStudent = existingStudent != null && !this.equals(existingStudent);
    if (isNewStudent)
    {
      aEvent.setStudent(this);
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
    //Unable to remove aEvent, as it must always have a student
    if (!this.equals(aEvent.getStudent()))
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

  public void delete()
  {
    for(int i=studentRoles.size(); i > 0; i--)
    {
      StudentRole aStudentRole = studentRoles.get(i - 1);
      aStudentRole.delete();
    }
    for(int i=assignments.size(); i > 0; i--)
    {
      Assignment aAssignment = assignments.get(i - 1);
      aAssignment.delete();
    }
    for(int i=events.size(); i > 0; i--)
    {
      Event aEvent = events.get(i - 1);
      aEvent.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "]";
  }
}