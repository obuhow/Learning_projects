package animal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DogTest {
    private Dog tenYearsOldDog;
    private Dog moreThenTenYearsOldDog;

    @BeforeEach
    void setUp() {
        tenYearsOldDog = new Dog("Pigeon", 10);
        moreThenTenYearsOldDog = new Dog("Daddy", 11);
    }

    @Test
    void testCatCreationTenOrLessYearsOld() {
        assertEquals("Pigeon", tenYearsOldDog.getName());
        assertEquals(10, tenYearsOldDog.getAge());
    }

    @Test
    void testCatCreationMoreThenTenYearsOld() {
        assertEquals("Daddy", moreThenTenYearsOldDog.getName());
        assertEquals(12, moreThenTenYearsOldDog.getAge());
    }

    @Test
    void testToString() {
        String expected = "Dog name = Daddy, age = 12";
        assertEquals(expected, moreThenTenYearsOldDog.toString());
    }
}