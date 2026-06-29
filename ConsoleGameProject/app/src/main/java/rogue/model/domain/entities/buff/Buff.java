package rogue.model.domain.entities.buff;

import java.time.Duration;
import java.time.Instant;

public class Buff {
    private int statIncrease;
    private Duration effectDuration;
    private Instant endTime;
    private String name;

    public Buff(int statIncrease, Duration effectDuration) {
        this.statIncrease = statIncrease;
        this.effectDuration = effectDuration;
        this.endTime = Instant.now().plusSeconds(effectDuration.getSeconds());
        this.name = "Шаблон";
    }

    public int getStatIncrease() {
        return statIncrease;
    }
    
    public void setStatIncrease(int statIncrease) {
        this.statIncrease = statIncrease;
    }

    public Duration getEffectEnd() {
        return effectDuration;
    }

    public void setEffectEnd(Duration effectDuration) {
        this.effectDuration = effectDuration;
    }

    public Duration getEffectDuration() {
        return effectDuration;
    }

    public void setEffectDuration(Duration effectDuration) {
        this.effectDuration = effectDuration;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(endTime);
    }
}
