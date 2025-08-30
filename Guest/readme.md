# Guest Module - Virtual Pet Sanctuary

This file documents the **Guest** class and its features for the Virtual Pet Sanctuary project. The Guest module allows visitors to browse, buy, and interact with pets using various data structures and algorithms.

---

## File Structure

- **Guest.java** — Implements all guest functionalities, including pet browsing, buying, favorites, history, recommendations, and interaction requests.

---

## Core Features & Data Structures

- **Circular Linked List:** For cycling through pets for sale.
- **Stack:** For pet viewing history.
- **Doubly Linked List:** For favorite pets navigation.
- **Singly Linked List:** For recent pet views.
- **Queue:** For pet interaction requests.
- **Graph (Adjacency List):** For pet recommendations.
- **Binary Search:** For searching pets by name.
- **Insertion Sort:** For sorting pets by age.

---

## Main Methods & Examples

### 1. `viewPetsForSale()`
Displays all pets available for sale using a circular linked list.

**Output Example:**
```
=== Available pets for sale ===
Pet{id=1, name='bunny', ...}
Pet{id=2, name='kitty', ...}
```

---

### 2. `cycleToNextPetForSale()`
Cycles to the next pet in the circular list.

**Output Example:**
```
Current pet: bunny
```

---

### 3. `markPetAsFavorite()`
Marks the current pet as favorite and adds it to a doubly linked list.

**Output Example:**
```
Marked as favorite: bunny
```

---

### 4. `buyPet()`
Buys a pet by name, removes it from the list, and deducts funds.

**Input Example:**
```
Enter pet name to buy: bunny
```
**Output Example:**
```
Congratulations, you have bought bunny for $100.0
```

---

### 5. `viewFavoritePets()`
Displays all favorite pets.

**Output Example:**
```
=== Favorite Pets ===
Pet{id=1, name='bunny', ...}
```

---

### 6. `viewPetEvents(EventManager eventManager)`
Displays unresolved events related to pets.

**Output Example:**
```
Viewing pet-related events..
Event: Illness | Date: 2025-08-30T12:00 | Resolved: No
```

---

### 7. `searchPetByName()`
Searches for a pet by name using binary search.

**Input Example:**
```
Enter Pet name: bunny
```
**Output Example:**
```
Pet found: Name: bunny, Type: Rabbit, Age: 2, Price: $100.0
```

---

### 8. `sortPetsByAge()`
Sorts pets by age using insertion sort.

**Output Example:**
```
Pets sorted by age (ascending):
Name: bunny, Type: Rabbit, Age: 2, Price: $100.0
Name: kitty, Type: Cat, Age: 3, Price: $120.0
```

---

### 9. `displayRecentViewedPet()`
Displays the singly linked list of recently viewed pets.

**Output Example:**
```
Recent Pet Views:
Name: bunny, Type: Rabbit, Age: 2
```

---

### 10. `addInteractionRequest(String petName)` & `displayInteractionRequests()`
Adds a pet to the interaction request queue and displays all requests.

**Input Example:**
```
Enter pet name for interaction request: bunny
```
**Output Example:**
```
Interaction request added for: bunny
Pending Interaction Requests:
Name: bunny, Type: Rabbit
```

---

### 11. `recommendSimilarPets(String petName)`
Recommends similar pets using a graph (adjacency list) and BFS.

**Output Example:**
```
=== Recommended Pets for bunny ===
Pet{id=2, name='kitty', ...}
```

---

### 12. `viewPetHistory()`
Displays the stack of recently viewed pets.

**Output Example:**
```
=== Pet Viewing History (Most Recent First) ===
Pet{id=1, name='bunny', ...}
```

---

### 13. `navigateFavoritePets(String direction)`
Navigates favorite pets in forward or backward direction.

**Input Example:**
```
navigateFavoritePets("forward");
```
**Output Example:**
```
Current favorite pet: bunny
```

---

### 14. `searchPetsByPriceRange(double minPrice, double maxPrice)`
Searches pets within a price range using linear search.

**Input Example:**
```
searchPetsByPriceRange(50, 150);
```
**Output Example:**
```
=== Pets in price range $50.0 - $150.0 ===
Pet{id=1, name='bunny', ...}
```

---

## Input/Output Details

- **Input:** User menu choices, pet names, price ranges, directions.
- **Output:** Console messages, updated lists, and pet data files.

---

## Notes

- All pet data is managed via `PetManager`.
- Funds are checked and updated via `Sanctuary`.
- Events are displayed using `EventManager`.
- Data structures ensure efficient navigation, search, and history tracking for guests.

---

**For further details, see code comments or contact the
