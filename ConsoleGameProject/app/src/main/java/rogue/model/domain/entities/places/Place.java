package rogue.model.domain.entities.places;

import java.util.ArrayList;
import java.util.List;

import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.consumables.ItemRoom;
import rogue.model.domain.entities.consumables.items.Item;

import lombok.extern.slf4j.Slf4j;
import lombok.Data;

@Data
public class Place {
    private List<Item> items = new ArrayList<>();
    private ObjectT geometry;

    public Place(Item item, Room room) {
        items.add(item);
        geometry = room.getRandomFreeCellInRoom();
    }

    public Place(List<Item> items, ObjectT geometry) {
        this.items = items;
        this.geometry = geometry;
    }

    public boolean addItems(Item item) {
        return items.add(item);
    }
}
