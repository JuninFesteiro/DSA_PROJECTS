package Pet;

public class Lion extends Pet {

    public Lion(int id, String name, boolean isForSale, int age, double price, int health, int mood, int hunger) {
        super(id, name, "lion", isForSale, age, price, health, mood, hunger);
    }

    @Override
    public void eat() {
        setPetHunger(getPetHunger() - 15);
        System.out.println(getPetName() + "(Lion) ate food. Hunger reduced to: " + getPetHunger());
    }

    @Override
    public void play() {
        setPetMood(getPetMood() + 8);
        System.out.println(getPetName() + "(Lion) Plays mood increased: " + getPetMood());
    }

    @Override
    public void heal() {
        setPetHealth(getPetHealth() + 20);
        System.out.println(getPetName() + "(Lion) health increased: " + getPetHealth());
    }
}