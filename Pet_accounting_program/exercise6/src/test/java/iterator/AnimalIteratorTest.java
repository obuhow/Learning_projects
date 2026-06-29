package iterator;

import animal.Animal;
import animal.Dog;
import animal.Cat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AnimalIteratorTest {
    AnimalIterator animalIterator;
    private List<Animal> pets;
    private Dog dog;
    private Cat cat;

    @BeforeEach
    public void setUp() {
        dog = new Dog("Gello", 3);
        cat = new Cat("Pejo", 1); 
        pets = new ArrayList<>();
        pets.add(dog);
        pets.add(cat);
        animalIterator = new AnimalIterator(pets);
    }

    @Test
    public void nextTest() {
        assertEquals(dog, animalIterator.next());
        assertEquals(cat, animalIterator.next());
    }

    @Test 
    void hasNext() {
        assertTrue(animalIterator.hasNext());
        animalIterator.next();
        assertTrue(animalIterator.hasNext());
        animalIterator.next();
        assertFalse(animalIterator.hasNext());
    }

    @Test 
    void reset() {
        assertEquals(dog, animalIterator.next());
        animalIterator.reset();
        assertEquals(dog, animalIterator.next());
        assertEquals(cat, animalIterator.next());
        animalIterator.reset();
        assertEquals(dog, animalIterator.next());
    }

}