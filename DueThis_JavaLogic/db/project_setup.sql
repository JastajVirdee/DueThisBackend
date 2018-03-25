DROP TABLE IF EXISTS Assignments;
DROP TABLE IF EXISTS Events;
DROP TABLE IF EXISTS Students;

CREATE TABLE IF NOT EXISTS Students(
    id                      VARCHAR(40) PRIMARY KEY,
    username                VARCHAR(40) NOT NULL,
    password                VARCHAR(40) NOT NULL,
    email                   VARCHAR(40) NOT NULL,
    experienced             BOOLEAN     NOT NULL,
    sundayAvailability      SMALLINT    NOT NULL,
    mondayAvailability      SMALLINT    NOT NULL,
    tuesdayAvailability     SMALLINT    NOT NULL,
    wednesdayAvailability   SMALLINT    NOT NULL,
    thursdayAvailability    SMALLINT    NOT NULL,
    fridayAvailability      SMALLINT    NOT NULL,
    saturdayAvailability    SMALLINT    NOT NULL
);

CREATE TABLE IF NOT EXISTS Assignments(
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(40) NOT NULL,
    course          VARCHAR(40) NOT NULL,
    dueDate         DATE        NOT NULL,
    completionTime  SMALLINT    ,
    isCompleted     BOOLEAN     NOT NULL,
    gradeWeight     FLOAT(2)    NOT NULL,
    fk_student_id   VARCHAR(40) NOT NULL,
    FOREIGN KEY(fk_student_id)  REFERENCES Students
);

CREATE TABLE IF NOT EXISTS Events(
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(40) NOT NULL,
    date            DATE        NOT NULL,
    startTime       TIME        NOT NULL,
    endTime         TIME        NOT NULL,
    repeatedWeekly  BOOLEAN     NOT NULL,
    fk_student_id   VARCHAR(40) NOT NULL,
    FOREIGN KEY(fk_student_id)  REFERENCES Students
);
