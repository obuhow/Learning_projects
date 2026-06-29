package rogue.model.domain.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import rogue.model.domain.entities.characters.Player;
import rogue.model.domain.entities.characters.communicators.Communicator;
import rogue.model.domain.entities.consumables.ItemRoom;
import rogue.model.domain.entities.passages.Passages;
import rogue.model.domain.entities.places.Place;
import rogue.model.domain.enums.Dimention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Level {
    public static final int START_ROOM_INDEX = 0;

    private Room[] rooms;
    private Passages passages;
    private int levelNum;
    private ObjectT endOfLevel;
    private ArrayList<Communicator> communicatorsOutsideRooms;

    private static final Logger log = LoggerFactory.getLogger(Level.class);

    public Level(Room[] rooms, Passages passages, int levelNum, ObjectT endOfLevel) {
        this.rooms = rooms;
        this.passages = passages;
        this.levelNum = levelNum;
        this.endOfLevel = endOfLevel;
        communicatorsOutsideRooms = new ArrayList<>();
    }

    public Level() {
        levelNum = 0;
        rooms = new Room[Room.ROOMS_NUM];
        passages = new Passages();
        endOfLevel = new ObjectT();
        communicatorsOutsideRooms = new ArrayList<>();
    }

    public void generateNextLevel() {
        levelNum++;
        log.info("=== Начало генерации уровня {} ===", levelNum);
        rooms = Room.generateRooms(levelNum);
        clearRoomContent(START_ROOM_INDEX);
        passages.generatePassages(rooms);
        endOfLevel = rooms[8].getRandomFreeCellInRoom();
    }

    private void clearRoomContent(int roomIndex) {
        Room room = rooms[roomIndex];
        if (room == null) {
            return;
        }
        if (room.getCommunicators() != null) {
            room.getCommunicators().clear();
        }
        if (room.getConsumables() != null && room.getConsumables().getItems() != null) {
            room.getConsumables().getItems().clear();
        }
    }


    public ArrayList<Communicator> getCommunicatorsOutsideRooms() {
        return communicatorsOutsideRooms;
    }

    public void setCommunicatorsOutsideRooms(ArrayList<Communicator> communicatorsOutsideRooms) {
        this.communicatorsOutsideRooms = communicatorsOutsideRooms;
    }

    public int getRoomCoordinateDimentiona(int i, Dimention dim) {
        return rooms[i].getCoordinate(dim.ordinal());
    }

    public int getRoomSizeDimentiona(int i, Dimention dim) {
        return rooms[i].getSize(dim.ordinal());
    }

    public int getPassagesCoordinateDimentiona(int i, Dimention dim) {
        return passages.getPassages()[i].getCoordinate(dim.ordinal());
    }
    public int getPassagesSizeDimentiona(int i, Dimention dim) {
        return passages.getPassages()[i].getSize(dim.ordinal());
    }

    public boolean checkLevelEnd(Player player) {
        return endOfLevel.equals(player);
    }

    public int getRoomIndexByCoord(ObjectT coord) {
        int x = coord.getCoordinate(Dimention.X.ordinal());
        int y = coord.getCoordinate(Dimention.Y.ordinal());

        for (int room = 0; room < Room.ROOMS_NUM; room++) {
            int x_room = getRoomCoordinateDimentiona(room, Dimention.X);
            int y_room = getRoomCoordinateDimentiona(room, Dimention.Y);
            
            int xsize = getRoomSizeDimentiona(room, Dimention.X);
            int ysize = getRoomSizeDimentiona(room, Dimention.Y);
            
            boolean checkx = (x >= x_room) && (x < x_room + xsize);
            boolean checky = (y >= y_room) && (y < y_room + ysize);
            if (checky && checkx)
                return room;
        }
        return -1;
    }

    public Room getRoomByCoord(ObjectT coord) {
        int index = getRoomIndexByCoord(coord);
        if (index >= 0) {
            return rooms[index];
        }
        return null;
    }

    public Room[] getRooms() {
        return rooms;
    }
    public Room getRoom(int index) {
        return rooms[index];
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    public Passages getPassages() {
        return passages;
    }

    public void setPassages(Passages passages) {
        this.passages = passages;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public ObjectT getEndOfLevel() {
        return endOfLevel;
    }

    public void setEndOfLevel(ObjectT endOfLevel) {
        this.endOfLevel = endOfLevel;
    }

    public Communicator getCommunicatorAt(int x, int y) {
        for (Room room : rooms) {
            if (room.getCommunicators() != null) {
                for (Communicator m : room.getCommunicators()) {
                    if (m != null && 
                        m.getCoordinate(Dimention.X.ordinal()) == x && 
                        m.getCoordinate(Dimention.Y.ordinal()) == y) {
                        return m;
                    }
                }
            }
        }
        if (!communicatorsOutsideRooms.isEmpty()) {
            for (Communicator m : communicatorsOutsideRooms) {
                if (m != null && 
                    m.getCoordinate(Dimention.X.ordinal()) == x && 
                    m.getCoordinate(Dimention.Y.ordinal()) == y) {
                    return m;
                }
                }
            }
        return null;
    }   

    public ItemRoom<?> getItemAt(int x, int y) {
        for (Room room : rooms) {
            if (room.getConsumables() != null) {
                ItemRoom<?> item = room.getConsumables().getItemAt(x, y);
                if (item != null) return item;
            }
        }
        return null;
    }

    public Place getPlaceAt(int x, int y) {
        ObjectT geometry = new ObjectT(new int[] {x, y});
        for (Room room : rooms) {
            Place place = room.getPlace();
            if (place != null && geometry.equals(place.getGeometry())) {
                return place;
            }
        }
        return null;
    }

    public void removeItem(ItemRoom<?> item) {
        for (Room room : rooms) {
            if (room.getConsumables() != null && !room.getConsumables().getItems().isEmpty()) {
                boolean result = room.getConsumables().removeItem(item);
                if (result) {
                    break;
                }
            }
        }
    }

    public void removeCommunicator(Communicator communicator) {
        if (communicator == null) return;

        for (Room room : rooms) {
            ArrayList<Communicator> communicators = room.getCommunicators();
            if (communicators != null && !communicators.isEmpty()) {
                communicators.remove(communicator);
            }
        }

        if (communicatorsOutsideRooms != null && !communicatorsOutsideRooms.isEmpty()) {
            communicatorsOutsideRooms.remove(communicator);
        }
    }

    public boolean isWalkable(int x, int y, Object ignore) {
        for (Room room : rooms) {
            int rx = room.getCoordinate(Dimention.X.ordinal());
            int ry = room.getCoordinate(Dimention.Y.ordinal());
            int rw = room.getSize(Dimention.X.ordinal());
            int rh = room.getSize(Dimention.Y.ordinal());
            if (x > rx && x < rx + rw - 1 && y > ry && y < ry + rh - 1) {
                boolean result = ignore instanceof Communicator ? !isCommunicatorAt(x, y, ignore) : true;
                log.debug("Проверка возможности хода в комнате: {}", result);
                return result;
            }
        }
        
        for (ObjectT p : passages.getPassages()) {
            if (p != null) {
                int px = p.getCoordinate(Dimention.X.ordinal());
                int py = p.getCoordinate(Dimention.Y.ordinal());
                int pw = p.getSize(Dimention.X.ordinal());
                int ph = p.getSize(Dimention.Y.ordinal());
                log.debug("Проверка возможности хода в проходе: {}, [{}, {}]", p.coordinates, x, y);
                if (x > px && x < px + pw - 1 && y > py && y < py + ph - 1) {
                    log.debug("Проверка возможности хода в проходе УДАЛАСЬ");
                    return true;
                }
            }
        }
        log.debug("Проверка возможности хода не удалась: {}", false);
        return false; 
    }

    private boolean isCommunicatorAt(int x, int y, Object ignore) {
        for (Room room : rooms) {
            if (room.getCommunicators() != null) {
                for (Communicator m : room.getCommunicators()) {
                    if (m != null && m != ignore &&
                        m.getCoordinate(Dimention.X.ordinal()) == x &&
                        m.getCoordinate(Dimention.Y.ordinal()) == y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<ObjectT> getPassageByCoord(Player player) {
        List<ObjectT> passagesList = new ArrayList<>();
        int x = player.getCoordinate(Dimention.X.ordinal());
        int y = player.getCoordinate(Dimention.Y.ordinal());
        for (ObjectT passage : passages.getPassages()) {
            if (passage == null) continue;
            int px = passage.getCoordinate(Dimention.X.ordinal());
            int py = passage.getCoordinate(Dimention.Y.ordinal());
            int pw = passage.getSize(Dimention.X.ordinal());
            int ph = passage.getSize(Dimention.Y.ordinal());
            if (x > px && x < px + pw && y > py && y < py + ph) {
                passagesList.add(passage);
            }
        }
        if (passagesList.isEmpty()) return null;
        return passagesList;
    }

    public void checkAndSetCommunicatorsRoom() {
        log.debug("checkAndSetCommunicatorsRoom");
        for (Room room : rooms) {
            log.debug("Room {}", room.getCoordinates());
            ArrayList<Communicator> communicators = room.getCommunicators();
            if (communicators == null) continue;
            Iterator<Communicator> iterator = communicators.iterator();
            while (iterator.hasNext()) {
                Communicator communicator = iterator.next();
                log.debug("Communicator в комнате {}", room.getCoordinates());
                Room communicatorsRoomInFact = getRoomByCoord(communicator);
                if (communicatorsRoomInFact == null) {
                    log.debug("Монстр вышел в проход");
                    communicatorsOutsideRooms.add(communicator);
                    iterator.remove();
                    continue;
                }
                log.debug("communicatorsRoomInFact {}", communicatorsRoomInFact.getCoordinates());
            }
        }
        if (!communicatorsOutsideRooms.isEmpty()) {
            Iterator<Communicator> iterator = communicatorsOutsideRooms.iterator();
            while (iterator.hasNext()) {
                Communicator communicator = iterator.next();
                log.debug("Communicator в проходе сейчас тут {}", communicator.getCoordinates());
                Room communicatorsRoomInFact = getRoomByCoord(communicator);
                log.debug("Communicator в проходе заходит в комнату {}", communicatorsRoomInFact);
                if (communicatorsRoomInFact != null) {
                    ArrayList<Communicator> communicators = communicatorsRoomInFact.getCommunicators();
                    if (communicators == null) communicatorsRoomInFact.setCommunicators(new ArrayList<>());
                    log.debug("Монстр зашёл в комнату {}, ArrayList {}", communicatorsRoomInFact.getCoordinates(), communicatorsRoomInFact.getCommunicators());
                    communicatorsRoomInFact.getCommunicators().add(communicator);
                    iterator.remove();
                }
            }
        }
        
    }
}
