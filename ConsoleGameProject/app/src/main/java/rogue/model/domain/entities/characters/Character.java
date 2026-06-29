package rogue.model.domain.entities.characters;

import rogue.model.domain.entities.ObjectT;

public class Character extends ObjectT {
    private double actPowers;
    private int relationQuality;
    private int impulsePower;

    public Character(ObjectT coords, double actPowers, int relationQuality, int impulsePower) {
        super(coords);
        this.actPowers = actPowers;
        this.relationQuality = relationQuality;
        this.impulsePower = impulsePower;
    }

    public double getActPowers() {
        return actPowers;
    }

    public void setActPowers(double actPowers) {
        this.actPowers = actPowers;
    }

    public int getRelationQuality() {
        return relationQuality;
    }

    public void setRelationQuality(int relationQuality) {
        this.relationQuality = relationQuality;
    }

    public int getImpulsePower() {
        return impulsePower;
    }

    public void setImpulsePower(int impulsePower) {
        this.impulsePower = impulsePower;
    }

}
