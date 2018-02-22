package controllerTests;

import static org.junit.Assert.*;
import model.Application;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.AccountController;
import controller.InvalidInputException;

public class TestAccountController {

	String username = "mike";
	String password = "abcd1234";
	String email = "mike@gmail.com";
	boolean experienced = true;
	int avail = 3;
	
	@After
	public void tearDown() throws Exception {
		Application manager = Application.getInstance();
		manager.delete();
	}
	
	@Test
	public void testCreateExperiencedAccountSuccess() {
		AccountController ac = new AccountController();
		
		Application manager = Application.getInstance();
		
		assertEquals(0, manager.numberOfStudents());
		
		try {
			ac.createAccount(username, password, email, experienced, avail, avail, avail, avail, avail, avail, avail);
		}
		catch (InvalidInputException e) {
			fail();
		}
		
		assertEquals(1, manager.numberOfStudents());
		
		assertEquals(username, manager.getStudent(0).getUsername());
		assertEquals(password, manager.getStudent(0).getPassword());
		assertEquals(email, manager.getStudent(0).getEmail());
		
		assertEquals(true, manager.getStudent(0).getExperienced());
		
		assertEquals(avail, manager.getStudent(0).getSundayAvailability());
		assertEquals(avail, manager.getStudent(0).getMondayAvailability());
		assertEquals(avail, manager.getStudent(0).getTuesdayAvailability());
		assertEquals(avail, manager.getStudent(0).getWednesdayAvailability());
		assertEquals(avail, manager.getStudent(0).getThursdayAvailability());
		assertEquals(avail, manager.getStudent(0).getFridayAvailability());
		assertEquals(avail, manager.getStudent(0).getSaturdayAvailability());

	}
	
	@Test
	public void testCreateNoviceAccountSuccess() {
		AccountController ac = new AccountController();
		Application manager = Application.getInstance();

		assertEquals(0, manager.numberOfStudents());
		
		try {
			ac.createAccount(username, password, email, !experienced, avail, avail, avail, avail, avail, avail, avail);
		}
		catch (InvalidInputException e) {
			fail();
		}
		
		assertEquals(1, manager.numberOfStudents());
		
		assertEquals(username, manager.getStudent(0).getUsername());
		assertEquals(password, manager.getStudent(0).getPassword());
		assertEquals(email, manager.getStudent(0).getEmail());
		
		assertEquals(false, manager.getStudent(0).getExperienced());
		
		assertEquals(0, manager.getStudent(0).getSundayAvailability());
		assertEquals(0, manager.getStudent(0).getMondayAvailability());
		assertEquals(0, manager.getStudent(0).getTuesdayAvailability());
		assertEquals(0, manager.getStudent(0).getWednesdayAvailability());
		assertEquals(0, manager.getStudent(0).getThursdayAvailability());
		assertEquals(0, manager.getStudent(0).getFridayAvailability());
		assertEquals(0, manager.getStudent(0).getSaturdayAvailability());
	}

}
