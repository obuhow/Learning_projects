package rogue.model.domain.entities.consumables.items;

import rogue.model.domain.enums.ConsumableTypes;

public class Food extends Item{
    private int toRegen;

    public Food(String name, int toRegen) {
        super(name);
        this.toRegen = toRegen;
    }

     public int getToRegen() {
        return toRegen;
    }

    public void setToRegen(int toRegen) {
        this.toRegen = toRegen;
    }

    @Override
    public ConsumableTypes getType() {
        return ConsumableTypes.FOOD;
    }
}
