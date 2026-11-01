package model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a single college event along with the students registered for it.
 *
 * <p>Each event has a participant limit. Once the number of registered
 * students reaches that limit, the event is marked as {@code FULL} and no
 * further registrations are accepted.</p>
 */
public class Event {

    private final int id;
    private final String name;
    private final String category;
    private final LocalDate date;
    private final String venue;
    private int participantLimit;
    private final ArrayList<Student> participants;

    /**
     * Creates a new event with an empty participant list.
     *
     * @param id               unique event ID
     * @param name             event title
     * @param category         category such as Technical, Cultural, Sports
     * @param date             date on which the event takes place
     * @param venue            location of the event
     * @param participantLimit maximum number of students allowed
     */
    public Event(int id, String name, String category, LocalDate date, String venue, int participantLimit) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.date = date;
        this.venue = venue;
        this.participantLimit = participantLimit;
        this.participants = new ArrayList<>();
    }

    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public int getParticipantLimit() {
        return participantLimit;
    }

    public ArrayList<Student> getParticipants() {
        return participants;
    }

    public int getRegisteredCount() {
        return participants.size();
    }

    public int getSeatsLeft() {
        return participantLimit - participants.size();
    }

    // ------------------------------------------------------------------
    // Status checks
    // ------------------------------------------------------------------

    /** @return {@code true} if the event is today or in the future */
    public boolean isUpcoming() {
        return !date.isBefore(LocalDate.now());
    }

    /** @return {@code true} if no seats are left */
    public boolean isFull() {
        return participants.size() >= participantLimit;
    }

    /**
     * Checks whether a student with the given roll number has already registered.
     *
     * @param rollNumber roll number to look for (case-insensitive)
     * @return {@code true} if the student is already in the participant list
     */
    public boolean isRegistered(String rollNumber) {
        for (Student student : participants) {
            if (student.getRollNumber().equalsIgnoreCase(rollNumber)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a short status label for display.
     *
     * @return {@code CLOSED} for past events, {@code FULL} when no seats are
     *         left, otherwise {@code OPEN}
     */
    public String getStatus() {
        if (!isUpcoming()) {
            return "CLOSED";
        } else if (isFull()) {
            return "FULL";
        } else {
            return "OPEN";
        }
    }

    // ------------------------------------------------------------------
    // Modifiers
    // ------------------------------------------------------------------

    /**
     * Adds a student to this event if a seat is available.
     *
     * @param student the student to register
     * @return {@code true} if the student was added
     */
    public boolean addParticipant(Student student) {
        if (!isUpcoming() || isFull() || isRegistered(student.getRollNumber())) {
            return false;
        }
        participants.add(student);
        return true;
    }

    /**
     * Updates the participant limit. The new limit must be at least 1 and
     * cannot be lower than the number of students already registered.
     *
     * @param newLimit the new maximum number of participants
     * @return {@code true} if the limit was updated
     */
    public boolean setParticipantLimit(int newLimit) {
        if (newLimit < 1 || newLimit < participants.size()) {
            return false;
        }
        participantLimit = newLimit;
        return true;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " (" + category + ", " + date + ")";
    }
}
