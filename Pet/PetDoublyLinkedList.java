package Pet;

public class PetDoublyLinkedList {
    // Fields
    private PetNode head; // Doubly linked list ka pehla node
    private PetNode tail; // Doubly linked list ka aakhri node
    private PetNode current; // Navigation ke liye current node
    private int size; // List mein nodes ki tadaad

    // Inner class for PetNode
    public class PetNode {
        private Pet data; // Pet object jo node mein store hai
        private PetNode next; // Agla node
        private PetNode prev; // Pichhla node

        public PetNode(Pet data) { // PetNode ka constructor
            this.data = data; // Pet data set karo
            this.next = null; // Agla node null karo
            this.prev = null; // Pichhla node null karo
        }

        public Pet getData() { // Pet data wapas karo
            return data; // Pet object return karo
        }

        public PetNode getNext() { // Agla node wapas karo
            return next; // Next node return karo
        }

        public PetNode getPrev() { // Pichhla node wapas karo
            return prev; // Previous node return karo
        }

        public void setNext(PetNode next) { // Agla node set karo
            this.next = next; // Next pointer update karo
        }

        public void setPrev(PetNode prev) { // Pichhla node set karo
            this.prev = prev; // Previous pointer update karo
        }
    }

    // Constructor
    public PetDoublyLinkedList() {
        this.head = null; // Head ko null karo
        this.tail = null; // Tail ko null karo
        this.current = null; // Current ko null karo
        this.size = 0; // Size ko zero karo
    }

    // Add a pet
    public void add(Pet pet) {
        if (pet == null) { // Check karo ke pet null toh nahi
            System.out.println("Null pet add nahi kar sakte"); // Error message dikhao
            return; // Method se bahar niklo
        }
        PetNode newNode = new PetNode(pet); // Naya node banao pet ke saath
        System.out.println("Pet add ho raha hai: " + pet.getPetName() + " (ID: " + pet.getPetId() + ")"); // Debug: Pet add hone ki tasdeeq
        if (head == null) { // Agar list khali hai
            head = newNode; // Head ko naya node banao
            tail = newNode; // Tail ko naya node banao
            current = newNode; // Current ko naya node banao
            System.out.println("Head/tail/current set kiya: " + pet.getPetName()); // Debug: Head/tail set hone ki tasdeeq
        } else {
            tail.setNext(newNode); // Tail ke baad naya node jodo
            newNode.setPrev(tail); // Naye node ka pichhla tail karo
            tail = newNode; // Tail ko naya node banao
            System.out.println("Tail ke baad add kiya: " + pet.getPetName()); // Debug: Tail append ki tasdeeq
        }
        size++; // Size badhao
        System.out.println("List ka size ab: " + size); // Debug: Current size dikhao
    }

    // Check if pet exists
    public boolean contains(Pet pet) {
        if (pet == null) { // Check karo ke pet null toh nahi
            System.out.println("Null pet check nahi kar sakte"); // Error message dikhao
            return false; // False wapas karo
        }
        PetNode curr = head; // Head se shuru karo
        while (curr != null) { // List ke har node ko check karo
            if (curr.getData().getPetId() == pet.getPetId()) { // Pet ID se compare karo
                System.out.println("Pet mil gaya: " + curr.getData().getPetName() + " (ID: " + curr.getData().getPetId() + ")"); // Debug: Pet milne ki tasdeeq
                return true; // True wapas karo agar ID match
            }
            curr = curr.getNext(); // Agle node pe jao
        }
        System.out.println("Pet nahi mila: ID " + pet.getPetId()); // Debug: Pet na milne ki tasdeeq
        return false; // False wapas karo agar nahi mila
    }

    // Display pets forward
    public void displayForward() {
        System.out.println("Pets aage ki taraf dikhaye ja rahe hain, size: " + size); // Debug: Size dikhao
        if (head == null) { // Check karo ke list khali toh nahi
            System.out.println("Koi pet assign nahi hai."); // Khali list ka message
            return; // Method se bahar niklo
        }
        PetNode curr = head; // Head se shuru karo
        while (curr != null) { // List ke har node ko check karo
            Pet pet = curr.getData(); // Current node se pet lo
            if (pet != null) { // Check karo ke pet null toh nahi
                System.out.println(pet.getPetName() + " (ID: " + pet.getPetId() + ")"); // Pet ka naam aur ID dikhao
            } else {
                System.out.println("Null pet list mein mila"); // Debug: Null pet ka error
            }
            curr = curr.getNext(); // Agle node pe jao
        }
    }

    // Display pets backward
    public void displayBackward() {
        System.out.println("Pets peechhe ki taraf dikhaye ja rahe hain, size: " + size); // Debug: Size dikhao
        if (tail == null) { // Check karo ke list khali toh nahi
            System.out.println("Koi pet assign nahi hai."); // Khali list ka message
            return; // Method se bahar niklo
        }
        PetNode curr = tail; // Tail se shuru karo
        while (curr != null) { // List ke har node ko check karo
            Pet pet = curr.getData(); // Current node se pet lo
            if (pet != null) { // Check karo ke pet null toh nahi
                System.out.println(pet.getPetName() + " (ID: " + pet.getPetId() + ")"); // Pet ka naam aur ID dikhao
            } else {
                System.out.println("Null pet list mein mila"); // Debug: Null pet ka error
            }
            curr = curr.getPrev(); // Pichhle node pe jao
        }
    }

    // Navigate to next critical pet
    public Pet navigateToNextCriticalPet() {
        System.out.println("Agla critical pet dhoondh rahe hain"); // Debug: Navigation shuru
        PetNode curr = current; // Current node se shuru karo
        while (curr != null) { // List ke har node ko check karo
            if (curr.getData().getPetHealth() < 50) { // Check karo ke health 50 se kam hai
                current = curr.getNext(); // Current ko agle node pe set karo
                System.out.println("Critical pet mila: " + curr.getData().getPetName()); // Debug: Critical pet ki tasdeeq
                return curr.getData(); // Critical pet wapas karo
            }
            curr = curr.getNext(); // Agle node pe jao
        }
        System.out.println("Koi critical pet nahi mila"); // Debug: Koi critical pet nahi
        return null; // Null wapas karo agar koi nahi mila
    }

    // Navigate to previous pet
    public Pet navigateToPreviousPet() {
        System.out.println("Pichhla pet dhoondh rahe hain"); // Debug: Navigation shuru
        if (current != null && current.getPrev() != null) { // Check karo ke current aur pichhla node hai
            current = current.getPrev(); // Current ko pichhle node pe set karo
            System.out.println("Pichhla pet: " + current.getData().getPetName()); // Debug: Pichhle pet ki tasdeeq
            return current.getData(); // Pichhla pet wapas karo
        }
        System.out.println("Koi pichhla pet nahi"); // Debug: Pichhla pet nahi mila
        return null; // Null wapas karo agar koi nahi
    }

    // Get head
    public PetNode getHead() {
        System.out.println("Head le rahe hain, size: " + size); // Debug: Size dikhao
        return head; // Head node wapas karo
    }

    // Get size
    public int size() {
        return size; // Nodes ki tadaad wapas karo
    }
}