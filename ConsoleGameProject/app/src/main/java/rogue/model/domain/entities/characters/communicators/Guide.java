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

public class Guide extends Communicator {

    private static final Logger log = LoggerFactory.getLogger(Guide.class);

    public Guide() {
        super(new ObjectT(), 100.0, 100, 30, AttractivenessType.HIGH, false, Directions.STOP);
    }


    public Guide(Room room) {
        super(room.getRandomFreeCellInRoom(), 100.0, 100, 30, AttractivenessType.HIGH, false, Directions.STOP);
    }

    public Guide(ObjectT coords, double actPowers, int relationQuality, int impulsePower, AttractivenessType attractiveness,
            boolean isChasing, Directions dir) {
        super(coords, actPowers, relationQuality, impulsePower, attractiveness, isChasing, dir);
    }

    @Override
    public CommunicatorType getType() {
        return CommunicatorType.GUIDE;
    }

    @Override
    protected void talkToPlayer(Player player, Level level) {
        if (CombatResolver.checkHit(getRelationQuality(), player.getRelationQuality())) {;
            int damage = CombatResolver.calculatePowersDiff(this, null);
            double actPowersValue = player.getActPowers() - damage;
            player.setActPowers(actPowersValue);
            if (new Random().nextInt(100) < 30) {
                player.setAsleep(true);
            }
        }
    }

    @Override
    protected void movePattern(Level level) {
        Directions[] diagonals = {
            Directions.DIAGONALLY_FORWARD_LEFT,
            Directions.DIAGONALLY_FORWARD_RIGHT,
            Directions.DIAGONALLY_BACK_LEFT,
            Directions.DIAGONALLY_BACK_RIGHT
        };
        boolean result = false;
        int i = 0;
        while(!result && i < 4) {
            Directions dir = diagonals[new Random().nextInt(diagonals.length)];
            result = moveIfPossible(dir, level);
            i++;
        }; 
    }
}
