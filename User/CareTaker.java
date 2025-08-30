package User; // User package define karta hai

import Pet.Pet; // Pet class import karta hai
import Pet.PetDoublyLinkedList; // PetDoublyLinkedList class import karta hai
import Pet.PetInteraction;
import Pet.PetManager; // PetManager class import karta hai
import Sanctuary.PetSocialNetwork;
import Sanctuary.PetAdventureQuest; // PetAdventureQuest import karta hai
import Event.Event; // Event class import karta hai
import Event.EventManager; // EventManager class import karta hai
import java.util.Scanner; // Scanner class import karta hai
import java.util.ArrayList; // ArrayList class import karta hai
import java.util.LinkedList; // LinkedList class import karta hai
import java.util.Queue; // Queue interface import karta hai

public class CareTaker extends User { // CareTaker class define karta hai jo User se extend hoti hai

    private PetDoublyLinkedList assignedPets; // Assigned pets ke liye doubly linked list banata hai
    private Queue<Pet> careQueue; // Pet care ke liye queue banata hai
    private PetHealthNode bstRoot; // Pet health ke liye BST root banata hai

    private class PetHealthNode { // Inner class PetHealthNode define karta hai

        Pet pet; // Pet object store karta hai
        int health; // Pet ka health store karta hai
        PetHealthNode left, right; // Left aur right child nodes define karta hai

        PetHealthNode(Pet pet) { // PetHealthNode constructor banata hai
            this.pet = pet; // Pet object set karta hai
            this.health = pet.getPetHealth(); // Pet ka health set karta hai
            this.left = this.right = null; // Left aur right child ko null set karta hai
        }
    }

    public CareTaker(String userName, String email, String password, int age, char role) { // CareTaker constructor banata hai
        super(userName, password, email, age, role); // Parent class (User) ka constructor call karta hai
        this.assignedPets = new PetDoublyLinkedList(); // Assigned pets ke liye doubly linked list initialize karta hai
        this.careQueue = new LinkedList<>(); // Care queue initialize karta hai
        this.bstRoot = null; // BST root ko null set karta hai
        System.out.println("CareTaker banaya: " + userName + ", Email: " + email + ", instance: " + this); // Debug message print karta hai
    }

    public void assignPetToCareTaker(Pet pet) { // Pet ko CareTaker ko assign karne ka method
        if (pet == null) { // Agar pet null hai
            System.out.println("Galat pet " + getUserName() + " ke liye"); // Error message print karta hai
            return; // Method se return karta hai
        }
        if (!assignedPets.contains(pet)) { // Agar pet pehle se assign nahi hai
            assignedPets.add(pet); // Pet ko assigned pets list mein add karta hai
            System.out.println("Pet assign kiya: " + pet.getPetName() + " (ID: " + pet.getPetId() + ") ko " + getUserName() + ", instance: " + this); // Debug message print karta hai
        } else { // Agar pet pehle se assign hai
            System.out.println("Pet " + pet.getPetName() + " pehle se assign hai " + getUserName() + " ko"); // Message print karta hai
        }
    }

    @Override
    public void interactWithPet(Pet pet) {
        // Line ~40: Agar pet null hai
        if (pet == null) {
            // Line ~41: Error message print karta hai
            System.out.println("Galat pet ke saath interact nahi kar sakte");
            // Line ~42: Method se return karta hai
            return;
        }
        // Line ~43: Agar pet assign nahi hai
        if (!isAssignedToPet(pet)) {
            // Line ~44: Error message print karta hai
            System.out.println("Pet " + pet.getPetName() + " assign nahi hai");
            // Line ~45: Method se return karta hai
            return;
        }
        // Line ~46: Interaction message print karta hai
        System.out.println(getUserName() + " pet ke saath interact kar raha hai: " + pet.getPetName());
        // Line ~47: Pet ko khana khilata hai
        pet.feedPet();
        // Line ~48: Feed message print karta hai
        System.out.println(pet.getPetName() + " has been fed.");
        // Line ~49: Pet ke saath khelta hai
        pet.playWithPet();
        // Line ~50: Play message print karta hai
        System.out.println(pet.getPetName() + " is happy after playing.");
        // Line ~51: Pet ki health improve karta hai
        pet.healPet();
        // Line ~52: Heal message print karta hai
        System.out.println(pet.getPetName() + " has been healed.");
        // Line ~53: Pet state ko pets.txt mein save karta hai
        pet.getPetManager().savePetsToFile("pets.txt");
    }

    public boolean isAssignedToPet(Pet pet) { // Pet assign hai ya nahi check karta hai
        if (pet == null) { // Agar pet null hai
            return false; // False return karta hai
        }
        return assignedPets.contains(pet); // Assigned pets list mein pet check karta hai
    }

    public void viewAssignedPets() { // Assigned pets display karne ka method
        System.out.println("Pets jo " + getUserName() + " ko assign hain:"); // Title print karta hai
        System.out.println("Assigned pets ki list check kar rahe hain, size: " + assignedPets.size() + ", instance: " + this); // Debug message print karta hai
        assignedPets.displayForward(); // Pets ko forward direction mein display karta hai
    }

    public void viewAssignedPetsReverse() { // Assigned pets reverse mein display karne ka method
        System.out.println("Pets ki reverse list:"); // Title print karta hai
        assignedPets.displayBackward(); // Pets ko backward direction mein display karta hai
    }

    public void resolveEvent(EventManager eventManager) { // Event resolve karne ka method
        Scanner scanner = new Scanner(System.in); // Scanner object banata hai
        System.out.println("Assigned pets:"); // Assigned pets ka title print karta hai
        assignedPets.displayForward(); // Assigned pets display karta hai
        System.out.print("Event resolve karne ke liye pet ka naam daal: "); // Pet name input ke liye prompt
        String petName = scanner.nextLine(); // Pet name input leta hai

        Pet petToResolve = null; // Pet to resolve variable banata hai
        for (PetDoublyLinkedList.PetNode node = assignedPets.getHead(); node != null; node = node.getNext()) { // Assigned pets ke liye loop
            if (node.getData().getPetName().equalsIgnoreCase(petName)) { // Agar pet name match karta hai
                petToResolve = node.getData(); // Pet set karta hai
                break; // Loop se bahar nikalta hai
            }
        }

        if (petToResolve != null) { // Agar pet mila
            eventManager.triggerEvent(petToResolve); // Event trigger karta hai
        } else { // Agar pet nahi mila
            System.out.println("Pet nahi mila ya assign nahi hai."); // Error message print karta hai
        }
    }

    public Pet navigateToNextCriticalPet() {
        // Line ~90: Critical pet dhoondhne ka message print karta hai
        System.out.println("Agla critical pet dhoondh rahe hain");
        // Line ~91: Critical pet ko doubly linked list se navigate karta hai
        Pet critical = assignedPets.navigateToNextCriticalPet();
        // Line ~92: Agar critical pet mila
        if (critical != null) {
            // Line ~93: Critical pet found message print karta hai
            System.out.println("Agla critical pet: " + critical.getPetName());
        } else {
            // Line ~94: Agar critical pet nahi mila
            System.out.println("Koi critical pet nahi mila.");
        }
        // Line ~95: Critical pet return karta hai
        return critical;
    }

    public Pet navigateToPreviousPet() { // Pichhla pet navigate karne ka method
        Pet previous = assignedPets.navigateToPreviousPet(); // Pichhla pet dhoondta hai
        if (previous != null) { // Agar pichhla pet mila
            System.out.println("Pichhla pet: " + previous.getPetName()); // Pet name print karta hai
        } else { // Agar pichhla pet nahi mila
            System.out.println("Koi pichhla pet nahi."); // Error message print karta hai
        }
        return previous; // Pichhla pet return karta hai
    }

    public void searchPetByMood(int moodThreshold) { // Mood threshold se pets dhoondne ka method
        System.out.println("Mood " + moodThreshold + " se kam wale pets dhoondh rahe hain:"); // Title print karta hai
        PetDoublyLinkedList.PetNode current = assignedPets.getHead(); // Current node ko head se shuru karta hai
        boolean found = false; // Found flag banata hai
        while (current != null) { // Jab tak current node hai
            Pet pet = current.getData(); // Pet data leta hai
            if (pet.getPetMood() < moodThreshold) { // Agar pet ka mood threshold se kam hai
                System.out.println(pet.getPetName() + " (Mood: " + pet.getPetMood() + ")"); // Pet details print karta hai
                found = true; // Found flag set karta hai
            }
            current = current.getNext(); // Current ko next node pe set karta hai
        }
        if (!found) { // Agar koi pet nahi mila
            System.out.println("Mood " + moodThreshold + " se kam wala koi pet nahi mila"); // Error message print karta hai
        }
    }

    public void sortAssignedPetsByHunger() { // Assigned pets ko hunger ke hisaab se sort karne ka method
        ArrayList<Pet> pets = new ArrayList<>(); // Pets ki list banata hai
        PetDoublyLinkedList.PetNode current = assignedPets.getHead(); // Current node ko head se shuru karta hai
        while (current != null) { // Jab tak current node hai
            pets.add(current.getData()); // Pet ko list mein add karta hai
            current = current.getNext(); // Current ko next node pe set karta hai
        }
        int n = pets.size(); // Pets ka size leta hai
        for (int i = 0; i < n - 1; i++) { // Selection sort ka outer loop chalata hai
            int minIndex = i; // Minimum index set karta hai
            for (int j = i + 1; j < n; j++) { // Inner loop chalata hai
                if (pets.get(j).getPetHunger() < pets.get(minIndex).getPetHunger()) { // Hunger compare karta hai
                    minIndex = j; // Minimum index update karta hai
                }
            }
            Pet temp = pets.get(minIndex); // Temporary pet store karta hai
            pets.set(minIndex, pets.get(i)); // Minimum index pe pet set karta hai
            pets.set(i, temp); // Original index pe temp pet set karta hai
        }
        System.out.println("\nPets ko hunger ke hisaab se sort kiya (ascending):"); // Sort complete message print karta hai
        for (Pet pet : pets) { // Har sorted pet ke liye loop
            System.out.println(pet.getPetName() + " (Hunger: " + pet.getPetHunger() + ")"); // Pet details print karta hai
        }
    }

    public void manageCareQueue(String action, Pet pet) { // Care queue manage karne ka method
        if (pet == null) { // Agar pet null hai
            System.out.println("Galat pet care queue ke liye"); // Error message print karta hai
            return; // Method se return karta hai
        }
        if (!isAssignedToPet(pet)) { // Agar pet assign nahi hai
            System.out.println("Pet " + pet.getPetName() + " assign nahi hai"); // Error message print karta hai
            return; // Method se return karta hai
        }
        if (action.equalsIgnoreCase("add")) { // Agar action add hai
            if (!careQueue.contains(pet)) { // Agar pet queue mein nahi hai
                careQueue.add(pet); // Pet ko queue mein add karta hai
                System.out.println("Pet " + pet.getPetName() + " queue mein add kiya"); // Success message print karta hai
            } else { // Agar pet pehle se queue mein hai
                System.out.println("Pet " + pet.getPetName() + " pehle se queue mein hai"); // Message print karta hai
            }
        } else if (action.equalsIgnoreCase("remove")) { // Agar action remove hai
            if (careQueue.remove(pet)) { // Agar pet queue se remove hota hai
                System.out.println("Pet " + pet.getPetName() + " queue se hataya"); // Success message print karta hai
            } else { // Agar pet queue mein nahi tha
                System.out.println("Pet " + pet.getPetName() + " queue mein nahi tha"); // Error message print karta hai
            }
        }
    }

    public void monitorPetHealth(Pet pet) { // Pet ki health monitor karne ka method
        if (pet == null) { // Agar pet null hai
            System.out.println("Galat pet health monitoring ke liye"); // Error message print karta hai
            return; // Method se return karta hai
        }
        if (!isAssignedToPet(pet)) { // Agar pet assign nahi hai
            System.out.println("Pet " + pet.getPetName() + " assign nahi hai"); // Error message print karta hai
            return; // Method se return karta hai
        }
        insertIntoBST(pet); // Pet ko BST mein insert karta hai
        System.out.println("Pet " + pet.getPetName() + " ki health monitor ki: Health = " + pet.getPetHealth()); // Health monitoring message print karta hai
    }

    private void insertIntoBST(Pet pet) { // Pet ko BST mein insert karne ka method
        bstRoot = insertIntoBSTRec(bstRoot, pet); // Recursive insert method call karta hai
    }

    private PetHealthNode insertIntoBSTRec(PetHealthNode node, Pet pet) { // BST mein pet insert karne ka recursive method
        if (node == null) { // Agar node null hai
            return new PetHealthNode(pet); // Naya node banata hai
        }
        if (pet.getPetHealth() < node.health) { // Agar pet ka health chhota hai
            node.left = insertIntoBSTRec(node.left, pet); // Left subtree mein insert karta hai
        } else if (pet.getPetHealth() > node.health) { // Agar pet ka health bada hai
            node.right = insertIntoBSTRec(node.right, pet); // Right subtree mein insert karta hai
        }
        return node; // Updated node return karta hai
    }

    public void loadCareTakerMenu(PetManager petManager, EventManager eventManager) { // CareTaker menu display karta hai
        Scanner scanner = new Scanner(System.in); // Scanner banata hai
        while (true) { // Loop jab tak exit na ho
            System.out.println("\n=== CareTaker Menu ==="); // Menu title
            System.out.println("1: View Assigned Pets"); // Option 1
            System.out.println("2: View Assigned Pets Reverse"); // Option 2
            System.out.println("3: Interact with Pet"); // Option 3
            System.out.println("4: Resolve Event"); // Option 4
            System.out.println("5: Navigate to Next Critical Pet"); // Option 5
            System.out.println("6: Navigate to Previous Pet"); // Option 6
            System.out.println("7: Search Pet by Mood"); // Option 7
            System.out.println("8: Sort Pets by Hunger"); // Option 8
            System.out.println("9: Manage Care Queue"); // Option 9
            System.out.println("10: Monitor Pet Health"); // Option 10
            System.out.println("11: Manage Pet Social Network"); // Option 11
            System.out.println("12: Manage Pet Adventures"); // NEW: Adventure quest option
            System.out.println("0: Logout"); // Option 0
            System.out.print("Enter choice: "); // Choice input mangta hai
            int choice = scanner.nextInt(); // Choice input leta hai
            scanner.nextLine(); // Buffer clear karta hai

            switch (choice) { // Choice ke hisaab se action
                case 1: // View assigned pets
                    viewAssignedPets(); // Assigned pets display karta hai
                    break; // Switch se bahar
                case 2: // View reverse
                    viewAssignedPetsReverse(); // Reverse mein pets display karta hai
                    break; // Switch se bahar
                case 3: // Interact with pet
                    System.out.print("Enter Pet Name to Interact: "); // Pet name mangta hai
                    String petName = scanner.nextLine(); // Pet name input
                    Pet pet = petManager.getPet(petName); // Pet dhoondta hai
                    if (pet != null && isAssignedToPet(pet)) { // Agar pet mila aur assign hai
                        interactWithPet(pet); // Pet ke saath interact karta hai
                    } else { // Warna
                        System.out.println("Pet nahi mila ya assign nahi hai."); // Error message
                    }
                    break; // Switch se bahar
                case 4: // Resolve event
                    resolveEvent(eventManager); // Event resolve karta hai
                    break; // Switch se bahar
                case 5: // Next critical pet
                    navigateToNextCriticalPet(); // Critical pet navigate karta hai
                    break; // Switch se bahar
                case 6: // Previous pet
                    navigateToPreviousPet(); // Pichhla pet navigate karta hai
                    break; // Switch se bahar
                case 7: // Search by mood
                    System.out.print("Enter Mood Threshold: "); // Mood threshold mangta hai
                    int moodThreshold = scanner.nextInt(); // Mood threshold input
                    scanner.nextLine(); // Buffer clear
                    searchPetByMood(moodThreshold); // Mood ke hisaab se search
                    break; // Switch se bahar
                case 8: // Sort by hunger
                    sortAssignedPetsByHunger(); // Hunger ke hisaab se sort
                    break; // Switch se bahar
                case 9: // Manage care queue
                    System.out.print("Enter action (add/remove): "); // Action mangta hai
                    String action = scanner.nextLine(); // Action input
                    System.out.print("Enter Pet Name: "); // Pet name mangta hai
                    String carePetName = scanner.nextLine(); // Pet name input
                    Pet carePet = petManager.getPet(carePetName); // Pet dhoondta hai
                    manageCareQueue(action, carePet); // Care queue manage karta hai
                    break; // Switch se bahar
                case 10: // Monitor pet health
                    System.out.print("Enter Pet Name to Monitor: "); // Pet name mangta hai
                    String monitorPetName = scanner.nextLine(); // Pet name input
                    Pet monitorPet = petManager.getPet(monitorPetName); // Pet dhoondta hai
                    monitorPetHealth(monitorPet); // Health monitor karta hai
                    break; // Switch se bahar
                case 11: // Manage pet social network
                    PetSocialNetwork socialNetwork = new PetSocialNetwork(petManager, new PetInteraction(10)); // PetSocialNetwork object banata hai
                    socialNetwork.socialNetworkMenu(); // Social network menu kholta hai
                    break; // Switch se bahar
                case 12: // NEW: Manage pet adventures
                    PetAdventureQuest adventureQuest = new PetAdventureQuest(petManager); // PetAdventureQuest object banata hai
                    adventureQuest.adventureMenu(); // Adventure menu kholta hai
                    break; // Switch se bahar
                case 0: // Logout
                    System.out.println("Logging out..."); // Logout message
                    return; // Method se return
                default: // Invalid choice
                    System.out.println("Galat choice, dobara try karo."); // Error message
            }
        }
    }
}
