package rogue.model.domain.entities.buff;

import java.util.ArrayList;

public class Buffs {
    private ArrayList<Buff> maxActPowers;
    private int currentActPowersBuffNum;

    private ArrayList<Buff> relationQuality;
    private int currentRelationQualityBuffNum;

    private ArrayList<Buff> impulsePower;
    private int currentImpulsePowerBuffNum;

    public final int CONSUMABLES_TYPE_MAX_NUM = 9;

    public Buffs() {
        this.maxActPowers = new ArrayList<Buff>(CONSUMABLES_TYPE_MAX_NUM);
        this.currentActPowersBuffNum = 0;
        this.relationQuality = new ArrayList<Buff>(CONSUMABLES_TYPE_MAX_NUM);
        this.currentRelationQualityBuffNum = 0;
        this.impulsePower = new ArrayList<Buff>(CONSUMABLES_TYPE_MAX_NUM);
        this.currentImpulsePowerBuffNum = 0;
    }
    
    public Buffs(ArrayList<Buff> maxActPowers, int currentActPowersBuffNum, ArrayList<Buff> relationQuality, int currentRelationQualityBuffNum, ArrayList<Buff> impulsePower,
            int currentImpulsePowerBuffNum) {
        this.maxActPowers = maxActPowers;
        this.currentActPowersBuffNum = currentActPowersBuffNum;
        this.relationQuality = relationQuality;
        this.currentRelationQualityBuffNum = currentRelationQualityBuffNum;
        this.impulsePower = impulsePower;
        this.currentImpulsePowerBuffNum = currentImpulsePowerBuffNum;
    }

    public ArrayList<Buff> getMaxActPowers() {
        return maxActPowers;
    }

    public void setMaxActPowers(ArrayList<Buff> maxActPowers) {
        this.maxActPowers = maxActPowers;
    }

    public int getCurrentActPowersBuffNum() {
        return currentActPowersBuffNum;
    }

    public void setCurrentActPowersBuffNum(int currentActPowersBuffNum) {
        this.currentActPowersBuffNum = currentActPowersBuffNum;
    }

    public ArrayList<Buff> getRelationQuality() {
        return relationQuality;
    }

    public void setRelationQuality(ArrayList<Buff> relationQuality) {
        this.relationQuality = relationQuality;
    }

    public int getCurrentRelationQualityBuffNum() {
        return currentRelationQualityBuffNum;
    }

    public void setCurrentRelationQualityBuffNum(int currentRelationQualityBuffNum) {
        this.currentRelationQualityBuffNum = currentRelationQualityBuffNum;
    }

    public ArrayList<Buff> getImpulsePower() {
        return impulsePower;
    }

    public void setImpulsePower(ArrayList<Buff> impulsePower) {
        this.impulsePower = impulsePower;
    }

    public int getCurrentImpulsePowerBuffNum() {
        return currentImpulsePowerBuffNum;
    }

    public void setCurrentImpulsePowerBuffNum(int currentImpulsePowerBuffNum) {
        this.currentImpulsePowerBuffNum = currentImpulsePowerBuffNum;
    }

    
}
