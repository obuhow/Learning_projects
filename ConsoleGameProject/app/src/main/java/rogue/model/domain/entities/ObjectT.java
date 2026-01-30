package rogue.model.domain.entities;

public class ObjectT {
    private int[] coordinates;
    private int[] size;

    public ObjectT(int[] coordinates, int[] size) {
        this.coordinates = coordinates;
        this.size = size;
    }

    public void set_coordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public void set_size(int[] size) {
        this.size = size;
    }

    public int[] get_coordinates() {
        return coordinates;
    }

    public int[] get_size() {
        return size;
    }

    
}
