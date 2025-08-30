package Guest; // Guest package mein class

import Event.Event; // Event class import
import Event.EventManager; // EventManager class import
import Pet.Pet; // Pet class import
import Pet.PetManager; // PetManager class import
import Sanctuary.Sanctuary; // Sanctuary class import

// Scanner for user input
// ArrayList for favorite pets and graph
// LinkedList for stack
// Queue for BFS in graph
// Iterator for Stack class
import java.util.*;

public class Guest { // Guest class

    // Fields
    private PetManager petManager; // PetManager ka reference
    private Sanctuary sanctuary; // Sanctuary ka reference
    private EventManager eventManager; // EventManager ka reference
    private ArrayList<Pet> favoritePets = new ArrayList<>(); // Favorite pets ki list
    private Stack<Pet> petHistory; // Stack for pet viewing history
    private FavoritePetNode favoriteHead; // Doubly linked list ka head for favorite pets
    private FavoritePetNode favoriteTail; // Doubly linked list ka tail for favorite pets
    private FavoritePetNode currentFavorite; // Current favorite pet node
    private ArrayList<Pet>[] adjacencyList; // Adjacency list for pet recommendation graph

    private PetViewNode recentViewsHead; // Recent views ke liye singly linked list ka head
    private Queue<Pet> interactionRequests; // Interaction requests ke liye queue
    // ===== Circular Linked List Setup =====

    private class PetNode { // Circular linked list ke liye PetNode class

        Pet pet; // Pet ka data
        PetNode next; // Agla node
        PetNode prev; // Pichhla node

        PetNode(Pet pet) { // Constructor
            this.pet = pet;
        }
    }

    // ===== Doubly Linked List Setup for Favorite Pets =====
    private class FavoritePetNode { // Doubly linked list ke liye FavoritePetNode class

        Pet pet; // Pet ka data
        FavoritePetNode next; // Agla node
        FavoritePetNode prev; // Pichhla node

        FavoritePetNode(Pet pet) { // Constructor
            this.pet = pet; // Pet data set karna
        }
    }

    // Inner class for singly linked list node
    private class PetViewNode { // Singly linked list node define karta hai

        Pet pet; // Pet object store karta hai
        PetViewNode next; // Next node ka reference

        PetViewNode(Pet pet) { // Constructor banata hai
            this.pet = pet; // Pet set karta hai
            this.next = null; // Next ko null set karta hai
        }
    }

    private PetNode head; // Circular linked list ka head
    private PetNode tail; // Circular linked list ka tail
    private PetNode currentPet; // Current pet node

    // Initialize circular list with pets for sale
    private void initializePetList(ArrayList<Pet> petsForSale) { // Pets for sale ke saath circular list initialize karna
        head = tail = currentPet = null;
        for (Pet pet : petsForSale) {
            addPetToCircularList(pet);
        }
        if (head != null) {
            currentPet = head;
        }
    }

    // Add pet to circular list
    private void addPetToCircularList(Pet pet) { // Pet ko circular list mein add karna
        PetNode newNode = new PetNode(pet);
        if (head == null) {
            head = tail = newNode;
            head.next = head;
            head.prev = head;
        } else {
            newNode.prev = tail;
            newNode.next = head;
            tail.next = newNode;
            head.prev = newNode;
            tail = newNode;
        }
    }

    // View pets for sale
    public void viewPetsForSale() { // Bikri ke liye pets dekhna
        initializePetList(petManager.getPetsForSale()); // Refresh circular list
        System.out.println("\n=== Available pets for sale ===");
        if (head == null) {
            System.out.println("No pets available for sale.");
            return;
        }
        PetNode temp = head;
        do {
            System.out.println(temp.pet);
            petHistory.push(temp.pet); // Pet ko history stack mein push karna
            temp = temp.next;
        } while (temp != head);
    }

    // Cycle to next pet for sale
    public Pet cycleToNextPetForSale() { // Agle pet for sale pe cycle karna
        if (currentPet == null) {
            return null;
        }
        Pet pet = currentPet.pet;
        petHistory.push(pet); // Pet ko history stack mein push karna
        currentPet = currentPet.next;
        return pet;
    }

    // Mark pet as favorite
    public void markPetAsFavorite() { // Pet ko favorite mark karna
        if (currentPet != null && !favoritePets.contains(currentPet.pet)) {
            favoritePets.add(currentPet.pet);
            addToFavoriteList(currentPet.pet); // Pet ko doubly linked list mein add karna
            System.out.println("Marked as favorite: " + currentPet.pet.getPetName());
        } else {
            System.out.println("Pet already in favorites or no pet selected.");
        }
    }

    // Buy pet by ID
    private void buyPetById(String petId) { // Pet ID se kharidna
        if (head == null) {
            System.out.println("No pets available.");
            return;
        }
        PetNode temp = head;
        boolean found = false;
        do {
            if (temp.pet.getPetId() == Integer.parseInt(petId)) {
                found = true;
                break;
            }
            temp = temp.next;
        } while (temp != head);

        if (!found) {
            System.out.println("Pet not found.");
            return;
        }

        Pet pet = temp.pet;
        double price = pet.getPetPrice();
        if (Sanctuary.getFunds() < price) {
            System.out.println("Insufficient sanctuary funds to process purchase.");
            return;
        }

        // Remove from circular list
        if (temp == head && temp == tail) {
            head = tail = null;
        } else {
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
            if (temp == head) {
                head = temp.next;
            }
            if (temp == tail) {
                tail = temp.prev;
            }
        }
        if (currentPet == temp) {
            currentPet = temp.next;
        }

        // Remove from PetManager and deduct funds
        petManager.removePet(pet.getPetName());
        Sanctuary.addFunds(-price); // Deduct price from sanctuary funds
        System.out.println("Pet bought: " + pet.getPetName() + " for $" + price);
    }

    // Constructors
    public Guest(PetManager petManager, Sanctuary sanctuary, EventManager eventManager) {
        // Line ~50: PetManager set karta hai
        this.petManager = petManager;
        // Line ~51: Sanctuary set karta hai
        this.sanctuary = sanctuary;
        // Line ~52: EventManager set karta hai
        this.eventManager = eventManager;
        // Line ~53: Pet history stack initialize karta hai
        this.petHistory = new Stack<>();
        // Line ~54: Favorite list initialize karta hai
        this.favoriteHead = this.favoriteTail = this.currentFavorite = null;
        // Line ~55: Circular list initialize karta hai
        initializePetList(petManager.getPetsForSale());
        // Line ~56: Recent views list ka head null set karta hai
        this.recentViewsHead = null;
        // Line ~57: Interaction requests queue initialize karta hai
        this.interactionRequests = new LinkedList<>();
    }
    // Guest Menu
    // Guest menu with PetManager argument
    public void guestMenu(PetManager petManager) { // Guest menu method define karta hai
        Scanner input = new Scanner(System.in); // Scanner object banata hai user input ke liye

        System.out.println("Guest, Enter your name: "); // Guest se naam input mangta hai
        String guestName = input.nextLine(); // Guest ka naam input leta hai

        System.out.println("\nWelcome, " + guestName + " What would you like to do? "); // Welcome message print karta hai

        int choice; // User choice store karne ke liye variable
        do { // Loop chalata hai jab tak user exit nahi karta
            System.out.println("\n=== Guest Menu ==="); // Menu title print karta hai
            System.out.println("1. View pets for sale"); // Option 1: Pets for sale dekhne ka
            System.out.println("2. Buy a pet"); // Option 2: Pet kharidne ka
            System.out.println("3. Cycle to next pet"); // Option 3: Agla pet cycle karne ka
            System.out.println("4. Mark pet as favorite"); // Option 4: Pet ko favorite mark karne ka
            System.out.println("5. View favorite pets"); // Option 5: Favorite pets dekhne ka
            System.out.println("6. View pet events"); // Option 6: Pet events dekhne ka
            System.out.println("7. Search pets by name"); // Option 7: Pet ko name se search karne ka
            System.out.println("8. Sort pets by age"); // Option 8: Pets ko age se sort karne ka
            System.out.println("9. View recent pet views"); // Option 9: Recent pet views dekhne ka
            System.out.println("10. Request pet interaction"); // Option 10: Pet interaction request karne ka
            System.out.println("0. Back to main menu"); // Option 0: Main menu pe wapas jane ka
            System.out.print("Enter choice: "); // Choice input ke liye prompt
            choice = input.nextInt(); // Choice input leta hai
            input.nextLine(); // Input buffer clear karta hai

            switch (choice) { // Choice ke hisaab se action
                case 1: // View pets for sale
                    viewPetsForSale(); // Pets for sale display karta hai
                    break; // Switch case se bahar nikalta hai
                case 2: // Buy a pet
                    buyPet(); // Pet kharidne ka method call karta hai
                    break; // Switch case se bahar nikalta hai
                case 3: // Cycle to next pet
                    Pet nextPet = cycleToNextPetForSale(); // Agla pet cycle karta hai
                    if (nextPet != null) { // Agar pet mila
                        System.out.println("Current pet: " + nextPet.getPetName()); // Current pet ka naam print karta hai
                    } else { // Agar pet nahi mila
                        System.out.println("No pets available."); // Error message print karta hai
                    }
                    break; // Switch case se bahar nikalta hai
                case 4: // Mark pet as favorite
                    markPetAsFavorite(); // Pet ko favorite mark karne ka method call karta hai
                    break; // Switch case se bahar nikalta hai
                case 5: // View favorite pets
                    viewFavoritePets(); // Favorite pets display karta hai
                    break; // Switch case se bahar nikalta hai
                case 6: // View pet events
                    viewPetEvents(eventManager); // Pet events display karta hai
                    break; // Switch case se bahar nikalta hai
                case 7: // Search pets by name
                    searchPetByName(); // Binary search se pet name dhoondhne ka method call karta hai
                    break; // Switch case se bahar nikalta hai
                case 8: // Sort pets by age
                    sortPetsByAge(); // Insertion sort se pets ko age ke basis pe sort karne ka method call karta hai
                    break; // Switch case se bahar nikalta hai
                case 9: // View recent pet views
                    displayRecentViewedPet(); // Recent pet views display karne ka method call karta hai
                    break; // Switch case se bahar nikalta hai
                case 10: // Request pet interaction
                    System.out.print("Enter pet name for interaction request: "); // Pet name input ke liye prompt
                    String petName = input.nextLine(); // Pet name input leta hai
                    addInteractionRequest(petName); // Interaction request queue mein add karta hai
                    displayInteractionRequests(); // Current interaction requests display karta hai
                    break; // Switch case se bahar nikalta hai
                case 0: // Back to main menu
                    System.out.println("Returning to main menu...."); // Main menu pe wapas jane ka message print karta hai
                    return; // Method se return karta hai
                default: // Invalid choice
                    System.out.println("Invalid choice. Please try again."); // Error message print karta hai
            }
        } while (choice != 0); // Jab tak choice 0 nahi hai, loop chalta hai
    }

    // Buy Pets
     private void buyPet() {
        // Line ~180: Scanner object banata hai
        Scanner input = new Scanner(System.in);
        // Line ~181: Pet name input ke liye prompt
        System.out.print("Enter pet name to buy: ");
        // Line ~182: Pet name input leta hai
        String petName = input.nextLine();
        // Line ~183: PetManager se pet dhoondhta hai
        Pet pet = petManager.getPet(petName);
        // Line ~184: Agar pet mila aur for sale hai
        if (pet != null && pet.getPetForSale()) {
            // Line ~185: Pet ka price leta hai
            double price = pet.getPetPrice();
            // Line ~186: Agar funds insufficient hain
            if (Sanctuary.getFunds() < price) {
                // Line ~187: Error message print karta hai
                System.out.println("Insufficient sanctuary funds to process purchase.");
                // Line ~188: Method se return karta hai
                return;
            }
            // Line ~189: Circular list se pet remove karne ka logic
            PetNode temp = head;
            boolean found = false;
            if (head != null) {
                do {
                    if (temp.pet.getPetName().equalsIgnoreCase(petName)) {
                        found = true;
                        break;
                    }
                    temp = temp.next;
                } while (temp != head);
            }
            if (found) {
                if (temp == head && temp == tail) {
                    head = tail = null;
                } else {
                    temp.prev.next = temp.next;
                    temp.next.prev = temp.prev;
                    if (temp == head) {
                        head = temp.next;
                    }
                    if (temp == tail) {
                        tail = temp.prev;
                    }
                }
                if (currentPet == temp) {
                    currentPet = temp.next;
                }
            }
            // Line ~190: PetManager se pet remove karta hai
            petManager.removePet(petName);
            // Line ~191: Funds deduct karta hai
            Sanctuary.addFunds(-price);
            // Line ~192: Pets.txt mein changes save karta hai
            petManager.savePetsToFile("pets.txt");
            // Line ~193: Success message print karta hai
            System.out.println("Congratulations, you have bought " + pet.getPetName() + " for $" + price);
        } else {
            // Line ~194: Error message print karta hai
            System.out.println("Sorry, this pet is not available for sale.");
        }
    }


    // View favorite pets
    private void viewFavoritePets() { // Favorite pets dekhna
        if (favoriteHead == null) { // Agar favorite list khali hai
            System.out.println("No favorite pets."); // Error message
            return;
        }
        System.out.println("\n=== Favorite Pets ==="); // Header
        FavoritePetNode temp = favoriteHead; // Start from head
        while (temp != null) { // Loop through doubly linked list
            System.out.println(temp.pet); // Pet details print
            temp = temp.next; // Next node
        }
    }

    // View pet events
    public void viewPetEvents(EventManager eventManager) { // Pet-related events dekhna
        System.out.println("Viewing pet-related events..");
        ArrayList<Event> unresolvedEvents = eventManager.getUnresolvedEvent();
        if (unresolvedEvents.isEmpty()) {
            System.out.println("No unresolved events.");
            return;
        }
        for (Event event : unresolvedEvents) {
            System.out.println(event.toString()); // Fixed: getEventDetails to toString
        }
    }

    // New Feature 1: Search Pets by Price Range (Linear Search)
    public void searchPetsByPriceRange(double minPrice, double maxPrice) { // Pets ko price range ke basis pe search karna
        if (head == null) { // Agar circular list khali hai
            System.out.println("No pets available for sale."); // Error message
            return;
        }
        System.out.println("\n=== Pets in price range $" + minPrice + " - $" + maxPrice + " ==="); // Header
        PetNode temp = head; // Start from head
        boolean found = false; // Flag for found pets
        do { // Loop through circular list
            Pet pet = temp.pet; // Current pet
            if (pet.getPetPrice() >= minPrice && pet.getPetPrice() <= maxPrice) { // Price range check
                System.out.println(pet); // Pet details print
                found = true; // Found flag set
            }
            temp = temp.next; // Next node
        } while (temp != head); // Continue until back to head
        if (!found) { // Agar koi pet nahi mila
            System.out.println("No pets found in price range."); // Error message
        }
    }

    // New Feature 2: View Pet Viewing History (Stack)
    public void viewPetHistory() { // Recently viewed pets ka history dekhna
        if (petHistory.isEmpty()) { // Agar stack khali hai
            System.out.println("No pet viewing history."); // Error message
            return;
        }
        System.out.println("\n=== Pet Viewing History (Most Recent First) ==="); // Header
        for (Pet pet : petHistory) { // Loop through stack
            System.out.println(pet); // Pet details print
        }
    }

    // New Feature 3: Navigate Favorite Pets (Doubly Linked List)
    public void navigateFavoritePets(String direction) { // Favorite pets ko navigate karna
        if (favoriteHead == null) { // Agar favorite list khali hai
            System.out.println("No favorite pets to navigate."); // Error message
            return;
        }
        if (currentFavorite == null) { // Agar current node null hai
            currentFavorite = favoriteHead; // Start from head
        }
        if (direction.equalsIgnoreCase("forward")) { // Forward direction check
            currentFavorite = currentFavorite.next; // Next node
            if (currentFavorite == null) { // Agar end of list
                currentFavorite = favoriteHead; // Wrap around to head
            }
            System.out.println("Current favorite pet: " + currentFavorite.pet.getPetName()); // Current pet print
        } else if (direction.equalsIgnoreCase("backward")) { // Backward direction check
            currentFavorite = currentFavorite.prev; // Previous node
            if (currentFavorite == null) { // Agar start of list
                currentFavorite = favoriteTail; // Wrap around to tail
            }
            System.out.println("Current favorite pet: " + currentFavorite.pet.getPetName()); // Current pet print
        } else { // Invalid direction
            System.out.println("Invalid direction. Use 'forward' or 'backward'."); // Error message
        }
    }

    // Helper: Add pet to favorite doubly linked list
    private void addToFavoriteList(Pet pet) { // Pet ko favorite doubly linked list mein add karna
        FavoritePetNode newNode = new FavoritePetNode(pet); // Naya node banana
        if (favoriteHead == null) { // Agar list khali hai
            favoriteHead = favoriteTail = newNode; // Head aur tail set
            newNode.next = null; // Next null
            newNode.prev = null; // Prev null
        } else { // List mein add
            newNode.prev = favoriteTail; // New node ka prev tail
            favoriteTail.next = newNode; // Tail ka next new node
            favoriteTail = newNode; // Tail update
            newNode.next = null; // Next null
        }
        if (currentFavorite == null) { // Agar current favorite null hai
            currentFavorite = favoriteHead; // Current to head
        }
    }

    // New Feature 4: Recommend Similar Pets (Graph - Adjacency List)
    public void recommendSimilarPets(String petName) { // Similar pets recommend karna
        Pet selectedPet = petManager.getPet(petName); // PetManager se pet lena
        if (selectedPet == null || !selectedPet.getPetForSale()) { // Pet invalid ya not for sale
            System.out.println("Pet not found or not available for sale."); // Error message
            return;
        }
        buildRecommendationGraph(); // Graph banana
        System.out.println("\n=== Recommended Pets for " + petName + " ==="); // Header
        ArrayList<Pet> recommendations = bfsRecommendations(selectedPet); // BFS se recommendations
        if (recommendations.isEmpty()) { // Agar koi recommendations nahi
            System.out.println("No similar pets found."); // Error message
        } else { // Recommendations hain
            for (Pet pet : recommendations) { // Loop through recommendations
                System.out.println(pet); // Pet details print
            }
        }
    }

    // Helper: Build recommendation graph
    @SuppressWarnings("unchecked") // Unchecked warning suppress karna
    private void buildRecommendationGraph() { // Pet recommendation graph banana
        ArrayList<Pet> pets = petManager.getPetsForSale(); // Pets for sale lena
        adjacencyList = new ArrayList[pets.size()]; // Adjacency list initialize
        for (int i = 0; i < pets.size(); i++) { // Loop through pets
            adjacencyList[i] = new ArrayList<Pet>(); // New list for each pet
        }
        for (int i = 0; i < pets.size(); i++) { // Loop for each pet
            Pet pet1 = pets.get(i); // Current pet
            for (int j = i + 1; j < pets.size(); j++) { // Compare with other pets
                Pet pet2 = pets.get(j); // Other pet
                if (pet1.getPetType().equals(pet2.getPetType())
                        || // Same type
                        Math.abs(pet1.getPetPrice() - pet2.getPetPrice()) <= 100) { // Price within $100
                    adjacencyList[i].add(pet2); // Add edge to pet2
                    adjacencyList[j].add(pet1); // Add edge to pet1
                }
            }
        }
    }

    // Helper: BFS for recommendations
    private ArrayList<Pet> bfsRecommendations(Pet startPet) { // BFS se recommendations lena
        ArrayList<Pet> pets = petManager.getPetsForSale(); // Pets for sale
        ArrayList<Pet> recommendations = new ArrayList<>(); // Recommendations list
        boolean[] visited = new boolean[pets.size()]; // Visited array
        Queue<Integer> queue = new LinkedList<>(); // BFS queue
        int startIndex = pets.indexOf(startPet); // Start pet ka index
        if (startIndex == -1) {
            return recommendations; // Agar pet nahi mila

        }
        visited[startIndex] = true; // Start pet visited
        queue.offer(startIndex); // Start index queue mein
        while (!queue.isEmpty()) { // Jab tak queue khali nahi
            int index = queue.poll(); // Current index
            for (Pet neighbor : adjacencyList[index]) { // Neighbors loop
                int neighborIndex = pets.indexOf(neighbor); // Neighbor ka index
                if (!visited[neighborIndex]) { // Agar neighbor unvisited
                    visited[neighborIndex] = true; // Mark visited
                    recommendations.add(neighbor); // Add to recommendations
                    queue.offer(neighborIndex); // Add to queue
                }
            }
        }
        return recommendations; // Recommendations return
    }

    // Stack class for pet history
    private class Stack<T> implements Iterable<T> { // Generic stack class with Iterable

        private LinkedList<T> list = new LinkedList<>(); // LinkedList for stack

        public void push(T item) { // Item push karna
            list.addFirst(item); // Add to front
        }

        public T pop() { // Item pop karna
            return list.isEmpty() ? null : list.removeFirst(); // Remove from front
        }

        public boolean isEmpty() { // Stack khali hai check
            return list.isEmpty(); // List empty check
        }

        @Override
        public Iterator<T> iterator() { // Iterator return karna
            return list.iterator(); // LinkedList ka iterator return
        }
    }

    // Method For binary search, Wrritten By Hassan 
    public void searchPetByName() {
        // Line ~350: Scanner object banata hai
        Scanner scanner = new Scanner(System.in);
        // Line ~351: Pet name input ke liye prompt
        System.out.print("Enter Pet name: ");
        // Line ~352: Target name input leta hai
        String targetName = scanner.nextLine();
        // Line ~353: Pets ki list petManager se leta hai
        ArrayList<Pet> pets = petManager.getPetsForSale();
        // Line ~354: Pet names ka array banata hai
        String[] petNames = new String[pets.size()];
        // Line ~355: Har pet ka name array mein store karta hai
        for (int i = 0; i < pets.size(); i++) {
            petNames[i] = pets.get(i).getPetName();
        }
        // Line ~356: Pet names ko alphabetically sort karta hai
        Arrays.sort(petNames, String.CASE_INSENSITIVE_ORDER);
        // Line ~357: Binary search se pet dhoondhta hai
        int result = binarySearch(petNames, targetName);
        // Line ~358: Agar pet mila
        if (result != -1) {
            // Line ~359: Pet object dhoondhne ka logic
            Pet petFound = null;
            for (Pet pet : pets) {
                if (pet.getPetName().equalsIgnoreCase(petNames[result])) {
                    petFound = pet;
                    break;
                }
            }
            // Line ~360: Pet details print karta hai
            System.out.println("Pet found: Name: " + petFound.getPetName() + ", Type: " + petFound.getClass().getSimpleName() + ", Age: " + petFound.getPetAge() + ", Price: $" + petFound.getPetPrice());
        } else {
            // Line ~361: Error message print karta hai
            System.out.println("Pet not found: " + targetName);
        }
    }

    // BinarySearch Method 
   public int binarySearch(String[] petNames, String targetName) {
        // Line ~370: Binary search ke liye left pointer
        int left = 0;
        // Line ~371: Binary search ke liye right pointer
        int right = petNames.length - 1;
        // Line ~372: Jab tak left right se chhota ya barabar hai
        while (left <= right) {
            // Line ~373: Middle index calculate karta hai
            int mid = left + (right - left) / 2;
            // Line ~374: Case-insensitive comparison
            int compare = targetName.compareToIgnoreCase(petNames[mid]);
            // Line ~375: Agar target mil gaya
            if (compare == 0) {
                // Line ~376: Index return karta hai
                return mid;
            } else if (compare < 0) {
                // Line ~377: Left half mein search karta hai
                right = mid - 1;
            } else {
                // Line ~378: Right half mein search karta hai
                left = mid + 1;
            }
        }
        // Line ~379: Agar pet nahi mila
        return -1;
    }

    // Sort pets by age using insertion sort // wrriten by hassan
    public void sortPetsByAge() {
        ArrayList<Pet> pets = petManager.getPetsForSale(); // sale wala pets ko arayList me store kardia

        // Converting pets to  fixed array
        Pet[] petArray = pets.toArray(new Pet[0]);

        // Loop for insertion sort
        for (int i = 1; i < petArray.length; i++) {
            Pet key = petArray[i]; //current pet
            int j = i - 1;

            // Jab tak prev chotha ha
            while (j >= 0 && petArray[j].getPetAge() > key.getPetAge()) {
                petArray[j + 1] = petArray[i]; //Shifting 
                j--;
            }
            petArray[j + 1] = key;

        }

        System.out.println("Pets sorted by age (ascending):"); // Sorted list ka title print karta hai
        for (Pet pet : petArray) { // Har sorted pet ke liye loop
            System.out.println("Name: " + pet.getPetName() + ", Type: " + pet.getClass().getSimpleName() + ", Age: " + pet.getPetAge() + ", Price: $" + pet.getPetPrice()); // Pet details print karta hai
        }

    }

    // Add pets recent view hoisrty written by hassan
    public void addRecentViewPet(String petName) {

        // Pet ko dhooondhna ha 
        Pet pet = petManager.getPet(petName);

        if (pet == null) {
            System.out.println("Pet not found: " + petName); // Error message print karta hai
            return; // Method se return karta hai
        }

        PetViewNode newNode = new PetViewNode(pet); //naya pet aya ha 
        if (recentViewsHead == null) { // Agar list khali hai
            recentViewsHead = newNode; // Head ko naya node set karta hai
        } else { // Agar list mein nodes hain
            PetViewNode current = recentViewsHead; // Current node ko head se shuru karta hai
            while (current.next != null) { // Jab tak next node hai
                current = current.next; // Current ko next pe le jata hai
            }
            current.next = newNode; // Last node ke baad naya node add karta hai
        }
        System.out.println("Added to recent views: " + pet.getPetName());
    }

    // Display recent View by Hassan 
    public void displayRecentViewedPet() {
        if (recentViewsHead == null) { // Agar list khali hai
            System.out.println("No recent pet views."); // Message print karta hai
            return; // Method se return karta hai
        }

        System.out.println("Recent Pet Views:"); // Title print karta hai
        PetViewNode current = recentViewsHead; // Current node ko head se shuru karta hai
        while (current != null) { // Jab tak current node hai
            Pet pet = current.pet; // Pet object leta hai
            System.out.println("Name: " + pet.getPetName() + ", Type: " + pet.getClass().getSimpleName() + ", Age: " + pet.getPetAge()); // Pet details print karta hai
            current = current.next; // Current ko next node pe le jata hai
        }
    }

    // Add interaction to requets queue
    public void addInteractionRequest(String petName) {
        Pet pet = petManager.getPet(petName);
        if (pet == null) { // Agar pet nahi mila
            System.out.println("Pet not found: " + petName); // Error message print karta hai
            return; // Method se return karta hai
        }

        // Agar pet pehle se queue me ha
        if (interactionRequests.contains(pet)) {
            System.out.println("Pet already in interaction request queue: " + petName); // Error message print karta hai
            return; // Method se return karta hai
        }

        interactionRequests.add(pet); // Pet ko queue mein add karta hai
        System.out.println("Interaction request added for: " + pet.getPetName());

    }

    //  Display interaction request queue written by hassan 
    public void displayInteractionRequests() { // Interaction requests display karne ka method
        if (interactionRequests.isEmpty()) { // Agar queue khali hai
            System.out.println("No interaction requests pending."); // Message print karta hai
            return; // Method se return karta hai
        }

        System.out.println("Pending Interaction Requests:"); // Title print karta hai
        for (Pet pet : interactionRequests) { // Har pet ke liye loop
            System.out.println("Name: " + pet.getPetName() + ", Type: " + pet.getClass().getSimpleName()); // Pet details print karta hai
        }
    }
}
