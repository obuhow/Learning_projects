package animal;

import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;

public class Dog extends Animal {	
    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public String toString() {
        return "Dog name = " + getName() + ", age = " + getAge();
    }

    @Override
    public double goToWalk() {
        long walkDuration = getAge() * 500;
        try {
            TimeUnit.MILLISECONDS.sleep(walkDuration);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return walkDuration / 1000.0;
    }	    
}


