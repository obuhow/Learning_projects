package animal;

public abstract class Animal {
    private String name;
    private int age;

    public Animal(String name, int age) {
        this.name = name;
	    this.age = incrementedAge(age);
    }

    public String getName() {
	    return name;
    }

    public int getAge() {
	    return age;
    }

    private int incrementedAge(int age) {
        if (age > 10) age += 1;
        return age;
    }
}
