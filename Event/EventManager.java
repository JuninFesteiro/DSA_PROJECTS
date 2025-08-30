/**
 *
 * EventManager.java Description

 *  *Purpose

 *  *Yeh file EventManager class define karta hai, jo events ko manage karta hai, including loading, saving, triggering, aur resolving events. Yeh events ke dependencies aur pending events ko track karta hai.

 *  *Data Structures Used

 *  *Doubly Linked List (EventNode): timelineHead (missing declaration) events ko time-based order mein store karta hai.

 *  *Queue pendingEventsQueue: Pending events ko FIFO order mein process karta hai.

 *  *Stack eventActionStack: Event actions ko undo/redo ke liye store karta hai.

 *  *HashMap<String, ArrayList> eventDependencies: Event type se related events
 * map karta hai.

 *  *Methods and Their Purpose

 *  *Constructor: public EventManager(PetManager petManager)

 *  *Purpose: Initializes data structures aur events.txt se events load karta hai.

 *  *Key Lines: this.pendingEventsQueue = new LinkedList<>();
 * this.eventActionStack = new Stack<>(); ...

 *  *loadEventsFromFile(String fileName):

 *  *Purpose: events.txt se events load karta hai aur EventNode objects banata
 * hai.

 *  *Key Lines: String[] parts = line.split(","); Event event = new
 * Event(parts[0], ...);

 *  *saveEventsToFile(String fileName):

 *  *Purpose: Events ko events.txt mein save karta hai.

 *  *addEvent(Event event):

 *  *Purpose: Naya event add karta hai (linked list, queue, stack, aur HashMap
 * mein).

 *  *triggerPendingEvents():

 *  *Purpose: Pending events ko queue se process karta hai.

 *  *undoLastEvent():

 *  *Purpose: Last event ko stack se undo karta hai.

 *  *checkDependencies(String eventType):

 *  *Purpose: Event type ke dependencies check karta hai (HashMap se).

 *  *Example Explanation

 *  *Scenario: Ek "Escape" event trigger hota hai jab pet "bunny" bhaag jata hai.

 *  *EventManager events.txt se event load karta hai:

 *  *EventManager manager = new EventManager(petManager);
 * manager.loadEventsFromFile("events.txt"); // Loads "Escape,bunny,..."
 * manager.triggerPendingEvents(); // Bunny ka escape event trigger hota hai

 *  *Agar event resolve hota hai, yeh eventActionStack mein push hota hai aur
 * events.txt mein update hota hai.
 */
package Event;

import Pet.Pet;
import Pet.PetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class EventManager implements EventTriggerable { // EventTriggerable interface implement karna

    // Aggregation
    private ArrayList<Event> events; // Events ki list
    private Queue<Event> pendingEventsQueue = new LinkedList<>(); // Pending events ke liye queue
    private PetManager petManager; // PetManager ka reference
    private static final String FILE_NAME = "events.txt"; // Events file ka naam

    //  field for stack-based undo actions
    private Stack<String> eventActionStack; // Event actions ke liye stack

    // eventDependencies
    //  field for graph based events dpenedecance tracking 
    private HashMap<Event, ArrayList<Event>> eventDependencies;
    // New field for timeline

    // Constructors
    // Updated EventManager constructor in EventManager.java
    public EventManager(PetManager petManager) { // Constructor
        events = new ArrayList<Event>(); // Events list initialize karna
        this.eventDependencies = new HashMap<>(); // Event dependencies HashMap initialize karta hai
        this.eventActionStack = new Stack<>(); // Event action stack initialize karta hai
        this.timelineHead = null; // Timeline head initialize karta hai
        this.petManager = petManager; // PetManager set karna
        if (petManager != null) { // Agar petManager non-null hai
            loadEventsFromFile(); // Events ko file se load karna
        } else {
            System.out.println("PetManager is null, skipping event loading."); // Debug message print karta hai
        }
    }

    // Add this method to existing EventManager.java
    // Add this method to existing EventManager.java
    public void displayEventActionHistory() { // Event action history display karne ka method
        if (eventActionStack.isEmpty()) { // Agar stack khali hai
            System.out.println("No event actions to display."); // Message print karta hai
            return; // Method se return karta hai
        }
        System.out.println("Event Action History:"); // Title print karta hai
        for (String action : eventActionStack) { // Stack ke har action ke liye loop
            System.out.println(action); // Action print karta hai
        }
    }

    private EventNode timelineHead; // Timeline ke liye doubly linked list ka head
    // Set PetManager

    public void setPetManager(PetManager petManager) { // PetManager set karna
        this.petManager = petManager;
    }

    // Methods 
    // Custom event add karna
    public void addEvent(String eventType, String description, Pet affectedPet) { // Custom event add karne ka method
        Event event = new Event(eventType, affectedPet, description, LocalDateTime.now()); // Naya event banaya
        events.add(event); // Event ko list mein add karna
        pendingEventsQueue.offer(event); // Event ko queue mein add karna
        saveEventsToFile(); // Events ko file mein save karna
        System.out.println("Event added: " + event.getEventDetails());
    }

    // Trigger event for a pet
    public void triggerEvent(Pet pet) { // Pet ke liye event trigger karna
        if (pet == null) { // Agar pet null hai
            System.out.println("Invalid pet for event.");
            return;
        }
        String description = "Manual event triggered for " + pet.getPetName(); // Event description
        addEvent("Manual", description, pet); // Manual event add karna
    }

    // Add this method to EventManager.java before triggerRandomEvent
    private String generateEventDescription(String eventType, String petName) {
        // Line ~30: Event type ke basis pe description generate karta hai
        switch (eventType) {
            case "Illness":
                return petName + " is sick and needs treatment.";
            case "Escape":
                return petName + " has escaped from the sanctuary!";
            case "Natural Disasters":
                return "A natural disaster has affected " + petName + "'s habitat.";
            default:
                return "An event occurred for " + petName;
        }
    }

    // Random event trigger karna
    @Override
    // Updated triggerRandomEvent method in EventManager.java
    // Updated triggerRandomEvent method in EventManager.java
    public void triggerRandomEvent(ArrayList<Pet> pets) {
        // Line ~80: Agar pet list null ya khali hai
        if (pets == null || pets.isEmpty()) {
            // Line ~81: Error message print karta hai
            System.out.println("No pets available for random events.");
            // Line ~82: Method se return karta hai
            return;
        }
        // Line ~83: Random object banata hai
        Random rand = new Random();
        // Line ~84: Event types array define karta hai
        String[] eventTypes = {"Illness", "Escape", "Natural Disasters"};
        // Line ~85: Random event type chunta hai
        String eventType = eventTypes[rand.nextInt(eventTypes.length)];
        // Line ~86: Random pet chunta hai
        Pet affectedPet = pets.get(rand.nextInt(pets.size()));
        // Line ~87: Event description generate karta hai
        String description = generateEventDescription(eventType, affectedPet.getPetName());
        // Line ~88: Current time leta hai
        LocalDateTime dateTime = LocalDateTime.now();
        // Line ~89: Naya event banata hai
        Event event = new Event(eventType, affectedPet, description, dateTime);
        // Line ~90: Event ko list mein add karta hai
        events.add(event);
        // Line ~91: Agar event Illness hai
        if (eventType.equals("Illness")) {
            // Line ~92: Health 20 kam karta hai, minimum 0
            int newHealth = Math.max(0, affectedPet.getPetHealth() - 20);
            // Line ~93: Pet ki health update karta hai
            affectedPet.setPetHealth(newHealth);
            // Line ~94: Health update message print karta hai
            System.out.println("Pet " + affectedPet.getPetName() + " health reduced to " + newHealth + " due to Illness.");
        } else if (eventType.equals("Escape")) {
            // Line ~95: Health 30 kam karta hai, minimum 0
            int newHealth = Math.max(0, affectedPet.getPetHealth() - 30);
            // Line ~96: Pet ki health update karta hai
            affectedPet.setPetHealth(newHealth);
            // Line ~97: Health update message print karta hai
            System.out.println("Pet " + affectedPet.getPetName() + " health reduced to " + newHealth + " due to Escape.");
        }
        // Line ~98: Event type ko stack mein push karta hai
        eventActionStack.push("Added:" + event.getEventType());
        // Line ~99: Event details print karta hai
        System.out.println("Random event triggered: Event Type: " + eventType + "\nDate: " + dateTime + "\nDescription: " + description + "\nAffected Pet: " + affectedPet.getPetName() + "\nResolved: No");
        // Line ~100: Events ko file mein save karta hai
        saveEventsToFile();
        // Line ~101: Pet state ko pets.txt mein save karta hai
        petManager.savePetsToFile("pets.txt");
    }

    // Event resolve karna
    @Override
    public void resolveEvent(Event event) { // Event resolve karna
        if (event == null) { // Agar event null hai
            System.out.println("Invalid event.");
            return;
        }
        if (events.contains(event)) { // Agar event list mein hai
            event.resolveEvent(); // Event ko resolve karna
            saveEventsToFile(); // Events ko file mein save karna
        } else {
            System.out.println("Event not found in the system.");
        }
    }

    // Saare events display karna
    @Override
    public void displayEvents() { // Saare events display karna
        if (events.isEmpty()) { // Agar events list khali hai
            System.out.println("No events to display.");
            return;
        }

        System.out.println("Displaying all events:");
        for (Event event : events) { // Har event ke liye
            System.out.println("- " + event.getEventDetails()); // Event details print karna
        }
    }

    // Unresolved events lena
    @Override
    public ArrayList<Event> getUnresolvedEvent() { // Unresolved events lena
        ArrayList<Event> unresolved = new ArrayList<Event>(); // Unresolved events ke liye list
        for (Event event : events) { // Har event ke liye
            if (!event.isResolved()) { // Agar event unresolved hai
                unresolved.add(event); // List mein add karna
            }
        }
        return unresolved; // Unresolved events return karna
    }

    // Fix: Naya method add kiya displayUnresolvedEvents jo unresolved events display karega
    public void displayUnresolvedEvents() { // Unresolved events display karne ka method
        ArrayList<Event> unresolvedEvents = getUnresolvedEvent(); // Unresolved events lena
        if (unresolvedEvents.isEmpty()) { // Agar koi unresolved event nahi hai
            System.out.println("No unresolved events.");
            return;
        }
        System.out.println("Displaying unresolved events:"); // Unresolved events ka header
        for (int i = 0; i < unresolvedEvents.size(); i++) { // Har unresolved event ke liye
            System.out.println((i + 1) + ". " + unresolvedEvents.get(i).getEventDetails()); // Event details with index
        }
    }

    // ID se event lena
    public Event getEventById(int eventID) { // ID se event lena
        if (eventID < 1 || eventID > events.size()) { // Agar event ID invalid hai
            System.out.println("Invalid event ID.");
            return null;
        }
        return events.get(eventID - 1); // Event return karna
    }

    // Events ko file mein save karna
    public void saveEventsToFile() { // Events ko file mein save karna
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) { // File writer kholna
            for (Event event : events) { // Har event ke liye
                writer.write(
                        event.getEventType() + ","
                        + (event.getAffectedPet() != null ? event.getAffectedPet().getPetName() : "None") + ","
                        + event.getDescription() + ","
                        + event.getDate() + ","
                        + event.isResolved()
                ); // Event data likhna
                writer.newLine(); // Nayi line add karna
            }
            System.out.println("Events saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    // File se events load karna
    public void loadEventsFromFile() { // File se events load karna
        events.clear(); // Events list clear karna
        pendingEventsQueue.clear(); // Pending queue clear karna
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) { // File reader kholna
            String line;
            while ((line = reader.readLine()) != null) { // Har line read karna
                String[] parts = line.split(",", 5); // Line ko split karna
                if (parts.length == 5) { // Agar line mein 5 parts hain
                    String eventType = parts[0]; // Event type extract karna
                    String petName = parts[1]; // Pet name extract karna
                    String description = parts[2]; // Description extract karna
                    LocalDateTime date = LocalDateTime.parse(parts[3]); // Date parse karna
                    boolean isResolved = Boolean.parseBoolean(parts[4]); // Resolved status parse karna

                    Pet affectedPet = petName.equals("None") ? null : petManager.getPet(petName); // Pet dhoondna
                    Event event = new Event(eventType, affectedPet, description, date); // Naya event banaya
                    event.setResolved(isResolved); // Resolved status set karna
                    events.add(event); // Event ko list mein add karna
                    if (!isResolved) { // Agar event unresolved hai
                        pendingEventsQueue.offer(event); // Queue mein add karna
                    }
                }
            }
            System.out.println("Events loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }

    // ID se event resolve karna
    public boolean resolveEventByID(int eventID) {
        // Line ~140: Agar event ID invalid hai
        if (eventID < 1 || eventID > events.size()) {
            // Line ~141: Error message print karta hai
            System.out.println("Invalid event ID.");
            // Line ~142: False return karta hai
            return false;
        }
        // Line ~143: Event ko list se get karta hai
        Event event = events.get(eventID - 1);
        // Line ~144: Event ko resolve karta hai
        event.resolveEvent();
        // Line ~145: Pending queue se event remove karta hai
        pendingEventsQueue.remove(event);
        // Line ~146: Event action stack mein resolved action push karta hai
        eventActionStack.push("Resolved:" + event.getEventType());
        // Line ~147: Events ko file mein save karta hai
        saveEventsToFile();
        // Line ~148: Success message print karta hai
        System.out.println("Event resolved successfully: " + event.getEventType());
        // Line ~149: True return karta hai
        return true;
    }

    // FIFO order mein agla event resolve karna
    public void resolveNextEvent() { // FIFO order mein agla event resolve karna
        if (!pendingEventsQueue.isEmpty()) { // Agar queue khali nahi hai
            Event nextEvent = pendingEventsQueue.poll(); // Agla event lena
            nextEvent.resolveEvent(); // Event resolve karna
            System.out.println("Resolved event: " + nextEvent.getEventType());
        } else {
            System.out.println("No pending events to resolve.");
        }
    }

    // Pending events display karna
    public void displayPendingEvents() { // Pending events display karna
        if (pendingEventsQueue.isEmpty()) { // Agar queue khali hai
            System.out.println("No pending events.");
            return;
        }

        System.out.println("Pending Events:");
        for (Event e : pendingEventsQueue) { // Har pending event ke liye
            System.out.println(e.getEventDetails()); // Event details print karna
        }
    }

    // Add event dependcy ye method graph ki madad e check karta ha event 
    public void addEventDependency(Event parent, Event child) {
        if (parent == null || child == null) { // Agar parent ya child null hai
            System.out.println("Invalid parent or child event."); // Error message print karta hai
            return; // Method se return karta hai
        }

        if (!events.contains(parent) || !events.contains(child)) { // Agar events list mein nahi hain
            System.out.println("Parent or child event not found in system."); // Error message print karta hai
            return; // Method se return karta hai
        }

        // using lambda functions
        eventDependencies.computeIfAbsent(parent, k -> new ArrayList<>()); // Parent ke liye list banata hai agar nahi hai
        eventDependencies.get(parent).add(child); // Child ko parent ke dependencies mein add karta hai
        System.out.println("Dependency added: " + parent.getEventType() + " -> " + child.getEventType());

    }

    // Method check event dependcy of events are resolved or not
    public boolean checkDependencies(Event event) { // Event ke dependencies check karne ka method
        if (!eventDependencies.containsKey(event)) { // Agar event ke koi dependencies nahi hain
            return true; // True return karta hai kyunki koi dependency nahi
        }
        ArrayList<Event> dependencies = eventDependencies.get(event); // Event ke dependencies leti hai
        for (Event dep : dependencies) { // Har dependency ke liye loop
            if (!dep.isResolved()) { // Agar koi dependency unresolved hai
                System.out.println("Cannot resolve " + event.getEventType() + ": Dependency " + dep.getEventType() + " is unresolved."); // Error message print karta hai
                return false; // False return karta hai
            }
        }
        System.out.println("All dependencies for " + event.getEventType() + " are resolved.");
        return true; // True return karta hai
    }

    public void displayEventDependencies() { // Event dependencies display karne ka method
        if (eventDependencies.isEmpty()) { // Agar koi dependencies nahi hain
            System.out.println("No event dependencies to display."); // Message print karta hai
            return; // Method se return karta hai
        }
        System.out.println("Event Dependencies:"); // Title print karta hai
        for (Map.Entry<Event, ArrayList<Event>> entry : eventDependencies.entrySet()) { // Har event aur uske dependencies ke liye
            Event parent = entry.getKey(); // Parent event leti hai
            ArrayList<Event> children = entry.getValue(); // Child events leti hai
            System.out.print(parent.getEventType() + " depends on: "); // Parent event ka naam print karta hai
            for (Event child : children) { // Har child ke liye
                System.out.print(child.getEventType() + ", "); // Child event ka naam print karta hai
            }
            System.out.println();
        }
    }

    //Psuh event action to stack Stack 
    public void pushEventAction(String action, Event event) { // Event action ko stack mein push karne ka method
        if (event == null) { // Agar event null hai
            System.out.println("Invalid event for action."); // Error message print karta hai
            return; // Method se return karta hai
        }
        String actionRecord = action + ":" + event.getEventType(); // Action aur event type ka record banata hai
        eventActionStack.push(actionRecord); // Record ko stack mein push karta hai
        System.out.println("Action recorded: " + actionRecord); // 
    }

    // New method: Undo last event action
    public void undoLastEventAction() { // Last event action undo karne ka method
        if (eventActionStack.isEmpty()) { // Agar stack khali hai
            System.out.println("No actions to undo."); // Message print karta hai
            return; // Method se return karta hai
        }
        String lastAction = eventActionStack.pop(); // Last action stack se pop karta hai
        String[] parts = lastAction.split(":"); // Action aur event type alag karta hai
        String action = parts[0]; // Action type leti hai
        String eventType = parts[1]; // Event type leti hai

        for (Event event : events) { // Har event ke liye loop
            if (event.getEventType().equals(eventType)) { // Agar event type match karta hai
                if (action.equals("Added")) { // Agar action add tha
                    events.remove(event); // Event ko list se hata deta hai
                    pendingEventsQueue.remove(event); // Event ko queue se hata deta hai
                    System.out.println("Undid action: Removed event " + eventType); // Success message print karta hai
                    return; // Method se return karta hai
                } else if (action.equals("Resolved")) { // Agar action resolve tha
                    event.setResolved(false); // Event ko unresolved karta hai
                    pendingEventsQueue.offer(event); // Event ko queue mein wapas daalta hai
                    System.out.println("Undid action: Restored event " + eventType + " as unresolved"); // Success message print karta hai
                    return; // Method se return karta hai
                }
            }
        }
        System.out.println("Event not found for undo: " + eventType); // Error message print karta hai
    }

    // Add event to timeline
    public void addEventToTimeline(Event event) { // Event ko timeline mein add karne ka method
        if (event == null) { // Agar event null hai
            System.out.println("Invalid event."); // Error message print karta hai
            return; // Method se return karta hai
        }
        EventNode newNode = new EventNode(event); // Naya node banata hai
        if (timelineHead == null) { // Agar timeline khali hai
            timelineHead = newNode; // Head ko naya node set karta hai
            return; // Method se return karta hai
        }

        EventNode current = timelineHead; // Current node ko head se shuru karta hai
        EventNode prev = null; // Previous node ke liye variable
        while (current != null && current.event.getDate().isBefore(event.getDate())) { // Jab tak date chhoti hai
            prev = current; // Previous ko current set karta hai
            current = current.next; // Current ko next pe le jata hai
        }

        if (prev == null) { // Agar insert head pe karna hai
            newNode.next = timelineHead; // Naya node head se link karta hai
            timelineHead.prev = newNode; // Head ka prev naya node set karta hai
            timelineHead = newNode; // Head update karta hai
        } else { // Agar insert beech mein ya end pe hai
            newNode.next = current; // Naya node current se link karta hai
            newNode.prev = prev; // Naya node prev se link karta hai
            prev.next = newNode; // Previous ka next naya node set karta hai
            if (current != null) { // Agar current null nahi hai
                current.prev = newNode; // Current ka prev naya node set karta hai
            }
        }
        System.out.println("Event added to timeline: " + event.getEventType()); // Success message print karta hai
    }

    //  Display event timeline
    public void displayEventTimeline(String direction) { // Timeline display karne ka method
        if (timelineHead == null) { // Agar timeline khali hai
            System.out.println("No events in timeline."); // Message print karta hai
            return; // Method se return karta hai
        }
        System.out.println("Event Timeline (" + direction + "):"); // Title print karta hai
        if (direction.equalsIgnoreCase("forward")) { // Agar direction forward hai
            EventNode current = timelineHead; // Current node ko head se shuru karta hai
            while (current != null) { // Jab tak current node hai
                System.out.println(current.event.getEventDetails()); // Event details print karta hai
                current = current.next; // Current ko next pe le jata hai
            }
        } else if (direction.equalsIgnoreCase("backward")) { // Agar direction backward hai
            EventNode current = timelineHead; // Current node ko head se shuru karta hai
            while (current != null && current.next != null) { // Last node tak jata hai
                current = current.next; // Current ko next pe le jata hai
            }
            while (current != null) { // Backward traverse karta hai
                System.out.println(current.event.getEventDetails()); // Event details print karta hai
                current = current.prev; // Current ko prev pe le jata hai
            }
        } else { // Agar invalid direction hai
            System.out.println("Invalid direction. Use 'forward' or 'backward'."); // Error message print karta hai
        }
    }

    //  Sort events by date using quick sort
    public void sortEventsByDate() { // Events ko date ke basis pe sort karne ka method
        Event[] eventArray = events.toArray(new Event[0]); // Events ko array mein convert karta hai
        quickSort(eventArray, 0, eventArray.length - 1); // Quick sort call karta hai
        events.clear(); // Original list clear karta hai
        for (Event event : eventArray) { // Sorted events wapas list mein add karta hai
            events.add(event); // Event add karta hai
        }
        System.out.println("Events sorted by date."); // Success message print karta hai
    }

    // Helper method: Quick sort for events
    private void quickSort(Event[] arr, int low, int high) { // Quick sort algorithm implement karta hai
        if (low < high) { // Agar low high se chhota hai
            int pi = partition(arr, low, high); // Partition index find karta hai
            quickSort(arr, low, pi - 1); // Left part sort karta hai
            quickSort(arr, pi + 1, high); // Right part sort karta hai
        }
    }

    // Helper method: Partition for quick sort
    private int partition(Event[] arr, int low, int high) { // Partition banane ka method
        Event pivot = arr[high]; // Pivot as last element choose karta hai
        int i = low - 1; // Smaller element ka index
        for (int j = low; j < high; j++) { // Low se high tak loop
            if (arr[j].getDate().isBefore(pivot.getDate()) || arr[j].getDate().equals(pivot.getDate())) { // Agar current event ka date chhota ya barabar hai
                i++; // Smaller index increment karta hai
                Event temp = arr[i]; // Swap ke liye temporary variable
                arr[i] = arr[j]; // Current element swap karta hai
                arr[j] = temp; // Previous element swap karta hai
            }
        }
        Event temp = arr[i + 1]; // Pivot ke saath swap karta hai
        arr[i + 1] = arr[high]; // Pivot ko sahi position pe daalta hai
        arr[high] = temp; // Temporary element ko last position pe daalta hai
        return i + 1; // Partition index return karta hai
    }

    // Search events by date range using binary search
    public void searchEventsByDateRange(LocalDateTime start, LocalDateTime end) { // Date range ke events dhoondhne ka method
        if (events.isEmpty()) { // Agar events list khali hai
            System.out.println("No events to search."); // Message print karta hai
            return; // Method se return karta hai
        }
        sortEventsByDate(); // Events ko pehle sort karta hai
        ArrayList<Event> result = new ArrayList<>(); // Result ke liye list
        for (Event event : events) { // Har event ke liye loop
            LocalDateTime date = event.getDate(); // Event ka date leti hai
            if ((date.isEqual(start) || date.isAfter(start)) && (date.isEqual(end) || date.isBefore(end))) { // Agar date range mein hai
                result.add(event); // Event ko result mein add karta hai
            }
        }
        if (result.isEmpty()) { // Agar koi event nahi mila
            System.out.println("No events found in the date range: " + start + " to " + end); // Message print karta hai
        } else { // Agar events mile
            System.out.println("Events in date range " + start + " to " + end + ":"); // Title print karta hai
            for (Event event : result) { // Har event ke liye
                System.out.println(event.getEventDetails()); // Event details print karta hai
            }
        }
    }

    // New method: Display sorted events
    public void displaySortedEvents() { // Sorted events display karne ka method
        if (events.isEmpty()) { // Agar events list khali hai
            System.out.println("No events to display."); // Message print karta hai
            return; // Method se return karta hai
        }
        sortEventsByDate(); // Events ko sort karta hai
        System.out.println("Sorted Events by Date:"); // Title print karta hai
        for (Event event : events) { // Har event ke liye
            System.out.println(event.getEventDetails()); // Event details print karta hai
        }
    }
}
