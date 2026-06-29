package rogue.model.domain.entities;

import java.util.LinkedList;

import rogue.model.domain.entities.consumables.items.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Backpack {
    private LinkedList<Food> foods;
    private LinkedList<Elixir> elixirs;
    private LinkedList<Scroll> scrolls;
    private LinkedList<Weapon> weapons;
    private int treasure;

    public static final int CONSUMABLES_MAX_NUM = 10;

    private static final Logger log = LoggerFactory.getLogger(Backpack.class);

    public Backpack(LinkedList<Food> foods, LinkedList<Elixir> elixirs, LinkedList<Scroll> scrolls,
            LinkedList<Weapon> weapons, int treasure) {
        this.foods = foods;
        this.elixirs = elixirs;
        this.scrolls = scrolls;
        this.weapons = weapons;
        this.treasure = treasure;
    }

    public Backpack() {
        foods = new LinkedList<>();
        elixirs = new LinkedList<>();
        scrolls = new LinkedList<>();
        weapons = new LinkedList<>();
        treasure = 0;
    }

    public void addItem(Item item) {
        if (item instanceof Food) addFood((Food) item);
        if (item instanceof Elixir) addElixir((Elixir) item);
        if (item instanceof Scroll) addScroll((Scroll) item);
        if (item instanceof Weapon) addWeapon((Weapon) item);
    }

    public Food getFood(int i) {
        return foods.get(i);
    }

    public int getFoodNum() {
        if (foods == null) {
            foods = new LinkedList<>();
        }
        return foods.size();
    }
    

    public Elixir getElixir(int i) {
        return elixirs.get(i);
    }

    public int getElixirNum() {
        if (elixirs == null) {
            elixirs = new LinkedList<>();
        }
        return elixirs.size();
    }

    public Scroll getScroll(int i) {
        return scrolls.get(i);
    }

    public int getScrollNum() {
        if (scrolls == null) {
            scrolls = new LinkedList<>();
        }
        return scrolls.size();
    }

    public int getTreasure() {
        return treasure;
    }
    public void setTreasure(int treasure) {
        this.treasure = treasure;
    }

    public void addTreasure(int treasure) {
        this.treasure += treasure;
    }

    public Weapon getWeapon(int i) {
        return weapons.get(i);
    }

    public int getWeaponNum() {
        if (weapons == null) {
            weapons = new LinkedList<>();
        }
        return weapons.size();
    }

    public void addFood(Food food) {
        if (foods.size() + 1 <= CONSUMABLES_MAX_NUM) {
            foods.add(food);
        }
    }

    public void addElixir(Elixir elixir) {
        if (elixirs.size() + 1 <= CONSUMABLES_MAX_NUM) {
            elixirs.add(elixir);
        }
    }

    public void addScroll(Scroll scroll) {
        if (scrolls.size() + 1 <= CONSUMABLES_MAX_NUM) {
            scrolls.add(scroll);
        }
    }

    public void addWeapon(Weapon weapon) {
        if (weapons.size() + 1 <= CONSUMABLES_MAX_NUM) {
            weapons.add(weapon);
        }
    }

    public void removeFood(int index) {
        if (index >= 0 && index < foods.size()) foods.remove(index);
    }

    public void removeElixir(int index) {
        if (index >= 0 && index < elixirs.size()) elixirs.remove(index);
    }

    public void removeScroll(int index) {
        if (index >= 0 && index < scrolls.size()) scrolls.remove(index);
    }

    public void removeWeapon(int index) {
        if (index >= 0 && index < weapons.size()) weapons.remove(index);
    }
}
