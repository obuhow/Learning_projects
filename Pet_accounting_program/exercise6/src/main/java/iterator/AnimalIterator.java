package iterator;

import java.util.List;
import animal.Animal;

public class AnimalIterator implements BaseIterator {
    private List<Animal> animalList;
    private int elementNumber;

    public AnimalIterator(List<Animal> animalList) {
        this.animalList = animalList;
        this.elementNumber = 0;
    }

    public Animal next() {
        return animalList.get(elementNumber++);
    }

    public boolean hasNext() {
        return elementNumber < animalList.size();
    }

    public void reset() {
        elementNumber = 0;
    }
}