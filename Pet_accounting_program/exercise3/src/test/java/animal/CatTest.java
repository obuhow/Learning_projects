package animal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatTest {
    private Cat cat;

    @BeforeEach
    void setUp() {
        cat = new Cat("Jessica", 2);
    }

    @Test
    void testCatCreation() {
        assertEquals("Jessica", cat.getName());
        assertEquals(2, cat.getAge());
    }

    @Test
    void testToString() {
        String expected = "Cat name = Jessica, age = 2. I can hunt for mice";
        assertEquals(expected, cat.toString());
    }

    @Test
    void testHunt() {
        String expected = "I can hunt for mice";
        assertEquals(expected, cat.hunt());
    }

    @Test
    void testImplementsOmnivore() {
        assertTrue(cat instanceof Omnivore);
    }
}