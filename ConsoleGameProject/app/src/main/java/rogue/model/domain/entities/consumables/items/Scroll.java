package rogue.model.domain.entities.consumables.items;

import rogue.model.domain.enums.ConsumableTypes;
import rogue.model.domain.enums.StatType;

public class Scroll extends Item {
    private StatType stat;
    private int increase;

    public Scroll(String name, StatType stat, int increase) {
        super(name);
        this.stat = stat;
        this.increase = increase;
    }

    public StatType getStat() {
        return stat;
    }

    public void setStat(StatType stat) {
        this.stat = stat;
    }

    public int getIncrease() {
        return increase;
    }

    public void setIncrease(int increase) {
        this.increase = increase;
    }
    
    @Override
    public ConsumableTypes getType() {
        return ConsumableTypes.SCROLL;
    }
}
