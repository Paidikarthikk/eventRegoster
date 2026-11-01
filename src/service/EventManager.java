package service;

import java.time.LocalDate;
import java.util.ArrayList;

import model.Event;
import model.Student;

/**
 * Central service that stores all events and provides operations on them:
 * adding events, listing upcoming events, searching by category and
 * registering students.
 */
public class EventManager {

    private final ArrayList<Event> events;
    private int nextId;

    public EventManager() {
        this.events = new ArrayList<>();
        this.nextId = 1;
    }

    /**
     * Creates a new event and assigns it the next available ID.
     *
     * @return the newly created event
     */
    public Event addEvent(String name, String category, LocalDate date, String venue, int limit) {
        Event event = new Event(nextId, name, category, date, venue, limit);
        events.add(event);
        nextId++;
        return event;
    }

    /** @return every event, including past ones */
    public ArrayList<Event> getAllEvents() {
        return events;
    }

    /** @return only events that are scheduled for today or later */
    public ArrayList<Event> getUpcomingEvents() {
        ArrayList<Event> upcoming = new ArrayList<>();
        for (Event event : events) {
            if (event.isUpcoming()) {
                upcoming.add(event);
            }
        }
        return upcoming;
    }

    /**
     * Looks up an event by its ID.
     *
     * @param id the event ID
     * @return the matching event, or {@code null} if none exists
     */
    public Event findEventById(int id) {
        for (Event event : events) {
            if (event.getId() == id) {
                return event;
            }
        }
        return null;
    }

    /**
     * Finds all events in the given category (case-insensitive).
     *
     * @param category category to search for, e.g. "technical"
     * @return list of matching events (empty if none match)
     */
    public ArrayList<Event> searchByCategory(String category) {
        ArrayList<Event> results = new ArrayList<>();
        for (Event event : events) {
            if (event.getCategory().equalsIgnoreCase(category.trim())) {
                results.add(event);
            }
        }
        return results;
    }

    /** @return the distinct categories of all stored events */
    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (Event event : events) {
            if (!categories.contains(event.getCategory())) {
                categories.add(event.getCategory());
            }
        }
        return categories;
    }

    /**
     * Registers a student for an event.
     *
     * @param event   the event to register for
     * @param student the student registering
     * @return {@code true} if registration succeeded
     */
    public boolean register(Event event, Student student) {
        return event.addParticipant(student);
    }

    /** Loads a few sample events so the portal is not empty on first run. */
    public void loadSampleData() {
        LocalDate today = LocalDate.now();
        addEvent("CodeSprint Hackathon", "Technical", today.plusDays(5), "CSE Lab Block", 50);
        addEvent("Robotics Workshop", "Technical", today.plusDays(12), "Mech Seminar Hall", 30);
        addEvent("Annual Cultural Fest", "Cultural", today.plusDays(20), "Main Auditorium", 200);
        addEvent("Classical Dance Night", "Cultural", today.plusDays(8), "Open Air Theatre", 3);
        addEvent("Inter-College Cricket", "Sports", today.plusDays(15), "College Ground", 22);
        addEvent("Startup Pitch Day", "Seminar", today.plusDays(3), "Conference Hall", 40);
        addEvent("Freshers Quiz", "Technical", today.minusDays(10), "Library Hall", 60);
    }
}
