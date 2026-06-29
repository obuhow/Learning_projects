package rogue.model.domain.entities.consumables.items;

import rogue.model.domain.enums.ConsumableTypes;

public class Weapon extends Item {
    private int impulsePower;

    public static final int NO_WEAPON = 0;

    public Weapon(String name, int impulsePower) {
        super(name);
        this.impulsePower = impulsePower;
    }

    public int getImpulsePower() {
        return impulsePower;
    }

    public void setImpulsePower(int impulsePower) {
        this.impulsePower = impulsePower;
    }

    @Override
    public ConsumableTypes getType() {
        return ConsumableTypes.WEAPON;
    }
}
