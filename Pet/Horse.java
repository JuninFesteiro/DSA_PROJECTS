package Pet;

public class Horse extends Pet {

    public Horse(int id, String name, boolean isForSale, int age, double price, int health, int mood, int hunger) {
        super(id, name, "horse", isForSale, age, price, health, mood, hunger);
    }

    @Override
    public void eat() {
        setPetHunger(getPetHunger() - 12);
        System.out.println(getPetName() + "(Horse) ate food. Hunger reduced to: " + getPetHunger());
    }

    @Override
    public void play() {
        setPetMood(getPetMood() + 15);
        System.out.println(getPetName() + "(Horse) Plays mood increased: " + getPetMood());
    }

    @Override
    public void heal() {
        setPetHealth(getPetHealth() + 18);
        System.out.println(getPetName() + "(Horse) health increased: " + getPetHealth());
    }
}