package rogue.model.domain.entities.characters.communicators;

import rogue.model.domain.entities.Level;
import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.characters.Character;
import rogue.model.domain.entities.characters.Player;
import rogue.model.domain.enums.Dimention;
import rogue.model.domain.enums.Directions;
import rogue.model.domain.enums.AttractivenessType;
import rogue.model.domain.enums.CommunicatorType;
import rogue.model.domain.utils.CombatResolver;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Communicator extends Character {
    private AttractivenessType attractiveness;
    private boolean isChasing;
    private Directions dir;

    private static final Logger log = LoggerFactory.getLogger(Communicator.class);

    public Communicator(ObjectT coords, double actPowers, int relationQuality, int impulsePower, AttractivenessType attractiveness,
            boolean isChasing, Directions dir) {
        super(coords, actPowers, relationQuality, impulsePower);
        this.attractiveness = attractiveness;
        this.isChasing = isChasing;
        this.dir = dir;
    }

    public Communicator() {
        super(new ObjectT(), 100.0, 10, 20);
        this.attractiveness = AttractivenessType.AVERAGE;
        this.isChasing = false;
        this.dir = Directions.STOP;
    }

    public Communicator(Room room) {
        super(room.getRandomFreeCellInRoom(), 100.0, 10, 10);
        this.attractiveness = AttractivenessType.LOW;
        this.isChasing = false;
        this.dir = Directions.STOP;
    }

    public AttractivenessType getAttractiveness() {
        return attractiveness;
    }

    public void setAttractiveness(AttractivenessType attractiveness) {
        this.attractiveness = attractiveness;
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

    public CommunicatorType getType() {
        return CommunicatorType.PHLEGMATIC;
    }

    public void setPower(int levelNum) {
        double PERCENTS_UPDATE_DIFFICULTY_COMMUNICATORS = 10.0;
        double percentsUpdate = 1 + PERCENTS_UPDATE_DIFFICULTY_COMMUNICATORS * levelNum / 100;
        setRelationQuality((int)(getRelationQuality() * percentsUpdate));
        setImpulsePower((int)(getImpulsePower() * percentsUpdate));
        setActPowers(getActPowers() * percentsUpdate);
    }
    
    public void takeTurn(Level level, Player player) {
        if (getActPowers() <= 0) return;

        boolean chase = false;
        if (player != null) {
            int dist = Math.abs(getCoordinate(Dimention.X.ordinal()) - player.getCoordinate(Dimention.X.ordinal()))
                     + Math.abs(getCoordinate(Dimention.Y.ordinal()) - player.getCoordinate(Dimention.Y.ordinal()));
            if (dist <= attractiveness.ordinal() + 1) { 
                chase = true;
            }
        }
        if (chase) {
            log.debug("Собеседник {} преследует игрока: {}", this.getType(), chase);
            moveTowardsPlayer(level, player);
        } else {
            log.debug("Собеседник {} пытается ходить", this.getType());
            movePattern(level);
        }
    }

    private void moveTowardsPlayer(Level level, Player player) {
        int dx = Integer.signum(player.getCoordinate(Dimention.X.ordinal()) - getCoordinate(Dimention.X.ordinal()));
        int dy = Integer.signum(player.getCoordinate(Dimention.Y.ordinal()) - getCoordinate(Dimention.Y.ordinal()));

        if (dx != 0) {
            int nx = getCoordinate(Dimention.X.ordinal()) + dx;
            int ny = getCoordinate(Dimention.Y.ordinal());
            if (tryMoveTo(nx, ny, level, player)) return;
        }
        
        if (dy != 0) {
            int nx = getCoordinate(Dimention.X.ordinal());
            int ny = getCoordinate(Dimention.Y.ordinal()) + dy;
            if (tryMoveTo(nx, ny, level, player)) return;
        }
        
        movePattern(level);
    }

    private boolean tryMoveTo(int x, int y, Level level, Player player) {
        boolean result = false;
        if (level.isWalkable(x, y, this)) {
            if (player.isAt(x, y)) {
                talkToPlayer(player, level);
                result = true;
            } else {
                log.debug("Собеседник делает шаг по направлению к игроку {} {}", x, y);
                setCoordinate(Dimention.X.ordinal(), x);
                setCoordinate(Dimention.Y.ordinal(), y);
                result = true;
            }
        }
        return result;
    }

    protected void talkToPlayer(Player player, Level level) {
        log.debug("Собеседник {} пытается говорить с игроком", getType());
        if (CombatResolver.checkHit(getRelationQuality(), player.getRelationQuality())) {
            int damage = CombatResolver.calculatePowersDiff(this, null);
            double actPowersValue = player.getActPowers() - damage;
            player.setActPowers(actPowersValue);
            log.info("Силы общаться игрока снижено на {} до {}", damage, actPowersValue);
        }
    }

    protected void movePattern(Level level) {
        Directions[] fourSides = {
            Directions.FORWARD,
            Directions.BACK,
            Directions.LEFT,
            Directions.RIGHT
        };
        boolean result = false;
        int i = 0;
        while(!result && i < 4) {
            Directions dir = fourSides[new Random().nextInt(fourSides.length)];
            log.debug("Попытка хода по направлению {} {}", dir.toString(), getType());
            result = moveIfPossible(dir, level);
            i++;
        }
    }

    protected boolean moveIfPossible(Directions dir, Level level) {
        boolean result = false;;
        int x = getCoordinate(Dimention.X.ordinal());
        int y = getCoordinate(Dimention.Y.ordinal());
        int nx = x, ny = y;
        switch (dir) {
            case FORWARD: ny--; break;
            case BACK: ny++; break;
            case LEFT: nx--; break;
            case RIGHT: nx++; break;
            case DIAGONALLY_FORWARD_LEFT: nx--; ny--; break;
            case DIAGONALLY_FORWARD_RIGHT: nx++; ny--; break;
            case DIAGONALLY_BACK_LEFT: nx--; ny++; break;
            case DIAGONALLY_BACK_RIGHT: nx++; ny++; break;
            case STOP:
                break;
            default:
                break;
        }
        log.debug("Проверка isWalkable {} {} [{}, {}]", getType(), dir, nx, ny);
        if (level.isWalkable(nx, ny, this)) {
            setCoordinate(Dimention.X.ordinal(), nx);
            setCoordinate(Dimention.Y.ordinal(), ny);
            result = true;
        }
        return result;
    }

    public int calculateTreasure() {
        return new Random().nextInt(10) + 1 + getImpulsePower() / 10;
    }
}
