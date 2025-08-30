package Event;

import Pet.Pet;
import Pet.PetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Random;

public class EventManager implements EventTriggerable {

    private ArrayList<Event> events;
    private PetManager petManager;
    private static final String FILE_NAME = "events.txt";

    // Priority Queue for Critical Events
    private PriorityQueue<Event> criticalEvents = new PriorityQueue<>(new Comparator<Event>() {
        @Override
        public int compare(Event e1, Event e2) {
            return getPriority(e2.getEventType()) - getPriority(e1.getEventType());
        }

        private int getPriority(String type) {
            switch (type) {
                case "Illness": return 3;
                case "Escape": return 2;
                default: return 1;
            }
        }
    });

    public EventManager(PetManager petManager) {
        events = new ArrayList<>();
        this.petManager = petManager;
        loadEventsFromFile();
    }

    public void addEvent(String eventType, String description, Pet affectedPet) {
        Event event = new Event(eventType, affectedPet, description, LocalDateTime.now());
        events.add(event);

        // Add to critical event queue if applicable
        if (eventType.equals("Illness") || eventType.equals("Escape")) {
            criticalEvents.add(event);
        }

        saveEventsToFile();
        System.out.println("Event added: " + event.getEventDetails());
    }

    public void displayUnresolvedEvents() {
        System.out.println("Unresolved Events..");
        for (Event event : events) {
            if (!event.isResolved()) {
                System.out.println(event.getEventDetails());
            }
        }
    }

    @Override
    public void triggerRandomEvent(ArrayList<Pet> pets) {
        if (pets.isEmpty()) {
            System.out.println("No pets available for random events.");
            return;
        }

        Random rand = new Random();
        Pet randomPet = pets.get(rand.nextInt(pets.size()));
        String[] eventTypes = {"Illness", "Escape", "Hunger Increase", "Loneliness"};
        String type = eventTypes[rand.nextInt(eventTypes.length)];
        String description = type + " occurred to " + randomPet.getName();

        addEvent(type, description, randomPet);
    }

    public void resolveEventByID(int eventId) {
        for (Event event : events) {
            if (event.getEventID() == eventId && !event.isResolved()) {
                event.resolve();
                criticalEvents.remove(event); // Remove if it was critical
                saveEventsToFile();
                System.out.println("Resolved event: " + event.getEventDetails());
                return;
            }
        }
        System.out.println("Event not found or already resolved.");
    }

    public void resolveHighestPriorityEvent() {
        if (!criticalEvents.isEmpty()) {
            Event critical = criticalEvents.poll();
            critical.resolve();
            saveEventsToFile();
            System.out.println("Resolved critical event: " + critical.getEventDetails());
        } else {
            System.out.println("No critical events to resolve.");
        }
    }

    public void previewCriticalEvents() {
        System.out.println("Critical Events in Priority Order:");
        for (Event event : criticalEvents) {
            System.out.println(event.getEventDetails());
        }
    }

    private void saveEventsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Event event : events) {
                writer.write(event.serialize());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    private void loadEventsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Event event = Event.deserialize(line, petManager);
                events.add(event);
                if (!event.isResolved() &&
                    (event.getEventType().equals("Illness") || event.getEventType().equals("Escape"))) {
                    criticalEvents.add(event);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }
}
