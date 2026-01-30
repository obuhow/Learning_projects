package rogue.model.domain.entities;

public class Character {
    private ObjectT coords;
    private double health;
    private int agility;
    private int strength;

    public Character(ObjectT coords, double health, int agility, int strength) {
        this.coords = coords;
        this.health = health;
        this.agility = agility;
        this.strength = strength;
    }

    public ObjectT getCoords() {
        return coords;
    }

    public void setCoords(ObjectT coords) {
        this.coords = coords;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}
