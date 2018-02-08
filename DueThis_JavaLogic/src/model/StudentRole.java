package model;


/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3785.4f7512d modeling language!*/



// line 32 "model.ump"
// line 68 "model.ump"
public class StudentRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StudentRole Associations
  private Student student;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StudentRole(Student aStudent)
  {
    boolean didAddStudent = setStudent(aStudent);
    if (!didAddStudent)
    {
      throw new RuntimeException("Unable to create studentRole due to student");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
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
      existingStudent.removeStudentRole(this);
    }
    student.addStudentRole(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Student placeholderStudent = student;
    this.student = null;
    if(placeholderStudent != null)
    {
      placeholderStudent.removeStudentRole(this);
    }
  }

}