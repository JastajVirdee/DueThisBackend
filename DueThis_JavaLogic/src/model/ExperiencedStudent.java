package model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3789.8ef58d1 modeling language!*/



// line 41 "model.ump"
// line 79 "model.ump"
public class ExperiencedStudent extends StudentRole
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ExperiencedStudent Attributes
  private int sundayAvailability;
  private int mondayAvailability;
  private int tuesdayAvailability;
  private int wednesdayAvailability;
  private int thursdayAvailability;
  private int fridayAvailability;
  private int saturdayAvailability;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ExperiencedStudent(Student aStudent, int aSundayAvailability, int aMondayAvailability, int aTuesdayAvailability, int aWednesdayAvailability, int aThursdayAvailability, int aFridayAvailability, int aSaturdayAvailability)
  {
    super(aStudent);
    sundayAvailability = aSundayAvailability;
    mondayAvailability = aMondayAvailability;
    tuesdayAvailability = aTuesdayAvailability;
    wednesdayAvailability = aWednesdayAvailability;
    thursdayAvailability = aThursdayAvailability;
    fridayAvailability = aFridayAvailability;
    saturdayAvailability = aSaturdayAvailability;
  }

  //------------------------
  // INTERFACE
  //------------------------

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

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "sundayAvailability" + ":" + getSundayAvailability()+ "," +
            "mondayAvailability" + ":" + getMondayAvailability()+ "," +
            "tuesdayAvailability" + ":" + getTuesdayAvailability()+ "," +
            "wednesdayAvailability" + ":" + getWednesdayAvailability()+ "," +
            "thursdayAvailability" + ":" + getThursdayAvailability()+ "," +
            "fridayAvailability" + ":" + getFridayAvailability()+ "," +
            "saturdayAvailability" + ":" + getSaturdayAvailability()+ "]";
  }
}