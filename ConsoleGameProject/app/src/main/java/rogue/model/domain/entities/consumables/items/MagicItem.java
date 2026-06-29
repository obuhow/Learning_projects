package rogue.model.domain.entities.consumables.items;

import rogue.model.domain.enums.StatType;

public class MagicItem extends Item {
    private StatType stat;
    private int increase;

    public MagicItem(String name, StatType stat, int increase) {
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
}

