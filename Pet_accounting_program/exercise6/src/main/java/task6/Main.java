package task6;

import animal.Animal;
import animal.Dog;
import animal.Cat;
import iterator.AnimalIterator;
import utils.PScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.Objects;

public class Main {
    private static PScanner scanner;

    public static void main(String[] args) {
        List<Animal> pets;
        int numberOfPets;
        scanner = new PScanner(System.in);
	
        try {
            numberOfPets = scanner.nextPositiveInt();
            pets = IntStream.range(0, numberOfPets).mapToObj(pet -> scanPet()).filter(Objects::nonNull).collect(Collectors.toList());

            AnimalIterator animalIterator = new AnimalIterator(pets);
            while (animalIterator.hasNext()) {
                System.out.println(animalIterator.next());
            }
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
