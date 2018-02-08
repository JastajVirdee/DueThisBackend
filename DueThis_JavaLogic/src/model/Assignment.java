/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3785.4f7512d modeling language!*/


import java.sql.Date;

// line 5 "model.ump"
// line 53 "model.ump"
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
  private float gradeWeight;
  private Duration completitionTime;

  //Assignment Associations
  private Student student;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Assignment(String aId, String aName, String aCourse, Date aDueDate, float aGradeWeight, Duration aCompletitionTime, Student aStudent)
  {
    id = aId;
    name = aName;
    course = aCourse;
    dueDate = aDueDate;
    gradeWeight = aGradeWeight;
    completitionTime = aCompletitionTime;
    boolean didAddStudent = setStudent(aStudent);
    if (!didAddStudent)
    {
      throw new RuntimeException("Unable to create assignment due to student");
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

  public boolean setGradeWeight(float aGradeWeight)
  {
    boolean wasSet = false;
    gradeWeight = aGradeWeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setCompletitionTime(Duration aCompletitionTime)
  {
    boolean wasSet = false;
    completitionTime = aCompletitionTime;
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

  public float getGradeWeight()
  {
    return gradeWeight;
  }

  public Duration getCompletitionTime()
  {
    return completitionTime;
  }
  /* Code from template association_GetOne */
  public Student getStudent()
  {
    return student;
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

  public void delete()
  {
    Student placeholderStudent = student;
    this.student = null;
    if(placeholderStudent != null)
    {
      placeholderStudent.removeAssignment(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "," +
            "course" + ":" + getCourse()+ "," +
            "gradeWeight" + ":" + getGradeWeight()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dueDate" + "=" + (getDueDate() != null ? !getDueDate().equals(this)  ? getDueDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "completitionTime" + "=" + (getCompletitionTime() != null ? !getCompletitionTime().equals(this)  ? getCompletitionTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "student = "+(getStudent()!=null?Integer.toHexString(System.identityHashCode(getStudent())):"null");
  }
}