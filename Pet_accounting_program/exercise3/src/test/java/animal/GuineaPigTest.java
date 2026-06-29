package animal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuineaPigTest {
    private GuineaPig guineaPig;

    @BeforeEach
    void setUp() {
        guineaPig = new GuineaPig("Nif-Niffy", 6);
    }

    @Test
    void testGuineaPigCreation() {
        assertEquals("Nif-Niffy", guineaPig.getName());
        assertEquals(6, guineaPig.getAge());
    }

    @Test
    void testToString() {
        String expected = "GuineaPig name = Nif-Niffy, age = 6. I can chill for 12 hours";
        assertEquals(expected, guineaPig.toString());
    }

    @Test
    void testChill() {
        String expected = "I can chill for 12 hours";
        assertEquals(expected, guineaPig.chill());
    }

    @Test
    void testImplementsHerbivore() {
        assertTrue(guineaPig instanceof Herbivore);
    }
}