package rogue.model.domain.entities.consumables;

import java.util.ArrayList;
import java.util.List;

import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.consumables.templates.ElixirTemplates;
import rogue.model.domain.entities.consumables.templates.FoodTemplates;
import rogue.model.domain.entities.consumables.templates.ItemTemplates;
import rogue.model.domain.entities.consumables.templates.ScrollTemplates;
import rogue.model.domain.entities.consumables.templates.WeaponTemplates;

public class ConsumablesRoomFactory {

    public static ConsumablesRoom getConsumablesRoom(Room room, int levelNum) {

        ConsumablesRoom consumablesRoom = new ConsumablesRoom(new ArrayList<>(List.of(
            new ItemRoom<>(ItemTemplates.getRandom(levelNum), room))
        ));
        return consumablesRoom;
    }

    public static ConsumablesRoom getWeaponRoom(Room room, int levelNum) {
        ConsumablesRoom consumablesRoom = new ConsumablesRoom(new ArrayList<>(List.of(
            new ItemRoom<>(WeaponTemplates.INSTANCE.getWeapon(levelNum), room))
        ));
        return consumablesRoom;
    }
}
