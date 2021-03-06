package controller;

import java.util.List;
import java.util.UUID;

import model.Application;
import model.Student;
import persistence.SQLiteInterface;

public class AccountController {
    public boolean createAccount(String uname, String pword, String email,
            boolean experiencedStudent, int sun, int mon, int tues, int wed, int thurs, int fri,
            int sat) throws InvalidInputException {
        String error = "";

        // - Check if required fields are null
        if (uname == null || uname.trim().length() == 0)
            error += "Username is required to create an account! ";

        if (pword == null || pword.trim().length() == 0)
            error += "Password is required to create an account! ";

        if (email == null || email.trim().length() == 0)
            error += "Email is required to create an account! ";
        else if (!email.contains("@"))
            error += "Email address is invalid! ";

        // - Check if username or email address is already in use
        Application manager = Application.getInstance();
        int numAccounts = manager.numberOfStudents();

        // - TODO Could be done in persistence using integrity constraints
        for (int i = 0; i < numAccounts; i++) {
            if (manager.getStudent(i).getUsername().equals(uname.trim())) {
                error += "Username is already in use! ";
            }

            if (manager.getStudent(i).getEmail().equals(email.trim())) {
                error += "Email is already in use! ";
            }
        }

        // - TODO Code duplication
        if (experiencedStudent) {
            if (sun < 0 || sun > 24) {
                error += "Sunday hours must be between 0 and 24! ";
            }

            if (mon < 0 || mon > 24) {
                error += "Monday hours must be between 0 and 24! ";
            }

            if (tues < 0 || tues > 24) {
                error += "Tuesday hours must be between 0 and 24! ";
            }

            if (wed < 0 || wed > 24) {
                error += "Wednesday hours must be between 0 and 24! ";
            }

            if (thurs < 0 || thurs > 24) {
                error += "Thursday hours must be between 0 and 24! ";
            }

            if (fri < 0 || fri > 24) {
                error += "Friday hours must be between 0 and 24! ";
            }

            if (sat < 0 || sat > 24) {
                error += "Saturday hours must be between 0 and 24! ";
            }
        }

        if (error.length() > 0) {
            throw new InvalidInputException(error);
        }

        String id = UUID.randomUUID().toString();

        // - For a novice student availabilities must be nil
        if (!experiencedStudent) {
            sun = 0;
            mon = 0;
            tues = 0;
            wed = 0;
            thurs = 0;
            fri = 0;
            sat = 0;
        }

        Student s = new Student(id, uname, pword, email, experiencedStudent, sun, mon, tues, wed,
                thurs, fri, sat, manager);

        // - Try to commit to the database
        SQLiteInterface.ensureConnection();

        boolean op = SQLiteInterface.insertIntoStudents(SQLiteInterface.getConnection(), s);
        if (!op)
            throw new InvalidInputException("Failed to commit to database");

        return op;
    }

    public boolean deleteAccount(String uname, String pword) throws InvalidInputException {
        String error = "";
        Application manager = Application.getInstance();

        if (!manager.hasStudents()) {
            error = "No students have been created";
            throw new InvalidInputException(error);
        }

        // Get a list of the students and search for the one we want to delete
        List<Student> studentList = manager.getStudents();
        for (Student s : studentList) {
            if (s.getUsername().equals(uname)) {
                if (s.getPassword().equals(pword)) {
                    // - Try to commit to the database
                    // - FIXME Cascade delete
                    SQLiteInterface.ensureConnection();

                    boolean op = SQLiteInterface.deleteStudents(SQLiteInterface.getConnection(), s);
                    if (!op)
                        throw new InvalidInputException("Failed to commit to database");

                    s.delete();

                    return op;
                } else {
                    error = "Password entered does not match";
                    throw new InvalidInputException(error);
                }
            }
        }

        // This will probably never happen but if a user does not exist
        // Put this for completeness mostly. Candidate for removal.
        error = "User does not exist";
        throw new InvalidInputException(error);
    }

    // Only asking for username or email, not both in this case.
    public Student logIn(String uname, String pword) throws InvalidInputException {
        String error = "";
        Student s = null;

        if (uname == null || uname.trim().length() == 0)
            error += "Username or email is required to log in! ";

        if (pword == null || pword.trim().length() == 0)
            error += "Password is required to log in! ";

        if (error.length() > 0)
            throw new InvalidInputException(error);

        Application manager = Application.getInstance();

        if (uname.contains("@")) {
            for (Student a : manager.getStudents()) {
                if (a.getEmail().equals(uname.trim()) && a.getPassword().equals(pword.trim())) {
                    s = a;
                    break;
                }
            }
        } else {
            for (Student a : manager.getStudents()) {
                if (a.getUsername().equals(uname.trim()) && a.getPassword().equals(pword.trim())) {
                    s = a;
                    break;
                }
            }
        }

        if (s == null) {
            error = "Invalid Username/Email or Password!";
            throw new InvalidInputException(error);
        }

        return s;
    }

    public Student changeRole(Student a, boolean experienced, int sun, int mon, int tues, int wed,
            int thurs, int fri, int sat) throws InvalidInputException {
        String error = "";
        Application manager = Application.getInstance();

        if (a == null || !manager.getStudents().contains(a))
            error += "A valid student must be passed! ";

        if (experienced) {
            // Make sure hours between 0 and 24 inclusive
            if (sun < 0 || sun > 24)
                error += "Sunday hours must be between 0 and 24! ";

            if (mon < 0 || mon > 24)
                error += "Monday hours must be between 0 and 24! ";

            if (tues < 0 || tues > 24)
                error += "Tuesday hours must be between 0 and 24! ";

            if (wed < 0 || wed > 24)
                error += "Wednesday hours must be between 0 and 24! ";

            if (thurs < 0 || thurs > 24)
                error += "Thursday hours must be between 0 and 24! ";

            if (fri < 0 || fri > 24)
                error += "Friday hours must be between 0 and 24! ";

            if (sat < 0 || sat > 24)
                error += "Saturday hours must be between 0 and 24! ";
        }

        if (!experienced) {
            sun = 0;
            mon = 0;
            tues = 0;
            wed = 0;
            thurs = 0;
            fri = 0;
            sat = 0;
        }

        if (error.length() > 0)
            throw new InvalidInputException(error);

        a.setExperienced(experienced);
        a.setSundayAvailability(sun);
        a.setMondayAvailability(mon);
        a.setTuesdayAvailability(tues);
        a.setWednesdayAvailability(wed);
        a.setThursdayAvailability(thurs);
        a.setFridayAvailability(fri);
        a.setSaturdayAvailability(sat);

        // - Try to commit to the database
        SQLiteInterface.ensureConnection();

        boolean op = SQLiteInterface.updateStudents(SQLiteInterface.getConnection(), a);
        if (!op)
            throw new InvalidInputException("Failed to commit to database");

        return a;
    }
}
