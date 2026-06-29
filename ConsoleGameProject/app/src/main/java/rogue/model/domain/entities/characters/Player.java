package rogue.model.domain.entities.characters;

import rogue.model.domain.entities.characters.communicators.Student;
import rogue.model.domain.enums.ConsumableTypes;
import rogue.model.domain.enums.Dimention;
import rogue.model.domain.enums.Directions;
import rogue.model.domain.utils.CombatResolver;

import java.util.Objects;

import rogue.model.domain.entities.Backpack;
import rogue.model.domain.entities.Level;
import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.buff.Buff;
import rogue.model.domain.entities.buff.Buffs;
import rogue.model.domain.entities.characters.communicators.Communicator;
import rogue.model.domain.entities.consumables.ItemRoom;
import rogue.model.domain.entities.consumables.items.Elixir;
import rogue.model.domain.entities.consumables.items.Food;
import rogue.model.domain.entities.consumables.items.Scroll;
import rogue.model.domain.entities.consumables.items.Weapon;
import rogue.model.domain.entities.places.Place;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends Character {
    private static final Logger log = LoggerFactory.getLogger(Player.class);

    private int regenLimit;
    private Backpack backpack;
    private Weapon weapon;
    private Buffs elixirBuffs;
    private boolean asleep;
    private boolean isInPlace;

    public Player(ObjectT coords, double actPowers, int relationQuality, int impulsePower, int regenLimit, Backpack backpack,
            Weapon weapon, Buffs elixirBuffs) {
        super(coords, actPowers, relationQuality, impulsePower);
        this.regenLimit = regenLimit;
        this.backpack = backpack;
        this.weapon = weapon;
        this.elixirBuffs = elixirBuffs;
        isInPlace = false;
    }

    public Player() {
        super(new ObjectT(), 500, 70, 70);
        this.regenLimit = 500;
        this.backpack = new Backpack();
        this.weapon = null;
        this.elixirBuffs = new Buffs();
        isInPlace = false;
    }

    public int getRegenLimit() {
        return regenLimit;
    }

    public void setRegenLimit(int regenLimit) {
        this.regenLimit = regenLimit;
    }

    public Backpack getBackpack() {
        if (backpack == null) {
            backpack = new Backpack();
        }
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Buffs getElixirBuffs() {
        return elixirBuffs;
    }

    public void setElixirBuffs(Buffs elixirBuffs) {
        this.elixirBuffs = elixirBuffs;
    }

    public int getPlayerCoordinate(Dimention dim) {
        return this.coordinates[dim.ordinal()];
    }

    public boolean isAsleep() { return asleep; }
    public void setAsleep(boolean asleep) { this.asleep = asleep; }

    public boolean isAt(int x, int y) {
        return getCoordinate(Dimention.X.ordinal()) == x && getCoordinate(Dimention.Y.ordinal()) == y;
    }
    
    public boolean isInPlace() {
        return isInPlace;
    }

    public void setInPlace(boolean isInPlace) {
        this.isInPlace = isInPlace;
    }

    public boolean tryMove(Directions dir, Level level) {
        if (asleep) {
            asleep = false;  
            return false;
        }

        int x = getCoordinate(Dimention.X.ordinal());
        int y = getCoordinate(Dimention.Y.ordinal());
        switch (dir) {
            case FORWARD: y--; break;
            case BACK: y++; break;
            case LEFT: x--; break;
            case RIGHT: x++; break;
            default: return false;
        }

        if (!level.isWalkable(x, y, this)) return false;

        Communicator communicator = level.getCommunicatorAt(x, y);
        if (Objects.nonNull(communicator)) {
            talkToCommunicator(communicator, level);
            return true;
        }

        ItemRoom<?> item = level.getItemAt(x, y);
        if (Objects.nonNull(item)) pickUpItem(item, level);

        Place place = level.getPlaceAt(x,y);
        if (Objects.nonNull(place)) {
            isInPlace = true;
        } else {
            isInPlace = false;
        }

        setCoordinate(Dimention.X.ordinal(), x);
        setCoordinate(Dimention.Y.ordinal(), y);
        log.debug("Совершен шаг");
        return true;
    }

    private void talkToCommunicator(Communicator communicator, Level level) {
        log.info("Начата атака на монстра");
        if (communicator instanceof Student && !((Student) communicator).isFirstAttackDone()) {
            ((Student) communicator).setFirstAttackDone(true);
            log.info("Пропущена атака в первый ход удара вампира");
            return;
        }

        if (!CombatResolver.checkHit(getRelationQuality(), communicator.getRelationQuality())) {
            log.info("Посчитана вероятность удара и удар пропущен");
            return;
        }

        int damage = CombatResolver.calculatePowersDiff(this, weapon);
        log.info("Посчитана сила удара {}", damage);
        double communicatorActPowersValue = communicator.getActPowers() - damage;
        communicator.setActPowers(communicatorActPowersValue);
        log.info("Удар нанесён. Здоровье монстра составляет {}", communicatorActPowersValue);

        if (communicator.getActPowers() <= 0) {
            int treasure = communicator.calculateTreasure();
            log.info("Монстр убит {}", communicator.getClass());
            level.removeCommunicator(communicator);
            backpack.setTreasure(backpack.getTreasure() + treasure);
            log.info("Поднято {} золота", treasure);
        }
    }

    // private void enterTheSpace(ItemRoom<?> itemRoom, Level level) {

    // }

   private void pickUpItem(ItemRoom<?> itemRoom, Level level) {
       log.debug("Совершена попытка поднять предмет {}", itemRoom.getClass());
       backpack.addItem(itemRoom.getItem());
       log.info("Поднят предмет {}", itemRoom.getItem().getName());
       level.removeItem(itemRoom);
   }

    public void useConsumable(ConsumableTypes type, int consumable_pos, Room room) {
        switch (type) {
        case SCROLL: 
            useScroll(consumable_pos);
            break;
        case ELIXIR:
            useElixir(consumable_pos);
            break;
        case FOOD:
            useFood(consumable_pos);
            break;
        case WEAPON:
            equipWeapon(consumable_pos, room);
            break;
        default:
            break;
        }
    }

    public void useFood(int index) {
        if (index < 0 || index >= backpack.getFoodNum()) return;
        Food food = backpack.getFood(index);
        double newActPowers = Math.min(getActPowers() + food.getToRegen(), getRegenLimit());
        setActPowers(newActPowers);
        backpack.removeFood(index);
    }

    public void useElixir(int index) {
        if (index < 0 || index >= backpack.getElixirNum()) return;
        Elixir elixir = backpack.getElixir(index);
        Buff buff = new Buff(elixir.getIncrease(), elixir.getDuration()); 
        switch (elixir.getStat()) {
            case RELATION_QUALITY:
                setRelationQuality(getRelationQuality() + elixir.getIncrease());
                elixirBuffs.getRelationQuality().add(buff);
                elixirBuffs.setCurrentRelationQualityBuffNum(elixirBuffs.getCurrentRelationQualityBuffNum() + 1);
                break;
            case IMPULSE_POWER:
                setImpulsePower(getImpulsePower() + elixir.getIncrease());
                elixirBuffs.getImpulsePower().add(buff);
                elixirBuffs.setCurrentImpulsePowerBuffNum(elixirBuffs.getCurrentImpulsePowerBuffNum() + 1);
                break;
            case MAX_ACT_POWERS:
                setRegenLimit(getRegenLimit() + elixir.getIncrease());
                setActPowers(getActPowers() + elixir.getIncrease());
                elixirBuffs.getMaxActPowers().add(buff);
                elixirBuffs.setCurrentActPowersBuffNum(elixirBuffs.getCurrentActPowersBuffNum() + 1);
                break;
            default: break;
        }
        backpack.removeElixir(index);
    }

    public void useScroll(int index) {
        if (index < 0 || index >= backpack.getScrollNum()) return;
        Scroll scroll = backpack.getScroll(index);
        switch (scroll.getStat()) {
            case RELATION_QUALITY:
                setRelationQuality(getRelationQuality() + scroll.getIncrease());
                break;
            case IMPULSE_POWER:
                setImpulsePower(getImpulsePower() + scroll.getIncrease());
                break;
            case MAX_ACT_POWERS:
                setRegenLimit(getRegenLimit() + scroll.getIncrease());
                setActPowers(getActPowers() + scroll.getIncrease());
                break;
            default: break;
        }
        backpack.removeScroll(index);
    }

    public void equipWeapon(int index, Room room) { 
        if (index >= 0 && index < backpack.getWeaponNum()) {
            Weapon newWeapon = backpack.getWeapon(index);
            if (this.weapon != null) {
                room.getConsumables().addItems(new ItemRoom<Weapon>(weapon, room.getFreeCellAround(coordinates)));
            }
            this.weapon = newWeapon;
            backpack.removeWeapon(index);
        }
    }
}
