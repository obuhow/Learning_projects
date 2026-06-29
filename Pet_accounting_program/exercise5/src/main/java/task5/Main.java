package task5;

import animal.Animal;
import animal.Dog;
import animal.Cat;
import utils.PScanner;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.lang.InterruptedException;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.Objects;

public class Main {
    private static PScanner scanner;

    public static void main(String[] args) {
        final double programStartTime = System.currentTimeMillis() / 1000.0;
        List<Animal> pets;
        int numberOfPets;
	    scanner = new PScanner(System.in);

        try {
            numberOfPets = scanner.nextPositiveInt();
            pets = IntStream.range(0, numberOfPets).mapToObj(pet -> scanPet()).collect(Collectors.toList());

            pets.parallelStream().filter(Objects::nonNull).forEach(pet -> { 
                double startTime = System.currentTimeMillis() / 1000.0 - programStartTime;
                double durationWalk = pet.goToWalk();
                double endTime = System.currentTimeMillis() / 1000.0 - programStartTime;
                System.out.println(String.format("%s, start time = %.2f, end time = %.2f", pet, startTime, endTime));
                 }
                );
        } catch (InputMismatchException e) {
	        System.err.println(e.getMessage() + ". Please try again");
        }
    }

    public static Animal scanPet() {
	    Animal theAnimal = null;
        String name;
	    int age;
        try {
            if (scanner.hasNext()) {
                switch (scanner.next()) {
                    case "dog": 
                        name = scanner.next();
                        age = scanner.nextPositiveInt();
                        theAnimal = new Dog(name, age);
                        break;
                    case "cat":
                        name = scanner.next();
                        age = scanner.nextPositiveInt();
                        theAnimal = new Cat(name, age);
                        break;
                    default: System.out.println("Incorrect input. Unsupported pet type");
                }
            }
        } catch (InputMismatchException e) {
            System.err.println(e.getMessage() + ". Please try again");
        }
	    return theAnimal;
    }
}
