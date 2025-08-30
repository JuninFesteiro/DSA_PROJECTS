package Sanctuary; // Sanctuary package define karta hai

import Event.EventManager; // EventManager class import karta hai
import Guest.Guest; // Guest class import karta hai
import Pet.Pet; // Pet class import karta hai
import Pet.PetManager; // PetManager class import karta hai
import User.UserManager; // UserManager class import karta hai
import java.util.ArrayDeque; // ArrayDeque class import karta hai
import java.util.ArrayList; // ArrayList class import karta hai
import java.util.PriorityQueue; // PriorityQueue class import karta hai
import java.util.Queue; // Queue interface import karta hai
import java.util.Stack; // Stack class import karta hai
import java.util.Scanner; // Scanner class import karta hai
import java.util.LinkedList; // Scanner class import karta hai

public class Sanctuary { // Sanctuary class define karta hai

    private static double funds; // Sanctuary ke funds ka track rakhta hai (static variable)
    private static Stack<Double> fundHistory = new Stack<>(); // Funds ke changes ka history stack mein rakhta hai
    private static Queue<String> loadHistory = new LinkedList<>(); // Load operations ka history queue mein rakhta hai
    private PriorityQueue<Pet> eventQueue; // Events ke liye priority queue define karta hai
    private UserManager userManager; // UserManager ka reference store karta hai
    private PetManager petManager; // PetManager ka reference store karta hai
    private EventManager eventManager; // EventManager ka reference store karta hai

    // Constructor
    public Sanctuary(double initialFunds) { // Sanctuary ka constructor initial funds ke saath
        funds = initialFunds; // Initial funds set karta hai
        fundHistory = new Stack<>(); // Fund history stack initialize karta hai
        fundHistory.push(funds); // Initial funds ko stack mein push karta hai
        this.eventManager = new EventManager(null); // EventManager ko null ke saath initialize karta hai
        this.petManager = new PetManager(null); // PetManager ko null ke saath initialize karta hai
        this.userManager = new UserManager(petManager, eventManager); // UserManager ko PetManager aur EventManager ke saath initialize karta hai
        this.petManager.setUserManager(userManager); // PetManager ke UserManager ko set karta hai
        this.eventManager.setPetManager(petManager); // EventManager ke PetManager ko set karta hai
        this.petManager.loadPetsFromFile("pets.txt"); // Pets ko pets.txt file se load karta hai
        this.userManager.loadUsers("users.txt"); // Users ko users.txt file se load karta hai
        this.petManager.loadAssignmentsFromFile("assignments.txt"); // Assignments ko assignments.txt file se load karta hai
        this.eventQueue = new PriorityQueue<>((p1, p2) -> Integer.compare(p2.getPetHealth(), p1.getPetHealth())); // Event queue ko health-based priority ke saath initialize karta hai
        loadHistory = new ArrayDeque<>(); // Load history deque initialize karta hai
        loadHistory.offer("Sanctuary initialized with funds: $" + initialFunds); // Initial funds ka message load history mein add karta hai
    }

    // Add funds to Sanctuary
    public static void addFunds(double amount) { // Funds add karne ka method
        if (amount > 0) { // Check karta hai ke amount positive hai
            funds += amount; // Funds mein amount add karta hai
            fundHistory.push(funds); // Updated funds ko stack mein push karta hai
            loadHistory.offer("Added $" + amount + " to funds. Total: $" + funds); // Load history mein entry add karta hai
            System.out.println("Funds added: $" + amount); // Funds add hone ka message print karta hai
        } else { // Agar amount positive nahi hai
            System.out.println("Invalid amount. Funds not added."); // Error message print karta hai
        }
    }

    // Add pet to Sanctuary
    public void addPetToSanctuary(Pet pet) { // Pet ko sanctuary mein add karne ka method
        petManager.addPet(pet); // PetManager ke addPet method ko call karta hai
    }

    // Get current funds
    public static double getFunds() { // Current funds return karne ka method
        return funds; // Current funds return karta hai
    }

        // Yeh method loadHistory queue ko return karta hai
    public static Queue<String> getLoadHistory() {
        return loadHistory;
    }

    // Public getter for fundHistory to allow Manager to access Stack
    public static Stack<Double> getFundHistory() { // Fund history stack return karne ka method
        return fundHistory; // Fund history stack return karta hai
    }

    // Get UserManager
    public UserManager getUserManager() { // UserManager return karne ka method
        return userManager; // UserManager ka reference return karta hai
    }

    // Get PetManager
    public PetManager getPetManager() { // PetManager return karne ka method
        return petManager; // PetManager ka reference return karta hai
    }

    // Get EventManager
    public EventManager getEventManager() { // EventManager return karne ka method
        return eventManager; // EventManager ka reference return karta hai
    }

    // Run daily updates
    public void runDailyUpdates() { // Daily updates chalane ka method
        ArrayList<Pet> pets = petManager.getPetsForSale(); // Saare pets jo sale ke liye hain, unki list leta hai
        for (Pet pet : pets) { // Har pet ke liye loop chalata hai
            pet.simulateDailyNeeds(); // Pet ki daily needs simulate karta hai
            if (pet.getPetHealth() < 30 || pet.getPetHunger() > 70 || pet.getPetMood() < 30) { // Agar pet critical state mein hai
                eventQueue.offer(pet); // Pet ko priority queue mein add karta hai
            }
        }
        if (!eventQueue.isEmpty()) { // Agar event queue khali nahi hai
            Pet criticalPet = eventQueue.poll(); // Critical pet ko queue se nikalna
            eventManager.triggerEvent(criticalPet); // Critical pet ke liye event trigger karta hai
        }
        loadHistory.offer("Daily updates completed at " + java.time.LocalDateTime.now()); // Daily updates ka record load history mein add karta hai
    }

    // Display main menu
    public void displayMenu() { // Main menu display karne ka method
        Scanner scanner = new Scanner(System.in); // Scanner object banata hai user input ke liye
        int choice; // User choice store karne ke liye variable
        do { // Loop chalata hai jab tak user exit nahi karta
            System.out.println("\n=== Virtual Pet Sanctuary Simulator ==="); // Menu title print karta hai
            System.out.println("1: Login"); // Login option print karta hai
            System.out.println("2: Register"); // Register option print karta hai
            System.out.println("3: Continue as Guest"); // Guest option print karta hai
            System.out.println("0: Exit"); // Exit option print karta hai
            System.out.print("Enter choice: "); // User se choice mangta hai
            choice = scanner.nextInt(); // User ka input choice leta hai
            scanner.nextLine(); // Input buffer clear karta hai

            switch (choice) { // User ke choice ke hisaab se action
                case 1: // Login option
                    userManager.loginMenu(petManager, eventManager, this); // Login menu kholta hai
                    break; // Switch case se bahar nikalta hai
                case 2: // Register option
                    userManager.registerMenu(); // Register menu kholta hai
                    break; // Switch case se bahar nikalta hai
                case 3: // Guest option
                    Guest guest = new Guest(petManager, this, eventManager); // Guest object banata hai
                    guest.guestMenu(petManager); // Guest menu kholta hai
                    break; // Switch case se bahar nikalta hai
                case 0: // Exit option
                    System.out.println("Exiting..."); // Exit message print karta hai
                    break; // Switch case se bahar nikalta hai
                default: // Invalid choice
                    System.out.println("Invalid choice. Try again."); // Error message print karta hai
            }
        } while (choice != 0); // Jab tak choice 0 nahi hai, loop chalta hai
    }
}