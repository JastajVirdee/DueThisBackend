package controllerTests;

import static org.junit.Assert.*;
import model.Application;
import model.Student;

import org.junit.After;
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
            ac.createAccount(username, password, email, experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
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
            ac.createAccount(username, password, email, !experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
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

    @Test
    public void testCreateNoUsername() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());
        String error = "";
        try {
            ac.createAccount(null, password, email, experienced, avail, avail, avail, avail, avail,
                    avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Username is required to create an account! ");

        error = "";

        try {
            ac.createAccount("", password, email, experienced, avail, avail, avail, avail, avail,
                    avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Username is required to create an account! ");

        error = "";

        try {
            ac.createAccount("    ", password, email, experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Username is required to create an account! ");
    }

    @Test
    public void testCreateNoEmail() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());
        String error = "";
        try {
            ac.createAccount(username, password, null, experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Email is required to create an account! ");

        error = "";

        try {
            ac.createAccount(username, password, "", experienced, avail, avail, avail, avail, avail,
                    avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Email is required to create an account! ");

        error = "";

        try {
            ac.createAccount(username, password, "    ", experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Email is required to create an account! ");
    }

    @Test
    public void testCreateNoPassword() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());
        String error = "";
        try {
            ac.createAccount(username, null, email, experienced, avail, avail, avail, avail, avail,
                    avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Password is required to create an account! ");

        error = "";

        try {
            ac.createAccount(username, "", email, experienced, avail, avail, avail, avail, avail,
                    avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Password is required to create an account! ");

        error = "";

        try {
            ac.createAccount(username, "   ", email, experienced, avail, avail, avail, avail, avail,
                    avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Password is required to create an account! ");
    }

    @Test
    public void testCreateInvalidEmail() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());

        String invalidEmail = "invalidEmail";
        String error = "";

        try {
            ac.createAccount(username, password, invalidEmail, experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error, "Email address is invalid! ");
    }

    @Test
    public void testCreateDuplicateUsername() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());

        try {
            ac.createAccount(username, password, email, experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        assertEquals(1, manager.numberOfStudents());

        assertEquals(username, manager.getStudent(0).getUsername());

        String newEmail = "mikesmith@gmail.com";
        String newPass = "abc123";

        String error = "";

        try {
            ac.createAccount(username, newPass, newEmail, experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(1, manager.numberOfStudents());
        assertEquals(error, "Username is already in use! ");
    }

    @Test
    public void testCreateDuplicateEmail() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());

        try {
            ac.createAccount(username, password, email, experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        assertEquals(1, manager.numberOfStudents());

        assertEquals(email, manager.getStudent(0).getEmail());

        String newUsername = "mikesmith";
        String newPass = "abc123";

        String error = "";

        try {
            ac.createAccount(newUsername, newPass, email, experienced, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(1, manager.numberOfStudents());
        assertEquals(error, "Email is already in use! ");
    }

    @Test
    public void testCreateNegativeAvailability() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());

        int negAvail = -1;

        String error = "";

        try {
            ac.createAccount(username, password, email, experienced, negAvail, negAvail, negAvail,
                    negAvail, negAvail, negAvail, negAvail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error,
                "Sunday hours must be between 0 and 24! Monday hours must be between 0 and 24! "
                        + "Tuesday hours must be between 0 and 24! Wednesday hours must be between 0 and 24! "
                        + "Thursday hours must be between 0 and 24! Friday hours must be between 0 and 24! "
                        + "Saturday hours must be between 0 and 24! ");

    }

    @Test
    public void testCreateOver24Availability() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());

        int over24Avail = 25;

        String error = "";

        try {
            ac.createAccount(username, password, email, experienced, over24Avail, over24Avail,
                    over24Avail, over24Avail, over24Avail, over24Avail, over24Avail);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        assertEquals(0, manager.numberOfStudents());
        assertEquals(error,
                "Sunday hours must be between 0 and 24! Monday hours must be between 0 and 24! "
                        + "Tuesday hours must be between 0 and 24! Wednesday hours must be between 0 and 24! "
                        + "Thursday hours must be between 0 and 24! Friday hours must be between 0 and 24! "
                        + "Saturday hours must be between 0 and 24! ");

    }

    @Test
    public void testDeleteAccountValidInfo() {
        AccountController ac = new AccountController();

        Application manager = Application.getInstance();

        // Create some accounts
        try {
            ac.createAccount("Person", "1234", "email@email.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
            ac.createAccount("User", "pass", "email@gmail.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            ac.deleteAccount("User", "pass");
        } catch (InvalidInputException e) {
            fail();
        }

        // Check is the user name is not in the application anymore
        for (Student s : manager.getStudents()) {
            assertEquals(false, s.getUsername().equals("User"));
        }

        // Check that the number of students went down
        assertEquals(1, manager.numberOfStudents());
    }

    @Test
    public void testDeleteAccountWrongPassword() {
        AccountController ac = new AccountController();

        Application manager = Application.getInstance();

        // Create some accounts
        try {
            ac.createAccount("Person", "1234", "email@email.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
            ac.createAccount("User", "pass", "email@gmail.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            ac.deleteAccount("User", "1234");
        } catch (InvalidInputException e) {
            // Check that nothing was altered
            assertEquals(2, manager.numberOfStudents());
            assertEquals("Password entered does not match", e.getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testDeleteAccountNoStudents() {
        AccountController ac = new AccountController();

        Application manager = Application.getInstance();

        assertEquals(0, manager.numberOfStudents());

        try {
            ac.deleteAccount("User", "1234");
        } catch (InvalidInputException e) {
            // Check that nothing was altered
            assertEquals("No students have been created", e.getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testLogInSuccessUsername() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();
        Student s = null;

        try {
            ac.createAccount("Person", "1234", "email@email.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            s = ac.logIn("Person", "1234");
        } catch (InvalidInputException e) {
            fail();
        }

        assertEquals("Person", s.getUsername());
        assertEquals("1234", s.getPassword());
        assertEquals("email@email.com", s.getEmail());
        assertEquals(experienced, s.getExperienced());
    }

    @Test
    public void testLogInSuccessEmail() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();
        Student s = null;

        try {
            ac.createAccount("Person", "1234", "email@email.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            s = ac.logIn("email@email.com", "1234");
        } catch (InvalidInputException e) {
            fail();
        }

        assertEquals("Person", s.getUsername());
        assertEquals("1234", s.getPassword());
        assertEquals("email@email.com", s.getEmail());
        assertEquals(experienced, s.getExperienced());
    }

    @Test
    public void testLogInNoUsernameOrEmail() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();
        Student s = null;

        try {
            ac.createAccount("Person", "1234", "email@email.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            s = ac.logIn("    ", "1234");
        } catch (InvalidInputException e) {
            assertEquals("Username or email is required to log in! ", e.getMessage());
        }

        try {
            s = ac.logIn(null, "1234");
        } catch (InvalidInputException e) {
            assertEquals("Username or email is required to log in! ", e.getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testLogInNoPassword() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();
        Student s = null;

        try {
            ac.createAccount("Person", "1234", "email@email.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            s = ac.logIn("Person", "     ");
        } catch (InvalidInputException e) {
            assertEquals("Password is required to log in! ", e.getMessage());
        }

        try {
            s = ac.logIn("Person", null);
        } catch (InvalidInputException e) {
            assertEquals("Password is required to log in! ", e.getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testLogInNoMatch() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();
        Student s = null;

        try {
            ac.createAccount("Person", "1234", "email@email.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            s = ac.logIn("NotPerson", "1234");
        } catch (InvalidInputException e) {
            assertEquals("Invalid Username/Email or Password!", e.getMessage());
        }

        try {
            s = ac.logIn("email@notemail.com", "1234");
        } catch (InvalidInputException e) {
            assertEquals("Invalid Username/Email or Password!", e.getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testSwitchSuccessNoviceToExperienced() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        try {
            ac.createAccount("Person", "1234", "email@email.com", false, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        Student s = manager.getStudent(0);

        try {
            ac.changeRole(s, experienced, avail, avail, avail, avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        assertEquals(experienced, manager.getStudent(0).getExperienced());
        assertEquals(avail, manager.getStudent(0).getSundayAvailability());
        assertEquals(avail, manager.getStudent(0).getMondayAvailability());
        assertEquals(avail, manager.getStudent(0).getTuesdayAvailability());
        assertEquals(avail, manager.getStudent(0).getWednesdayAvailability());
        assertEquals(avail, manager.getStudent(0).getThursdayAvailability());
        assertEquals(avail, manager.getStudent(0).getFridayAvailability());
        assertEquals(avail, manager.getStudent(0).getSaturdayAvailability());
    }

    @Test
    public void testSwitchSuccessExperiencedToNovice() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        try {
            ac.createAccount("Person", "1234", "email@email.com", experienced, avail, avail, avail,
                    avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        Student s = manager.getStudent(0);

        try {
            ac.changeRole(s, false, avail, avail, avail, avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        assertEquals(false, manager.getStudent(0).getExperienced());
        assertEquals(0, manager.getStudent(0).getSundayAvailability());
        assertEquals(0, manager.getStudent(0).getMondayAvailability());
        assertEquals(0, manager.getStudent(0).getTuesdayAvailability());
        assertEquals(0, manager.getStudent(0).getWednesdayAvailability());
        assertEquals(0, manager.getStudent(0).getThursdayAvailability());
        assertEquals(0, manager.getStudent(0).getFridayAvailability());
        assertEquals(0, manager.getStudent(0).getSaturdayAvailability());
    }

    @Test
    public void testSwitchInvalidStudent() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        try {
            ac.changeRole(null, experienced, avail, avail, avail, avail, avail, avail, avail);
        } catch (InvalidInputException e) {
            assertEquals("A valid student must be passed! ", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void testSwitchNegativeHours() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        try {
            ac.createAccount("Person", "1234", "email@email.com", false, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            ac.changeRole(manager.getStudent(0), experienced, -avail, -avail, -avail, -avail,
                    -avail, -avail, -avail);
        } catch (InvalidInputException e) {
            assertEquals(
                    "Sunday hours must be between 0 and 24! Monday hours must be between 0 and 24! "
                            + "Tuesday hours must be between 0 and 24! Wednesday hours must be between 0 and 24! "
                            + "Thursday hours must be between 0 and 24! Friday hours must be between 0 and 24! "
                            + "Saturday hours must be between 0 and 24! ",
                    e.getMessage());
            return;
        }

        fail();
    }

    @Test
    public void testSwitchHoursTooBig() {
        AccountController ac = new AccountController();
        Application manager = Application.getInstance();

        try {
            ac.createAccount("Person", "1234", "email@email.com", false, avail, avail, avail, avail,
                    avail, avail, avail);
        } catch (InvalidInputException e) {
            fail();
        }

        try {
            ac.changeRole(manager.getStudent(0), experienced, 25, 25, 25, 25, 25, 25, 25);
        } catch (InvalidInputException e) {
            assertEquals(
                    "Sunday hours must be between 0 and 24! Monday hours must be between 0 and 24! "
                            + "Tuesday hours must be between 0 and 24! Wednesday hours must be between 0 and 24! "
                            + "Thursday hours must be between 0 and 24! Friday hours must be between 0 and 24! "
                            + "Saturday hours must be between 0 and 24! ",
                    e.getMessage());
            return;
        }

        fail();
    }

}
