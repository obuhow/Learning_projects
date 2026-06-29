package rogue.model.domain.entities;

import rogue.model.domain.entities.characters.communicators.Communicator;

public class BattleInfo {
    private boolean is_fight; 
    private Communicator enemy;
    private boolean student_first_talkTo;
    private boolean tractor_cooldown; 
    private boolean player_asleep;

    public static final int MAXIMUM_FIGHTS = 8;
    
    public BattleInfo() {
        this(false,
            null,
            true,
            false,
            true
        );
    }

    public BattleInfo(boolean is_fight, Communicator enemy, boolean student_first_talkTo, boolean tractor_cooldown,
            boolean player_asleep) {
        this.is_fight = is_fight;
        this.enemy = enemy;
        this.student_first_talkTo = student_first_talkTo;
        this.tractor_cooldown = tractor_cooldown;
        this.player_asleep = player_asleep;
    }

    public boolean isFight() {
        return is_fight;
    }

    public void setIsFight(boolean is_fight) {
        this.is_fight = is_fight;
    }

    public Communicator getEnemy() {
        return enemy;
    }

    public void setEnemy(Communicator enemy) {
        this.enemy = enemy;
    }

    public boolean isStudentFirstAttack() {
        return student_first_talkTo;
    }

    public void setStudentFirstAttack(boolean student_first_talkTo) {
        this.student_first_talkTo = student_first_talkTo;
    }

    public boolean isTractorCooldown() {
        return tractor_cooldown;
    }

    public void setTractorCooldown(boolean tractor_cooldown) {
        this.tractor_cooldown = tractor_cooldown;
    }

    public boolean isPlayerAsleep() {
        return player_asleep;
    }

    public void setPlayerAsleep(boolean player_asleep) {
        this.player_asleep = player_asleep;
    }
}
