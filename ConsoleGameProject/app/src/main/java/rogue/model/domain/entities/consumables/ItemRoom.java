package rogue.model.domain.entities.consumables;

import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.consumables.items.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemRoom<T extends Item> {
    private T item;
    private ObjectT geometry;

    private static final Logger log = LoggerFactory.getLogger(ItemRoom.class);

    public ItemRoom() {
        this.item = null;
        this.geometry = null;
    }

    public ItemRoom(T item, ObjectT geometry) {
        this.item = item;
        this.geometry = geometry;
    }

    public ItemRoom(T item, Room room) {
        this.item = item;
        this.geometry = room.getRandomFreeCellInRoom();
    }
    
    public Item getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public ObjectT getCoordinates() {
        return geometry;
    }

    public void setCoordinates(ObjectT geometry) {
        this.geometry = geometry;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return this == o || geometry.equals(((ItemRoom<?>)o).getCoordinates());
    }
}
