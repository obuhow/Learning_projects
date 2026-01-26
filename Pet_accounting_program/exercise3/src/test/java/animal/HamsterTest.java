package animal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HamsterTest {
    private Hamster hamster;

    @BeforeEach
    void setUp() {
        hamster = new Hamster("Buffy", 1);
    }

    @Test
    void testHamsterCreation() {
        assertEquals("Buffy", hamster.getName());
        assertEquals(1, hamster.getAge());
    }

    @Test
    void testToString() {
        String expected = "Hamster name = Buffy, age = 1. I can chill for 8 hours";
        assertEquals(expected, hamster.toString());
    }

    @Test
    void testChill() {
        String expected = "I can chill for 8 hours";
        assertEquals(expected, hamster.chill());
    }

    @Test
    void testImplementsHerbivore() {
        assertTrue(hamster instanceof Herbivore);
    }
}