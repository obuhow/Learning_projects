package rogue.model.domain.entities.characters.communicators;


import java.util.Random;

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

public class Tractor extends Communicator {
    private boolean tractorCooldown;

    private static final Logger log = LoggerFactory.getLogger(Tractor.class);

    public Tractor() {
        super(new ObjectT(), 150.0, 25, 100, AttractivenessType.AVERAGE, false, Directions.STOP);
        tractorCooldown = false;
    }

    public Tractor(Room room) {
        super(room.getRandomFreeCellInRoom(), 150.0, 25, 100, AttractivenessType.AVERAGE, false, Directions.STOP);
        tractorCooldown = false;
    }

    public Tractor(ObjectT coords, double actPowers, int relationQuality, int impulsePower, AttractivenessType attractiveness,
            boolean isChasing, Directions dir) {
        super(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
        tractorCooldown = false;
    }

    @Override
    public CommunicatorType getType() {
        return CommunicatorType.TRACTOR;
    }

    @Override
    protected void talkToPlayer(Player player, Level level) {
        if (CombatResolver.checkHit(getRelationQuality(), player.getRelationQuality())) {;
            int damage = CombatResolver.calculatePowersDiff(this, null);
            tractorCooldown = true;
            double actPowersValue = player.getActPowers() - damage;
            player.setActPowers(actPowersValue);
        }
    }

    @Override
    protected void movePattern(Level level) {
        if (tractorCooldown) {
            tractorCooldown = false;
            return;
        }
        for (int i = 0; i < 2; i++) {
            boolean result = false;
            int j = 0;
            while(!result && j < 4) {
                Directions dir = Directions.values()[new Random().nextInt(4)];
                result = moveIfPossible(dir, level);
                j++;
            }; 
            if (result) break;
        }
    }
}
