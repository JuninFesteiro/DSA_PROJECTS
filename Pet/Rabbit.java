package Pet;

public class Rabbit extends Pet {

    public Rabbit(int id, String name, boolean isForSale, int age, double price, int health,int mood,int hunger) {
        super(id, name, "rabbit", isForSale, age, price, health, mood, hunger);
    }

    @Override
    public void eat() {
        setPetHunger(getPetHunger() - 8);
        System.out.println(getPetName() + "(Rabbit) ate food. Hunger reduced to: " + getPetHunger());
    }

    @Override
    public void play() {
        setPetMood(getPetMood() + 10);
        System.out.println(getPetName() + "(Rabbit) Plays mood increased: " + getPetMood());
    }

    @Override
    public void heal() {
        setPetHealth(getPetHealth() + 14);
        System.out.println(getPetName() + "(Rabbit) health increased: " + getPetHealth());
    }
}