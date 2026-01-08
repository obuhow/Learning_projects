package task3;

import animal.Animal;
import animal.Dog;
import animal.Cat;
import utils.PScanner;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;

public class Main {
    private static PScanner scanner;

    public static void main(String[] args) {
        List<Animal> pets;
        int numberOfPets;
	    boolean needRetry;
	    scanner = new PScanner(System.in);
	    do {
	        try {
                numberOfPets = scanner.nextPositiveInt();
                pets = scanPetsList(numberOfPets);
                needRetry = false;
                pets.iterator().forEachRemaining(System.out::println);
	        } catch (InputMismatchException e) {
		        System.err.println(e.getMessage() + ". Please try again");
                needRetry = true;
	        }
	    } while(needRetry);
    }

    public static List<Animal> scanPetsList(int numberOfPets) {
        List<Animal> pets = new ArrayList<>();

	    for (int i = 0; i < numberOfPets; i++) {
	        try {
                pets.add(scanPet());
	        } catch (InputMismatchException e) {
                System.err.println(e.getMessage());
	        }
	    }
	    return pets;	
    }

    public static Animal scanPet() throws InputMismatchException {
        Animal theAnimal = null;
	    String name;
	    int age;
	    double mass;
	
	    if (scanner.hasNext()) {
            switch (scanner.next()) {
                case "dog": 
                    name = scanner.next();
                    age = scanner.nextPositiveInt();
                    mass = scanner.nextPositiveDouble();
                    theAnimal = new Dog(name, age, mass);
                    break;
                case "cat":
                    name = scanner.next();
                    age = scanner.nextPositiveInt();
                    mass = scanner.nextPositiveDouble();
                    theAnimal = new Cat(name, age, mass);
                    break;
                default: throw new InputMismatchException("Incorrect input. Unsupported pet type");
	        }
	    }
	    return theAnimal;
    }
}
