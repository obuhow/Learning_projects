package rogue.model.domain.entities;

public class Monster {
    private Character baseStats;
    private MonsterType type;
    private HostilityType hostility;
    private boolean isChasing;
    private Directions dir;

    public Monster(Character baseStats, MonsterType type, HostilityType hostility, 
                     boolean isChasing, Directions dir) {
        this.baseStats = baseStats;
        this.type = type;
        this.hostility = hostility;
        this.isChasing = isChasing;
        this.dir = dir;
    }

    public Character getBaseStats() {
        return baseStats;
    }

    public void setBaseStats(Character baseStats) {
        this.baseStats = baseStats;
    }

    public MonsterType getType() {
        return type;
    }

    public void setType(MonsterType type) {
        this.type = type;
    }

    public HostilityType getHostility() {
        return hostility;
    }

    public void setHostility(HostilityType hostility) {
        this.hostility = hostility;
    }

    public boolean isChasing() {
        return isChasing;
    }

    public void setChasing(boolean chasing) {
        this.isChasing = chasing;
    }

    public Directions getDir() {
        return dir;
    }

    public void setDir(Directions dir) {
        this.dir = dir;
    }
}
