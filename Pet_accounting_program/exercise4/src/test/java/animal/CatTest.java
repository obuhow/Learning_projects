package animal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatTest {
    private Cat tenYearsOldCat;
    private Cat moreThenTenYearsOldCat;

    @BeforeEach
    void setUp() {
        tenYearsOldCat = new Cat("Teeny", 10);
        moreThenTenYearsOldCat = new Cat("Mommy", 11);
    }

    @Test
    void testCatCreationTenOrLessYearsOld() {
        assertEquals("Teeny", tenYearsOldCat.getName());
        assertEquals(10, tenYearsOldCat.getAge());
    }

    @Test
    void testCatCreationMoreThenTenYearsOld() {
        assertEquals("Mommy", moreThenTenYearsOldCat.getName());
        assertEquals(12, moreThenTenYearsOldCat.getAge());
    }

    @Test
    void testToString() {
        String expected = "Cat name = Mommy, age = 12";
        assertEquals(expected, moreThenTenYearsOldCat.toString());
    }
}