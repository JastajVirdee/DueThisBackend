package model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3789.8ef58d1 modeling language!*/



// line 47 "model.ump"
// line 86 "model.ump"
public class Availability
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Day { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Availability Attributes
  private Day day;
  private int hoursAvailable;

  //Availability Associations
  private ExperiencedStudent experiencedStudent;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Availability(Day aDay, int aHoursAvailable, ExperiencedStudent aExperiencedStudent)
  {
    day = aDay;
    hoursAvailable = aHoursAvailable;
    boolean didAddExperiencedStudent = setExperiencedStudent(aExperiencedStudent);
    if (!didAddExperiencedStudent)
    {
      throw new RuntimeException("Unable to create availability due to experiencedStudent");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDay(Day aDay)
  {
    boolean wasSet = false;
    day = aDay;
    wasSet = true;
    return wasSet;
  }

  public boolean setHoursAvailable(int aHoursAvailable)
  {
    boolean wasSet = false;
    hoursAvailable = aHoursAvailable;
    wasSet = true;
    return wasSet;
  }

  public Day getDay()
  {
    return day;
  }

  public int getHoursAvailable()
  {
    return hoursAvailable;
  }
  /* Code from template association_GetOne */
  public ExperiencedStudent getExperiencedStudent()
  {
    return experiencedStudent;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setExperiencedStudent(ExperiencedStudent aExperiencedStudent)
  {
    boolean wasSet = false;
    //Must provide experiencedStudent to availability
    if (aExperiencedStudent == null)
    {
      return wasSet;
    }

    //experiencedStudent already at maximum (7)
    if (aExperiencedStudent.numberOfAvailabilities() >= ExperiencedStudent.maximumNumberOfAvailabilities())
    {
      return wasSet;
    }
    
    ExperiencedStudent existingExperiencedStudent = experiencedStudent;
    experiencedStudent = aExperiencedStudent;
    if (existingExperiencedStudent != null && !existingExperiencedStudent.equals(aExperiencedStudent))
    {
      boolean didRemove = existingExperiencedStudent.removeAvailability(this);
      if (!didRemove)
      {
        experiencedStudent = existingExperiencedStudent;
        return wasSet;
      }
    }
    experiencedStudent.addAvailability(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ExperiencedStudent placeholderExperiencedStudent = experiencedStudent;
    this.experiencedStudent = null;
    if(placeholderExperiencedStudent != null)
    {
      placeholderExperiencedStudent.removeAvailability(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "hoursAvailable" + ":" + getHoursAvailable()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "day" + "=" + (getDay() != null ? !getDay().equals(this)  ? getDay().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "experiencedStudent = "+(getExperiencedStudent()!=null?Integer.toHexString(System.identityHashCode(getExperiencedStudent())):"null");
  }
}
