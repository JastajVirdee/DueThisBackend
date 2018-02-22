package controller;

import java.util.UUID;

import model.Application;
import model.Student;

public class AccountController {
	
	public boolean createAccount(String uname, String pword, String email, boolean experiencedStudent, int sun, int mon, int tues, int wed, int thurs, int fri, int sat) throws InvalidInputException {
		String error = "";
		
		//Check if required fields are null
		if (uname == null || uname.trim().length() == 0) {
			
			error += "Username is required to create an account! ";
		}
		
		if (pword == null || pword.trim().length() == 0) {
			
			error += "Password is required to create an account! ";
		}
		
		if (email == null || email.trim().length() == 0) {
			 
			error += "Email is required to create an account! ";
		}
		else if (!email.contains("@")) {
			
			error += "Email address is invalid! ";
		}
		
		//Check if username or email address is already in use
		Application manager = Application.getInstance();
		
		int numAccounts = manager.numberOfStudents();
		
		for(int i=0; i<numAccounts; i++) {
			
			if (manager.getStudent(i).getUsername().equals(uname.trim())) {
				error += "Username is already in use! ";
			}
			
			if (manager.getStudent(i).getEmail().equals(email.trim())) {
				error += "Email is already in use! ";
			}
		}
		
		//Check if the availabilities are valid integers if the student wishes to be experienced
		if (experiencedStudent) {
			
			// Make sure hours between 0 and 24 inclusive
			if (sun < 0 || sun > 24)
			{
				error += "Sunday hours must be between 0 and 24! ";
			}
			if (mon < 0 || mon > 24)
			{
				error += "Monday hours must be between 0 and 24! ";
			}
			if (tues < 0 || tues > 24)
			{
				error += "Tuesday hours must be between 0 and 24! ";
			}
			if (wed < 0 || wed > 24)
			{
				error += "Wednesday hours must be between 0 and 24! ";
			}
			if (thurs < 0 || thurs > 24)
			{
				error += "Thursday hours must be between 0 and 24! ";
			}
			if (fri < 0 || fri > 24)
			{
				error += "Friday hours must be between 0 and 24! ";
			}
			if (sat < 0 || sat > 24)
			{
				error += "Saturday hours must be between 0 and 24! ";
			}
		}
		
		//If there is any errors, throw the error
		if (error.length() > 0) {
			throw new InvalidInputException(error);
		}
		
		
	
		
		
		String id = UUID.randomUUID().toString();
				

		//If the student wasnt experienced set all the availabilities to zero
		if (!experiencedStudent) {
			sun = 0; mon = 0; tues = 0; wed = 0; thurs = 0; fri = 0; sat = 0;
		}
		
		//Create the student
		Student s = new Student(id, uname, pword, email, experiencedStudent, sun, mon, tues, wed, thurs, fri, sat, manager);
		return true;
		
	}

}
