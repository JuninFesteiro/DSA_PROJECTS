// Class for pets adventures jo ke pets ke advantures ke liya use hota ha
// This class allow pets to makes adventures or quests in sanctuary where the can explore collect items and complete challenges. Its Unqiue beacuse it turns the sanctuary into inactrative adventures gamss
// User interacion from manager and caretaker
// Quest creation managr quest with challenges
// Caretkaer assign pets to to question priortized based on health mood
// Adventure simulation Pets navigate graph(collect) collect items Queue
// At the ends sort outcomes 
// undo failed steps as stacks
// Data structures used
// Arrays store quest challenges
// Stacks undo pet moves during adventures 
// Queue managers items collection 
// Linked list log Adventures
// Trees Decision Tree for adevtures paths (BST where nodes represent choices based on pet health mood)
// Graphs :Adventures maps as graph(Shortest path)
package Sanctuary;

import java.time.LocalDateTime;
// Importing packages
import java.util.*;

import Pet.Pet;
import Pet.PetManager;



public class PetAdventureQuest { // PetAdventureQuest class define karta hai
    private PetManager petManager; // PetManager ka reference store karta hai
    private HashMap<String, ArrayList<String>> questChallenges; // Quest names se challenges map karta hai (HashMap)
    private Stack<String> moveHistory; // Adventure moves ka history (Stack)
    private Queue<String> itemQueue; // Items collection ke liye queue
    private LinkedList<String> adventureLog; // Adventure events ka log (Linked List)
    private AdventureNode decisionTreeRoot; // Decision tree for paths (Tree)
    private PriorityQueue<Pet> adventurePriorityQueue; // Low-health pets prioritize (Heap)
    private HashMap<Integer, ArrayList<Integer>> adventureMap; // Adventure map as graph (Graph)
    private String[] adventureArray; // Quest challenges array (Arrays)
    private Scanner scanner; // User input ke liye scanner

    // Inner class for adventure event
    private class AdventureEvent { // AdventureEvent inner class define karta hai
        Pet pet1, pet2; // Do pets jo adventure mein honge
        String questName; // Quest ka naam
        LocalDateTime eventTime; // Event ka time

        AdventureEvent(Pet pet1, Pet pet2, String questName, LocalDateTime time) { // Constructor banata hai
            this.pet1 = pet1; // Pehla pet set karta hai
            this.pet2 = pet2; // Doosra pet set karta hai
            this.questName = questName; // Quest name set karta hai
            this.eventTime = time; // Event time set karta hai
        }

        @Override
        public String toString() { // Event details string mein return karta hai
            return pet1.getPetName() + " and " + pet2.getPetName() + " scheduled for " + questName + " at " + eventTime;
        }
    }

    // Inner class for tree node
    private class AdventureNode { // AdventureNode inner class define karta hai
        String choice; // Choice description store karta hai
        AdventureNode left, right; // Left aur right child nodes

        AdventureNode(String choice) { // Constructor banata hai
            this.choice = choice; // Choice set karta hai
            this.left = this.right = null; // Left aur right null set karta hai
        }
    }

    // Constructor
    public PetAdventureQuest(PetManager petManager) { // Constructor banata hai
        this.petManager = petManager; // PetManager set karta hai
        this.questChallenges = new HashMap<>(); // HashMap initialize karta hai
        this.moveHistory = new Stack<>(); // Stack initialize karta hai
        this.itemQueue = new LinkedList<>(); // Queue initialize karta hai
        this.adventureLog = new LinkedList<>(); // LinkedList initialize karta hai
        this.adventurePriorityQueue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getPetHealth(), p2.getPetHealth())); // Heap low health ke liye
        this.adventureMap = new HashMap<>(); // Graph initialize karta hai
        this.decisionTreeRoot = null; // Tree root null set karta hai
        this.scanner = new Scanner(System.in); // Scanner initialize karta hai
        this.adventureArray = new String[0]; // Arrays initialize karta hai
        initializeQuestChallenges(); // Quest challenges initialize karta hai
        buildAdventureMap(); // Adventure map build karta hai
    }

    // Initialize quest challenges using HashMap
    private void initializeQuestChallenges() { // Quest challenges set karta hai
        ArrayList<String> forestChallenges = new ArrayList<>(); // Forest quest list
        forestChallenges.add("Collect Item"); // Item add karta hai
        forestChallenges.add("Avoid Obstacle"); // Obstacle add karta hai
        forestChallenges.add("Reach Goal"); // Goal add karta hai
        questChallenges.put("Forest Adventure", forestChallenges); // HashMap mein store karta hai

        ArrayList<String> mountainChallenges = new ArrayList<>(); // Mountain quest list
        mountainChallenges.add("Climb Hill"); // Hill add karta hai
        mountainChallenges.add("Cross River"); // River add karta hai
        mountainChallenges.add("Find Treasure"); // Treasure add karta hai
        questChallenges.put("Mountain Quest", mountainChallenges); // HashMap mein store karta hai
    }

    // Build adventure map using Graph (HashMap)
    private void buildAdventureMap() { // Adventure map banata hai
        adventureMap.put(1, new ArrayList<>()); // Node 1 add karta hai
        adventureMap.get(1).add(2); // Edge 1->2
        adventureMap.get(1).add(3); // Edge 1->3
        adventureMap.put(2, new ArrayList<>()); // Node 2 add karta hai
        adventureMap.get(2).add(4); // Edge 2->4
        adventureMap.put(3, new ArrayList<>()); // Node 3 add karta hai
        adventureMap.get(3).add(5); // Edge 3->5
        adventureMap.put(4, new ArrayList<>()); // Node 4 add karta hai
        adventureMap.put(5, new ArrayList<>()); // Node 5 add karta hai
    }

    // Build decision tree
    private void buildDecisionTree() { // Decision tree banata hai
        decisionTreeRoot = new AdventureNode("Start Adventure"); // Root node
        decisionTreeRoot.left = new AdventureNode("Take Left Path"); // Left child
        decisionTreeRoot.right = new AdventureNode("Take Right Path"); // Right child
        decisionTreeRoot.left.left = new AdventureNode("Avoid Obstacle"); // Left-left child
        decisionTreeRoot.left.right = new AdventureNode("Collect Item"); // Left-right child
        decisionTreeRoot.right.left = new AdventureNode("Cross River"); // Right-left child
        decisionTreeRoot.right.right = new AdventureNode("Reach Goal"); // Right-right child
    }

    // Prioritize low-health pets using Heap (PriorityQueue)
    private void prioritizeLowHealthPets() { // Low-health pets prioritize karta hai
        adventurePriorityQueue.clear(); // Heap clear karta hai
        ArrayList<Pet> pets = petManager.getPetsForSale(); // Pets ki list leta hai
        for (Pet pet : pets) { // Har pet ke liye
            if (pet.getPetHealth() < 50) { // Agar health 50 se kam hai
                adventurePriorityQueue.offer(pet); // Heap mein add karta hai
            }
        }
        if (adventurePriorityQueue.isEmpty()) { // Agar heap khali hai
            System.out.println("No low-health pets found."); // Message print karta hai
        } else { // Warna
            System.out.println("Low-health pets prioritized:"); // Prioritized pets print
            while (!adventurePriorityQueue.isEmpty()) { // Jab tak heap khali nahi hota
                Pet pet = adventurePriorityQueue.poll(); // Pet nikal kar print karta hai
                System.out.println(pet.getPetName() + " (Health: " + pet.getPetHealth() + ")");
            }
        }
    }

    // Schedule adventure quest
    private void scheduleAdventureQuest(Pet pet1, Pet pet2, String questName) { // Adventure quest schedule karta hai
        if (pet1 == null || pet2 == null) { // Agar pets null hain
            System.out.println("Invalid pets for adventure quest."); // Error message
            return; // Return karta hai
        }
        if (!questChallenges.containsKey(questName)) { // Agar quest name valid nahi
            System.out.println("Invalid quest name."); // Error message
            return; // Return karta hai
        }
        AdventureEvent event = new AdventureEvent(pet1, pet2, questName, LocalDateTime.now().plusHours(1)); // Naya event banata hai
        itemQueue.offer(event.toString()); // Event queue mein add karta hai
        String log = pet1.getPetName() + " and " + pet2.getPetName() + " scheduled for " + questName + " at " + event.eventTime; // Log entry
        adventureLog.add(log); // LinkedList mein log add karta hai
        moveHistory.push(log); // Stack mein history add karta hai
        System.out.println("Scheduled: " + event); // Schedule confirmation
    }

    // Run adventure simulation
    public void runAdventureSimulation(Pet targetPet, String questName) { // Adventure simulation chalata hai
        if (targetPet == null) { // Agar target pet null hai
            System.out.println("Invalid pet for adventure."); // Error message
            return; // Return karta hai
        }
        if (!questChallenges.containsKey(questName)) { // Agar quest name valid nahi
            System.out.println("Invalid quest name."); // Error message
            return; // Return karta hai
        }
        buildDecisionTree(); // Decision tree banata hai
        prioritizeLowHealthPets(); // Low-health pets prioritize karta hai
        System.out.println("Starting adventure for " + targetPet.getPetName() + " in " + questName); // Adventure start message
        ArrayList<String> challenges = questChallenges.get(questName); // Challenges get karta hai
        adventureArray = challenges.toArray(new String[0]); // Array mein convert karta hai (Arrays)
        Arrays.sort(adventureArray); // Array sort karta hai (Sorting)
        for (String challenge : adventureArray) { // Har challenge ke liye
            itemQueue.offer(challenge); // Queue mein challenge add karta hai
            System.out.println("Challenge: " + challenge); // Challenge print
        }
        while (!itemQueue.isEmpty()) { // Jab tak queue khali nahi
            String challenge = itemQueue.poll(); // Challenge nikal kar process karta hai
            adventureLog.add(targetPet.getPetName() + " completed challenge: " + challenge); // LinkedList mein log add karta hai
            moveHistory.push(targetPet.getPetName() + " completed " + challenge); // Stack mein move add karta hai
            System.out.println("Completed: " + challenge); // Completed message
        }
        // Binary Search for a specific challenge
        String searchChallenge = "Collect Item"; // Example search
        int index = Arrays.binarySearch(adventureArray, searchChallenge); // Binary Search karta hai
        if (index >= 0) { // Agar mila
            System.out.println("Found challenge in array: " + adventureArray[index]); // Found message
        } else {
            System.out.println("Challenge not found."); // Not found message
        }
        // Graph traversal (BFS) for adventure path
        int start = 1, goal = 5; // Example nodes
        ArrayList<Integer> path = bfsShortestPath(start, goal); // BFS karta hai
        System.out.println("Adventure path: " + path); // Path print
        // Update pet stats after successful adventure
        targetPet.setPetMood(targetPet.getPetMood() + 10); // Mood increase karta hai
        targetPet.setPetHealth(targetPet.getPetHealth() + 5); // Health increase karta hai
    }

    // BFS for shortest path in graph
    private ArrayList<Integer> bfsShortestPath(int start, int goal) { // BFS implement karta hai
        Queue<Integer> queue = new LinkedList<>(); // Queue for BFS
        boolean[] visited = new boolean[6]; // Visited array
        int[] parent = new int[6]; // Parent array for path reconstruction
        Arrays.fill(parent, -1); // Parent array initialize
        queue.offer(start); // Start node queue mein add
        visited[start] = true; // Start visited set
        while (!queue.isEmpty()) { // Jab tak queue khali nahi
            int node = queue.poll(); // Node nikal kar
            if (node == goal) { // Agar goal mila
                break; // Loop se bahar
            }
            for (int neighbor : adventureMap.getOrDefault(node, new ArrayList<>())) { // Neighbors traverse
                if (!visited[neighbor]) { // Agar unvisited
                    visited[neighbor] = true; // Visited set
                    parent[neighbor] = node; // Parent set
                    queue.offer(neighbor); // Queue mein add
                }
            }
        }
        if (parent[goal] == -1) { // Agar path nahi mila
            System.out.println("No path found."); // Message print
            return new ArrayList<>(); // Empty list return
        }
        ArrayList<Integer> path = new ArrayList<>(); // Path list
        for (int at = goal; at != -1; at = parent[at]) { // Path reconstruct
            path.add(at); // Node add
        }
        Collections.reverse(path); // Path reverse karta hai
        return path; // Path return
    }

    // Undo last move
    public void undoLastMove() { // Last move undo karta hai
        if (moveHistory.isEmpty()) { // Agar stack khali hai
            System.out.println("No moves to undo."); // Message print karta hai
            return; // Return karta hai
        }
        String lastMove = moveHistory.pop(); // Stack se last move nikal karta hai
        adventureLog.remove(lastMove); // LinkedList se remove karta hai
        System.out.println("Undone move: " + lastMove); // Undo confirmation
    }

    // Display adventure log
    public void displayAdventureLog() { // Adventure log display karta hai
        if (adventureLog.isEmpty()) { // Agar log khali hai
            System.out.println("No adventure history available."); // Message print karta hai
            return; // Return karta hai
        }
        System.out.println("Adventure Log:"); // Log print karta hai
        for (String log : adventureLog) { // Har log entry ke liye
            System.out.println(log); // Log print karta hai
        }
    }

    // Display adventure menu
    public void adventureMenu() { // Adventure menu display karta hai
        int choice; // User choice ke liye variable
        do { // Loop jab tak exit na ho
            System.out.println("\n=== Pet Adventure Quest Menu ==="); // Menu title
            System.out.println("1: Start Adventure for a Pet"); // Option 1
            System.out.println("2: Schedule Adventure Quest for Two Pets"); // Option 2
            System.out.println("3: Prioritize Low-Health Pets for Adventure"); // Option 3
            System.out.println("4: Undo Last Move"); // Option 4
            System.out.println("5: Display Adventure Log"); // Option 5
            System.out.println("0: Exit"); // Option 0
            System.out.print("Enter choice: "); // Choice input mangta hai
            choice = scanner.nextInt(); // Choice input leta hai
            scanner.nextLine(); // Buffer clear karta hai

            switch (choice) { // Choice ke hisaab se action
                case 1: // Start adventure
                    System.out.print("Enter Pet Name for Adventure: "); // Pet name mangta hai
                    String petName = scanner.nextLine(); // Pet name input
                    Pet targetPet = petManager.getPet(petName); // Pet dhoondta hai
                    System.out.print("Enter Quest Name (Forest Adventure/Mountain Quest): "); // Quest name mangta hai
                    String questName = scanner.nextLine(); // Quest name input
                    runAdventureSimulation(targetPet, questName); // Adventure chalata hai
                    break; // Switch se bahar
                case 2: // Schedule quest
                    System.out.print("Enter First Pet Name: "); // Pehla pet name
                    String pet1Name = scanner.nextLine(); // Input leta hai
                    System.out.print("Enter Second Pet Name: "); // Doosra pet name
                    String pet2Name = scanner.nextLine(); // Input leta hai
                    Pet pet1 = petManager.getPet(pet1Name); // Pehla pet dhoondta hai
                    Pet pet2 = petManager.getPet(pet2Name); // Doosra pet dhoondta hai
                    System.out.print("Enter Quest Name: "); // Quest name mangta hai
                    String questName2 = scanner.nextLine(); // Quest name input (Fixed variable name)
                    scheduleAdventureQuest(pet1, pet2, questName2); // Quest schedule karta hai
                    break; // Switch se bahar
                case 3: // Prioritize low-health pets
                    prioritizeLowHealthPets(); // Low-health pets prioritize karta hai
                    break; // Switch se bahar
                case 4: // Undo last move
                    undoLastMove(); // Last move undo karta hai
                    break; // Switch se bahar
                case 5: // Display adventure log
                    displayAdventureLog(); // Adventure log display karta hai
                    break; // Switch se bahar
                case 0: // Exit
                    System.out.println("Exiting Adventure Menu..."); // Exit message
                    break; // Switch se bahar
                default: // Invalid choice
                    System.out.println("Invalid choice. Try again."); // Error message
            }
        } while (choice != 0); // Jab tak exit na ho
    }
}