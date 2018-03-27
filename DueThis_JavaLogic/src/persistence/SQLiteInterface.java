package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.Duration;

import controller.InvalidInputException;
import model.Application;
import model.Assignment;
import model.Event;
import model.Student;

public class SQLiteInterface {
    private static String createTableAssignments = "CREATE TABLE IF NOT EXISTS Assignments(\n"
            + "    id              VARCHAR(40) PRIMARY KEY,\n"
            + "    name            VARCHAR(40) NOT NULL,\n"
            + "    course          VARCHAR(40) NOT NULL,\n"
            + "    dueDate         VARCHAR(40) NOT NULL,\n"
            + "    completionTime  SMALLINT            ,\n"
            + "    isCompleted     BOOLEAN     NOT NULL,\n"
            + "    gradeWeight     FLOAT(2)    NOT NULL,\n"
            + "    fk_student_id   VARCHAR(40) NOT NULL,\n"
            + "    FOREIGN KEY(fk_student_id)  REFERENCES Students\n);";
    private static String createTableEvents = "CREATE TABLE IF NOT EXISTS Events(\n"
            + "    id              VARCHAR(40) PRIMARY KEY,\n"
            + "    name            VARCHAR(40) NOT NULL,\n"
            + "    date            VARCHAR(40) NOT NULL,\n"
            + "    startTime       VARCHAR(40) NOT NULL,\n"
            + "    endTime         VARCHAR(40) NOT NULL,\n"
            + "    repeatedWeekly  BOOLEAN     NOT NULL,\n"
            + "    fk_student_id   VARCHAR(40) NOT NULL,\n"
            + "    FOREIGN KEY(fk_student_id)  REFERENCES Students\n);";
    private static String createTableStudents = "CREATE TABLE IF NOT EXISTS Students(\n"
            + "    id                      VARCHAR(40) PRIMARY KEY,\n"
            + "    username                VARCHAR(40) NOT NULL,\n"
            + "    password                VARCHAR(40) NOT NULL,\n"
            + "    email                   VARCHAR(40) NOT NULL,\n"
            + "    experienced             BOOLEAN     NOT NULL,\n"
            + "    sundayAvailability      SMALLINT    NOT NULL,\n"
            + "    mondayAvailability      SMALLINT    NOT NULL,\n"
            + "    tuesdayAvailability     SMALLINT    NOT NULL,\n"
            + "    wednesdayAvailability   SMALLINT    NOT NULL,\n"
            + "    thursdayAvailability    SMALLINT    NOT NULL,\n"
            + "    fridayAvailability      SMALLINT    NOT NULL,\n"
            + "    saturdayAvailability    SMALLINT    NOT NULL \n);";

    private static String dropTableAssignments = "DROP TABLE IF EXISTS Assignments;";
    private static String dropTableEvents = "DROP TABLE IF EXISTS Events;";
    private static String dropTableStudents = "DROP TABLE IF EXISTS Students;";

    private static String insertAssignmentPrepared = "INSERT INTO Assignments VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static String insertEventPrepared = "INSERT INTO Events Values (?, ?, ?, ?, ?, ?, ?);";
    private static String insertStudentPrepared = "INSERT INTO Students Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static String selectAssignmentPrepared = "SELECT * FROM Assignments;";
    private static String selectEventPrepared = "SELECT * FROM Events;";
    private static String selectStudentPrepared = "SELECT * FROM Students;";

    private static String updateAssignmentPrepared = "UPDATE Assignments SET name = ?,"
            + " course = ?, dueDate = ?, completionTime = ?, isCompleted = ?, gradeWeight = ?,"
            + " fk_student_id = ? WHERE id = ?;";
    private static String updateEventPrepared = "UPDATE Events SET name = ?, date = ?,"
            + " startTime = ?, endTime = ?, repeatedWeekly = ?, fk_student_id = ? WHERE id = ?;";
    private static String updateStudentPrepared = "UPDATE Students SET username = ?,"
            + " password = ?, email = ?, experienced = ?, sundayAvailability = ?,"
            + " mondayAvailability = ?, tuesdayAvailability = ?,"
            + " wednesdayAvailability = ?, thursdayAvailability = ?,"
            + " fridayAvailability = ?, saturdayAvailability = ? WHERE id = ?;";

    private static String deleteAssignmentPrepared = "DELETE FROM Assignments WHERE id = ?;";
    private static String deleteEventPrepared = "DELETE FROM Events WHERE id = ?;";
    private static String deleteStudentPrepared = "DELETE FROM Students WHERE id = ?;";

    public static Connection connectOrCreate(String fileName) {
        if (fileName == null)
            return null;

        String url = fileName;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    public static boolean createTables(Connection connection) {
        if (connection == null)
            return false;

        boolean r = !executeStatement(connection, createTableStudents);
        r &= !executeStatement(connection, createTableAssignments);
        r &= !executeStatement(connection, createTableEvents);

        return r;
    }

    public static boolean dropTables(Connection connection) {
        if (connection == null)
            return false;

        boolean r = executeStatement(connection, dropTableStudents);
        r &= executeStatement(connection, dropTableAssignments);
        r &= executeStatement(connection, dropTableEvents);

        return r;
    }

    public static boolean executePreparedStatement(PreparedStatement ps) {
        if (ps == null)
            return false;

        try {
            ps.executeUpdate(); // - No need to handle
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean executeStatement(Connection connection, String statement) {
        if (connection == null || statement == null)
            return false;

        try {
            Statement s = connection.createStatement();
            return s.execute(statement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean newDB(String fn) {
        if (fn == null)
            return false;

        Connection c = newDBKeepConnection(fn);
        if (c == null)
            return false;

        try {
            c.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static Connection newDBKeepConnection(String fn) {
        if (fn == null)
            return null;

        Connection c = connectOrCreate(fn);
        if (c == null)
            return null;

        boolean r = dropTables(c);
        r &= createTables(c);

        if (!r) {
            try {
                c.close();
                return null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return c;
    }

    public static boolean insertIntoAssignments(Connection connection, Assignment a) {
        if (connection == null || a == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(insertAssignmentPrepared);

            ps.setString(1, a.getId());
            ps.setString(2, a.getName());
            ps.setString(3, a.getCourse());
            ps.setString(4, a.getDueDate().toString());
            ps.setLong(5, a.getCompletionTime().getSeconds());
            ps.setBoolean(6, a.getIsCompleted());
            ps.setFloat(7, a.getGradeWeight());
            ps.setString(8, a.getStudent().getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean updateAssignments(Connection connection, Assignment a) {
        if (connection == null || a == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(updateAssignmentPrepared);

            ps.setString(1, a.getName());
            ps.setString(2, a.getCourse());
            ps.setString(3, a.getDueDate().toString());
            ps.setLong(4, a.getCompletionTime().getSeconds());
            ps.setBoolean(5, a.getIsCompleted());
            ps.setFloat(6, a.getGradeWeight());
            ps.setString(7, a.getStudent().getId());
            ps.setString(8, a.getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean deleteAssignments(Connection connection, Assignment a) {
        if (connection == null || a == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(deleteAssignmentPrepared);
            ps.setString(1, a.getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean insertIntoEvents(Connection connection, Event event) {
        if (connection == null || event == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(insertEventPrepared);

            ps.setString(1, event.getId());
            ps.setString(2, event.getName());
            ps.setString(3, event.getDate().toString());
            ps.setString(4, event.getStartTime().toString());
            ps.setString(5, event.getEndTime().toString());
            ps.setBoolean(6, event.getRepeatedWeekly());
            ps.setString(7, event.getStudent().getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean updateEvents(Connection connection, Event event) {
        if (connection == null || event == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(updateEventPrepared);

            ps.setString(1, event.getName());
            ps.setString(2, event.getDate().toString());
            ps.setString(3, event.getStartTime().toString());
            ps.setString(4, event.getEndTime().toString());
            ps.setBoolean(5, event.getRepeatedWeekly());
            ps.setString(6, event.getStudent().getId());
            ps.setString(7, event.getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean deleteEvents(Connection connection, Event event) {
        if (connection == null || event == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(deleteEventPrepared);
            ps.setString(1, event.getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean insertIntoStudents(Connection connection, Student student) {
        if (connection == null || student == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(insertStudentPrepared);

            ps.setString(1, student.getId());
            ps.setString(2, student.getUsername());
            ps.setString(3, student.getPassword());
            ps.setString(4, student.getEmail());
            ps.setBoolean(5, student.getExperienced());
            ps.setInt(6, student.getSundayAvailability());
            ps.setInt(7, student.getMondayAvailability());
            ps.setInt(8, student.getTuesdayAvailability());
            ps.setInt(9, student.getWednesdayAvailability());
            ps.setInt(10, student.getThursdayAvailability());
            ps.setInt(11, student.getFridayAvailability());
            ps.setInt(12, student.getSaturdayAvailability());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean updateStudents(Connection connection, Student student) {
        if (connection == null || student == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(updateStudentPrepared);

            ps.setString(1, student.getUsername());
            ps.setString(2, student.getPassword());
            ps.setString(3, student.getEmail());
            ps.setBoolean(4, student.getExperienced());
            ps.setInt(5, student.getSundayAvailability());
            ps.setInt(6, student.getMondayAvailability());
            ps.setInt(7, student.getTuesdayAvailability());
            ps.setInt(8, student.getWednesdayAvailability());
            ps.setInt(9, student.getThursdayAvailability());
            ps.setInt(10, student.getFridayAvailability());
            ps.setInt(11, student.getSaturdayAvailability());
            ps.setString(12, student.getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean deleteStudents(Connection connection, Student student) {
        if (connection == null || student == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(deleteStudentPrepared);
            ps.setString(1, student.getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean loadDB(String fn, Application app) {
        if (fn == null || app == null)
            return false;

        // - Persistence is incrementally updated. The application data is synchronized
        // - as soon as it is added. Thus, if the application is empty, we would load
        // - from the database.
        if (app.getAssignments().size() == 0 && app.getEvents().size() == 0
                && app.getStudents().size() == 0) {

            Connection c = connectOrCreate(fn);
            if (c == null)
                return false;

            boolean r = createTables(c);

            r &= selectFromStudents(c, app);
            r &= selectFromEvents(c, app);
            r &= selectFromAssignments(c, app);

            try {
                c.close();
                return r;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return false;
    }

    public static boolean selectFromAssignments(Connection connection, Application app) {
        if (connection == null || app == null)
            return false;

        try {
            Statement s = connection.createStatement();
            ResultSet r = s.executeQuery(selectAssignmentPrepared);

            while (r.next()) {
                Student student = null;

                for (int i = 0; i < app.numberOfStudents(); i++) {
                    if (app.getStudent(i).getId().compareTo(r.getString("fk_student_id")) == 0)
                        student = app.getStudent(i);
                }

                if (student == null)
                    return false;

                @SuppressWarnings("unused")
                Assignment a = new Assignment(r.getString("id"), r.getString("name"),
                        r.getString("course"), Date.valueOf(r.getString("dueDate")),
                        r.getBoolean("isCompleted"), r.getFloat("gradeWeight"),
                        Duration.ofSeconds(r.getLong("completionTime")), student, app);
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean selectFromEvents(Connection connection, Application app) {
        if (connection == null || app == null)
            return false;

        try {
            Statement s = connection.createStatement();
            ResultSet r = s.executeQuery(selectEventPrepared);

            while (r.next()) {
                Student student = null;

                for (int i = 0; i < app.numberOfStudents(); i++) {
                    if (app.getStudent(i).getId().compareTo(r.getString("fk_student_id")) == 0)
                        student = app.getStudent(i);
                }

                if (student == null)
                    return false;

                @SuppressWarnings("unused")
                Event e = new Event(r.getString("id"), r.getString("name"),
                        Date.valueOf(r.getString("date")), Time.valueOf(r.getString("startTime")),
                        Time.valueOf(r.getString("endTime")), r.getBoolean("repeatedWeekly"),
                        student, app);
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean selectFromStudents(Connection connection, Application app) {
        if (connection == null || app == null)
            return false;

        try {
            Statement s = connection.createStatement();
            ResultSet r = s.executeQuery(selectStudentPrepared);

            while (r.next()) {
                @SuppressWarnings("unused")
                Student student = new Student(r.getString("id"), r.getString("username"),
                        r.getString("password"), r.getString("email"), r.getBoolean("experienced"),
                        r.getInt("sundayAvailability"), r.getInt("mondayAvailability"),
                        r.getInt("tuesdayAvailability"), r.getInt("wednesdayAvailability"),
                        r.getInt("thursdayAvailability"), r.getInt("fridayAvailability"),
                        r.getInt("saturdayAvailability"), app);
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // - Persistence singleton
    // - A bit of a sin but OK.
    private static SQLiteInterface theInstance = null;
    private static Connection connection = null;

    public static Connection getConnection() throws InvalidInputException {
        if (connection == null)
            ensureConnection();

        return connection;
    }

    private SQLiteInterface() throws InvalidInputException {
        if (connection == null) {
            connection = connectOrCreate(persistenceFilename);

            if (connection == null)
                throw new InvalidInputException("Failed to connect to database");

            if (!SQLiteInterface.createTables(connection))
                throw new InvalidInputException("Failed to conditionally create tables");
        }
    }

    public static SQLiteInterface ensureConnection() throws InvalidInputException {
        if (theInstance == null)
            theInstance = new SQLiteInterface();

        return theInstance;
    }

    public static boolean load() {
        return SQLiteInterface.loadDB(getFilename(), Application.getInstance());
    }

    public static void deleteConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String persistenceFilename = "jdbc:sqlite:duethis.db";

    public static String getFilename() {
        return persistenceFilename;
    }

    public static void setFilename(String filename) {
        if (filename != null)
            persistenceFilename = filename;
    }
}
