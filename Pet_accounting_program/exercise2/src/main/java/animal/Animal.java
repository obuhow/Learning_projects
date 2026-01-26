package animal;

public abstract class Animal {
    private String name;
    private int age;
    private double mass;

    public Animal(String name, int age, double mass) {
        this.name = name;
        this.age = age;
	    this.mass = mass;
    }

    public String getName() {
	    return name;
    }

    public int getAge() {
	    return age;
    }

    public double getMass() {
	    return mass;
    }
    
    public double getFeedInfoKg() {
        return mass;
    }
}
