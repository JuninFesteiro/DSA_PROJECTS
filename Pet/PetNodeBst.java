package Pet;

import Pet.Pet;

// Class Pet Node Bst
public class PetNodeBst { //Pet Node BST class

    // Fields
    private Pet data; //Pet ka data
    private int petId; //Pet ka ID key
    private PetNodeBst left; //Left child
    private PetNodeBst right; //Right child

    // Constructor
    public PetNodeBst(Pet data) { //Constructor
        this.data = data;
        this.petId = data.getPetId();
    }

    // Methods
    public Pet getData() { //Data lena
        return data;
    }

    // Set Data
    public void setData(Pet data) { //Data set karna
        this.data = data;
        this.petId = data.getPetId();
    }

    // Get Pet Id
    public int getPetId() { //Pet ka ID lena
        return petId;
    }
    
    // Get left Child
    public PetNodeBst getLeft() { //Left child lena
        return left;
    }
    
    // Get Right Child
    public PetNodeBst getRight() { //Right child lena
        return right;
    }
     
    // set Left Child
    public void setLeft(PetNodeBst left) { //Left child set karna
        this.left = left;
    }
     
    // set Right Child
    public void setRight(PetNodeBst right) { //Right child set karna
        this.right = right;
    }
}