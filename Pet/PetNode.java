package Pet;

import Pet.Pet;

public class PetNode {

    // Fields
    private Pet data; //Pet ka data
    private PetNode next; //Agla node
    private PetNode prev; //Pichhla node

    // Constructor
    public PetNode(Pet data) { //Constructor
        this.data = data;
        this.next = null;
        this.prev = null;
    }
     
    // Get Data
    public Pet getData() { //Data lena
        return data;
    }

    // Set Data
    public void setData(Pet data) { //Data set karna
        this.data = data;
    }
    
    // Get Next
    public PetNode getNext() { //Agla node lena
        return next;
    }
    
    // Get Prev
    public PetNode getPrev() { //Pichhla node lena
        return prev;
    }
    
    // SetNexr
    public void setNext(PetNode pet) { //Agla node set karna
        next = pet;
    }
    
    // Set Prev
    public void setPrev(PetNode pet) { //Pichhla node set karna
        prev = pet;
    }
}