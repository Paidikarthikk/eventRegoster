import java.time.LocalDate;
import java.util.ArrayList;

import model.Event;
import model.Student;
import service.EventManager;
import ui.ConsoleUI;

/**
 * Entry point of the College Event Registration System.
 *
 * <p>Shows a menu-driven console portal where students can browse upcoming
 * events, register for them and search by category, and where an admin can
 * add events and change participant limits.</p>
 */
public class Main {

    private static final String ADMIN_PASSWORD = "admin123";
    private static final EventManager manager = new EventManager();

    public static void main(String[] args) {
        manager.loadSampleData();
        ConsoleUI.printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = ConsoleUI.readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    displayUpcomingEvents();
                    break;
                case 2:
                    registerForEvent();
                    break;
                case 3:
                    viewParticipants();
                    break;
                case 4:
                    searchByCategory();
                    break;
                case 5:
                    setParticipantLimit();
                    break;
                case 6:
                    addNewEvent();
                    break;
                case 0:
                    running = false;
                    System.out.println();
                    ConsoleUI.success("Thank you for using the portal. Goodbye!");
                    break;
                default:
                    ConsoleUI.error("Invalid choice. Please pick an option from the menu.");
            }
        }
    }

    private static void printMenu() {
        ConsoleUI.printHeading("Main Menu");
        System.out.println("  1. Display upcoming events");
        System.out.println("  2. Register for an event");
        System.out.println("  3. View registered participants");
        System.out.println("  4. Search events by category");
        System.out.println("  5. Set participant limit   (admin)");
        System.out.println("  6. Add a new event         (admin)");
        System.out.println("  0. Exit");
        System.out.println();
    }

    // ------------------------------------------------------------------
    // Student features
    // ------------------------------------------------------------------

    private static void displayUpcomingEvents() {
        ConsoleUI.printHeading("Upcoming Events");
        printEventTable(manager.getUpcomingEvents(), "No upcoming events right now.");
    }

    private static void registerForEvent() {
        displayUpcomingEvents();
        if (manager.getUpcomingEvents().isEmpty()) {
            return;
        }

        int eventId = ConsoleUI.readInt("Event ID to register for: ");
        Event event = manager.findEventById(eventId);

        if (event == null) {
            ConsoleUI.error("No event found with ID " + eventId + ".");
            return;
        } else if (!event.isUpcoming()) {
            ConsoleUI.error("Registration closed: \"" + event.getName() + "\" has already happened.");
            return;
        } else if (event.isFull()) {
            ConsoleUI.error("Sorry, \"" + event.getName() + "\" is full ("
                    + event.getParticipantLimit() + "/" + event.getParticipantLimit() + ").");
            return;
        }

        String roll = ConsoleUI.readText("Roll number : ");
        if (event.isRegistered(roll)) {
            ConsoleUI.error("Roll number " + roll + " is already registered for \"" + event.getName() + "\".");
            return;
        }

        String name = ConsoleUI.readText("Full name   : ");
        String dept = ConsoleUI.readText("Department  : ");
        String email = ConsoleUI.readText("Email       : ");

        if (!email.contains("@") || !email.contains(".")) {
            ConsoleUI.error("Invalid email address. Registration cancelled.");
            return;
        }

        Student student = new Student(roll, name, dept, email);
        if (manager.register(event, student)) {
            ConsoleUI.success("Registered " + name + " for \"" + event.getName() + "\".");
            ConsoleUI.info("Seats left: " + event.getSeatsLeft());
        } else {
            ConsoleUI.error("Registration failed. Please try again.");
        }
    }

    private static void viewParticipants() {
        ConsoleUI.printHeading("All Events");
        printEventTable(manager.getAllEvents(), "No events available.");

        int eventId = ConsoleUI.readInt("Event ID: ");
        Event event = manager.findEventById(eventId);
        if (event == null) {
            ConsoleUI.error("No event found with ID " + eventId + ".");
            return;
        }

        ConsoleUI.printHeading("Participants - " + event.getName());
        ArrayList<Student> participants = event.getParticipants();

        if (participants.isEmpty()) {
            ConsoleUI.info("No students have registered yet.");
        } else {
            String[] headers = {"No.", "Roll No", "Name", "Department", "Email"};
            ArrayList<String[]> rows = new ArrayList<>();
            for (int i = 0; i < participants.size(); i++) {
                Student s = participants.get(i);
                rows.add(new String[] {
                        String.valueOf(i + 1), s.getRollNumber(), s.getName(), s.getDepartment(), s.getEmail()
                });
            }
            ConsoleUI.printTable(headers, rows);
        }
        ConsoleUI.info("Registered: " + event.getRegisteredCount() + " / " + event.getParticipantLimit());
    }

    private static void searchByCategory() {
        ConsoleUI.printHeading("Search by Category");
        ConsoleUI.info("Available categories: " + String.join(", ", manager.getCategories()));

        String category = ConsoleUI.readText("Category: ");
        ArrayList<Event> results = manager.searchByCategory(category);

        ConsoleUI.printHeading("Results for \"" + category + "\"");
        printEventTable(results, "No events found in this category.");
    }

    // ------------------------------------------------------------------
    // Admin features
    // ------------------------------------------------------------------

    private static void setParticipantLimit() {
        if (!checkAdmin()) {
            return;
        }

        ConsoleUI.printHeading("Set Participant Limit");
        printEventTable(manager.getAllEvents(), "No events available.");

        int eventId = ConsoleUI.readInt("Event ID: ");
        Event event = manager.findEventById(eventId);
        if (event == null) {
            ConsoleUI.error("No event found with ID " + eventId + ".");
            return;
        }

        ConsoleUI.info("Current limit: " + event.getParticipantLimit()
                + "  |  Registered: " + event.getRegisteredCount());
        int newLimit = ConsoleUI.readInt("New limit: ");

        if (event.setParticipantLimit(newLimit)) {
            ConsoleUI.success("Limit for \"" + event.getName() + "\" updated to " + newLimit + ".");
        } else {
            ConsoleUI.error("Limit must be at least 1 and not less than the "
                    + event.getRegisteredCount() + " students already registered.");
        }
    }

    private static void addNewEvent() {
        if (!checkAdmin()) {
            return;
        }

        ConsoleUI.printHeading("Add New Event");
        String name = ConsoleUI.readText("Event name : ");
        String category = ConsoleUI.readText("Category   : ");
        String venue = ConsoleUI.readText("Venue      : ");
        LocalDate date = ConsoleUI.readDate("Date (YYYY-MM-DD): ");
        int limit = ConsoleUI.readInt("Participant limit: ");

        if (date.isBefore(LocalDate.now())) {
            ConsoleUI.error("Event date cannot be in the past. Event not added.");
        } else if (limit < 1) {
            ConsoleUI.error("Participant limit must be at least 1. Event not added.");
        } else {
            Event event = manager.addEvent(name, category, date, venue, limit);
            ConsoleUI.success("Event \"" + event.getName() + "\" added with ID " + event.getId() + ".");
        }
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private static boolean checkAdmin() {
        String password = ConsoleUI.readText("Admin password: ");
        if (password.equals(ADMIN_PASSWORD)) {
            return true;
        }
        ConsoleUI.error("Incorrect password. Access denied.");
        return false;
    }

    private static void printEventTable(ArrayList<Event> events, String emptyMessage) {
        if (events.isEmpty()) {
            ConsoleUI.info(emptyMessage);
            return;
        }

        String[] headers = {"ID", "Event", "Category", "Date", "Venue", "Seats", "Status"};
        ArrayList<String[]> rows = new ArrayList<>();
        for (Event e : events) {
            rows.add(new String[] {
                    String.valueOf(e.getId()),
                    e.getName(),
                    e.getCategory(),
                    e.getDate().toString(),
                    e.getVenue(),
                    e.getRegisteredCount() + "/" + e.getParticipantLimit(),
                    e.getStatus()
            });
        }
        ConsoleUI.printTable(headers, rows);
    }
}
