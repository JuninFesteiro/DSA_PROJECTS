# Virtual Pet Sanctuary Simulator

# Virtual Pet Sanctuary - Complete Documentation
A Java-based console application that simulates a virtual pet sanctuary. Manage pets, users, events, funds, and adventures using various data structures and file persistence.

## Features

- **User Roles:** Manager, CareTaker, Guest
- **User Management:** Register, Login, Assign roles
- **Pet Management:** Add, remove, view, buy, assign, and interact with pets
- **Event Management:** Trigger, resolve, and manage pet-related events
- **Funds Management:** Add, track, and undo fund changes
- **Assignments:** Assign pets to CareTakers
- **Pet Adventures:** Simulate pet quests using graphs, trees, stacks, and queues
- **Social Network:** Analyze pet interactions
- **Persistence:** Data saved in `users.txt`, `pets.txt`, `assignments.txt`, `funds.txt`, `events.txt`

## Data Structures Used

- **ArrayList:** Users, pets, logs
- **Stack:** Fund history, adventure moves, pet viewing history
- **Queue:** Event queue, load history, item collection, interaction requests
- **LinkedList:** Adventure logs, pet lists
- **PriorityQueue:** Critical pets, adventure prioritization
- **HashMap:** Quest challenges, adventure maps
- **Binary Search Tree:** Users and pets organization
- **Custom Linked List:** Favorite pets, recent pet views

## File Structure

- `Main.java` — Entry point
- `Sanctuary/` — Sanctuary logic, adventure quests
- `Pet/` — Pet classes and management
- `User/` — User, Manager, CareTaker, Guest classes
- `Event/` — Event and event management
- `users.txt` — User data
- `pets.txt` — Pet data
- `assignments.txt` — Pet assignments to CareTakers
- `funds.txt` — Sanctuary funds
- `events.txt` — Event logs

## How to Run

1. **Compile:**
   ```sh
   javac Main.java
   ```
2. **Run:**
   ```sh
   java Main
   ```

## Usage

- **Main Menu:** Login, Register, Continue as Guest, Exit
- **Manager:** Add funds, manage pets, assign CareTakers, manage events, analyze social network, manage adventures
- **CareTaker:** View assigned pets, interact, resolve events, monitor health, manage care queue
- **Guest:** View/buy pets, mark favorites, view pet events, request interactions

## Sample Data Files

- `users.txt` — Stores users in CSV format
- `pets.txt` — Stores pets in CSV format
- `assignments.txt` — CareTaker-pet assignments
- `funds.txt` — Sanctuary funds (single value)
- `events.txt` — Event logs


This document combines all module documentation for the Virtual Pet Sanctuary project, including **Sanctuary**, **Pet**, **User**, **Event**, and **Guest** modules.

---

## Sanctuary Module

The **Sanctuary** module acts as the central hub for managing pets, users, events, funds, and advanced features like pet adventures and social networks.

### File Structure

- `Sanctuary/Sanctuary.java` — Main class for sanctuary operations, funds, and menu navigation.
- `Sanctuary/PetAdventureQuest.java` — Manages pet adventure quests using multiple data structures.
- `Sanctuary/PetSocialNetwork.java` — Analyzes and recommends pet social interactions using advanced algorithms and data structures.

### Core Features

#### Sanctuary.java

- **Funds Management:** Tracks and updates sanctuary funds using a static variable and a Stack for undo/redo.
- **Pet Management:** Uses `PetManager` to add, remove, and manage pets.
- **User Management:** Uses `UserManager` for user registration, login, and role assignment.
- **Event Management:** Uses `EventManager` to handle all event-related operations.
- **Daily Updates:** Simulates daily pet needs and triggers critical events.
- **History Tracking:** Maintains a Queue for load history and a Stack for fund changes.
- **Menu Navigation:** Provides the main menu for login, registration, guest access, and exit.

**Example Usage:**
```java
Sanctuary sanctuary = new Sanctuary(1000.0);
sanctuary.displayMenu();
```

#### PetAdventureQuest.java

- **Interactive Quests:** Pets can participate in adventure quests, collecting items, making decisions, and facing challenges.
- **Data Structures:** Uses HashMap (quest challenges), Stack (move history), Queue (item collection), LinkedList (adventure log), Tree (decision tree), PriorityQueue (low-health pets), Graph (adventure maps), Arrays (challenge storage).
- **Key Methods:** `runAdventureSimulation`, `scheduleAdventureQuest`, `prioritizeLowHealthPets`, `undoLastMove`, `displayAdventureLog`, `adventureMenu`.

#### PetSocialNetwork.java

- **Social Analysis:** Analyzes and recommends social interactions between pets.
- **Data Structures:** Graph (adjacency matrix), Stack (interaction history), PriorityQueue (mood), HashMap (preferences), Tree (BST), Queue, LinkedList, Arrays.
- **Key Methods:** `recommendCompatiblePets`, `scheduleSocialEvent`, `prioritizeLowMoodPets`, `undoLastInteraction`, `displayInteractionLog`, `socialNetworkMenu`.

---

## Pet Module

The **Pet** module manages all pet-related data, classes, and interactions.

### File Structure

- `Pet/Pet.java` — Abstract base class for all pets.
- `Pet/Dog.java`, `Pet/Rabbit.java`, `Pet/Horse.java`, `Pet/Lion.java` — Concrete pet subclasses.
- `Pet/InteractPet.java` — Interface for pet interactions.
- `Pet/PetManager.java` — Manages all pets, including storage, search, assignment, and file I/O.
- `Pet/PetNode.java` — Node for singly linked list.
- `Pet/PetNodeBst.java` — Node for binary search tree.
- `Pet/PetInteraction.java` — Manages pet-to-pet interactions as a graph.

### Core Classes

#### Pet.java (Abstract)

- **Fields:** `petId`, `petName`, `petType`, `petForSale`, `petAge`, `petPrice`, `petHunger`, `petHealth`, `petMood`, `PetManager petManager`
- **Key Methods:** `feedPet()`, `playWithPet()`, `healPet()`, `simulateDailyNeeds()`, file I/O, abstract `eat()`, `play()`, `heal()`

#### Pet Subclasses

- **Dog:** `eat()` reduces hunger by 10, `play()` increases mood by 12, `heal()` increases health by 16.
- **Rabbit:** `eat()` reduces hunger by 8, `play()` increases mood by 10, `heal()` increases health by 14.
- **Horse:** `eat()` reduces hunger by 12, `play()` increases mood by 15, `heal()` increases health by 18.
- **Lion:** `eat()` reduces hunger by 15, `play()` increases mood by 8, `heal()` increases health by 20.

#### InteractPet.java

- Interface for `eat()`, `play()`, `heal()`.

#### PetManager.java

- **Data Structures:** Singly Linked List (PetNode), BST (PetNodeBst), HashMap (CareTaker to pets).
- **Key Methods:** `addPet`, `removePet`, `getPet`, `getPets`, `getPetsForSale`, `displayAllPets`, `assignPetToCareTaker`, file I/O, sorting, filtering.

#### PetInteraction.java

- **Graph:** Manages pet-to-pet interactions using adjacency matrix.
- **Key Methods:** `addPet`, `addInteraction`, `displayInteraction`, `findCommonInteractions`, `detectInteractionGraph`.

---

## User Module

The **User** module manages all user-related data, authentication, roles, and user-specific features.

### File Structure

- `User/User.java` — Abstract base class for all users.
- `User/Manager.java` — Manager role.
- `User/CareTaker.java` — CareTaker role.
- `User/UserManager.java` — Manages user registration, login, storage, and role assignment.
- `User/UserNodeBST.java` — Node for BST for fast user lookup.
- `User/Interactable.java` — Interface for user-pet interaction.
- `User/PetHealthNode.java` — (Inner class in CareTaker) Node for BST to track pet health.

### Core Classes

#### User.java (Abstract)

- **Fields:** `userName`, `password`, `email`, `age`, `role`, `lastLogin`, static `totalUser`
- **Key Methods:** Getters/Setters, `interactWithPet(Pet pet)` (abstract), `toString()`

#### Manager.java

- **Features:** Funds management, pet management, event management, reporting, critical pet prioritization, pet interaction tracking, social network & adventures.
- **Example:**
    ```java
    Manager manager = new Manager("Alice", "alice@email.com", "password123", 30, 'M');
    manager.addFunds(5000);
    manager.generateReport(petManager);
    ```

#### CareTaker.java

- **Features:** Assigned pets (doubly linked list), care queue, pet health monitoring (BST), pet interaction, event resolution, navigation, social network & adventures.
- **Example:**
    ```java
    CareTaker ct = new CareTaker("Bob", "bob@email.com", "password456", 25, 'C');
    ct.assignPetToCareTaker(pet);
    ct.interactWithPet(pet);
    ```

#### UserManager.java

- **Data Structures:** ArrayList (users), BST (UserNodeBST), Singleton (Manager).
- **Key Methods:** `register`, `login`, `removeUser`, `getUser`, `isEmailRegistered`, file I/O, search by prefix, count active users.

#### UserNodeBST.java

- Node for BST used in `UserManager`.

#### Interactable.java

- Interface for `interactWithPet(Pet pet)`.

#### PetHealthNode.java

- Node for BST to track assigned pets by health (in CareTaker).

---

## Event Module

The **Event** module tracks, triggers, resolves, and manages all events in the sanctuary.

### File Structure

- `Event/Event.java` — Defines the Event class.
- `Event/EventNode.java` — Node for doubly linked list (timeline).
- `Event/EventManager.java` — Manages all events, dependencies, timeline, undo/redo, file I/O.
- `Event/EventTriggerable.java` — Interface for event-related actions.

### Core Classes

#### Event.java

- **Fields:** `eventType`, `date`, `description`, `affectedPet`, `isResolved`
- **Key Methods:** `getEventDetails()`, `isCritical()`, `resolveEvent()`, getters/setters, `toString()`

**Example:**
```java
Pet bunny = new Rabbit(1, "bunny", true, 2, 100.0);
Event escape = new Event("Escape", bunny, "bunny has escaped!", LocalDateTime.now());
System.out.println(escape.getEventDetails());
escape.resolveEvent();
```

#### EventNode.java

- Node for doubly linked list (timeline).

#### EventTriggerable.java

- Interface for `triggerRandomEvent`, `resolveEvent`, `displayEvents`, `getUnresolvedEvent`.

#### EventManager.java

- **Data Structures:** ArrayList (events), Queue (pending events), Stack (undo/redo), HashMap (dependencies), Doubly Linked List (timeline).
- **Key Methods:** `addEvent`, `triggerRandomEvent`, `resolveEvent`, `displayEvents`, `getUnresolvedEvent`, `displayUnresolvedEvents`, `getEventById`, file I/O, dependencies, undo/redo, timeline, sorting, searching.

**Example Usage:**
```java
eventManager.triggerRandomEvent(petManager.getAllPets());
eventManager.displayUnresolvedEvents();
eventManager.resolveEventByID(1);
eventManager.undoLastEventAction();
```

---

## Guest Module

The **Guest** module allows visitors to browse, buy, and interact with pets using various data structures and algorithms.

### File Structure

- `Guest/Guest.java` — Implements all guest functionalities.

### Core Features

- **Circular Linked List:** For cycling through pets for sale.
- **Stack:** For pet viewing history.
- **Doubly Linked List:** For favorite pets navigation.
- **Singly Linked List:** For recent pet views.
- **Queue:** For pet interaction requests.
- **Graph (Adjacency List):** For pet recommendations.
- **Binary Search:** For searching pets by name.
- **Insertion Sort:** For sorting pets by age.

### Main Methods

- `viewPetsForSale()`: Displays pets for sale.
- `cycleToNextPetForSale()`: Cycles to next pet.
- `markPetAsFavorite()`: Marks pet as favorite.
- `buyPet()`: Buys a pet.
- `viewFavoritePets()`: Displays favorite pets.
- `viewPetEvents(EventManager)`: Displays unresolved pet events.
- `searchPetByName()`: Searches pet by name.
- `sortPetsByAge()`: Sorts pets by age.
- `displayRecentViewedPet()`: Shows recent pet views.
- `addInteractionRequest(String)`, `displayInteractionRequests()`: Manages interaction requests.
- `recommendSimilarPets(String)`: Recommends similar pets.
- `viewPetHistory()`: Shows pet viewing history.
- `navigateFavoritePets(String)`: Navigates favorite pets.
- `searchPetsByPriceRange(double, double)`: Searches pets by price range.

---

## Example Main Menu

```
=== Virtual Pet Sanctuary Simulator ===
1: Login
2: Register
3: Continue as Guest
0: Exit
Enter choice:
```

---

## Notes

- All modules use advanced data structures for efficient management, analytics, and undo/redo.
- Data is persisted in text files (`users.txt`, `pets.txt`, `events.txt`, etc.).
- Menus and features are role-based and interactive.
- The project is extensible for future features.

---


## Author

- [Hassan Shehzad]
