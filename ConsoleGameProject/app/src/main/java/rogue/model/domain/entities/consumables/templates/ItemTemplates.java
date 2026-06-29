package rogue.model.domain.entities.consumables.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rogue.model.domain.entities.consumables.items.Item;

public abstract class ItemTemplates<T extends Item> {

    private static final Logger log = LoggerFactory.getLogger(ItemTemplates.class);

    public static Item getRandom(int levelNum) {
        int chance = new Random().nextInt(100);
        if (chance < 35) {
            return FoodTemplates.INSTANCE.getRandomFood(levelNum);
        } else if (chance < 65) {
            return ElixirTemplates.INSTANCE.getRandomElixir(levelNum);
        } else {
            return ScrollTemplates.INSTANCE.getRandomScroll(levelNum);
        }
    }
}
