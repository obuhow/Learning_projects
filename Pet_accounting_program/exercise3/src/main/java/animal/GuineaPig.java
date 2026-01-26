package animal;

public class GuineaPig extends Animal implements Herbivore {
    public GuineaPig(String name, int age) {
        super(name, age);
    }

    @Override
    public String toString() {
        return String.format("GuineaPig name = %s, age = %d. ", getName(), getAge()) + chill();
    }

    public String chill() {
        return "I can chill for 12 hours";
    }
}