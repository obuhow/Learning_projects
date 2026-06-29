package rogue.model.domain.entities.consumables.items;

import rogue.model.domain.enums.ConsumableTypes;

public class Item {
    private String name;

    public Item() {
        this.name = "";
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    };

    public void setName(String name) {
        this.name = name;
    };

    public ConsumableTypes getType() {
        return null;
    }
}
