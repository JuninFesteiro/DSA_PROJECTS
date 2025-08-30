# User Module - Virtual Pet Sanctuary

The User module handles user registration, login, roles, and user-specific features for the Virtual Pet Sanctuary project.

---

## Features

- Register and login users (Manager or CareTaker)
- Role-based menus and permissions
- Fast user lookup using a Binary Search Tree (BST)
- Only one Manager allowed (Singleton pattern)
- Assign pets to CareTakers
- Track user activity and last login
- Persist user data in `users.txt`

---

## Main Classes and Functionality

### User.java (Abstract Class)

- **Defines common fields:**  
  `userName`, `password`, `email`, `age`, `role`, `lastLogin`  
  These store the user's basic information and last login time.

- **Static field:**  
  `totalUser` — Tracks the total number of users created.

- **Constructor:**  
  Initializes all user fields when a new user is created.

- **Getters/Setters:**  
  Allow access and modification of user fields.

- **Abstract method:**  
  `interactWithPet(Pet pet)` — Forces subclasses to define how a user interacts with a pet.

- **toString():**  
  Returns a string representation of the user for display or debugging.

---

### Manager.java

- **Inherits from User:**  
  Gets all fields and methods from User.

- **Manages funds:**  
  Methods to add, save, load, and undo funds using a Stack for undo/redo.

- **Pet management:**  
  Can add or remove pets, and assign pets to CareTakers.

- **Event management:**  
  Can view, resolve, and manage event dependencies and timelines.

- **Reporting:**  
  Can generate sanctuary reports (e.g., number of pets, funds, events).

- **Critical pet prioritization:**  
  Uses a PriorityQueue to find and handle pets with low health.

- **Pet interaction tracking:**  
  Uses a Graph (Adjacency Matrix) to track pet interactions.

- **Access to advanced features:**  
  Can analyze the pet social network and manage pet adventures.

---

### CareTaker.java

- **Inherits from User:**  
  Gets all fields and methods from User.

- **Assigned pets:**  
  Uses a doubly linked list to manage pets assigned to the CareTaker.

- **Care queue:**  
  Uses a Queue to manage the order in which pets are cared for.

- **Pet health monitoring:**  
  Uses a BST to track the health of assigned pets.

- **Pet interaction:**  
  Can feed, play with, and heal assigned pets.

- **Event resolution:**  
  Can resolve events for assigned pets.

- **Navigation/search/sort:**  
  Can navigate assigned pets (forward/backward), search by mood, and sort by hunger.

- **Access to advanced features:**  
  Can use the pet social network and adventure features.

---

### UserManager.java

- **Stores all users:**  
  Uses an ArrayList to keep all user objects in memory.

- **Fast lookup:**  
  Uses a BST (UserNodeBST) for quick user lookup by email.

- **Singleton Manager:**  
  Ensures only one Manager can be registered in the system.

- **register():**  
  Registers a new user, checks for unique email, and adds to the BST and ArrayList.

- **login():**  
  Authenticates a user by email, password, and role.

- **removeUser():**  
  Removes a user from the system by email.

- **getUser():**  
  Retrieves a user object by email.

- **isEmailRegistered():**  
  Checks if an email is already registered.

- **loadUsers()/saveUsers():**  
  Loads users from and saves users to `users.txt`.

- **findUserEmailPrefix():**  
  Finds users whose email starts with a given prefix.

- **countActiveUser():**  
  Counts users who have logged in since a given time.

---

### UserNodeBST.java

- **Node for BST:**  
  Contains a User object, the user's email (as the key), and left/right child nodes.

---

### Interactable.java (Interface)

- **Defines:**  
  `interactWithPet(Pet pet)` — All user types must implement this method to interact with pets.

---

### PetHealthNode.java (Inner class in CareTaker)

- **Node for BST:**  
  Used to track assigned pets by health for efficient health-based operations.

---

## Example Registration & Login Flow

1. **Register a Manager:**
   - User enters name, email, password, age, and role.
   - System checks if email is unique and if a Manager already exists.
   - If valid, creates a Manager object and adds it to the system.
   - Saves user data to `users.txt`.

2. **Login as CareTaker:**
   - User enters email, password, and role.
   - System searches for the user in the BST.
   - If credentials match, logs in the user and updates `lastLogin`.

---

## File Structure

- `users.txt` — Stores all user data in CSV format

---

## Notes

- All user actions and assignments are role-based.
- User data is loaded and saved automatically.
- Menus and features are different for Manager and CareTaker.
- Advanced data structures (BST, LinkedList, Queue, Stack) are used for efficient management.

---
