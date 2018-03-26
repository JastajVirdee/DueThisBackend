package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.Duration;

import model.Application;
import model.Assignment;
import model.Event;
import model.Student;

public class SQLiteIntegration {
    private static String createTableAssignments = "CREATE TABLE IF NOT EXISTS Assignments(\n"
            + "    id              VARCHAR(40) PRIMARY KEY,\n"
            + "    name            VARCHAR(40) NOT NULL,\n"
            + "    course          VARCHAR(40) NOT NULL,\n"
            + "    dueDate         DATE        NOT NULL,\n" + "    completionTime  SMALLINT    ,\n"
            + "    isCompleted     BOOLEAN     NOT NULL,\n"
            + "    gradeWeight     FLOAT(2)    NOT NULL,\n"
            + "    fk_student_id   VARCHAR(40) NOT NULL,\n"
            + "    FOREIGN KEY(fk_student_id)  REFERENCES Students\n" + ");";
    private static String createTableEvents = "CREATE TABLE IF NOT EXISTS Events(\n"
            + "    id              VARCHAR(40) PRIMARY KEY,\n"
            + "    name            VARCHAR(40) NOT NULL,\n"
            + "    date            DATE        NOT NULL,\n"
            + "    startTime       TIME        NOT NULL,\n"
            + "    endTime         TIME        NOT NULL,\n"
            + "    repeatedWeekly  BOOLEAN     NOT NULL,\n"
            + "    fk_student_id   VARCHAR(40) NOT NULL,\n"
            + "    FOREIGN KEY(fk_student_id)  REFERENCES Students\n" + ");";
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
            + "    saturdayAvailability    SMALLINT    NOT NULL\n" + ");";

    private static String dropTableAssignments = "DROP TABLE IF EXISTS Assignments;";
    private static String dropTableEvents = "DROP TABLE IF EXISTS Events;";
    private static String dropTableStudents = "DROP TABLE IF EXISTS Students;";

    private static String insertAssignmentPrepared = "INSERT INTO Assignments VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static String insertEventPrepared = "INSERT INTO Events Values (?, ?, ?, ?, ?, ?, ?);";
    private static String insertStudentPrepared = "INSERT INTO Students Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static String selectAssignmentPrepared = "SELECT * FROM Assignments;";
    private static String selectEventPrepared = "SELECT * FROM Events;";
    private static String selectStudentPrepared = "SELECT * FROM Students;";

    public static Connection connectOrCreate(String fileName) {
        if (fileName == null)
            return null;

        String url = "jdbc:sqlite:" + fileName;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
        }

        return connection;
    }

    public static boolean createTables(Connection connection) {
        if (connection == null)
            return false;

        boolean r = executeStatement(connection, createTableStudents);
        r &= executeStatement(connection, createTableAssignments);
        r &= executeStatement(connection, createTableEvents);

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
        }

        return false;
    }

    public static boolean initDB(String fn) {
        if (fn == null)
            return false;

        Connection c = connectOrCreate(fn);
        if (c == null)
            return false;

        boolean r = dropTables(c);
        r &= createTables(c);

        try {
            c.close();
            return r;
        } catch (Exception e) {
        }

        return false;
    }

    public static Connection initDBKeep(String fn) {
        if (fn == null)
            return null;

        Connection c = connectOrCreate(fn);
        if (c == null)
            return null;

        @SuppressWarnings("unused")
        boolean r = dropTables(c);
        r &= createTables(c);

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
            ps.setDate(4, a.getDueDate());
            ps.setLong(5, a.getCompletionTime().getSeconds());
            ps.setBoolean(6, a.getIsCompleted());
            ps.setFloat(7, a.getGradeWeight());
            ps.setString(8, a.getStudent().getId());

            return executePreparedStatement(ps);
        } catch (Exception e) {
        }

        return false;
    }

    public static boolean insertIntoEvents(Connection connection, Event e) {
        if (connection == null || e == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(insertEventPrepared);
            ps.setString(1, e.getId());
            ps.setString(2, e.getName());
            ps.setDate(3, e.getDate());
            ps.setTime(4, e.getStartTime());
            ps.setTime(5, e.getEndTime());
            ps.setBoolean(6, e.getRepeatedWeekly());
            ps.setString(7, e.getStudent().getId());

            return executePreparedStatement(ps);
        } catch (Exception ex) {
        }

        return false;
    }

    public static boolean insertIntoStudents(Connection connection, Student s) {
        if (connection == null || s == null)
            return false;

        try {
            PreparedStatement ps = connection.prepareStatement(insertStudentPrepared);
            ps.setString(1, s.getId());
            ps.setString(2, s.getUsername());
            ps.setString(3, s.getPassword());
            ps.setString(4, s.getEmail());
            ps.setBoolean(5, s.getExperienced());
            ps.setInt(6, s.getSundayAvailability());
            ps.setInt(7, s.getMondayAvailability());
            ps.setInt(8, s.getTuesdayAvailability());
            ps.setInt(9, s.getWednesdayAvailability());
            ps.setInt(10, s.getThursdayAvailability());
            ps.setInt(11, s.getFridayAvailability());
            ps.setInt(12, s.getSaturdayAvailability());

            return executePreparedStatement(ps);
        } catch (SQLException e) {
        }

        return false;
    }

    public static boolean loadDB(String fn, Application app) {
        if (fn == null || app == null)
            return false;

        Connection c = connectOrCreate(fn);
        if (c == null)
            return false;

        app.delete();

        boolean r = selectFromStudents(c, app);
        r &= selectFromEvents(c, app);
        r &= selectFromAssignments(c, app);

        try {
            c.close();
        } catch (Exception e) {
        }

        return r;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Application app = Application.getInstance();

        Student s = new Student("0", "x", "x", "x", false, 0, 0, 0, 0, 0, 0, 0, app);
        Student s2 = new Student("1", "x", "x", "x", false, 0, 0, 0, 0, 0, 0, 0, app);
        Assignment a = new Assignment("0", "y", "y", new Date(0), false, 0.3f,
                Duration.ofSeconds(1), s, app);
        Event e = new Event("0", "z", new Date(0), new Time(0), new Time(0), false, s, app);

        app.addStudent(s);
        app.addStudent(s2);
        app.addAssignment(a);
        app.addEvent(e);

        saveDB("test.db", app);

        loadDB("test.db", app);

        saveDB("test.db", app);
    }

    public static boolean saveDB(String fn, Application app) {
        if (fn == null || app == null)
            return false;

        Connection c = initDBKeep(fn);
        if (c == null)
            return false;

        boolean r = true;

        for (Student s : app.getStudents())
            r &= insertIntoStudents(c, s);

        for (Assignment a : app.getAssignments())
            r &= insertIntoAssignments(c, a);

        for (Event e : app.getEvents())
            r &= insertIntoEvents(c, e);

        try {
            c.close();
        } catch (Exception e) {
        }

        return r;
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
                    if (app.getStudent(i).getId().compareTo(r.getString(8)) == 0)
                        student = app.getStudent(i);
                }

                if (student == null)
                    return false;

                @SuppressWarnings("unused")
                Assignment a = new Assignment(r.getString(1), r.getString(2), r.getString(3),
                        r.getDate(4), r.getBoolean(5), r.getFloat(6),
                        Duration.ofSeconds(r.getLong(7)), student, app);
            }

            return true;
        } catch (Exception e) {
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
                    if (app.getStudent(i).getId().compareTo(r.getString(7)) == 0)
                        student = app.getStudent(i);
                }

                if (student == null)
                    return false;

                @SuppressWarnings("unused")
                Event e = new Event(r.getString(1), r.getString(2), r.getDate(3), r.getTime(4),
                        r.getTime(5), r.getBoolean(6), student, app);
            }

            return true;
        } catch (Exception e) {
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
                Student student = new Student(r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getBoolean(5), r.getInt(6), r.getInt(7), r.getInt(8),
                        r.getInt(9), r.getInt(10), r.getInt(11), r.getInt(12), app);
            }

            return true;
        } catch (Exception e) {
        }

        return false;
    }

    private String persistenceFilename = "duethis.db";

    public String getPersistenceFilename() {
        return persistenceFilename;
    }

    public boolean loadPersistence() {
        return SQLiteIntegration.loadDB(this.getPersistenceFilename(), Application.getInstance());
    }

    public boolean savePersistence() {
        return SQLiteIntegration.saveDB(this.getPersistenceFilename(), Application.getInstance());
    }

    public void setPersistenceFilename(String persistenceFilename) {
        if (persistenceFilename != null)
            this.persistenceFilename = persistenceFilename;
    }

}
