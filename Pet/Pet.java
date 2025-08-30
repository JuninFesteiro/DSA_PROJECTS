package Pet;

public abstract class Pet {
    private PetManager petManager;
    private int petId;
    private String petName;
    private boolean petForSale;
    private int petAge;
    private double petPrice;
    private int petHunger;
    private int petHealth;
    private int petMood;
    private String petType;

    // Updated existing constructor
    public Pet(int petId, String petName, boolean petForSale, int petAge, double petPrice, int health, int mood, int hunger, String petType, PetManager petManager) {
        this.petId = petId;
        this.petName = petName;
        this.petForSale = petForSale;
        this.petAge = petAge;
        this.petPrice = petPrice;
        this.petHunger = hunger;
        this.petHealth = health;
        this.petMood = mood;
        this.petType = petType;
        this.petManager = petManager;
    }

    // Updated fixed constructor for subclasses
    public Pet(int petId, String petName, String petType, boolean petForSale, int petAge, double petPrice, int health, int mood, int hunger) {
        this.petId = petId;
        this.petName = petName;
        this.petForSale = petForSale;
        this.petAge = petAge;
        this.petPrice = petPrice;
        this.petHunger = hunger;
        this.petHealth = health;
        this.petMood = mood;
        this.petType = petType;
        this.petManager = null;
    }

    // Getters
    public PetManager getPetManager() {
        return petManager;
    }

    public int getPetId() { return petId; }
    public String getPetName() { return petName; }
    public boolean getPetForSale() { return petForSale; }
    public int getPetAge() { return petAge; }
    public double getPetPrice() { return petPrice; }
    public int getPetHunger() { return petHunger; }
    public int getPetHealth() { return petHealth; }
    public int getPetMood() { return petMood; }
    public String getPetType() { return petType; }

    // Setters
    public void setPetHunger(int hunger) {
        this.petHunger = Math.max(0, Math.min(100, hunger));
    }
    public void setPetHealth(int health) {
        this.petHealth = Math.max(0, Math.min(100, health));
    }
    public void setPetMood(int mood) {
        this.petMood = Math.max(0, Math.min(100, mood));
    }

    // Methods
    public void feedPet() {
        setPetHunger(petHunger - 10);
        System.out.println(petName + " has been fed.");
        if (petManager != null) {
            petManager.savePetsToFile("pets.txt");
        }
    }

    public void playWithPet() {
        setPetMood(petMood + 10);
        System.out.println(petName + " is happy after playing.");
        if (petManager != null) {
            petManager.savePetsToFile("pets.txt");
        }
    }

    public void healPet() {
        setPetHealth(petHealth + 10);
        System.out.println(petName + " has been healed.");
        if (petManager != null) {
            petManager.savePetsToFile("pets.txt");
        }
    }

    public void simulateDailyNeeds() {
        setPetHunger(petHunger + 5);
        setPetHealth(petHealth - 5);
        setPetMood(petMood - 5);
        System.out.println(petName + " daily needs updated: Hunger=" + petHunger + ", Health=" + petHealth + ", Mood=" + petMood);
    }

    public abstract void eat();
    public abstract void play();
    public abstract void heal();

    public String toFileString() {
        return petId + "," + petName + "," + petForSale + "," + petAge + "," + petPrice + "," +
               petHunger + "," + petHealth + "," + petMood + "," + petType;
    }

    public static Pet fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 9) return null;
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        boolean forSale = Boolean.parseBoolean(parts[2]);
        int age = Integer.parseInt(parts[3]);
        double price = Double.parseDouble(parts[4]);
        int hunger = Integer.parseInt(parts[5]);
        int health = Integer.parseInt(parts[6]);
        int mood = Integer.parseInt(parts[7]);
        String type = parts[8];

        Pet pet;
        switch (type.toLowerCase()) {
            case "dog":
                pet = new Dog(id, name, forSale, age, price, health, mood, hunger);
                break;
            case "rabbit":
                pet = new Rabbit(id, name, forSale, age, price, health, mood, hunger);
                break;
            case "horse":
                pet = new Horse(id, name, forSale, age, price, health, mood, hunger);
                break;
            case "lion":
                pet = new Lion(id, name, forSale, age, price, health, mood, hunger);
                break;
            default:
                return null;
        }
        return pet;
    }

    @Override
    public String toString() {
        return "Pet [ID=" + petId + ", Name=" + petName + ", For Sale=" + petForSale +
               ", Age=" + petAge + ", Price=$" + petPrice + ", Hunger=" + petHunger +
               ", Health=" + petHealth + ", Mood=" + petMood + ", Type=" + petType + "]";
    }
}