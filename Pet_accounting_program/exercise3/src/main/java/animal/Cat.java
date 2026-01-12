package animal;

public class Cat extends Animal implements Omnivore {
    public Cat(String name, int age) {
	    super(name, age);
    }

    @Override
    public String toString() {
        return String.format("Cat name = %s, age = %d. ", getName(),  getAge()) + hunt();
    }

    public String hunt() {
        return "I can hunt for mice";
    }
}
