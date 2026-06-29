package rogue.model.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rogue.model.domain.entities.characters.communicators.Communicator;
import rogue.model.domain.entities.characters.communicators.CommunicatorFactory;
import rogue.model.domain.entities.consumables.ConsumablesRoom;
import rogue.model.domain.entities.consumables.ConsumablesRoomFactory;
import rogue.model.domain.entities.places.Place;
import rogue.model.domain.entities.places.PlaceFactory;
import rogue.model.domain.enums.Dimention;
import rogue.model.domain.enums.PlaceType;

public class Room extends ObjectT {
    private ConsumablesRoom consumables;
    private ArrayList<Communicator> communicators;
    private Place place;

    private static final Logger log = LoggerFactory.getLogger(Room.class);

    public static final int ROOMS_IN_WIDTH = 3;
    public static final int ROOMS_IN_HEIGHT = 3;
    public static final int ROOMS_NUM = ROOMS_IN_WIDTH * ROOMS_IN_HEIGHT;

    public static final int REGION_WIDTH = 27;
    public static final int REGION_HEIGHT = 10;

    public static final int MIN_ROOM_WIDTH = 6;
    public static final int MAX_ROOM_WIDTH = REGION_WIDTH - 2;

    public static final int MIN_ROOM_HEIGHT = 5;
    public static final int MAX_ROOM_HEIGHT = (REGION_HEIGHT - 2);

    public Room() {
        this(new ConsumablesRoom(), new ArrayList<Communicator>());
    }

    public Room(ConsumablesRoom consumables, ArrayList<Communicator> communicators) {
        this.consumables = consumables;
        this.communicators = communicators;
        this.place = null;
    }

    public static Room[] generateRooms(int levelNum) {
        var random = new Random();
        var rooms = new Room[ROOMS_NUM];
        for (int i = 0; i < ROOMS_NUM; ++i) {
            log.info("Генерация комнаты {}", i);
            int width_room = random.nextInt(MIN_ROOM_WIDTH, MAX_ROOM_WIDTH);
            int height_room = random.nextInt(MIN_ROOM_HEIGHT, MAX_ROOM_HEIGHT);
            
            int left_range_coord = (i % ROOMS_IN_WIDTH) * REGION_WIDTH + 1;
            int right_range_coord = (i % ROOMS_IN_WIDTH + 1) * REGION_WIDTH - width_room - 1;
            int x_coord = random.nextInt(left_range_coord, right_range_coord);
            
            int up_range_coord = (i / ROOMS_IN_WIDTH) * REGION_HEIGHT + 1;
            int bottom_range_coord = (i / ROOMS_IN_WIDTH + 1) * REGION_HEIGHT - height_room - 1;
            int y_coord = random.nextInt(up_range_coord, bottom_range_coord);
            
            rooms[i] = new Room();
            rooms[i].setSize(Dimention.X.ordinal(), width_room);
            rooms[i].setSize(Dimention.Y.ordinal(), height_room);
            
            rooms[i].setCoordinate(Dimention.X.ordinal(), x_coord);
            rooms[i].setCoordinate(Dimention.Y.ordinal(), y_coord);
            
            if (i == 1) {
                rooms[i].setConsumables(ConsumablesRoomFactory.getWeaponRoom(rooms[i], levelNum));
                rooms[i].setPlace(PlaceFactory.createPlace(PlaceType.CAFE, rooms[i]));

            } 
            if (i > 1) {
                rooms[i].setConsumables(ConsumablesRoomFactory.getConsumablesRoom(rooms[i], levelNum));
                rooms[i].setCommunicators(CommunicatorFactory.getCommunicatorsArray(rooms[i], levelNum));   
            }         
        }
        return rooms;
    }

    public ConsumablesRoom getConsumables() {
        return consumables;
    }

    public void setConsumables(ConsumablesRoom consumables) {
        this.consumables = consumables;
    }

    public ArrayList<Communicator> getCommunicators() {
        return communicators;
    }

    public void setCommunicators(ArrayList<Communicator> communicators) {
        this.communicators = communicators;
    }
    
    public void setPlace(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    public boolean isCommunicatorAt(int x, int y) {
            if (getCommunicators() != null) {
                for (Communicator m : this.getCommunicators()) {
                    if (m != null &&
                        m.getCoordinate(Dimention.X.ordinal()) == x &&
                        m.getCoordinate(Dimention.Y.ordinal()) == y) {
                        return true;
                    }
                }
            }
        return false;
    }

    public ObjectT getRandomFreeCellInRoom() {
        ObjectT result = null;
        int x = getCoordinate(Dimention.X.ordinal());
        int y = getCoordinate(Dimention.Y.ordinal());
        int w = getSize(Dimention.X.ordinal());
        int h = getSize(Dimention.Y.ordinal());
        List<ObjectT> free = new ArrayList<>();
        for (int i = x + 1; i < x + w - 1; i++) {
            for (int j = y + 1; j < y + h - 1; j++) {
                if (!isCommunicatorAt(i, j)) {
                    free.add(new ObjectT(new int[] {i, j}));
                }
            }
        }
        if (!free.isEmpty()) result = free.get(new Random().nextInt(free.size()));
        return result;
    }

    public ObjectT getFreeCellAround(int[] coordinates) {
        int x = coordinates[Dimention.X.ordinal()];
        int y = coordinates[Dimention.Y.ordinal()];
        ObjectT freeCell = new ObjectT(new int[] {x + 1, y});
        return freeCell;
    }

    public boolean isInRoom(int x1, int y1) {
        boolean result = true;

        int x = getCoordinate(Dimention.X.ordinal());
        int y = getCoordinate(Dimention.Y.ordinal());
        int w = getSize(Dimention.X.ordinal());
        int h = getSize(Dimention.Y.ordinal());

        if (x1 <= x || x1 >= x + w || y1 <= y || y1 <= y + h ) result = false;

        return result;
    }

    public boolean isWall (int x1, int y1) {
        int x = getCoordinate(Dimention.X.ordinal());
        int y = getCoordinate(Dimention.Y.ordinal());
        int w = getSize(Dimention.X.ordinal());
        int h = getSize(Dimention.Y.ordinal());
        
        if ((x1 == x || x1 == x + w) && (y1 == y || y1 == y + h))
            return true;

        if (x1 == x && y1 > y && y1 < y + h) return true;
        if (x1 == x + w && y1 > y && y1 < y + h) return true;
        if (y1 == y && x1 > x && x1 < x + w) return true;
        if (y1 == y + h && x1 > x && x1 < x + w) return true;
        return false;
    }
}
