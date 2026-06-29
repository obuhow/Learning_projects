package rogue.model.domain.entities.places;

import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.consumables.items.Food;
import rogue.model.domain.enums.PlaceType;

public class PlaceFactory {
    public static Place createPlace(PlaceType type, Room room) {
        Place place = null;
        switch (type) {
            case PlaceType.CAFE:
                place = new Place(new Food("Полезная еда", 60), room);
                break;
            default:
                break;
        }
        return place;
    }
}
