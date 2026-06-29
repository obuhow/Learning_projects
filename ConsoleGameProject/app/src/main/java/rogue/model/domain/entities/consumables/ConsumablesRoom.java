package rogue.model.domain.entities.consumables;


import java.util.ArrayList;
import java.util.List;

import rogue.model.domain.entities.consumables.items.*;
import rogue.model.domain.enums.Dimention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumablesRoom {
    private List<ItemRoom<? extends Item>> items = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(ConsumablesRoom.class);

    public ConsumablesRoom() {
        this.items = new ArrayList<>();
    }

    public ConsumablesRoom(List<ItemRoom<? extends Item>> items) {
        this.items = items;
    }

    public List<ItemRoom<? extends Item>> getItems() {
        if (items == null) return null; 
        return items;
    }

    public void setItems(List<ItemRoom<? extends Item>> items) {
        this.items = items;
    }

    public boolean addItems(ItemRoom<? extends Item> item) {
        return items.add(item);
    }

    public ItemRoom<?> getItemAt(int x, int y) {
        ItemRoom<?> itemRoom = null;
        for (ItemRoom<?> i : items) if (i != null && i.getCoordinates().getCoordinate(Dimention.X.ordinal()) == x 
            && i.getCoordinates().getCoordinate(Dimention.Y.ordinal()) == y) itemRoom = i;
        return itemRoom;
    }

    public boolean removeItem(ItemRoom<?> item) {
        boolean result = false;
        try {
            result = items.remove(item);
        } catch (Error | Exception e ) {
            e.printStackTrace();
        }
        return result;
    }
}
