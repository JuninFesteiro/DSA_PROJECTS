/** Feature Description
 *
 * Feature Description
 * This feature allows the sanctuary to analyze and recommend social interactions between pets based on their interaction history, health, mood, and preferences. It will:

 *  *Track pet interactions using a Graph (via PetInteraction.java's adjacency matrix).
 * Maintain a history of interactions using a Stack for undo functionality.
 * Prioritize pets for social activities using a Heap (Priority Queue) based on mood.
 * Store interaction preferences in a HashMap for quick lookup.
 * Use a Tree (BST) to organize pets by their social compatibility score.
 * Maintain a Queue for scheduling social events.
 * Use a Linked List to log interaction events.
 * Store pet groups in Arrays for sorting and searching.
 * Implement Sorting (e.g., QuickSort) to rank pets by compatibility and Searching (e.g., Binary Search) to find suitable interaction partners.

 *  *This feature will be implemented as a standalone module in the Sanctuary package, interacting with existing classes (Pet, PetInteraction, PetManager) but not modifying their core logic, ensuring minimal disturbance to your existing codebase.

 *  *Why This Feature?

 *  *Unique: Pet social network analysis and recommendation is not currently implemented in your project, adding a novel aspect to the sanctuary simulator.
 * Non-Invasive: It can be added as a new class or method in Sanctuary.java or a new file in the Sanctuary package, using existing PetInteraction and PetManager methods without altering their functionality.
 * Comprehensive Data Structure Usage:

 *  *Graph: Uses PetInteraction's adjacency matrix to analyze pet relationships.
 * Stack: Stores interaction history for undo/redo.
 * Heap: Prioritizes pets with low mood for social interactions.
 * HashMap: Stores pet compatibility preferences (e.g., which pets prefer to interact with others).
 * Tree: Organizes pets by a computed social compatibility score in a BST.
 * Queue: Schedules social events for pets.
 * Linked List: Logs interaction events with timestamps.
 * Arrays: Groups pets for sorting and searching.
 * Sorting: Ranks pets by compatibility using QuickSort.
 * Searching: Finds compatible pets using Binary Search.
 *

 *  *Enhances Gameplay: Adds a social dimension to the pet sanctuary, making it more engaging for users (Managers, CareTakers, Guests).
 *

 *  *High-Level Overview of Implementation

 *  *New Class: Create a PetSocialNetwork.java in the Sanctuary package to handle this feature.
 * Functionality:

 *  *Analyze Interactions: Use PetInteraction.getInteractionMatrix() (Graph) to identify frequently interacting pets.
 * Compatibility Score: Calculate a compatibility score for each pet based on health, mood, and interaction frequency, stored in a HashMap.
 * BST Organization: Insert pets into a BST based on compatibility scores for efficient retrieval.
 * Heap Prioritization: Use a PriorityQueue (Heap) to prioritize low-mood pets for social events.
 * Event Scheduling: Schedule social events in a Queue (LinkedList) for sequential processing.
 * Interaction History: Log interactions in a Linked List and maintain a Stack for undoing recent interactions.
 * Group Formation: Store pet groups in an Array, sort them by compatibility (QuickSort), and search for optimal partners (Binary Search).
 * Recommendation: Recommend pairs or groups of pets for social activities, displayed in the Manager or CareTaker menu.
 *

 *  *Menu Integration: Add a new option in Manager.loadManagerMenu and CareTaker.loadCareTakerMenu to access this feature.
 * Output: Display recommended pet pairs/groups, interaction logs, and allow undoing interactions.
 *

 *  *Example User Interaction

 *  *Manager Menu Option: "12: Analyze and Recommend Pet Social Interactions"

 *  *Select this option to:

 *  *View top compatible pet pairs (sorted by compatibility score).
 * Schedule a social event for low-mood pets (prioritized via Heap).
 * Undo recent interactions (using Stack).
 * Search for pets compatible with a specific pet (Binary Search in sorted Array).
 * View interaction history (Linked List).
 *
 *
 *

 *  *CareTaker Menu Option: "11: Manage Pet Social Events"

 *  *Assign pets to social events from the scheduled Queue.
 * Monitor social compatibility via BST traversal.
 *
 *
 *

 *  *Data Structure Usage

 *  *Graph: PetInteraction's adjacency matrix tracks pet interactions.
 * Stack: Stores interaction actions (e.g., "Pet A interacted with Pet B") for undo.
 * Heap: PriorityQueue prioritizes pets with mood < 50 for social events.
 * HashMap: Maps pet IDs to compatibility preferences (e.g., {petId: [preferredPetIds]}).
 * Tree: BST stores pets by compatibility score (e.g., PetSocialNode with score, pet).
 * Queue: LinkedList schedules social events (e.g., {petA, petB, eventTime}).
 * Linked List: Logs interaction events (e.g., {petA, petB, timestamp, outcome}).
 * Arrays: Stores pet groups for sorting (QuickSort by compatibility) and searching (Binary Search).
 * Sorting: QuickSort to rank pets by compatibility score.
 * Searching: Binary Search to find pets within a compatibility score range.
 *
 *
 */
// Import package
package Sanctuary;

// Yeh required classes import karta hai
import Pet.Pet;
import Pet.PetManager;
import Pet.PetInteraction;
import java.time.LocalDateTime;
import java.util.*;

// Yeh PetSocialNetwork class define karta hai jo pet ke social interactions manage karta hai
public class PetSocialNetwork {

    // Yeh fields declare karta hai
    private PetManager petManager; // PetManager ka reference store karta hai
    private PetInteraction petInteraction; // PetInteraction ka reference store karta hai
    private HashMap<Integer, ArrayList<Integer>> compatibilityPreferences; // Pet ID se preferred pet IDs map karta hai
    private Stack<String> interactionHistory; // Interaction history stack mein store karta hai
    private Queue<SocialEvent> eventQueue; // Social events ke liye queue
    private LinkedList<String> eventLog; // Interaction events ka log
    private PetSocialNode bstRoot; // Compatibility score ke liye BST root
    private PriorityQueue<Pet> moodPriorityQueue; // Low-mood pets ke liye heap

    // Yeh inner class BST node ke liye define karta hai
    private class PetSocialNode {
        // Pet object aur compatibility score store karta hai
        Pet pet; // Pet object store karta hai
        double compatibilityScore; // Compatibility score store karta hai
        PetSocialNode left, right; // Left aur right child nodes

        // Yeh constructor PetSocialNode banata hai
        PetSocialNode(Pet pet, double score) {
            this.pet = pet; // Pet set karta hai
            this.compatibilityScore = score; // Score set karta hai
            this.left = this.right = null; // Left aur right null set karta hai
        }
    }

    // Yeh inner class social event ke liye define karta hai
    private class SocialEvent {
        // Do pets aur event time store karta hai
        Pet pet1, pet2; // Do pets jo interact karenge
        LocalDateTime eventTime; // Event ka time

        // Yeh constructor SocialEvent banata hai
        SocialEvent(Pet pet1, Pet pet2, LocalDateTime time) {
            this.pet1 = pet1; // Pehla pet set karta hai
            this.pet2 = pet2; // Doosra pet set karta hai
            this.eventTime = time; // Event time set karta hai
        }

        // Yeh event details string mein return karta hai
        @Override
        public String toString() {
            return pet1.getPetName() + " and " + pet2.getPetName() + " scheduled to interact at " + eventTime;
        }
    }

    // Yeh constructor PetSocialNetwork initialize karta hai
    public PetSocialNetwork(PetManager petManager, PetInteraction petInteraction) {
        this.petManager = petManager; // PetManager set karta hai
        this.petInteraction = petInteraction; // PetInteraction set karta hai
        this.compatibilityPreferences = new HashMap<>(); // HashMap initialize karta hai
        this.interactionHistory = new Stack<>(); // Stack initialize karta hai
        this.eventQueue = new LinkedList<>(); // Queue initialize karta hai
        this.eventLog = new LinkedList<>(); // LinkedList initialize karta hai
        this.moodPriorityQueue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getPetMood(), p2.getPetMood())); // Heap low mood ke liye
        this.bstRoot = null; // BST root null set karta hai
        initializeCompatibilityPreferences(); // Compatibility preferences initialize karta hai
    }

    // Yeh method compatibility preferences initialize karta hai
    private void initializeCompatibilityPreferences() {
        ArrayList<Pet> pets = petManager.getPets(); // Pets ki list leta hai
        int[][] matrix = petInteraction.getInteractionMatrix(); // Interaction matrix leta hai
        int maxSize = Math.min(pets.size(), matrix.length); // Matrix size aur pets list ka minimum leta hai
        for (int i = 0; i < maxSize; i++) { // Har valid pet ke liye loop
            petInteraction.addPet(pets.get(i)); // Pet ko PetInteraction mein add karta hai
            ArrayList<Integer> preferredPets = new ArrayList<>(); // Preferred pets ki list
            for (int j = 0; j < maxSize; j++) { // Har doosre pet ke liye
                if (matrix[i][j] > 2) { // Agar interaction count > 2 hai
                    preferredPets.add(j); // Preferred pet index add karta hai
                }
            }
            compatibilityPreferences.put(i, preferredPets); // HashMap mein store karta hai
        }
    }

    // Yeh method do pets ke beech compatibility score calculate karta hai
    private int calculateCompatibilityScore(Pet pet1, Pet pet2) {
        if (pet1 == null || pet2 == null || pet1 == pet2) { // Agar pet1 ya pet2 null hai ya same pets hain
            return 0; // Zero score return karta hai
        }
        ArrayList<Pet> pets = petManager.getPets(); // PetManager se saare pets ki list leti hai
        int index1 = pets.indexOf(pet1); // Pet1 ka index dhoondta hai
        int index2 = pets.indexOf(pet2); // Pet2 ka index dhoondta hai
        if (index1 == -1 || index2 == -1) { // Agar koi pet nahi mila
            return 0; // Zero score return karta hai
        }
        int[][] matrix = petInteraction.getInteractionMatrix(); // Interaction matrix leta hai
        if (index1 >= matrix.length || index2 >= matrix.length) { // Agar index matrix size se bada hai
            return 0; // Zero score return karta hai
        }
        int baseScore = matrix[index1][index2]; // Adjacency matrix se interaction score leti hai
        int healthDiff = Math.abs(pet1.getPetHealth() - pet2.getPetHealth()); // Health difference calculate karta hai
        int moodDiff = Math.abs(pet1.getPetMood() - pet2.getPetMood()); // Mood difference calculate karta hai
        return Math.max(0, baseScore - (healthDiff + moodDiff) / 2); // Final compatibility score calculate karta hai
    }

    // Yeh method BST mein pet insert karta hai
    private PetSocialNode insertIntoBST(PetSocialNode node, Pet pet, double score) {
        if (node == null) { // Agar node null hai
            return new PetSocialNode(pet, score); // Naya node banata hai
        }
        if (score < node.compatibilityScore) { // Agar score chhota hai
            node.left = insertIntoBST(node.left, pet, score); // Left subtree mein insert
        } else { // Agar score bara ya equal hai
            node.right = insertIntoBST(node.right, pet, score); // Right subtree mein insert
        }
        return node; // Node return karta hai
    }

    // Yeh method target pet ke liye compatibility BST banata hai
    private void buildCompatibilityBST(Pet targetPet) {
        ArrayList<Pet> pets = petManager.getPets(); // PetManager se saare pets ki list leti hai
        if (pets.isEmpty() || targetPet == null) { // Agar pets list khali hai ya targetPet null hai
            System.out.println("No pets available or invalid target pet."); // Error message print karta hai
            return; // Method se return karta hai
        }
        int[][] matrix = petInteraction.getInteractionMatrix(); // Interaction matrix leta hai
        int maxSize = Math.min(pets.size(), matrix.length); // Matrix size aur pets list ka minimum leta hai
        bstRoot = null; // BST root reset karta hai
        for (int i = 0; i < maxSize; i++) { // Har valid pet ke liye
            Pet pet = pets.get(i); // Pet leti hai
            petInteraction.addPet(pet); // Pet ko PetInteraction mein add karta hai
            if (pet != targetPet) { // Agar pet targetPet ke barabar nahi hai
                int score = calculateCompatibilityScore(targetPet, pet); // Compatibility score calculate karta hai
                bstRoot = insertIntoBST(bstRoot, pet, score); // BST mein insert karta hai
            }
        }
    }

    // Yeh method BST se sorted pets leta hai
    private void getSortedPetsByCompatibility(PetSocialNode node, ArrayList<Pet> result) {
        if (node == null) { // Agar node null hai
            return; // Return karta hai
        }
        getSortedPetsByCompatibility(node.left, result); // Left subtree traverse karta hai
        result.add(node.pet); // Pet add karta hai
        getSortedPetsByCompatibility(node.right, result); // Right subtree traverse karta hai
    }

    // Yeh method QuickSort implement karta hai pets ke liye
    private void quickSortPets(Pet[] pets, int low, int high, Pet targetPet) {
        if (low < high) { // Agar low high se chhota hai
            int pi = partition(pets, low, high, targetPet); // Partition index find karta hai
            quickSortPets(pets, low, pi - 1, targetPet); // Left part sort karta hai
            quickSortPets(pets, pi + 1, high, targetPet); // Right part sort karta hai
        }
    }

    // Yeh method QuickSort ke liye partition karta hai
    private int partition(Pet[] pets, int low, int high, Pet targetPet) {
        double pivot = calculateCompatibilityScore(targetPet, pets[high]); // Pivot score
        int i = low - 1; // Smaller element ka index
        for (int j = low; j < high; j++) { // Array traverse karta hai
            if (calculateCompatibilityScore(targetPet, pets[j]) >= pivot) { // Agar score pivot se bara ya equal
                i++; // Increment index
                Pet temp = pets[i]; // Swap karta hai
                pets[i] = pets[j];
                pets[j] = temp;
            }
        }
        Pet temp = pets[i + 1]; // Pivot ke saath swap
        pets[i + 1] = pets[high];
        pets[high] = temp;
        return i + 1; // Partition index return karta hai
    }

    // Yeh method binary search se compatibility range mein pets dhoondta hai
    private ArrayList<Pet> binarySearchByCompatibility(Pet[] pets, double minScore, double maxScore, Pet targetPet) {
        ArrayList<Pet> result = new ArrayList<>(); // Result list banata hai
        for (Pet pet : pets) { // Har pet ke liye
            double score = calculateCompatibilityScore(targetPet, pet); // Score calculate karta hai
            if (score >= minScore && score <= maxScore) { // Agar score range mein hai
                result.add(pet); // Pet add karta hai
            }
        }
        return result; // Result return karta hai
    }

    // Yeh method compatible pets recommend karta hai
    public void recommendCompatiblePets(Pet targetPet) {
        if (targetPet == null) { // Agar target pet null hai
            System.out.println("Invalid pet selected."); // Error message
            return; // Return karta hai
        }
        buildCompatibilityBST(targetPet); // BST banata hai
        ArrayList<Pet> sortedPets = new ArrayList<>(); // Sorted pets ki list
        getSortedPetsByCompatibility(bstRoot, sortedPets); // BST se sorted pets leta hai
        if (sortedPets.isEmpty()) { // Agar sorted pets khali hai
            System.out.println("No compatible pets found for " + targetPet.getPetName()); // Message print karta hai
            return; // Return karta hai
        }
        System.out.println("Recommended pets for " + targetPet.getPetName() + ":"); // Recommendation print karta hai
        for (Pet pet : sortedPets) { // Har recommended pet ke liye
            double score = calculateCompatibilityScore(targetPet, pet); // Score calculate karta hai
            System.out.println(pet.getPetName() + " (Compatibility Score: " + score + ")"); // Pet aur score print karta hai
        }

        // Convert to array for QuickSort and Binary Search
        Pet[] petArray = sortedPets.toArray(new Pet[0]); // Array mein convert karta hai
        quickSortPets(petArray, 0, petArray.length - 1, targetPet); // QuickSort karta hai
        System.out.println("\nSorted by compatibility (QuickSort):"); // Sorted list print karta hai
        for (Pet pet : petArray) { // Har pet ke liye
            System.out.println(pet.getPetName() + " (Score: " + calculateCompatibilityScore(targetPet, pet) + ")"); // Pet aur score
        }

        // Binary search for pets in a specific compatibility range
        double minScore = 50.0, maxScore = 100.0; // Compatibility range define karta hai
        ArrayList<Pet> rangePets = binarySearchByCompatibility(petArray, minScore, maxScore, targetPet); // Binary search karta hai
        if (!rangePets.isEmpty()) { // Agar range mein pets hain
            System.out.println("\nPets in compatibility range " + minScore + " to " + maxScore + ":"); // Range pets print
            for (Pet pet : rangePets) { // Har pet ke liye
                System.out.println(pet.getPetName()); // Pet name print karta hai
            }
        }
    }

    // Yeh method social event schedule karta hai
    public void scheduleSocialEvent(Pet pet1, Pet pet2) {
        if (pet1 == null || pet2 == null) { // Agar pets null hain
            System.out.println("Invalid pets for social event."); // Error message
            return; // Return karta hai
        }
        SocialEvent event = new SocialEvent(pet1, pet2, LocalDateTime.now().plusHours(1)); // Naya event banata hai
        eventQueue.offer(event); // Event queue mein add karta hai
        String log = pet1.getPetName() + " and " + pet2.getPetName() + " scheduled for social event at " + event.eventTime; // Log entry
        eventLog.add(log); // LinkedList mein log add karta hai
        interactionHistory.push(log); // Stack mein history add karta hai
        System.out.println("Scheduled: " + event); // Schedule confirmation
        Sanctuary.getLoadHistory().add(log); // Sanctuary ke loadHistory mein event add karta hai
    }

    // Yeh method low-mood pets prioritize karta hai
    public void prioritizeLowMoodPets() {
        moodPriorityQueue.clear(); // Heap clear karta hai
        ArrayList<Pet> pets = petManager.getPets(); // Pets ki list leta hai
        for (Pet pet : pets) { // Har pet ke liye
            if (pet.getPetMood() < 50) { // Agar mood 50 se kam hai
                moodPriorityQueue.offer(pet); // Heap mein add karta hai
            }
        }
        if (moodPriorityQueue.isEmpty()) { // Agar heap khali hai
            System.out.println("No low-mood pets found."); // Message print karta hai
        } else { // Warna
            System.out.println("Low-mood pets prioritized:"); // Prioritized pets print
            while (!moodPriorityQueue.isEmpty()) { // Jab tak heap khali nahi hota
                Pet pet = moodPriorityQueue.poll(); // Pet nikal kar print karta hai
                System.out.println(pet.getPetName() + " (Mood: " + pet.getPetMood() + ")");
            }
        }
    }

    // Yeh method last interaction undo karta hai
    public void undoLastInteraction() {
        if (interactionHistory.isEmpty()) { // Agar stack khali hai
            System.out.println("No interactions to undo."); // Message print karta hai
            return; // Return karta hai
        }
        String lastInteraction = interactionHistory.pop(); // Stack se last interaction nikal karta hai
        eventLog.remove(lastInteraction); // LinkedList se remove karta hai
        System.out.println("Undone interaction: " + lastInteraction); // Undo confirmation
    }

    // Yeh method interaction log display karta hai
    public void displayInteractionLog() {
        if (eventLog.isEmpty()) { // Agar log khali hai
            System.out.println("No interaction history available."); // Message print karta hai
            return; // Return karta hai
        }
        System.out.println("Interaction Log:"); // Log print karta hai
        for (String log : eventLog) { // Har log entry ke liye
            System.out.println(log); // Log print karta hai
        }
    }

    // Yeh method social network menu display karta hai
    public void socialNetworkMenu() {
        Scanner scanner = new Scanner(System.in); // Scanner banata hai
        int choice; // User choice ke liye variable
        do { // Loop jab tak exit na ho
            System.out.println("\n=== Pet Social Network Menu ==="); // Menu title
            System.out.println("1: Recommend Compatible Pets"); // Option 1
            System.out.println("2: Schedule Social Event"); // Option 2
            System.out.println("3: Prioritize Low-Mood Pets"); // Option 3
            System.out.println("4: Undo Last Interaction"); // Option 4
            System.out.println("5: Display Interaction Log"); // Option 5
            System.out.println("0: Exit"); // Option 0
            System.out.print("Enter choice: "); // Choice input mangta hai
            choice = scanner.nextInt(); // Choice input leta hai
            scanner.nextLine(); // Buffer clear karta hai

            switch (choice) { // Choice ke hisaab se action
                case 1: // Recommend compatible pets
                    System.out.print("Enter Pet Name for Recommendations: "); // Pet name mangta hai
                    String petName = scanner.nextLine(); // Pet name input
                    Pet targetPet = petManager.getPet(petName); // Pet dhoondta hai
                    recommendCompatiblePets(targetPet); // Recommendations deta hai
                    break; // Switch se bahar
                case 2: // Schedule social event
                    System.out.print("Enter First Pet Name: "); // Pehla pet name
                    String pet1Name = scanner.nextLine(); // Input leta hai
                    System.out.print("Enter Second Pet Name: "); // Doosra pet name
                    String pet2Name = scanner.nextLine(); // Input leta hai
                    Pet pet1 = petManager.getPet(pet1Name); // Pehla pet dhoondta hai
                    Pet pet2 = petManager.getPet(pet2Name); // Doosra pet dhoondta hai
                    scheduleSocialEvent(pet1, pet2); // Event schedule karta hai
                    break; // Switch se bahar
                case 3: // Prioritize low-mood pets
                    prioritizeLowMoodPets(); // Low-mood pets prioritize karta hai
                    break; // Switch se bahar
                case 4: // Undo last interaction
                    undoLastInteraction(); // Last interaction undo karta hai
                    break; // Switch se bahar
                case 5: // Display interaction log
                    displayInteractionLog(); // Interaction log display karta hai
                    break; // Switch se bahar
                case 0: // Exit
                    System.out.println("Exiting Social Network Menu..."); // Exit message
                    break; // Switch se bahar
                default: // Invalid choice
                    System.out.println("Invalid choice. Try again."); // Error message
            }
        } while (choice != 0); // Jab tak exit na ho
    }
}