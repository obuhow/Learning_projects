package animal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DogTest {
    private Dog dog;

    @BeforeEach
    void setUp() {
        dog = new Dog("Jacobe", 4);
    }

    @Test
    void testDogCreation() {
        assertEquals("Jacobe", dog.getName());
        assertEquals(4, dog.getAge());
    }

    @Test
    void testToString() {
        String expected = "Dog name = Jacobe, age = 4. I can hunt for robbers";
        assertEquals(expected, dog.toString());
    }

    @Test
    void testHunt() {
        assertEquals("I can hunt for robbers", dog.hunt());
    }

    @Test
    void testImplementsOmnivore() {
        assertTrue(dog instanceof Omnivore);
    }
}