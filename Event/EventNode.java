 package Event;
 // New inner class for doubly linked list node
    public class EventNode { // Doubly linked list node define karta hai

        Event event; // Event object store karta hai
        EventNode prev; // Previous node ka reference
        EventNode next; // Next node ka reference

        EventNode(Event event) { // Constructor banata hai
            this.event = event; // Event set karta hai
            this.prev = null; // Previous ko null set karta hai
            this.next = null; // Next ko null set karta hai
        }
    }
