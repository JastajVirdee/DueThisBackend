package model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3781.8b4a64e modeling language!*/


import java.util.*;

// line 41 "model.ump"
// line 83 "model.ump"
public class ExperiencedStudent extends StudentRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ExperiencedStudent Associations
  private List<Availability> availabilities;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ExperiencedStudent(Student aStudent)
  {
    super(aStudent);
    availabilities = new ArrayList<Availability>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Availability getAvailability(int index)
  {
    Availability aAvailability = availabilities.get(index);
    return aAvailability;
  }

  public List<Availability> getAvailabilities()
  {
    List<Availability> newAvailabilities = Collections.unmodifiableList(availabilities);
    return newAvailabilities;
  }

  public int numberOfAvailabilities()
  {
    int number = availabilities.size();
    return number;
  }

  public boolean hasAvailabilities()
  {
    boolean has = availabilities.size() > 0;
    return has;
  }

  public int indexOfAvailability(Availability aAvailability)
  {
    int index = availabilities.indexOf(aAvailability);
    return index;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfAvailabilitiesValid()
  {
    boolean isValid = numberOfAvailabilities() >= minimumNumberOfAvailabilities() && numberOfAvailabilities() <= maximumNumberOfAvailabilities();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfAvailabilities()
  {
    return 7;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAvailabilities()
  {
    return 7;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfAvailabilities()
  {
    return 7;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Availability addAvailability(Day aDay, int aHoursAvailable)
  {
    if (numberOfAvailabilities() >= maximumNumberOfAvailabilities())
    {
      return null;
    }
    else
    {
      return new Availability(aDay, aHoursAvailable, this);
    }
  }

  public boolean addAvailability(Availability aAvailability)
  {
    boolean wasAdded = false;
    if (availabilities.contains(aAvailability)) { return false; }
    if (numberOfAvailabilities() >= maximumNumberOfAvailabilities())
    {
      return wasAdded;
    }

    ExperiencedStudent existingExperiencedStudent = aAvailability.getExperiencedStudent();
    boolean isNewExperiencedStudent = existingExperiencedStudent != null && !this.equals(existingExperiencedStudent);

    if (isNewExperiencedStudent && existingExperiencedStudent.numberOfAvailabilities() <= minimumNumberOfAvailabilities())
    {
      return wasAdded;
    }

    if (isNewExperiencedStudent)
    {
      aAvailability.setExperiencedStudent(this);
    }
    else
    {
      availabilities.add(aAvailability);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAvailability(Availability aAvailability)
  {
    boolean wasRemoved = false;
    //Unable to remove aAvailability, as it must always have a experiencedStudent
    if (this.equals(aAvailability.getExperiencedStudent()))
    {
      return wasRemoved;
    }

    //experiencedStudent already at minimum (7)
    if (numberOfAvailabilities() <= minimumNumberOfAvailabilities())
    {
      return wasRemoved;
    }
    availabilities.remove(aAvailability);
    wasRemoved = true;
    return wasRemoved;
  }

  public void delete()
  {
    for(int i=availabilities.size(); i > 0; i--)
    {
      Availability aAvailability = availabilities.get(i - 1);
      aAvailability.delete();
    }
    super.delete();
  }

}