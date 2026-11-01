package model;

/**
 * Represents a student who can register for college events.
 *
 * <p>A student is uniquely identified by their roll number, which is used
 * to prevent the same student from registering twice for one event.</p>
 */
public class Student {

    private final String rollNumber;
    private final String name;
    private final String department;
    private final String email;

    /**
     * Creates a new student.
     *
     * @param rollNumber unique college roll number
     * @param name       full name of the student
     * @param department department / branch (e.g. CSE, ECE)
     * @param email      contact email address
     */
    public Student(String rollNumber, String name, String department, String email) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.department = department;
        this.email = email;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + " (" + rollNumber + ")";
    }
}
