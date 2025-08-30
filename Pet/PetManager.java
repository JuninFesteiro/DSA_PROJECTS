package Pet; // Pet package define karta hai

import User.CareTaker; // CareTaker class import karta hai
import User.User; // User class import karta hai
import User.UserManager; // UserManager class import karta hai
import java.io.*; // File handling ke liye import
import java.util.*; // Collections classes ke liye import

public class PetManager { // PetManager class define karta hai

    private PetNode head; // Singly Linked List ka head
    private PetNode tail; // Singly Linked List ka tail
    private PetNodeBst root; // BST ka root
    private UserManager userManager; // UserManager ka reference
    private Map<CareTaker, ArrayList<Pet>> caretakerPets; // CareTaker aur unke pets ka map

    // Constructor
    public PetManager(UserManager userManager) { // PetManager constructor UserManager ke saath
          // Line ~15: Singly Linked List ka head null set karta hai
        this.head = null;
        // Line ~16: Singly Linked List ka tail null set karta hai
        this.tail = null;
        // Line ~17: BST ka root null set karta hai
        this.root = null;
        // Line ~18: UserManager reference set karta hai
        this.userManager = userManager;
        // Line ~19: CaretakerPets map initialize karta hai
        this.caretakerPets = new HashMap<>();
        // Line ~20: Agar UserManager null nahi hai
        if (userManager != null) {
            // Line ~21: Pets ko file se load karta hai
            loadPetsFromFile("pets.txt");
        }
    }


    // Replace the existing getPets method in PetManager.java
    public ArrayList<Pet> getPets() { // Saare pets ki list return karne ka method
        ArrayList<Pet> petList = new ArrayList<>(); // Nayi ArrayList banata hai
        PetNode curr = head; // Current node ko head se shuru karta hai
        while (curr != null) { // Jab tak current node hai
            petList.add(curr.getData()); // Pet ko list mein add karta hai
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
        return petList; // Pets ki ArrayList return karta hai
    }

    // Getter for UserManager
    public UserManager getUserManager() { // UserManager return karne ka method
        return userManager; // UserManager reference return karta hai
    }

    // Setter for UserManager
    public void setUserManager(UserManager userManager) { // UserManager set karne ka method
        this.userManager = userManager; // UserManager reference set karta hai
        System.out.println("UserManager set in PetManager: " + userManager); // Debug message print karta hai
    }

    // Add a pet
    public void addPet(Pet pet) { // Pet add karne ka method
        if (pet == null) { // Agar pet null hai
            System.out.println("Invalid pet"); // Error message print karta hai
            return; // Return karta hai
        }
        if (isDuplicatedId(pet.getPetId()) || isDuplicatedName(pet.getPetName())) { // Agar pet ID ya name duplicate hai
            System.out.println("Pet already exists"); // Error message print karta hai
            return; // Return karta hai
        }
        PetNode newNode = new PetNode(pet); // Naya PetNode banata hai
        if (head == null) { // Agar list khali hai
            head = newNode; // Head ko naya node set karta hai
            tail = newNode; // Tail ko naya node set karta hai
        } else { // Agar list khali nahi hai
            tail.setNext(newNode); // Tail ke next ko naya node set karta hai
            tail = newNode; // Tail ko update karta hai
        }
        root = insertBST(root, pet); // Pet ko BST mein insert karta hai
        System.out.println("Pet added successfully."); // Success message print karta hai
        savePetsToFile("pets.txt"); // Pets ko file mein save karta hai
    }

    // Insert pet into BST
    public PetNodeBst insertBST(PetNodeBst node, Pet pet) {
        // Line ~60: Agar node null hai
        if (node == null) {
            // Line ~61: Naya node banata hai
            return new PetNodeBst(pet);
        }
        // Line ~62: Pet ID compare karta hai
        if (pet.getPetId() < node.getPetId()) {
            // Line ~63: Left subtree mein insert karta hai
            node.setLeft(insertBST(node.getLeft(), pet));
        } else if (pet.getPetId() > node.getPetId()) {
            // Line ~64: Right subtree mein insert karta hai
            node.setRight(insertBST(node.getRight(), pet));
        }
        // Line ~65: Node return karta hai
        return node;
    }

    // Check for duplicate pet by ID
    private boolean isDuplicatedId(int petId) { // Pet ID duplicate hai ya nahi check karta hai
        return searchBST(root, petId) != null; // BST search result return karta hai
    }

    // Search pet in BST by ID
    public PetNodeBst searchBST(PetNodeBst node, int petId) {
        // Line ~70: Agar node null hai ya ID match karta hai
        if (node == null || node.getPetId() == petId) {
            // Line ~71: Node return karta hai
            return node;
        }
        // Line ~72: Agar pet ID chhota hai
        if (petId < node.getPetId()) {
            // Line ~73: Left subtree mein search karta hai
            return searchBST(node.getLeft(), petId);
        }
        // Line ~74: Right subtree mein search karta hai
        return searchBST(node.getRight(), petId);
    }

    // Get pet by ID from BST
    public Pet getPetByIdBST(int petId) {
        // Line ~80: BST mein pet search karta hai
        PetNodeBst petNode = searchBST(root, petId);
        // Line ~81: Agar pet nahi mila
        if (petNode == null) {
            // Line ~82: Error message print karta hai
            System.out.println("Pet not found in BST: ID " + petId);
            // Line ~83: Null return karta hai
            return null;
        }
        // Line ~84: Pet data return karta hai
        return petNode.getData();
    }

    // Remove pet
    public void removePet(String name) { // Pet name se remove karne ka method
        if (head == null) { // Agar list khali hai
            System.out.println("No pets to remove"); // Error message print karta hai
            return; // Return karta hai
        }
        Pet pet = getPet(name); // Pet name se pet dhoondta hai
        if (pet == null) { // Agar pet nahi mila
            System.out.println("Pet not found: " + name); // Error message print karta hai
            return; // Return karta hai
        }
        int petID = pet.getPetId(); // Pet ka ID leta hai
        if (head.getData().getPetName().equalsIgnoreCase(name)) { // Agar pet head pe hai
            head = head.getNext(); // Head ko next node pe set karta hai
            if (head == null) { // Agar head null ho gaya
                tail = null; // Tail bhi null karta hai
            }
        } else { // Agar pet head pe nahi hai
            PetNode prev = head; // Previous node track karta hai
            PetNode curr = head.getNext(); // Current node track karta hai
            while (curr != null) { // Jab tak current node hai
                if (curr.getData().getPetName().equalsIgnoreCase(name)) { // Agar pet name match karta hai
                    prev.setNext(curr.getNext()); // Previous ka next update karta hai
                    if (curr == tail) { // Agar current tail hai
                        tail = prev; // Tail ko previous pe set karta hai
                    }
                    break; // Loop se bahar nikalta hai
                }
                prev = curr; // Previous ko current pe set karta hai
                curr = curr.getNext(); // Current ko next pe set karta hai
            }
        }
        root = deleteBST(root, petID); // BST se pet remove karta hai
        System.out.println("Pet removed successfully."); // Success message print karta hai
        savePetsToFile("pets.txt"); // Pets ko file mein save karta hai
    }

    // Delete pet from BST
    public PetNodeBst deleteBST(PetNodeBst node, int petId) { // BST se pet delete karne ka method
        if (node == null) { // Agar node null hai
            return null; // Null return karta hai
        }
        if (petId < node.getPetId()) { // Agar pet ID chhota hai
            node.setLeft(deleteBST(node.getLeft(), petId)); // Left subtree se delete karta hai
        } else if (petId > node.getPetId()) { // Agar pet ID bada hai
            node.setRight(deleteBST(node.getRight(), petId)); // Right subtree se delete karta hai
        } else { // Agar pet ID match karta hai
            if (node.getLeft() == null) { // Agar left child nahi hai
                return node.getRight(); // Right child return karta hai
            } else if (node.getRight() == null) { // Agar right child nahi hai
                return node.getLeft(); // Left child return karta hai
            }
            PetNodeBst minNode = findMin(node.getRight()); // Right subtree ka minimum node dhoondta hai
            node.setData(minNode.getData()); // Node ka data replace karta hai
            node.setRight(deleteBST(node.getRight(), minNode.getPetId())); // Minimum node ko delete karta hai
        }
        return node; // Updated node return karta hai
    }

    // Find minimum node in BST
  private PetNodeBst findMin(PetNodeBst node) {
        // Line ~90: Agar node null hai
        if (node == null) {
            // Line ~91: Null return karta hai
            return null;
        }
        // Line ~92: Jab tak left child hai
        while (node.getLeft() != null) {
            // Line ~93: Left child pe jata hai
            node = node.getLeft();
        }
        // Line ~94: Minimum node return karta hai
        return node;
    }

    // Get pet by name
    public Pet getPet(String name) { // Pet name se pet dhoondne ka method
        PetNode curr = head; // Current node ko head se shuru karta hai
        while (curr != null) { // Jab tak current node hai
            if (curr.getData().getPetName().equalsIgnoreCase(name)) { // Agar pet name match karta hai
                return curr.getData(); // Pet data return karta hai
            }
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
        System.out.println("Pet not found: " + name); // Error message print karta hai
        return null; // Null return karta hai
    }

    // Display all pets
    public void displayAllPets() { // Saare pets display karne ka method
        PetNode curr = head; // Current node ko head se shuru karta hai
        if (curr == null) { // Agar list khali hai
            System.out.println("No pets available."); // Message print karta hai
        }
        while (curr != null) { // Jab tak current node hai
            System.out.println(curr.getData()); // Pet details print karta hai
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
    }

    // Get pet by ID
    public Pet getPetById(int petId) { // Pet ID se pet dhoondne ka method
        PetNode curr = head; // Current node ko head se shuru karta hai
        while (curr != null) { // Jab tak current node hai
            if (curr.getData().getPetId() == petId) { // Agar pet ID match karta hai
                return curr.getData(); // Pet data return karta hai
            }
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
        System.out.println("Pet not found: ID " + petId); // Error message print karta hai
        return null; // Null return karta hai
    }

    // Get pets available for sale
    public ArrayList<Pet> getPetsForSale() { // Sale ke liye available pets ki list lene ka method
        ArrayList<Pet> petsForSale = new ArrayList<>(); // Pets ki list banata hai
        PetNode curr = head; // Current node ko head se shuru karta hai
        while (curr != null) { // Jab tak current node hai
            if (curr.getData().getPetForSale()) { // Agar pet sale ke liye available hai
                petsForSale.add(curr.getData()); // Pet ko list mein add karta hai
            }
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
        return petsForSale; // Pets list return karta hai
    }

    

    // Select pet by index
    public Pet selectPet(int index) { // Index se pet select karne ka method
        if (index < 0) { // Agar index invalid hai
            System.out.println("Invalid index"); // Error message print karta hai
            return null; // Null return karta hai
        }
        PetNode curr = head; // Current node ko head se shuru karta hai
        int count = 0; // Index count karta hai
        while (curr != null) { // Jab tak current node hai
            if (count == index) { // Agar index match karta hai
                return curr.getData(); // Pet data return karta hai
            }
            count++; // Count increment karta hai
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
        System.out.println("Pet not found at this index: " + index); // Error message print karta hai
        return null; // Null return karta hai
    }

    // Save pets to file
    public void savePetsToFile(String filename) { // Pets ko file mein save karne ka method
        System.out.println("Attempting to save pets to: " + filename); // Save attempt message print karta hai
        File file = new File(filename); // File object banata hai
        System.out.println("Absolute path: " + file.getAbsolutePath()); // File path print karta hai
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) { // File writer open karta hai
            PetNode curr = head; // Current node ko head se shuru karta hai
            if (curr == null) { // Agar list khali hai
                System.out.println("No pets to save. List is empty."); // Message print karta hai
                return; // Return karta hai
            }
            while (curr != null) { // Jab tak current node hai
                System.out.println("Writing pet: " + curr.getData().toFileString()); // Pet data print karta hai
                writer.write(curr.getData().toFileString()); // Pet data file mein likhta hai
                writer.newLine(); // Nayi line add karta hai
                curr = curr.getNext(); // Current ko next node pe set karta hai
            }
            System.out.println("Pets saved successfully to: " + file.getAbsolutePath()); // Success message print karta hai
        } catch (IOException e) { // Agar IO error hota hai
            System.out.println("Error saving pets: " + e.getMessage()); // Error message print karta hai
            e.printStackTrace(); // Stack trace print karta hai
        }
    }

    // Load pets from file
    public void loadPetsFromFile(String filename) { // Pets ko file se load karne ka method
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { // File reader open karta hai
            String line; // Line variable banata hai
            while ((line = reader.readLine()) != null) { // File se har line padhta hai
                try {
                    Pet pet = Pet.fromFileString(line); // Line se pet object banata hai
                    if (pet != null) { // Agar pet valid hai
                        addPet(pet); // Pet ko add karta hai
                    } else { // Agar pet invalid hai
                        System.out.println("Invalid pet data in file: " + line); // Error message print karta hai
                    }
                } catch (Exception e) { // Agar parsing error hota hai
                    System.out.println("Error parsing line: " + line + ", Error: " + e.getMessage()); // Error message print karta hai
                }
            }
        } catch (FileNotFoundException e) { // Agar file nahi milti
            System.out.println("No pets file found, starting fresh."); // Message print karta hai
        } catch (IOException e) { // Agar IO error hota hai
            System.out.println("Error loading pets: " + e.getMessage()); // Error message print karta hai
        }
    }

    // Assign pet to CareTaker
    public void assignPetToCareTaker(CareTaker careTaker, Pet pet) { // Pet ko CareTaker ko assign karne ka method
        if (careTaker == null) { // Agar CareTaker null hai
            System.out.println("Invalid CareTaker."); // Error message print karta hai
            return; // Return karta hai
        }
        if (pet == null) { // Agar pet null hai
            System.out.println("Invalid pet."); // Error message print karta hai
            return; // Return karta hai
        }
        if (!caretakerPets.containsKey(careTaker)) { // Agar CareTaker map mein nahi hai
            caretakerPets.put(careTaker, new ArrayList<>()); // Nayi list banata hai
        }
        if (!caretakerPets.get(careTaker).contains(pet)) { // Agar pet pehle se assign nahi hai
            caretakerPets.get(careTaker).add(pet); // Pet ko list mein add karta hai
            careTaker.assignPetToCareTaker(pet); // CareTaker ke assigned pets mein add karta hai
            System.out.println("Assigned " + pet.getPetName() + " (ID: " + pet.getPetId() + ") to " + careTaker.getUserName() + ", instance: " + careTaker); // Debug message print karta hai
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("assignments.txt", true))) { // Assignments file open karta hai
                writer.write(careTaker.getEmail().toLowerCase() + "," + pet.getPetId()); // Assignment file mein likhta hai
                writer.newLine(); // Nayi line add karta hai
                System.out.println("Saved assignment to assignments.txt: " + careTaker.getEmail().toLowerCase() + "," + pet.getPetId()); // Success message print karta hai
            } catch (IOException e) { // Agar IO error hota hai
                System.out.println("Error saving assignment: " + e.getMessage()); // Error message print karta hai
            }
        } else { // Agar pet pehle se assign hai
            System.out.println("Pet " + pet.getPetName() + " already assigned to " + careTaker.getUserName()); // Message print karta hai
        }
    }

    // Load assignments from file
    public void loadAssignmentsFromFile(String filename) { // Assignments ko file se load karne ka method
        System.out.println("Attempting to load assignments from: " + filename); // Load attempt message print karta hai
        caretakerPets.clear(); // CaretakerPets map clear karta hai
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { // File reader open karta hai
            String line; // Line variable banata hai
            while ((line = reader.readLine()) != null) { // File se har line padhta hai
                System.out.println("Reading assignment: " + line); // Line print karta hai
                try {
                    String[] parts = line.split(","); // Line ko comma se split karta hai
                    if (parts.length != 2) { // Agar line invalid hai
                        System.out.println("Invalid assignment data: " + line); // Error message print karta hai
                        continue; // Next line pe jata hai
                    }
                    String caretakerEmail = parts[0].trim().toLowerCase(); // CareTaker email extract karta hai aur lowercase karta hai
                    int petId = Integer.parseInt(parts[1].trim()); // Pet ID extract karta hai
                    User user = userManager.getUser(caretakerEmail); // Email se user dhoondta hai
                    if (user == null) { // Agar user nahi mila
                        System.out.println("CareTaker not found for email: " + caretakerEmail); // Error message print karta hai
                        continue; // Next line pe jata hai
                    }
                    Pet pet = getPetById(petId); // Pet ID se pet dhoondta hai
                    if (pet == null) { // Agar pet nahi mila
                        System.out.println("Pet not found for ID: " + petId); // Error message print karta hai
                        continue; // Next line pe jata hai
                    }
                    System.out.println("CareTaker: " + user.getUserName() + ", Pet: " + pet.getPetName() + ", Email: " + user.getEmail() + ", instance: " + user); // Debug message print karta hai
                    if (user instanceof CareTaker) { // Agar user CareTaker hai
                        CareTaker careTaker = (CareTaker) user; // User ko CareTaker mein cast karta hai
                        if (!caretakerPets.containsKey(careTaker)) { // Agar CareTaker map mein nahi hai
                            caretakerPets.put(careTaker, new ArrayList<>()); // Nayi list banata hai
                        }
                        if (!caretakerPets.get(careTaker).contains(pet)) { // Agar pet pehle se assign nahi hai
                            caretakerPets.get(careTaker).add(pet); // Pet ko list mein add karta hai
                            careTaker.assignPetToCareTaker(pet); // CareTaker ke assigned pets mein add karta hai
                            System.out.println("Assigned " + pet.getPetName() + " (ID: " + petId + ") to " + careTaker.getUserName()); // Success message print karta hai
                        } else { // Agar pet pehle se assign hai
                            System.out.println("Pet " + pet.getPetName() + " already assigned to " + careTaker.getUserName()); // Message print karta hai
                        }
                    } else { // Agar user CareTaker nahi hai
                        System.out.println("User is not a CareTaker: " + caretakerEmail); // Error message print karta hai
                    }
                } catch (Exception e) { // Agar parsing error hota hai
                    System.out.println("Error parsing assignment: " + line + ", Error: " + e.getMessage()); // Error message print karta hai
                }
            }
            System.out.println("Total assignments loaded: " + caretakerPets.size()); // Total assignments print karta hai
        } catch (FileNotFoundException e) { // Agar file nahi milti
            System.out.println("No assignments file found, starting fresh."); // Message print karta hai
        } catch (IOException e) { // Agar IO error hota hai
            System.out.println("Error loading assignments: " + e.getMessage()); // Error message print karta hai
        }
    }

    // Find pets by ID range
    public ArrayList<Pet> findPetByIdRange(int minId, int maxId) { // Pet ID range se pets dhoondne ka method
        ArrayList<Pet> result = new ArrayList<>(); // Result list banata hai
        findPetByIdRangeHelper(root, minId, maxId, result); // Helper method call karta hai
        if (result.isEmpty()) { // Agar koi pet nahi mila
            System.out.println("No pets found in the given range"); // Error message print karta hai
            return null; // Null return karta hai
        }
        return result; // Result list return karta hai
    }

    // Helper method for finding pets by ID range
    private void findPetByIdRangeHelper(PetNodeBst node, int minId, int maxId, ArrayList<Pet> result) { // ID range se pets dhoondne ka helper method
        if (node == null) { // Agar node null hai
            return; // Return karta hai
        }
        if (node.getPetId() > minId) { // Agar node ID minimum se bada hai
            findPetByIdRangeHelper(node.getLeft(), minId, maxId, result); // Left subtree mein search karta hai
        }
        if (node.getPetId() >= minId && node.getPetId() <= maxId) { // Agar node ID range mein hai
            result.add(node.getData()); // Pet ko result list mein add karta hai
        }
        if (node.getPetId() < maxId) { // Agar node ID maximum se chhota hai
            findPetByIdRangeHelper(node.getRight(), minId, maxId, result); // Right subtree mein search karta hai
        }
    }

    // Get next available ID
    public int getNextAvailableId() {
        // Line ~100: Agar BST khali hai
        if (root == null) {
            // Line ~101: 1 return karta hai
            return 1;
        }
        // Line ~102: Maximum ID dhoondta hai
        PetNodeBst maxNode = findMin(root);
        // Line ~103: Agar maxNode null hai
        if (maxNode == null) {
            // Line ~104: 1 return karta hai
            return 1;
        }
        // Line ~105: Agla ID return karta hai
        return maxNode.getPetId() + 1;
    }

    // Filter pets by type
    public void filterPetsByType(String type) { // Pet type se filter karne ka method
        PetNode curr = head; // Current node ko head se shuru karta hai
        while (curr != null) { // Jab tak current node hai
            if (curr.getData().getPetType().equalsIgnoreCase(type)) { // Agar pet type match karta hai
                System.out.println(curr.getData()); // Pet details print karta hai
            }
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
    }

    // Sort pets by health
    public void sortPetsByHealth() { // Pets ko health ke hisaab se sort karne ka method
        if (head == null || head.getNext() == null) { // Agar list khali hai ya ek hi node hai
            displayAllPets(); // Saare pets display karta hai
            return; // Return karta hai
        }
        head = mergeSort(head); // Merge sort apply karta hai
        tail = head; // Tail ko head pe set karta hai
        while (tail.getNext() != null) { // Tail ko last node tak le jata hai
            tail = tail.getNext(); // Tail ko next node pe set karta hai
        }
        displayAllPets(); // Sorted pets display karta hai
    }

    // Merge sort for Singly Linked List
    private PetNode mergeSort(PetNode head) { // Singly linked list ko merge sort karne ka method
        if (head == null || head.getNext() == null) { // Agar list khali hai ya ek node hai
            return head; // Head return karta hai
        }
        PetNode slow = head, fast = head.getNext(); // Slow aur fast pointers banata hai
        while (fast != null && fast.getNext() != null) { // Middle node dhoondne ke liye
            slow = slow.getNext(); // Slow pointer ek step aage badhta hai
            fast = fast.getNext().getNext(); // Fast pointer do step aage badhta hai
        }
        PetNode secondHalf = slow.getNext(); // Second half ka start
        slow.setNext(null); // List ko do parts mein split karta hai
        PetNode left = mergeSort(head); // Left half sort karta hai
        PetNode right = mergeSort(secondHalf); // Right half sort karta hai
        return merge(left, right); // Dono halves merge karta hai
    }

    // Merge two sorted lists
    private PetNode merge(PetNode left, PetNode right) { // Do sorted lists merge karne ka method
        PetNode dummy = new PetNode(null); // Dummy node banata hai
        PetNode current = dummy; // Current pointer dummy pe set karta hai
        while (left != null && right != null) { // Jab tak dono lists mein nodes hain
            if (left.getData().getPetHealth() <= right.getData().getPetHealth()) { // Health compare karta hai
                current.setNext(left); // Left node ko add karta hai
                left = left.getNext(); // Left pointer aage badhta hai
            } else { // Agar right node ka health kam hai
                current.setNext(right); // Right node ko add karta hai
                right = right.getNext(); // Right pointer aage badhta hai
            }
            current = current.getNext(); // Current pointer aage badhta hai
        }
        if (left != null) { // Agar left list mein nodes bache hain
            current.setNext(left); // Bache hue left nodes add karta hai
        }
        if (right != null) { // Agar right list mein nodes bache hain
            current.setNext(right); // Bache hue right nodes add karta hai
        }
        return dummy.getNext(); // Merged list ka head return karta hai
    }

    // Check for duplicate pet by name
    private boolean isDuplicatedName(String petName) { // Pet name duplicate hai ya nahi check karta hai
        PetNode curr = head; // Current node ko head se shuru karta hai
        while (curr != null) { // Jab tak current node hai
            if (curr.getData().getPetName().equalsIgnoreCase(petName)) { // Agar pet name match karta hai
                return true; // True return karta hai
            }
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
        return false; // False return karta hai
    }

    // Get total pet count
    public int getPetCount() { // Total pets ki ginti lene ka method
        int count = 0; // Count variable banata hai
        PetNode curr = head; // Current node ko head se shuru karta hai
        while (curr != null) { // Jab tak current node hai
            count++; // Count increment karta hai
            curr = curr.getNext(); // Current ko next node pe set karta hai
        }
        return count; // Total count return karta hai
    }
}