/**
 * Event.java Description

Purpose

Yeh file Event class define karta hai, jo sanctuary mein hone wale events (jaise pet escape, manual events) ko represent karta hai. Yeh abstract class hai jo EventTriggerable interface implement karta hai aur event ke common attributes aur methods provide karta hai.

Data Structures Used

None: Sirf fields (String, LocalDateTime, boolean, Pet) hain, koi explicit data structure nahi.

Methods and Their Purpose





Constructor: public Event(String type, String petName, String description, LocalDateTime timeStamp, boolean isResolved, Pet pet)





Purpose: Event object banata hai aur attributes (type, petName, description, timestamp, resolved status, aur pet reference) set karta hai.



Key Lines: this.type = type; this.petName = petName; ...



Getters: getType(), getPetName(), getDescription(), getTimeStamp(), isResolved(), getPet()





Purpose: Event ke attributes access karne ke liye (e.g., event type ya pet name lena).



Setters: setType(String), setPetName(String), setDescription(String), setTimeStamp(LocalDateTime), setResolved(boolean), setPet(Pet)





Purpose: Event ke attributes modify karne ke liye.



Abstract Method: public abstract void triggerEvent(EventManager eventManager)





Purpose: Event-specific behavior define karta hai, jo subclasses (jaise Escape, Manual) implement karte hain.



toString(): public String toString()





Purpose: Event details ka string representation deta hai (e.g., "Event: Escape, Pet: bunny, ...").



Key Lines: return type + "," + petName + "," + description + "," + timeStamp + "," + isResolved;

Example Explanation

Scenario: Ek pet "bunny" sanctuary se escape karta hai.





Event.java ka use hota hai ek Escape event object banane ke liye:

Pet bunny = new Rabbit(1, "bunny", true, 2, 100.0);
Event escape = new Escape("Escape", "bunny", "bunny has escaped!", LocalDateTime.now(), false, bunny);
escape.triggerEvent(eventManager); // Escape event trigger hota hai



Yeh event events.txt mein save hota hai (Escape,bunny,bunny has escaped from the sanctuary!,2025-08-03T11:02:48.605020600,true) aur EventManager usko process karta hai.
 */


package Event;
// Event file to handle events

import java.time.LocalDateTime;
import Pet.Pet;

public class Event {

    // Fields
    private String eventType;
    private LocalDateTime date;
    private String description;
    private Pet affectedPet;
    private boolean isResolved;

// Constructors
    public Event(String eventType, Pet affectedPet, String description, LocalDateTime date) {
        this.eventType = eventType;
        this.affectedPet = affectedPet;
        this.description = description;
        this.date = date;
        this.isResolved = false; //By Default is always false
    }

// Methods
// Check event details
    // Method to get event details
    public String getEventDetails() {
        return "Event Type: " + eventType + "\n"
                + "Date: " + date.toString() + "\n"
                + "Description: " + description + "\n"
                + "Affected Pet: " + (affectedPet != null ? affectedPet.getPetName() : "None") + "\n"
                + "Resolved: " + (isResolved ? "Yes" : "No");
    }

// Check pet is in citical
    public boolean isCritical() {

        return eventType.equalsIgnoreCase("Illness") || eventType.equalsIgnoreCase("Escape");
    }

// Resolve the event
    public void resolveEvent() {
        if (!isResolved) {
            isResolved = true;
            System.out.println("Event '" + eventType + "' has been resolved.");
        } else {
            System.out.println("Event '" + eventType + "' is already resolved.");
        }
    }

// Get Event type
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    // Local Date time
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Get local date time
    public LocalDateTime getDate() {
        return date;
    }

    // Get Description
    public String getDescription() {
        return description;
    }

    public Pet getAffectedPet() {
        return affectedPet;
    }

    public void setAffectedPet(Pet affectedPet) {
        this.affectedPet = affectedPet;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        this.isResolved = resolved;
    }

    @Override
    public String toString() {
        return "Event: " + eventType + " | Date: " + date + " | Resolved: " + (isResolved ? "Yes" : "No");
    }
}
