package Pet;

public class PetInteraction {

    private Pet pets[]; //Pets store karne ke liye array
    private int[][] adjacenyMatrix; //Interactions ke liye adjacency matrix
    private int maxPets; //Pets ki maximum tadaad
    private int petCount; //Pets ka track rakhna

    // Pet Interaction
    public PetInteraction(int maxPets) { //Pet interaction ka constructor
        this.maxPets = maxPets;
        this.petCount = 0;
        this.pets = new Pet[maxPets];
        this.adjacenyMatrix = new int[maxPets][maxPets];

        // Initialize adjaceny matrix with 0
        for (int i = 0; i < maxPets; i++) { //Adjacency matrix ko 0 se initialize karna
            for (int j = 0; j < maxPets; j++) {
                adjacenyMatrix[i][j] = 0;
            }
        }
    }

    // Add a pet to graph with dynamic check
    // Yeh method pet ko add karta hai
    public boolean addPet(Pet pet) {
        if (pet == null || petCount >= maxPets) { // Agar pet null hai ya limit exceed hai
            return false; // False return karta hai
        }
        for (int i = 0; i < petCount; i++) { // Existing pets check karta hai
            if (pets[i] == pet) { // Agar pet already exists
                return false; // False return karta hai
            }
        }
        pets[petCount] = pet; // Pet ko array mein add karta hai
        petCount++; // Pet count increment karta hai
        return true; // True return karta hai
    }

    // Yeh method interaction matrix return karta hai
   
    // Add interaction between two pets with weight(1-5)
    public boolean addInteraction(String petName1, String petName2, int weight) { //Do pets ke beech interaction add karna weight ke saath (1-5)
        // Check weight
        if (weight < 1 || weight > 5) { //Weight check karna
            System.out.println("Weight must be between 1 and 5");
            return false;
        }

        // Find indexes of both pets
        int index1 = findPetIndex(petName1); //Dono pets ke indexes dhoondna
        int index2 = findPetIndex(petName2);

        // Check Self interaction or pet no foud
        if (index1 == -1 || index2 == -1 || index1 == index2) { //Self interaction ya pet na milne ka check
            System.out.println("Pet not found or pet have elf interatcion");
            return false;
        }

        // Directed Interation
        adjacenyMatrix[index1][index2] = weight; //Directed interaction

        // UniDirected for simplicity
        adjacenyMatrix[index2][index1] = weight; //Undirected interaction simplicity ke liye

        System.out.println(petName1 + " Pet interacts with " + petName2);
        return true;
    }

    // Find pet index in array
    private int findPetIndex(String petName) { //Array mein pet ka index dhoondna
        for (int i = 0; i < petCount; i++) {
            if (pets[i] != null && pets[i].getPetName().equalsIgnoreCase(petName)) {
                return i;
            }
        }

        // No found
        return -1; //Nahi mila
    }

    // Get interaction matrix
    public int[][] getInteractionMatrix() { // Adjacency matrix return karna
        return adjacenyMatrix;
    }

    // Get pet index
    public int getPetIndex(int index) { // Pet ka index return karna
        if (index >= 0 && index < petCount && pets[index] != null) {
            return pets[index].getPetId();
        }
        return -1; // Invalid index ya pet nahi mila
    }

    // Display interaction matrix with weights
    public void displayInteraction() { //Interaction matrix weights ke saath display karna
        if (petCount == 0) {
            System.out.println("No pets to interaction to display");
            return;
        }

        System.out.println(" ");
        for (int i = 0; i < petCount; i++) {
            System.out.print(pets[i].getPetName() + " ");
        }
        System.out.println();

        for (int i = 0; i < petCount; i++) {
            System.out.println(pets[i].getPetName() + " ");

            for (int j = 0; j < petCount; j++) {
                System.out.print(adjacenyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Common inetraction between pets
    public void findCommonInteractions(String petName1, String petName2) { //Pets ke beech common interaction dhoondna
        int index1 = findPetIndex(petName1);
        int index2 = findPetIndex(petName2);

        // If pet not found
        if (index1 == -1 || index2 == -1) { //Agar pet nahi mila
            System.out.println("One or both pets not found ");
            return;
        }

        System.out.println(petName1 + " and " + petName2 + " Have common interaction with: ");
        for (int i = 0; i < petCount; i++) {
            if (i != index1 && i != index2 && adjacenyMatrix[index1][i] > 0 && adjacenyMatrix[index2][i] > 0) {
                System.out.println(pets[i].getPetName() + "(Weight:   " + adjacenyMatrix[index1][i] + ")");
            }
        }
    }

    // Detect cylce in interaction grapgh with help of dfs
    public boolean detectInteractionGraph() { //Interaction graph mein cycle detect karna DFS ke saath
        boolean[] visited = new boolean[maxPets];
        boolean[] rec = new boolean[maxPets]; //Cycle detection ke liye recursive stack

        for (int i = 0; i < petCount; i++) {
            if (detectCycle(i, visited, rec)) {
                System.out.println("Cycle detectec in interaction graph");
                return true;
            }
        }
        System.out.println("No cycle detect in graph");
        return false;
    }

    // method to detect cycle
    private boolean detectCycle(int v, boolean[] visited, boolean[] rec) { //Cycle detect karne ka method
        visited[v] = true;
        rec[v] = true;

        for (int i = 0; i < petCount; i++) {
            if (adjacenyMatrix[v][i] > 0) {
                if (!visited[i]) {
                    if (detectCycle(i, visited, rec)) {
                        return true;
                    }
                } else if (rec[i]) {
                    return true;
                }
            }
        }

        rec[v] = false;
        return false;
    }
}