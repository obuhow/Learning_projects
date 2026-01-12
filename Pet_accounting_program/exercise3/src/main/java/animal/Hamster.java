package animal;

public class Hamster extends Animal implements Herbivore {
  
    public Hamster(String name, int age) {
        super(name, age);
    }

    @Override
    public String toString() {
        return String.format("Hamster name = %s, age = %d. ", getName(), getAge()) + chill();
    }

    public String chill() {
        return "I can chill for 8 hours";
    }
}