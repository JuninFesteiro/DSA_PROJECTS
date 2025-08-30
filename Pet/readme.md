# Pet Module - Virtual Pet Sanctuary

This document describes the **Pet** module, which manages all pet-related data, classes, and interactions in the Virtual Pet Sanctuary project.

---

## File Structure

- **Pet.java** — Abstract base class for all pets.
- **Dog.java, Rabbit.java, Horse.java, Lion.java** — Concrete pet subclasses with species-specific behaviors.
- **InteractPet.java** — Interface for pet interactions (eat, play, heal).
- **PetManager.java** — Manages all pets, including storage, search, assignment, and file I/O.
- **PetNode.java** — Node for singly linked list (pet list).
- **PetNodeBst.java** — Node for binary search tree (pet search).
- **PetInteraction.java** — Manages pet-to-pet interactions using a graph (adjacency matrix).

---

## Core Classes & Responsibilities

### 1. `Pet.java` (Abstract Class)

Defines the common attributes and methods for all pets.

**Fields:**
- `petId`, `petName`, `petType`, `petForSale`, `petAge`, `petPrice`, `petHunger`, `petHealth`, `petMood`
- `PetManager petManager` (optional reference)

**Key Methods:**
- `feedPet()`, `playWithPet()`, `healPet()`, `simulateDailyNeeds()`
- `toFileString()` / `fromFileString(String line)` for file I/O
- Abstract: `eat()`, `play()`, `heal()`
- Getters/Setters for all fields

**Example:**
```java
Pet dog = new Dog(1, "Buddy", true, 3, 150.0, 80, 70, 40);
dog.eat(); // Buddy(Dog) ate food. Hunger reduced to: 30
```

---

### 2. Pet Subclasses

Each subclass implements species-specific logic for `eat()`, `play()`, and `heal()`.

#### `Dog.java`
- `eat()`: Reduces hunger by 10
- `play()`: Increases mood by 12
- `heal()`: Increases health by 16

#### `Rabbit.java`
- `eat()`: Reduces hunger by 8
- `play()`: Increases mood by 10
- `heal()`: Increases health by 14

#### `Horse.java`
- `eat()`: Reduces hunger by 12
- `play()`: Increases mood by 15
- `heal()`: Increases health by 18

#### `Lion.java`
- `eat()`: Reduces hunger by 15
- `play()`: Increases mood by 8
- `heal()`: Increases health by 20

---

### 3. `InteractPet.java` (Interface)

Defines:
- `eat()`
- `play()`
- `heal()`

Implemented by all pet subclasses.

---

### 4. `PetManager.java`

Manages all pets using:
- **Singly Linked List** (PetNode): For storing all pets.
- **Binary Search Tree** (PetNodeBst): For fast search by pet ID.
- **HashMap**: For mapping CareTakers to their assigned pets.

**Key Methods:**
- `addPet(Pet pet)`, `removePet(String name)`, `getPet(String name)`, `getPetById(int id)`
- `getPets()`, `getPetsForSale()`, `displayAllPets()`
- `assignPetToCareTaker(CareTaker, Pet)`
- `savePetsToFile(String)`, `loadPetsFromFile(String)`
- `sortPetsByHealth()`, `filterPetsByType(String)`
- `findPetByIdRange(int minId, int maxId)`

**Example:**
```java
PetManager pm = new PetManager(userManager);
pm.addPet(new Dog(1, "Buddy", true, 3, 150.0, 80, 70, 40));
pm.displayAllPets();
```

---

### 5. `PetNode.java` & `PetNodeBst.java`

- **PetNode:** Node for singly linked list (fields: `data`, `next`, `prev`)
- **PetNodeBst:** Node for BST (fields: `data`, `petId`, `left`, `right`)

---

### 6. `PetInteraction.java`

Manages pet-to-pet interactions as a **graph** (adjacency matrix).

**Key Methods:**
- `addPet(Pet pet)`: Adds a pet to the graph
- `addInteraction(String petName1, String petName2, int weight)`: Adds/updates interaction
- `displayInteraction()`: Shows the interaction matrix
- `findCommonInteractions(String petName1, String petName2)`: Finds common friends
- `detectInteractionGraph()`: Detects cycles in the interaction graph

**Example:**
```java
PetInteraction pi = new PetInteraction(10);
pi.addPet(dog);
pi.addPet(rabbit);
pi.addInteraction("Buddy", "Fluffy", 3);
pi.displayInteraction();
```

---

## Input/Output Details

- **Input:** Pet data from user or file, pet names/IDs, interaction weights.
- **Output:** Console messages, updated `pets.txt`, assignment files, and interaction matrix.

---

## Example Usage

```java
PetManager pm = new PetManager(userManager);
pm.addPet(new Dog(1, "Buddy", true, 3, 150.0, 80, 70, 40));
pm.addPet(new Rabbit(2, "Fluffy", true, 2, 100.0, 90, 80, 30));
pm.displayAllPets();

PetInteraction pi = new PetInteraction(10);
pi.addPet(pm.getPet("Buddy"));
pi.addPet(pm.getPet("Fluffy"));
pi.addInteraction("Buddy", "Fluffy", 4);
pi.displayInteraction();
```

**Sample Output:**
```
Pet [ID=1, Name=Buddy, ...]
Pet [ID=2, Name=Fluffy, ...]
Buddy Pet interacts with Fluffy
Buddy Fluffy 
Buddy 
0 4 
Fluffy 
4 0 
```

---

## Notes

- All pet data is persisted in `pets.txt`.
- Pet assignments are tracked in `assignments.txt`.
- Pet interactions are managed as a graph for social network analysis.
- Data structures ensure efficient storage, search, and management of pets.

---

**For further details, see code comments or contact the project