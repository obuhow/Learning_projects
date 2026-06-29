package rogue.model.domain.entities;

import java.util.Objects;
import java.util.Random;

import rogue.model.domain.enums.Dimention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectT {
    protected int[] coordinates;
    protected int[] sizes;

    private static final Logger log = LoggerFactory.getLogger(ObjectT.class);

    public ObjectT() {
        this(new int[]{20, 20}, new int[]{1,1});
    }

    public ObjectT(int[] coordinates) {
        this(coordinates, new int[]{1,1});
    }

    public ObjectT(ObjectT other) {
        this.coordinates = other.coordinates;
        this.sizes = other.sizes;
    }

    public ObjectT(int[] coordinates, int[] size) {
        this.coordinates = coordinates;
        this.sizes = size;
    }

    public static ObjectT generateEntityGeometry(Room room) {
        int rx = room.getCoordinate(Dimention.X.ordinal());
        int ry = room.getCoordinate(Dimention.Y.ordinal());
        int rw = room.getSize(Dimention.X.ordinal());
        int rh = room.getSize(Dimention.Y.ordinal());
        
        Random random = new Random();
        int[] coordinates = new int[] {rx + random.nextInt(1, rw - 3), ry + random.nextInt(1, rh - 3)};
        return new ObjectT(coordinates, new int[] {1, 1});
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (Objects.isNull(o) || getClass() != o.getClass()) return false;
        
        ObjectT other = (ObjectT) o;
        return (coordinates[Dimention.X.ordinal()] == other.getCoordinates()[Dimention.X.ordinal()])
            && (coordinates[Dimention.Y.ordinal()] == other.getCoordinates()[Dimention.Y.ordinal()]);
    }

    @Override
    public int hashCode() {
    return Objects.hash(coordinates[Dimention.X.ordinal()], 
                        coordinates[Dimention.Y.ordinal()]);
}

    public void setCoordinate(int i, int value) {
        this.coordinates[i] = value;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public void setSizes(int[] sizes) {
        this.sizes = sizes;
    }
    public void setSize(int i, int value) {
        this.sizes[i] = value;
    }

    public int getCoordinate(int i) {
        return coordinates[i];
    }

    public int getSize(int i) {
        return sizes[i];
    }

    public int[] getCoordinates() {
        return coordinates;
    }
    public int[] getSizes() {
        return sizes;
    }

    public static boolean isInSpace(int x1, int y1, ObjectT space) {
        if (space == null) return false;
        boolean result = true;

        int x = space.getCoordinate(Dimention.X.ordinal());
        int y = space.getCoordinate(Dimention.Y.ordinal());
        int w = space.getSize(Dimention.X.ordinal());
        int h = space.getSize(Dimention.Y.ordinal());

        if (x1 <= x || x1 >= x + w || y1 <= y || y1 >= y + h ) result = false;

        return result;
    }
}
