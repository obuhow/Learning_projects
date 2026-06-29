package rogue.model.domain.entities.characters.communicators;

import rogue.model.domain.entities.Level;
import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.characters.Player;
import rogue.model.domain.enums.Directions;
import rogue.model.domain.enums.AttractivenessType;
import rogue.model.domain.enums.CommunicatorType;
import rogue.model.domain.utils.CombatResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Student extends Communicator {
    private boolean firstAttackDone;
    
    private static final Logger log = LoggerFactory.getLogger(Student.class);

    public Student() {
        super(new ObjectT(), 50.0, 75, 25, AttractivenessType.HIGH, false, Directions.STOP);
        firstAttackDone = false;
    }

    public Student(Room room) {
        super(room.getRandomFreeCellInRoom(), 50.0, 75, 125, AttractivenessType.HIGH, false, Directions.STOP);
        firstAttackDone = false;
    }

    public Student(ObjectT coords, double actPowers, int relationQuality, int impulsePower, AttractivenessType attractiveness,
                   boolean isChasing, Directions dir) {
        super(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
        firstAttackDone = false;
    }

    public boolean isFirstAttackDone() {
        return firstAttackDone;
    }

    public void setFirstAttackDone(boolean firstAttackDone) {
        this.firstAttackDone = firstAttackDone;
    }

    @Override
    public CommunicatorType getType() {
        return CommunicatorType.STUDENT;
    }

    @Override
    protected void talkToPlayer(Player player, Level level) {
        if (CombatResolver.checkHit(getRelationQuality(), player.getRelationQuality())) {;
            int damage = CombatResolver.calculatePowersDiff(this, null);
            int regenValue = player.getRegenLimit() - damage;
            player.setRegenLimit(regenValue);
            if (player.getActPowers() > player.getRegenLimit()) {
                player.setActPowers(regenValue);
            }
        }
    }
}
