# Event Module - Virtual Pet Sanctuary

This folder contains all classes related to **event management** in the Virtual Pet Sanctuary project. The event system tracks, triggers, resolves, and manages all events (like pet illness, escape, disasters, and manual events) that occur in the sanctuary.

---

## File Structure

- **Event.java** — Defines the Event class (represents a single event)
- **EventNode.java** — Node for doubly linked list (timeline of events)
- **EventManager.java** — Manages all events, dependencies, timeline, undo/redo, file I/O
- **EventTriggerable.java** — Interface for event-related actions

---

## Core Classes and Their Responsibilities

### 1. `Event.java`

Represents a single event in the sanctuary.

**Fields:**
- `eventType` (String): Type of event (e.g., Illness, Escape, Manual)
- `date` (LocalDateTime): When the event occurred
- `description` (String): Details about the event
- `affectedPet` (Pet): The pet involved (can be null)
- `isResolved` (boolean): Whether the event is resolved

**Key Methods:**
- `getEventDetails()`: Returns a formatted string with all event info.
- `isCritical()`: Returns true if event is "Illness" or "Escape".
- `resolveEvent()`: Marks the event as resolved.
- Getters/Setters for all fields.
- `toString()`: Short summary of the event.

**Example:**
```java
Pet bunny = new Rabbit(1, "bunny", true, 2, 100.0);
Event escape = new Event("Escape", bunny, "bunny has escaped!", LocalDateTime.now());
System.out.println(escape.getEventDetails());
escape.resolveEvent();
```

**Sample Output:**
```
Event Type: Escape
Date: 2025-08-30T12:00:00
Description: bunny has escaped!
Affected Pet: bunny
Resolved: No
Event 'Escape' has been resolved.
```

---

### 2. `EventNode.java`

Defines a node for a **doubly linked list** to maintain a timeline of events.

**Fields:**
- `event` (Event): The event stored in this node
- `prev` (EventNode): Previous node
- `next` (EventNode): Next node

**Usage Example:**
Used internally by `EventManager` for event timeline.

---

### 3. `EventTriggerable.java`

Interface for event-related actions.

**Methods:**
- `void triggerRandomEvent(ArrayList<Pet> pets)`
- `void resolveEvent(Event event)`
- `void displayEvents()`
- `ArrayList<Event> getUnresolvedEvent()`

---

### 4. `EventManager.java`

Manages all event-related operations.

**Key Data Structures:**
- `ArrayList<Event> events`: All events
- `Queue<Event> pendingEventsQueue`: Unresolved events (FIFO)
- `Stack<String> eventActionStack`: Undo/redo actions
- `HashMap<Event, ArrayList<Event>> eventDependencies`: Event dependency graph
- `EventNode timelineHead`: Head of doubly linked list for event timeline

---

## EventManager Methods & Examples

### `addEvent(String eventType, String description, Pet affectedPet)`
Adds a new event to the system.

**Input:**
- `eventType`: "Illness"
- `description`: "bunny is sick"
- `affectedPet`: Pet object

**Output:**
- Event added to list, queue, and file.
- Console:  
  `Event added: Event Type: Illness ...`

---

### `triggerRandomEvent(ArrayList<Pet> pets)`
Triggers a random event for a random pet.

**Input:**  
List of pets

**Output:**  
- Random event added, pet health updated if needed.
- Console:
  ```
  Pet bunny health reduced to 80 due to Illness.
  Random event triggered: Event Type: Illness ...
  ```

---

### `resolveEvent(Event event)`
Marks an event as resolved.

**Input:**  
Event object

**Output:**  
- Event marked as resolved, file updated.
- Console:  
  `Event 'Illness' has been resolved.`

---

### `displayEvents()`
Displays all events.

**Output:**  
```
Displaying all events:
- Event Type: Illness ...
- Event Type: Escape ...
```

---

### `getUnresolvedEvent()`
Returns a list of unresolved events.

---

### `displayUnresolvedEvents()`
Prints all unresolved events with index.

---

### `getEventById(int eventID)`
Returns the event with the given ID (1-based index).

---

### `saveEventsToFile()` / `loadEventsFromFile()`
Saves/loads all events to/from `events.txt`.

**File Format:**  
`eventType,petName,description,date,isResolved`

**Example:**
```
Escape,bunny,bunny has escaped!,2025-08-30T12:00:00,false
```

---

### `resolveEventByID(int eventID)`
Resolves the event with the given ID.

---

### `resolveNextEvent()`
Resolves the next event in the pending queue (FIFO).

---

### `displayPendingEvents()`
Displays all pending (unresolved) events.

---

### `addEventDependency(Event parent, Event child)`
Adds a dependency: `child` cannot be resolved until `parent` is resolved.

---

### `checkDependencies(Event event)`
Checks if all dependencies for an event are resolved.

---

### `displayEventDependencies()`
Displays all event dependencies.

---

### `pushEventAction(String action, Event event)`
Pushes an action (Added/Resolved) to the undo stack.

---

### `undoLastEventAction()`
Undoes the last event action (removes or marks unresolved).

---

### `addEventToTimeline(Event event)`
Adds an event to the doubly linked list timeline (sorted by date).

---

### `displayEventTimeline(String direction)`
Displays the event timeline in forward or backward order.

---

### `sortEventsByDate()`
Sorts all events by date using quick sort.

---

### `searchEventsByDateRange(LocalDateTime start, LocalDateTime end)`
Finds and displays events within a date range.

---

### `displaySortedEvents()`
Displays all events sorted by date.

---

## Example Usage Flow

1. **Trigger a random event:**
   ```java
   eventManager.triggerRandomEvent(petManager.getAllPets());
   ```
   Output:
   ```
   Pet bunny health reduced to 80 due to Illness.
   Random event triggered: Event Type: Illness ...
   ```

2. **Display unresolved events:**
   ```java
   eventManager.displayUnresolvedEvents();
   ```
   Output:
   ```
   1. Event Type: Illness ...
   ```

3. **Resolve an event by ID:**
   ```java
   eventManager.resolveEventByID(1);
   ```
   Output:
   ```
   Event 'Illness' has been resolved.
   Event resolved successfully: Illness
   ```

4. **Undo last action:**
   ```java
   eventManager.undoLastEventAction();
   ```
   Output:
   ```
   Undid action: Restored event Illness as unresolved
   ```

---

## Input/Output Details

- **Input:** User actions (trigger, resolve, undo), pet list, event IDs, date ranges.
- **Output:** Console messages, updated `events.txt`, updated pet health, event timeline, dependencies, and undo/redo stack.

---

## Notes

- All events are persisted in `events.txt`.
- Pet health is updated in `pets.txt` if affected by events.
- Timeline and dependencies allow for advanced event analysis and management.
- Undo/redo supports robust event management for managers.

---

**For any questions or issues, refer to the code comments or contact
