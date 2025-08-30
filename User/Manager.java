package User;

import Event.Event;
import Event.EventManager;
import Pet.*;
import Sanctuary.PetSocialNetwork;
import Sanctuary.Sanctuary;
import Sanctuary.PetAdventureQuest;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

// Manager class inherits User
public class Manager extends User {

    // Fields
    private double totalFunds = 0; // Total funds ka track rakhna

    // Constructor
    public Manager(String userName, String email, String password, int age, char role) {
        super(userName, password, email, age, role); // Parent class constructor call karna
        loadFunds(); // Funds ko file se load karna
    }

    // Interact with Pet
    @Override
    public void interactWithPet(Pet pet) {
        System.out.println("Manager: " + super.getUserName() + " managing sanctuary for pet " + pet.getPetName()); // Pet
        // ke
        // saath
        // interaction
    }

    // Add funds
    public void addFunds(double addFunds) {
        if (addFunds < 0) { // Agar amount negative hai
            System.out.println("Amount to add must be greater than 0");
        } else {
            totalFunds += addFunds; // Funds add karna
            System.out.println("Funds added: $" + addFunds + " | Total Funds: $" + totalFunds);
            saveFunds(); // Funds save karna
            Sanctuary.addFunds(addFunds); // Sanctuary ke funds ko bhi update karna
        }
    }

    // Save funds to file
    public void saveFunds() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("funds.txt"))) { // File writer kholna
            writer.write(String.valueOf(totalFunds)); // Funds ko string mein convert kar ke likhna
            System.out.println("Funds saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving funds: " + e.getMessage());
        }
    }

    // Load funds from file
    public void loadFunds() {
        try (BufferedReader reader = new BufferedReader(new FileReader("funds.txt"))) { // File reader kholna
            totalFunds = Double.parseDouble(reader.readLine()); // Funds ko parse karna
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading funds. Defaulting to $0.");
            totalFunds = 0; // Default funds set karna
        }
    }

    // Generate report
    public void generateReport(PetManager petManager) {
        System.out.println("\n=== Sanctuary Report ===");
        System.out.println("Total Funds: $" + totalFunds); // Total funds display karna
        System.out.println("Total Pets: " + petManager.getPetCount()); // Total pets count display karna
        System.out.println("Pets Available for Sale: " + petManager.getPetsForSale().size()); // Pets for sale count
    }

    // DSA: Linear Search and Array List yaha impliment hoi HMAZA
    // Search pets by price range using ArrayList and Linear Search
    public void searchPetsByPriceRange(PetManager petManager, double minPrice, double maxPrice) {
        ArrayList<Pet> pets = petManager.getPetsForSale(); // Pets for sale ki list lena
        ArrayList<Pet> result = new ArrayList<>(); // Result ke liye ArrayList banaya
        for (Pet pet : pets) { // Har pet ke liye linear search
            if (pet.getPetPrice() >= minPrice && pet.getPetPrice() <= maxPrice) { // Agar price range mein hai
                result.add(pet); // Pet ko result list mein add karna
            }
        }
        if (result.isEmpty()) { // Agar koi pet nahi mila
            System.out.println("No pets found in price range $" + minPrice + " - $" + maxPrice);
        } else {
            System.out.println("Pets in price range $" + minPrice + " - $" + maxPrice + ":");
            for (Pet pet : result) { // Har pet ke details print karna
                System.out.println(pet); // Pet ki details display karna
            }
        }
    }

    // Undo last fund change using Stack //Hamza DSA
    public void undoLastFundChange() {
        // Line ~90: Fund history stack get karta hai
        if (Sanctuary.getFundHistory().size() <= 1) {
            // Line ~91: Agar fund history mein 1 ya kam entries hain
            System.out.println("No fund changes to undo.");
            // Line ~92: Method se return karta hai
            return;
        }
        // Line ~93: Current funds ko pop karta hai
        Sanctuary.getFundHistory().pop();
        // Line ~94: Previous funds ko restore karta hai
        totalFunds = Sanctuary.getFundHistory().peek();
        // Line ~95: Funds ko file mein save karta hai
        saveFunds();
        // Line ~96: Undo message print karta hai
        System.out.println("Last fund change undone. Current funds: $" + totalFunds);
    }

    // Prioritize critical pets using PriorityQueue
    public void prioritizeCriticalPets(PetManager petManager) {
        PriorityQueue<Pet> criticalPets = new PriorityQueue<>(Comparator.comparingInt(Pet::getPetHealth)); // Min-heap
        // based on
        // health
        ArrayList<Pet> allPets = petManager.getPetsForSale(); // Saare pets lena
        for (Pet pet : allPets) { // Har pet ke liye
            if (pet.getPetHealth() < 50) { // Agar health 30 se kam hai
                criticalPets.offer(pet); // Pet ko PriorityQueue mein add karna
            }
        }
        if (criticalPets.isEmpty()) { // Agar koi critical pet nahi hai
            System.out.println("No critical pets found.");
        } else {
            System.out.println("Critical pets (sorted by health):");
            while (!criticalPets.isEmpty()) { // Jab tak queue khali nahi hoti
                Pet pet = criticalPets.poll(); // Agla critical pet nikaalna
                System.out.println(pet.getPetName() + " (Health: " + pet.getPetHealth() + ")"); // Pet details display
                // karna
            }
        }
    }

    // Track pet interactions using Graph (Adjacency Matrix)
    public void trackPetInteractions(PetInteraction petInteraction, PetManager petManager) {
        int[][] matrix = petInteraction.getInteractionMatrix(); // Adjacency matrix lena
        ArrayList<Pet> pets = petManager.getPetsForSale(); // Saare pets ki list lena
        System.out.println("Pet Interaction Graph:");
        for (int i = 0; i < matrix.length; i++) { // Har pet ke liye row check karna
            for (int j = 0; j < matrix[i].length; j++) { // Har pet ke liye column check karna
                if (matrix[i][j] > 0) { // Agar interaction hai
                    Pet pet1 = petManager.getPetById(petInteraction.getPetIndex(i)); // Pet1 ka object lena
                    Pet pet2 = petManager.getPetById(petInteraction.getPetIndex(j)); // Pet2 ka object lena
                    System.out.println(pet1.getPetName() + " interacted with " + pet2.getPetName() + " " + matrix[i][j]
                            + " times"); // Interaction details print karna
                }
            }
        }
    }

    // Manager Menu (Updated with New Options)
    // Only the updated loadManagerMenu method
  public void loadManagerMenu(PetManager petManager, EventManager eventManager, PetInteraction petInteraction, Sanctuary sanctuary) { // Manager menu display karta hai
        Scanner input = new Scanner(System.in); // Scanner banata hai
        int choice; // User choice ke liye variable
        do { // Loop jab tak exit na ho
            System.out.println("\n=== Manager Menu ==="); // Menu title
            System.out.println("1: Add Funds"); // Option 1
            System.out.println("2: Purchase Upgrades"); // Option 2
            System.out.println("3: Generate Report"); // Option 3
            System.out.println("4: Add Pet to Sanctuary"); // Option 4
            System.out.println("5: Remove Pet from Sanctuary"); // Option 5
            System.out.println("6: Manage Events"); // Option 6
            System.out.println("7: Assign Pet to CareTaker"); // Option 7
            System.out.println("8: Search Pets by Price Range"); // Option 8
            System.out.println("9: Undo Last Fund Change"); // Option 9
            System.out.println("10: Prioritize Critical Pets"); // Option 10
            System.out.println("11: Track Pet Interactions"); // Option 11
            System.out.println("12: Analyze Pet Social Network"); // Option 12
            System.out.println("13: Manage Pet Adventures"); // NEW: Adventure quest option
            System.out.println("0: Logout"); // Option 0
            System.out.print("Enter choice: "); // Choice input mangta hai
            choice = input.nextInt(); // Choice input leta hai

            switch (choice) { // Choice ke hisaab se action
                case 1: // Add funds
                    System.out.print("Enter amount to add: "); // Amount input
                    double amount = input.nextDouble(); // Amount leta hai
                    addFunds(amount); // Funds add karta hai
                    break; // Switch se bahar
                case 2: // Purchase upgrades
                    System.out.print("Enter upgrade to purchase (Playground/Clinic/Feeding Station): "); // Upgrade input
                    input.nextLine(); // Buffer clear
                    String upgradeName = input.nextLine(); // Upgrade name leta hai
                    updatePurchase(upgradeName); // Upgrade purchase karta hai
                    break; // Switch se bahar
                case 3: // Generate report
                    generateReport(petManager); // Report generate karta hai
                    break; // Switch se bahar
                case 4: // Add pet
                    addPetToSanctuary(sanctuary, input); // Pet add karta hai
                    break; // Switch se bahar
                case 5: // Remove pet
                    removePet(petManager); // Pet remove karta hai
                    break; // Switch se bahar
                case 6: // Manage events
                    eventMenu(eventManager, petManager); // Event menu kholta hai
                    break; // Switch se bahar
                case 7: // Assign pet to caretaker
                    assignPetToCareTaker(petManager); // Pet assign karta hai
                    break; // Switch se bahar
                case 8: // Search by price range
                    System.out.print("Enter minimum price: "); // Min price input
                    double minPrice = input.nextDouble(); // Min price leta hai
                    System.out.print("Enter maximum price: "); // Max price input
                    double maxPrice = input.nextDouble(); // Max price leta hai
                    searchPetsByPriceRange(petManager, minPrice, maxPrice); // Pets search karta hai
                    break; // Switch se bahar
                case 9: // Undo fund change
                    undoLastFundChange(); // Last fund change undo karta hai
                    break; // Switch se bahar
                case 10: // Prioritize critical pets
                    prioritizeCriticalPets(petManager); // Critical pets prioritize karta hai
                    break; // Switch se bahar
                case 11: // Track interactions
                    trackPetInteractions(petInteraction, petManager); // Interactions track karta hai
                    break; // Switch se bahar
                case 12: // Analyze pet social network
                    PetSocialNetwork socialNetwork = new PetSocialNetwork(petManager, petInteraction); // PetSocialNetwork object banata hai
                    socialNetwork.socialNetworkMenu(); // Social network menu kholta hai
                    break; // Switch se bahar
                case 13: // NEW: Manage pet adventures
                    PetAdventureQuest adventureQuest = new PetAdventureQuest(petManager); // PetAdventureQuest object banata hai
                    adventureQuest.adventureMenu(); // Adventure menu kholta hai
                    break; // Switch se bahar
                case 0: // Logout
                    System.out.println("Logging out..."); // Logout message
                    break; // Switch se bahar
                default: // Invalid choice
                    System.out.println("Invalid choice. Try again."); // Error message
            }
        } while (choice != 0); // Jab tak exit na ho
    }
    // Add Pet to Sanctuary
    public void addPetToSanctuary(Sanctuary sanctuary, Scanner scanner) {
        if (sanctuary == null || sanctuary.getPetManager() == null) {
            System.out.println("Error: Sanctuary or PetManager is not initialized.");
            return;
        }
     scanner.nextLine();

        // Pet type input with validation
        System.out.print("Enter pet type (dog/rabbit/horse/lion): ");
        String type = scanner.nextLine().trim().toLowerCase();
        while (!Arrays.asList("dog", "rabbit", "horse", "lion").contains(type)) {
            System.out.println("Invalid pet type. Please enter dog, rabbit, horse, or lion:");
            type = scanner.nextLine().trim().toLowerCase();
        }

        // Pet ID input with validation
        System.out.print("Enter pet ID: ");
        int id;
        while (true) {
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
                if (id <= 0) {
                    System.out.println("Pet ID must be a positive integer. Try again:");
                    continue;
                }
                if (sanctuary.getPetManager().getPetById(id) != null) {
                    System.out.println("Pet ID already exists. Try a different ID:");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for pet ID:");
            }
        }

        // Pet name input with validation
        System.out.print("Enter pet name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.println("Pet name cannot be empty. Try again:");
            name = scanner.nextLine().trim();
        }

        // For sale input with validation
        System.out.print("Is pet for sale (true/false): ");
        boolean forSale;
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                forSale = Boolean.parseBoolean(input);
                break;
            }
            System.out.println("Invalid input. Please enter true or false:");
        }

        // Pet age input with validation
        System.out.print("Enter pet age: ");
        int age;
        while (true) {
            try {
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age < 0) {
                    System.out.println("Age cannot be negative. Try again:");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for age:");
            }
        }

        // Pet price input with validation
        System.out.print("Enter pet price: $");
        double price;
        while (true) {
            try {
                price = Double.parseDouble(scanner.nextLine().trim());
                if (price < 0) {
                    System.out.println("Price cannot be negative. Try again:");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for price:");
            }
        }

        // Pet health input with validation
        System.out.print("Enter pet health (0-100): ");
        int health;
        while (true) {
            try {
                health = Integer.parseInt(scanner.nextLine().trim());
                if (health < 0 || health > 100) {
                    System.out.println("Health must be between 0 and 100. Try again:");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for health:");
            }
        }

        // Pet mood input with validation
        System.out.print("Enter pet mood (0-100): ");
        int mood;
        while (true) {
            try {
                mood = Integer.parseInt(scanner.nextLine().trim());
                if (mood < 0 || mood > 100) {
                    System.out.println("Mood must be between 0 and 100. Try again:");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for mood:");
            }
        }

        // Pet hunger input with validation
        System.out.print("Enter pet hunger (0-100): ");
        int hunger;
        while (true) {
            try {
                hunger = Integer.parseInt(scanner.nextLine().trim());
                if (hunger < 0 || hunger > 100) {
                    System.out.println("Hunger must be between 0 and 100. Try again:");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for hunger:");
            }
        }

        // Pet object creation
        Pet pet;
        switch (type) {
            case "dog":
                pet = new Dog(id, name, forSale, age, price, health, mood, hunger);
                break;
            case "rabbit":
                pet = new Rabbit(id, name, forSale, age, price, health, mood, hunger);
                break;
            case "horse":
                pet = new Horse(id, name, forSale, age, price, health, mood, hunger);
                break;
            case "lion":
                pet = new Lion(id, name, forSale, age, price, health, mood, hunger);
                break;
            default:
                System.out.println("Unexpected error in pet type selection.");
                return;
        }

        sanctuary.addPetToSanctuary(pet);
        System.out.println("Pet added successfully.");
    }

    // Remove Pet from Sanctuary
    public void removePet(PetManager petManager) {

        ArrayList<Pet> pets = petManager.getPets();
        if(pets.size()== 0){
            System.out.println("Santary is empty");
        }
        System.out.println("Pets ");
        for(Pet pet : pets){
            System.out.println(pet.getPetName());
        }
        Scanner input = new Scanner(System.in); // Scanner object banaya
        System.out.println("Enter pet Name to remove:");
        Pet pet = petManager.getPet(input.nextLine()); // Pet name se dhoondna
        if (pet != null) {
            petManager.removePet(pet.getPetName()); // Pet ko name se remove karna
            System.out.println("Pet " + pet.getPetName() + " removed successfully.");
        } else {
            System.out.println("Pet not found.");
        }
    }

    // Purchase Upgrades
    public void updatePurchase(String upgradeType) {
        double cost;
        switch (upgradeType.toLowerCase()) { // Upgrade type ke hisaab se cost set karna
            case "playground":
                cost = 5000;
                break;
            case "clinic":
                cost = 2000;
                break;
            case "feeding station":
                cost = 1000;
                break;
            default:
                System.out.println("Invalid upgrade. No purchase made.");
                return;
        }

        if (totalFunds >= cost) { // Agar funds kaafi hain
            totalFunds -= cost; // Funds deduct karna
            System.out.println("Upgrade '" + upgradeType + "' purchased successfully. Remaining funds: $" + totalFunds);
            saveFunds(); // Funds save karna
        } else {
            System.out.println(
                    "Insufficient funds for " + upgradeType + ". Required: $" + cost + ", Available: $" + totalFunds);
        }
    }
    // Event Management Menu

    // Event Management Menu
    // Updated eventMenu method in Manager.java
    public void eventMenu(EventManager eventManager, PetManager petManager) { // PetManager parameter add kiya
        Scanner input = new Scanner(System.in); // Scanner object banata hai user input ke liye
        int choice; // User choice store karne ke liye variable
        do { // Loop chalata hai jab tak user exit nahi karta
            System.out.println("\n=== Event Management ==="); // Menu title print karta hai
            System.out.println("1: View All Events"); // Option 1: Saare events dekhne ka
            System.out.println("2: View Unresolved Events"); // Option 2: Unresolved events dekhne ka
            System.out.println("3: Resolve an Event"); // Option 3: Event resolve karne ka
            System.out.println("4: Manage Event Dependencies"); // Option 4: Event dependencies manage karne ka
            System.out.println("5: Undo Event Actions"); // Option 5: Event actions undo karne ka
            System.out.println("6: View Event Timeline"); // Option 6: Event timeline dekhne ka
            System.out.println("7: Search Events by Date Range"); // Option 7: Events ko date range se search karne ka
            System.out.println("8: View Critical Events"); // Option 8: Critical events dekhne ka
            System.out.println("9: Trigger Random Event"); // Option 9: Random event trigger karne ka
            System.out.println("0: Back to Main Menu"); // Option 0: Main menu pe wapas jane ka
            System.out.print("Enter choice: "); // Choice input ke liye prompt
            choice = input.nextInt(); // Choice input leta hai
            input.nextLine(); // Input buffer clear karta hai

            switch (choice) { // Choice ke hisaab se action
                case 1: // View All Events
                    eventManager.displayEvents(); // Saare events display karta hai
                    break; // Switch case se bahar nikalta hai
                case 2: // View Unresolved Events
                    eventManager.displayUnresolvedEvents(); // Unresolved events display karta hai
                    break; // Switch case se bahar nikalta hai
                case 3: // Resolve an Event
                    System.out.print("Enter ID of Event to resolve: "); // Event ID input ke liye prompt
                    int eventID = input.nextInt(); // Event ID input leta hai
                    boolean success = resolveEventById(eventManager, eventID); // Event resolve karta hai
                    System.out.println(
                            success ? "Event resolved successfully!" : "Invalid event ID or already resolved."); // Result
                    // print
                    // karta
                    // hai
                    break; // Switch case se bahar nikalta hai
                case 4: // Manage Event Dependencies
                    System.out.println("1: Add Dependency, 2: Display Dependencies"); // Sub-options ke liye prompt
                    int depChoice = input.nextInt(); // Sub-choice input leta hai
                    input.nextLine(); // Input buffer clear karta hai
                    if (depChoice == 1) { // Add dependency ka option
                        System.out.print("Enter parent event ID: "); // Parent event ID input
                        int parentID = input.nextInt(); // Parent ID leta hai
                        System.out.print("Enter child event ID: "); // Child event ID input
                        int childID = input.nextInt(); // Child ID leta hai
                        input.nextLine(); // Input buffer clear karta hai
                        Event parent = eventManager.getEventById(parentID); // Parent event dhoondta hai
                        Event child = eventManager.getEventById(childID); // Child event dhoondta hai
                        eventManager.addEventDependency(parent, child); // Dependency add karta hai
                    } else if (depChoice == 2) { // Display dependencies ka option
                        eventManager.displayEventDependencies(); // Dependencies display karta hai
                    } else { // Invalid sub-choice
                        System.out.println("Invalid choice."); // Error message print karta hai
                    }
                    break; // Switch case se bahar nikalta hai
                case 5: // Undo Event Actions
                    System.out.println("1: Undo Last Action, 2: View Action History"); // Sub-options ke liye prompt
                    int undoChoice = input.nextInt(); // Sub-choice input leta hai
                    input.nextLine(); // Input buffer clear karta hai
                    if (undoChoice == 1) { // Undo last action ka option
                        eventManager.undoLastEventAction(); // Last action undo karta hai
                    } else if (undoChoice == 2) { // View action history ka option
                        eventManager.displayEventActionHistory(); // Action history display karta hai
                    } else { // Invalid sub-choice
                        System.out.println("Invalid choice."); // Error message print karta hai
                    }
                    break; // Switch case se bahar nikalta hai
                case 6: // View Event Timeline
                    System.out.print("Enter event ID to add to timeline (or 0 to skip): "); // Event ID input ke liye
                    // prompt
                    int timelineEventID = input.nextInt(); // Event ID input leta hai
                    input.nextLine(); // Input buffer clear karta hai
                    if (timelineEventID != 0) { // Agar ID 0 nahi hai
                        Event event = eventManager.getEventById(timelineEventID); // Event dhoondta hai
                        eventManager.addEventToTimeline(event); // Event ko timeline mein add karta hai
                    }
                    System.out.print("Enter direction (forward/backward): "); // Direction input ke liye prompt
                    String direction = input.nextLine(); // Direction input leta hai
                    eventManager.displayEventTimeline(direction); // Timeline display karta hai
                    break; // Switch case se bahar nikalta hai
                case 7: // Search Events by Date Range
                    System.out.print("Enter start date (yyyy-MM-dd'T'HH:mm:ss): "); // Start date input ke liye prompt
                    String startDateStr = input.nextLine(); // Start date input leta hai
                    System.out.print("Enter end date (yyyy-MM-dd'T'HH:mm:ss): "); // End date input ke liye prompt
                    String endDateStr = input.nextLine(); // End date input leta hai
                    try { // Date parsing ke liye try block
                        LocalDateTime start = LocalDateTime.parse(startDateStr); // Start date parse karta hai
                        LocalDateTime end = LocalDateTime.parse(endDateStr); // End date parse karta hai
                        eventManager.searchEventsByDateRange(start, end); // Date range ke events search karta hai
                    } catch (Exception e) { // Agar parsing fail hoti hai
                        System.out.println("Invalid date format. Use yyyy-MM-dd'T'HH:mm:ss"); // Error message print
                        // karta hai
                    }
                    break; // Switch case se bahar nikalta hai
                case 8: // View Critical Events
                    System.out.println("Critical Events:"); // Title print karta hai
                    boolean found = false; // Critical events milne ka flag
                    System.out.println("Checking unresolved events: " + eventManager.getUnresolvedEvent().size()); // Debug
                    // print:
                    // unresolved
                    // events
                    // ki
                    // count
                    for (Event event : eventManager.getUnresolvedEvent()) { // Unresolved events ke liye loop
                        System.out.println("Event type: " + event.getEventType()); // Debug print: har event ka type
                        if (event.isCritical()) { // Agar event critical hai
                            System.out.println(event.getEventDetails()); // Event details print karta hai
                            found = true; // Found flag set karta hai
                        }
                    }
                    if (!found) { // Agar koi critical event nahi mila
                        System.out.println("No critical events found."); // Message print karta hai
                    }
                    break; // Switch case se bahar nikalta hai
                case 9: // Trigger Random Event
                    eventManager.triggerRandomEvent(petManager.getPets()); // Random event trigger karta hai, getPets()
                    // use kiya
                    break; // Switch case se bahar nikalta hai
                case 0: // Back to Main Menu
                    System.out.println("Returning to main menu..."); // Main menu pe wapas jane ka message
                    break; // Switch case se bahar nikalta hai
                default: // Invalid choice
                    System.out.println("Invalid choice, try again."); // Error message print karta hai
            }
        } while (choice != 0); // Jab tak choice 0 nahi hai, loop chalta hai
    }

    // Resolve event by ID
    public boolean resolveEventById(EventManager eventManager, int eventID) {
        ArrayList<Event> events = eventManager.getUnresolvedEvent(); // Unresolved events lena
        if (eventID < 1 || eventID > events.size()) { // Agar event ID invalid hai
            return false;
        }
        events.get(eventID - 1).resolveEvent(); // Event resolve karna
        return true; // Success return karna
    }

    public void assignPetToCareTaker(PetManager petManager) {
        // Line ~290: Scanner object banata hai user input ke liye
        Scanner input = new Scanner(System.in);
        // Line ~291: Assign pet menu ka title print karta hai
        System.out.println("\n=== Assign Pet to a CareTaker ===");
        // Line ~292: Users ki list get karta hai
        ArrayList<User> users = petManager.getUserManager().getUsers();
        // Line ~293: Total users count print karta hai
        System.out.println("Total users loaded: " + users.size());
        // Line ~294: CareTakers ke liye ArrayList banata hai
        ArrayList<CareTaker> careTakers = new ArrayList<>();
        // Line ~295: Har user ke liye loop chalata hai
        for (User user : users) {
            // Line ~296: User ka name aur role print karta hai
            System.out.println("User: " + user.getUserName() + ", Role: " + user.getRol());
            // Line ~297: Agar user CareTaker hai
            if (user instanceof CareTaker) {
                // Line ~298: User ko CareTaker ke taur pe cast karta hai
                careTakers.add((CareTaker) user);
                // Line ~299: CareTaker add hone ka message print karta hai
                System.out.println("Added CareTaker: " + user.getUserName());
            }
        }
        // Line ~301: Available CareTakers count print karta hai
        System.out.println("Available CareTakers: " + careTakers.size());
        // Line ~302: Agar koi CareTaker nahi hai
        if (careTakers.isEmpty()) {
            // Line ~303: Error message print karta hai
            System.out.println("No available CareTakers.");
            // Line ~304: Method se return karta hai
            return;
        }
        // Line ~305: CareTaker selection ke liye prompt print karta hai
        System.out.println("Select a CareTaker:");
        // Line ~306: Har CareTaker ke liye index aur name print karta hai
        for (int i = 0; i < careTakers.size(); i++) {
            System.out.println((i + 1) + ". " + careTakers.get(i).getUserName());
        }
        // Line ~308: CareTaker number input mangta hai
        System.out.print("Enter CareTaker number: ");
        // Line ~309: User se CareTaker index input leta hai
        int caretakerIndex = input.nextInt();
        // Line ~310: Input buffer clear karta hai
        input.nextLine();
        // Line ~311: Agar invalid index hai
        if (caretakerIndex < 1 || caretakerIndex > careTakers.size()) {
            // Line ~312: Error message print karta hai
            System.out.println("Invalid selection.");
            // Line ~313: Method se return karta hai
            return;
        }
        // Line ~314: Selected CareTaker ko get karta hai
        CareTaker selectedCareTaker = careTakers.get(caretakerIndex - 1);
        // Line ~315: Saare pets display karta hai
        petManager.displayAllPets();
        // Line ~316: Pet name input mangta hai
        System.out.print("Enter Pet Name to Assign: ");
        // Line ~317: User se pet name input leta hai
        String petName = input.nextLine();
        // Line ~318: PetManager se pet ko name se dhoondhta hai
        Pet pet = petManager.getPet(petName);
        // Line ~319: Agar pet nahi mila
        if (pet == null) {
            // Line ~320: Error message print karta hai
            System.out.println("Pet not found.");
            // Line ~321: Method se return karta hai
            return;
        }
        // Line ~322: Pet ko CareTaker ko assign karta hai
        petManager.assignPetToCareTaker(selectedCareTaker, pet);
        // Line ~323: Assignment success message print karta hai
        System.out.println("Pet " + pet.getPetName() + " has been assigned to " + selectedCareTaker.getUserName());
    }

}
