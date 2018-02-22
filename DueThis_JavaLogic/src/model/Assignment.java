package model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3802.c2696fa modeling language!*/


import java.sql.Date;
import java.time.Duration;

// line 2 "model.ump"
// line 49 "model.ump"
public class Assignment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Assignment Attributes
  private String id;
  private String name;
  private String course;
  private Date dueDate;
  private boolean isCompleted;
  private float gradeWeight;
  private Duration completionTime;

  //Assignment Associations
  private Student student;
  private Application application;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Assignment(String aId, String aName, String aCourse, Date aDueDate, boolean aIsCompleted, float aGradeWeight, Duration aCompletionTime, Student aStudent, Application aApplication)
  {
    id = aId;
    name = aName;
    course = aCourse;
    dueDate = aDueDate;
    isCompleted = aIsCompleted;
    gradeWeight = aGradeWeight;
    completionTime = aCompletionTime;
    boolean didAddStudent = setStudent(aStudent);
    if (!didAddStudent)
    {
      throw new RuntimeException("Unable to create assignment due to student");
    }
    boolean didAddApplication = setApplication(aApplication);
    if (!didAddApplication)
    {
      throw new RuntimeException("Unable to create assignment due to application");
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

  public boolean setCourse(String aCourse)
  {
    boolean wasSet = false;
    course = aCourse;
    wasSet = true;
    return wasSet;
  }

  public boolean setDueDate(Date aDueDate)
  {
    boolean wasSet = false;
    dueDate = aDueDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsCompleted(boolean aIsCompleted)
  {
    boolean wasSet = false;
    isCompleted = aIsCompleted;
    wasSet = true;
    return wasSet;
  }

  public boolean setGradeWeight(float aGradeWeight)
  {
    boolean wasSet = false;
    gradeWeight = aGradeWeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setCompletionTime(Duration aCompletionTime)
  {
    boolean wasSet = false;
    completionTime = aCompletionTime;
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

  public String getCourse()
  {
    return course;
  }

  public Date getDueDate()
  {
    return dueDate;
  }

  public boolean getIsCompleted()
  {
    return isCompleted;
  }

  public float getGradeWeight()
  {
    return gradeWeight;
  }

  public Duration getCompletionTime()
  {
    return completionTime;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsCompleted()
  {
    return isCompleted;
  }
  /* Code from template association_GetOne */
  public Student getStudent()
  {
    return student;
  }
  /* Code from template association_GetOne */
  public Application getApplication()
  {
    return application;
  }
  /* Code from template association_SetOneToMany */
  public boolean setStudent(Student aStudent)
  {
    boolean wasSet = false;
    if (aStudent == null)
    {
      return wasSet;
    }

    Student existingStudent = student;
    student = aStudent;
    if (existingStudent != null && !existingStudent.equals(aStudent))
    {
      existingStudent.removeAssignment(this);
    }
    student.addAssignment(this);
    wasSet = true;
    return wasSet;
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
      existingApplication.removeAssignment(this);
    }
    application.addAssignment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Student placeholderStudent = student;
    this.student = null;
    if(placeholderStudent != null)
    {
      placeholderStudent.removeAssignment(this);
    }
    Application placeholderApplication = application;
    this.application = null;
    if(placeholderApplication != null)
    {
      placeholderApplication.removeAssignment(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "," +
            "course" + ":" + getCourse()+ "," +
            "isCompleted" + ":" + getIsCompleted()+ "," +
            "gradeWeight" + ":" + getGradeWeight()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dueDate" + "=" + (getDueDate() != null ? !getDueDate().equals(this)  ? getDueDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "completionTime" + "=" + (getCompletionTime() != null ? !getCompletionTime().equals(this)  ? getCompletionTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "student = "+(getStudent()!=null?Integer.toHexString(System.identityHashCode(getStudent())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "application = "+(getApplication()!=null?Integer.toHexString(System.identityHashCode(getApplication())):"null");
  }
}