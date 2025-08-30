# Sanctuary Module - Virtual Pet Sanctuary

This document describes the **Sanctuary** module, which acts as the central hub for managing pets, users, events, funds, and advanced features like pet adventures and social networks in the Virtual Pet Sanctuary project.

---

## File Structure

- **Sanctuary.java** — Main class for sanctuary operations, funds, and menu navigation.
- **PetAdventureQuest.java** — Manages pet adventure quests using multiple data structures.
- **PetSocialNetwork.java** — Analyzes and recommends pet social interactions using advanced algorithms and data structures.

---

## Core Features & Responsibilities

### 1. `Sanctuary.java`

The main class that coordinates all modules and provides the main menu for users.

**Key Responsibilities:**
- **Funds Management:** Tracks and updates sanctuary funds using a static variable and a Stack for undo/redo.
- **Pet Management:** Uses `PetManager` to add, remove, and manage pets.
- **User Management:** Uses `UserManager` for user registration, login, and role assignment.
- **Event Management:** Uses `EventManager` to handle all event-related operations.
- **Daily Updates:** Simulates daily pet needs and triggers critical events.
- **History Tracking:** Maintains a Queue for load history and a Stack for fund changes.
- **Menu Navigation:** Provides the main menu for login, registration, guest access, and exit.

**Key Methods:**
- `addFunds(double amount)`: Adds funds and logs the operation.
- `addPetToSanctuary(Pet pet)`: Adds a pet to the sanctuary.
- `runDailyUpdates()`: Simulates daily needs and triggers events for critical pets.
- `displayMenu()`: Main menu for user interaction.

**Example Usage:**
```java
Sanctuary sanctuary = new Sanctuary(1000.0);
sanctuary.displayMenu();
```

---

### 2. `PetAdventureQuest.java`

Allows pets to participate in interactive adventure quests, turning the sanctuary into a game-like experience.

**Data Structures Used:**
- **HashMap:** Maps quest names to challenges.
- **Stack:** Tracks adventure move history (undo/redo).
- **Queue:** Manages item collection during adventures.
- **LinkedList:** Logs adventure events.
- **Tree (Decision Tree):** Represents adventure paths and choices.
- **PriorityQueue (Heap):** Prioritizes low-health pets for adventures.
- **Graph (HashMap of Lists):** Represents adventure maps for pathfinding.
- **Arrays:** Stores and sorts quest challenges.

**Key Methods:**
- `runAdventureSimulation(Pet targetPet, String questName)`: Runs an adventure for a pet.
- `scheduleAdventureQuest(Pet pet1, Pet pet2, String questName)`: Schedules a quest for two pets.
- `prioritizeLowHealthPets()`: Prioritizes pets with low health.
- `undoLastMove()`: Undoes the last adventure move.
- `displayAdventureLog()`: Displays the adventure log.
- `adventureMenu()`: Provides a menu for adventure features.

**Example Usage:**
```
=== Pet Adventure Quest Menu ===
1: Start Adventure for a Pet
2: Schedule Adventure Quest for Two Pets
3: Prioritize Low-Health Pets for Adventure
4: Undo Last Move
5: Display Adventure Log
0: Exit
```

---

### 3. `PetSocialNetwork.java`

Analyzes and recommends social interactions between pets based on their interaction history, health, mood, and preferences.

**Data Structures Used:**
- **Graph (Adjacency Matrix):** Tracks pet interactions.
- **Stack:** Stores interaction history for undo/redo.
- **PriorityQueue (Heap):** Prioritizes pets for social activities based on mood.
- **HashMap:** Stores compatibility preferences.
- **Tree (BST):** Organizes pets by compatibility score.
- **Queue:** Schedules social events.
- **LinkedList:** Logs interaction events.
- **Arrays:** Groups pets for sorting and searching.
- **Sorting/Searching:** QuickSort and Binary Search for compatibility ranking.

**Key Methods:**
- `recommendCompatiblePets(Pet targetPet)`: Recommends compatible pets.
- `scheduleSocialEvent(Pet pet1, Pet pet2)`: Schedules a social event.
- `prioritizeLowMoodPets()`: Prioritizes pets with low mood.
- `undoLastInteraction()`: Undoes the last interaction.
- `displayInteractionLog()`: Shows the interaction log.
- `socialNetworkMenu()`: Provides a menu for social network features.

**Example Usage:**
```
=== Pet Social Network Menu ===
1: Recommend Compatible Pets
2: Schedule Social Event
3: Prioritize Low-Mood Pets
4: Undo Last Interaction
5: Display Interaction Log
0: Exit
```

---

## Input/Output Details

- **Input:** User menu choices, pet names, quest names, event details, fund amounts.
- **Output:** Console messages, updated files (`pets.txt`, `users.txt`, `events.txt`), logs, and real-time feedback.

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

- The Sanctuary module is the entry point and coordinator for all major features.
- All data is persisted in text files for reliability.
- Advanced data structures are used for efficient management, undo/redo, and analytics.
- The module is extensible for future features like new adventure types or social analytics.

---

**For further details, see code comments or contact the