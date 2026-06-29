package task3;

import animal.Animal;
import animal.Dog;
import animal.Cat;
import animal.Hamster;
import animal.GuineaPig;
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
                case "hamster": 
                    name = scanner.next();
                    age = scanner.nextPositiveInt();
                    theAnimal = new Hamster(name, age);
                    break;
                case "guinea":
                    name = scanner.next();
                    age = scanner.nextPositiveInt();
                    theAnimal = new GuineaPig(name, age);
                    break;
                default: throw new InputMismatchException("Incorrect input. Unsupported pet type");
	        }
	    }
	    return theAnimal;
    }
}
