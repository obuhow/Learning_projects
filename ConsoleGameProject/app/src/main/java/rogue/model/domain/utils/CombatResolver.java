package rogue.model.domain.utils;

import rogue.model.domain.entities.characters.Character;
import rogue.model.domain.entities.consumables.items.Weapon;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombatResolver {
    private static final Random RANDOM = new Random();

    private static Logger log = LoggerFactory.getLogger(CombatResolver.class);

    public static boolean checkHit(int talkToerRelationQuality, int defenderRelationQuality) {
        boolean result = false;
        int total = talkToerRelationQuality + defenderRelationQuality;
        if (total != 0) {
            int chance = talkToerRelationQuality * 100 / total;
            int random = RANDOM.nextInt(100);
            log.info("Выпало {} при шансах {}/100", random, chance);
            result = random < chance;
        }
        return result;
    }
    
    public static int calculatePowersDiff(Character talkToer, Weapon weapon) {
        int base = talkToer.getImpulsePower();
        if (weapon != null) {
            base += weapon.getImpulsePower();
        }
        return base / 2 + RANDOM.nextInt(base / 2 + 1);
    }
}