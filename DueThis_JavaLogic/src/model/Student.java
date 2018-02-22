package model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3802.c2696fa modeling language!*/


import java.util.*;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;

// line 14 "model.ump"
// line 55 "model.ump"
public class Student
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Student Attributes
  private String id;
  private String name;
  private boolean experienced;
  private int sundayAvailability;
  private int mondayAvailability;
  private int tuesdayAvailability;
  private int wednesdayAvailability;
  private int thursdayAvailability;
  private int fridayAvailability;
  private int saturdayAvailability;

  //Student Associations
  private List<Assignment> assignments;
  private List<Event> events;
  private Application application;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Student(String aId, String aName, boolean aExperienced, int aSundayAvailability, int aMondayAvailability, int aTuesdayAvailability, int aWednesdayAvailability, int aThursdayAvailability, int aFridayAvailability, int aSaturdayAvailability, Application aApplication)
  {
    id = aId;
    name = aName;
    experienced = aExperienced;
    sundayAvailability = aSundayAvailability;
    mondayAvailability = aMondayAvailability;
    tuesdayAvailability = aTuesdayAvailability;
    wednesdayAvailability = aWednesdayAvailability;
    thursdayAvailability = aThursdayAvailability;
    fridayAvailability = aFridayAvailability;
    saturdayAvailability = aSaturdayAvailability;
    assignments = new ArrayList<Assignment>();
    events = new ArrayList<Event>();
    boolean didAddApplication = setApplication(aApplication);
    if (!didAddApplication)
    {
      throw new RuntimeException("Unable to create student due to application");
    }
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

  public boolean setExperienced(boolean aExperienced)
  {
    boolean wasSet = false;
    experienced = aExperienced;
    wasSet = true;
    return wasSet;
  }

  public boolean setSundayAvailability(int aSundayAvailability)
  {
    boolean wasSet = false;
    sundayAvailability = aSundayAvailability;
    wasSet = true;
    return wasSet;
  }

  public boolean setMondayAvailability(int aMondayAvailability)
  {
    boolean wasSet = false;
    mondayAvailability = aMondayAvailability;
    wasSet = true;
    return wasSet;
  }

  public boolean setTuesdayAvailability(int aTuesdayAvailability)
  {
    boolean wasSet = false;
    tuesdayAvailability = aTuesdayAvailability;
    wasSet = true;
    return wasSet;
  }

  public boolean setWednesdayAvailability(int aWednesdayAvailability)
  {
    boolean wasSet = false;
    wednesdayAvailability = aWednesdayAvailability;
    wasSet = true;
    return wasSet;
  }

  public boolean setThursdayAvailability(int aThursdayAvailability)
  {
    boolean wasSet = false;
    thursdayAvailability = aThursdayAvailability;
    wasSet = true;
    return wasSet;
  }

  public boolean setFridayAvailability(int aFridayAvailability)
  {
    boolean wasSet = false;
    fridayAvailability = aFridayAvailability;
    wasSet = true;
    return wasSet;
  }

  public boolean setSaturdayAvailability(int aSaturdayAvailability)
  {
    boolean wasSet = false;
    saturdayAvailability = aSaturdayAvailability;
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

  public boolean getExperienced()
  {
    return experienced;
  }

  public int getSundayAvailability()
  {
    return sundayAvailability;
  }

  public int getMondayAvailability()
  {
    return mondayAvailability;
  }

  public int getTuesdayAvailability()
  {
    return tuesdayAvailability;
  }

  public int getWednesdayAvailability()
  {
    return wednesdayAvailability;
  }

  public int getThursdayAvailability()
  {
    return thursdayAvailability;
  }

  public int getFridayAvailability()
  {
    return fridayAvailability;
  }

  public int getSaturdayAvailability()
  {
    return saturdayAvailability;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isExperienced()
  {
    return experienced;
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
  /* Code from template association_GetOne */
  public Application getApplication()
  {
    return application;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAssignments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Assignment addAssignment(String aId, String aName, String aCourse, Date aDueDate, boolean aIsCompleted, float aGradeWeight, Duration aCompletionTime, Application aApplication)
  {
    return new Assignment(aId, aName, aCourse, aDueDate, aIsCompleted, aGradeWeight, aCompletionTime, this, aApplication);
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
  public Event addEvent(String aId, String aName, Date aDate, Time aStartTime, Time aEndTime, boolean aRepeatedWeekly, Application aApplication)
  {
    return new Event(aId, aName, aDate, aStartTime, aEndTime, aRepeatedWeekly, this, aApplication);
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
  /* Code from template association_SetOneToMany */
  public boolean setApplication(Application aApplication)
  {
    boolean wasSet = false;
    if (aApplication == null)
    {
      return wasSet;
    }

    Application existingApplication = application;
    application = aApplication;
    if (existingApplication != null && !existingApplication.equals(aApplication))
    {
      existingApplication.removeStudent(this);
    }
    application.addStudent(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
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
    Application placeholderApplication = application;
    this.application = null;
    if(placeholderApplication != null)
    {
      placeholderApplication.removeStudent(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "," +
            "experienced" + ":" + getExperienced()+ "," +
            "sundayAvailability" + ":" + getSundayAvailability()+ "," +
            "mondayAvailability" + ":" + getMondayAvailability()+ "," +
            "tuesdayAvailability" + ":" + getTuesdayAvailability()+ "," +
            "wednesdayAvailability" + ":" + getWednesdayAvailability()+ "," +
            "thursdayAvailability" + ":" + getThursdayAvailability()+ "," +
            "fridayAvailability" + ":" + getFridayAvailability()+ "," +
            "saturdayAvailability" + ":" + getSaturdayAvailability()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "application = "+(getApplication()!=null?Integer.toHexString(System.identityHashCode(getApplication())):"null");
  }
}