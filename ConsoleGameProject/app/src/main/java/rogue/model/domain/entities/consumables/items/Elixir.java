package rogue.model.domain.entities.consumables.items;

import java.time.Duration;

import rogue.model.domain.enums.ConsumableTypes;
import rogue.model.domain.enums.StatType;

public class Elixir extends MagicItem {
    private Duration duration;

    public Elixir(String name, StatType stat, int increase, Duration duration) {
        super(name, stat, increase);
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public ConsumableTypes getType() {
        return ConsumableTypes.ELIXIR;
    }
}
