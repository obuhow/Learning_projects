package rogue.view;

import java.util.ArrayList;
import java.util.Objects;

import com.googlecode.lanterna.TextColor;

import rogue.controller.GameInfo;
import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.characters.Player;
import rogue.model.domain.entities.characters.communicators.Networker;
import rogue.model.domain.entities.characters.communicators.Communicator;
import rogue.model.domain.entities.consumables.ItemRoom;
import rogue.model.domain.entities.consumables.items.Elixir;
import rogue.model.domain.entities.consumables.items.Food;
import rogue.model.domain.entities.consumables.items.Item;
import rogue.model.domain.entities.consumables.items.Scroll;
import rogue.model.domain.entities.places.Place;
import rogue.model.domain.enums.Dimention;
import rogue.model.domain.enums.StatType;

public class Map {
    private char[][] map;
    private TextColor.ANSI[][] colors;
    private boolean[] visible_rooms;
    private ArrayList<Boolean> visible_passages;
    
    public static final int MAP_HEIGHT = Room.ROOMS_IN_HEIGHT * Room.REGION_HEIGHT;
    public static final int MAP_WIDTH = Room.ROOMS_IN_WIDTH * Room.REGION_WIDTH;


    public Map() {
        this.map = new char[MAP_HEIGHT][MAP_WIDTH];
        this.colors = new TextColor.ANSI[MAP_HEIGHT][MAP_WIDTH];
        this.visible_rooms = new boolean[Room.ROOMS_NUM];
        this.visible_passages = new ArrayList<>();
    }

    public Map(boolean[] visible_rooms, ArrayList<Boolean> visible_passages) {
        this.map = new char[MAP_HEIGHT][MAP_WIDTH];
        this.colors = new TextColor.ANSI[MAP_HEIGHT][MAP_WIDTH];
        this.visible_rooms = visible_rooms;
        this.visible_passages = visible_passages;
    }

    public void clear() {
        clearFrame();
        for (int i = 0; i < Room.ROOMS_NUM; i++) {
            visible_rooms[i] = false;
        }
        visible_passages.clear();
    }

    public void clearFrame() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                map[i][j] = ' ';
                colors[i][j] = TextColor.ANSI.BLACK;
            }
        }
    }

    public void roomsToMap(GameInfo gameInfo) {
        for (int i = 0; i < Room.ROOMS_NUM; i++)
        {
            if (!visible_rooms[i] && gameInfo.getLevel().getRoomIndexByCoord(gameInfo.getPlayer()) != i)
                continue;
            
            int x1 = gameInfo.getLevel().getRoomCoordinateDimentiona(i, Dimention.X);
            int y1 = gameInfo.getLevel().getRoomCoordinateDimentiona(i, Dimention.Y);
            
            int xsize = gameInfo.getLevel().getRoomSizeDimentiona(i, Dimention.X);
            int ysize = gameInfo.getLevel().getRoomSizeDimentiona(i, Dimention.Y);
            for (int y = 0; y < MAP_HEIGHT; y++) {
                for (int x = 0; x < MAP_WIDTH; x++) {
                    boolean checkx = (x == x1 || x == x1 + xsize - 1) && (y1 <= y && y < y1 + ysize);
                    boolean checky = (y == y1 || y == y1 + ysize - 1) && (x1 <= x && x < x1 + xsize);
                    if (checky)
                        gameInfo.getMap().setMapElem(y, x, '-');
                    else if (checkx)
                        gameInfo.getMap().setMapElem(y, x, '|');
                }
            }
            
            visible_rooms[i] = true;
        }
    }

    public void passagesToMap(GameInfo gameInfo) {
        for (int i = 0, end = gameInfo.getLevel().getPassages().getPassagesNum(); i < end; ++i) {
            boolean visible = true;
            ensureVisiblePassageIndex(i);
            ObjectT playerCoords = gameInfo.getPlayer();
            ObjectT passage = gameInfo.getLevel().getPassages().getPassages()[i];
            if (!visible_passages.get(i) && characterOutsideBorder(playerCoords, passage))
                visible = false;
        
            int x1 = gameInfo.getLevel().getPassagesCoordinateDimentiona(i, Dimention.X);
            int y1 = gameInfo.getLevel().getPassagesCoordinateDimentiona(i, Dimention.Y);
            
            int xsize = gameInfo.getLevel().getPassagesSizeDimentiona(i, Dimention.X);
            int ysize = gameInfo.getLevel().getPassagesSizeDimentiona(i, Dimention.Y);
            
            for (int y = 0; y < MAP_HEIGHT; y++) {
                for (int x = 0; x < MAP_WIDTH; x++) {
                    boolean checkx = (x1 < x && x < x1 + xsize - 1) && (y1 < y && y < y1 + ysize - 1);
                    ObjectT coords = new ObjectT(new int[]{x,y}, null);
                    int room = gameInfo.getLevel().getRoomIndexByCoord(coords);
                    if (checkx && visible) {
                        if (room != -1)
                            gameInfo.getMap().setMapElem(y, x, '+');
                        else
                            gameInfo.getMap().setMapElem(y, x, '#');
                    }
                    else if (checkx && room != -1 && visible_rooms[room])
                        gameInfo.getMap().setMapElem(y, x, '+');
                }
            }
            
            visible_passages.set(i, visible);
        }
    }

    public void playerToMap(GameInfo gameInfo) {
        int x = gameInfo.getPlayer().getPlayerCoordinate(Dimention.X);
        int y = gameInfo.getPlayer().getPlayerCoordinate(Dimention.Y);
        gameInfo.getMap().setMapElem(y, x, '@');
    }

    public void communicatorsToMap(GameInfo gameInfo) {
        char[] communicator_letters = {'p', 's', 'b', 't', 'g'};
        ArrayList<Communicator> visibleCommunicators = new ArrayList<>();
        Player player = gameInfo.getPlayer();
        int playerRoom = gameInfo.getLevel().getRoomIndexByCoord(player);
        ArrayList<Communicator> communicatorsOutsideRooms = gameInfo.getLevel().getCommunicatorsOutsideRooms();
        
        if (playerRoom == -1 && (communicatorsOutsideRooms == null || communicatorsOutsideRooms.isEmpty())) return;

        for (int i = 0; i < Room.ROOMS_NUM; ++i) {
            if (i != playerRoom) continue;
            Room room = gameInfo.getLevel().getRooms()[i];
            ArrayList<Communicator> communicatorsInRoom = room.getCommunicators();
            if (communicatorsInRoom == null) continue;
            for (int j = 0; j < communicatorsInRoom.size(); j++) {
                Communicator communicator = room.getCommunicators().get(j);
                if (!communicator.equals(player)) visibleCommunicators.add(communicator);
            }
        }
        
        if (playerRoom == -1) {
            for (int i = 0; i < gameInfo.getLevel().getPassages().getPassages().length; i++) {
                if (!visibleCommunicators.isEmpty()) continue;
                ObjectT passage = gameInfo.getLevel().getPassages().getPassages()[i];
                boolean playerIsHere = ObjectT.isInSpace(player.getCoordinate(Dimention.X.ordinal()), 
                        player.getCoordinate(Dimention.Y.ordinal()), passage);
                if (!playerIsHere) continue;

                for (Communicator communicator : communicatorsOutsideRooms) {
                    boolean communicatorIsHere = ObjectT.isInSpace(communicator.getCoordinate(Dimention.X.ordinal()),
                        communicator.getCoordinate(Dimention.Y.ordinal()), passage);
                    if (communicatorIsHere && playerIsHere) {
                        visibleCommunicators.add(communicator);
                    }
                }
            }
        }
        

        for (Communicator communicator : visibleCommunicators) {
            int x = communicator.getCoordinate(Dimention.X.ordinal());
            int y = communicator.getCoordinate(Dimention.Y.ordinal());
            int type = communicator.getType().ordinal();
            gameInfo.getMap().setMapElem(y, x, communicator_letters[type], getCommunicatorColor(communicator_letters[type]));
            if (communicator instanceof Networker && ((Networker) communicator).isInvisible()) {
                gameInfo.getMap().setMapElem(y, x, ' ');
            }
        }
    }

    public void consumablesToMap(GameInfo gameInfo) {
        char[] itemLetters = {'c', 't', 'l', 'r'};
        Player player = gameInfo.getPlayer();
        int playerRoom = gameInfo.getLevel().getRoomIndexByCoord(player);
        for (int i = 1; i < Room.ROOMS_NUM; ++i) {
            if (!visible_rooms[i] || i != playerRoom) continue;
            Room room = gameInfo.getLevel().getRooms()[i];
            for (ItemRoom<?> itemRoom : room.getConsumables().getItems()) {
                ObjectT itemCoordinates = itemRoom.getCoordinates();
                if (!itemCoordinates.equals(player)) {
                    int x = itemCoordinates.getCoordinate(Dimention.X.ordinal());
                    int y = itemCoordinates.getCoordinate(Dimention.Y.ordinal());
                    Item item = itemRoom.getItem();
                    int type = item instanceof Food ? 0 :
                               item instanceof Elixir ? 1 :
                               item instanceof Scroll ? 2 : 3;
                    gameInfo.getMap().setMapElem(y, x, itemLetters[type], getItemColor(item));
                }
            }
        }
    }

    public void placesToMap(GameInfo gameInfo) {
        char[] itemLetters = {'C'};
        Player player = gameInfo.getPlayer();
        int playerRoom = gameInfo.getLevel().getRoomIndexByCoord(player);
        for (int i = 1; i < Room.ROOMS_NUM; ++i) {
            if (!visible_rooms[i] || i != playerRoom) continue;
            Room room = gameInfo.getLevel().getRooms()[i];
            Place place = room.getPlace();
            if (Objects.isNull(place)) continue;
            ObjectT geometry = place.getGeometry();
            if (!geometry.equals(player)) {
                int x = geometry.getCoordinate(Dimention.X.ordinal());
                int y = geometry.getCoordinate(Dimention.Y.ordinal());
                gameInfo.getMap().setMapElem(y, x, itemLetters[0], TextColor.ANSI.BLUE);
            }
        }
    }


    public void levelEndToMap(GameInfo gameInfo) {
        ObjectT endOfLevel = gameInfo.getLevel().getEndOfLevel();
        int roomEnd = gameInfo.getLevel().getRoomIndexByCoord(endOfLevel);
        int playerRoom = gameInfo.getLevel().getRoomIndexByCoord(gameInfo.getPlayer());
        if (roomEnd == -1 || !visible_rooms[roomEnd] || roomEnd != playerRoom) {
            return;
        }
        int x = endOfLevel.getCoordinate(Dimention.X.ordinal());
        int y = endOfLevel.getCoordinate(Dimention.Y.ordinal());
        gameInfo.getMap().setMapElem(y, x, 'I', TextColor.ANSI.CYAN);
    }

    public boolean getVisibleRoom(int i) {
        return visible_rooms[i];
    }

    public ArrayList<Boolean> getVisiblePassages() {
        return visible_passages;
    }

    public char[] getMapLine(int y) {
        return map[y];
    }
    public char getMapElem(int y, int x) {
        return map[y][x];
    }
    public TextColor.ANSI getMapColor(int y, int x) {
        return colors[y][x];
    }
    public void setMapElem(int y, int x, char elem) {
        map[y][x] = elem;
        colors[y][x] = getDefaultColor(elem);
    }
    public void setMapElem(int y, int x, char elem, TextColor.ANSI color) {
        map[y][x] = elem;
        colors[y][x] = color;
    }

    private TextColor.ANSI getDefaultColor(char elem) {
        return switch (elem) {
            case '+', '-', '|', '.', '#' -> TextColor.ANSI.BLACK;
            case '@' -> TextColor.ANSI.BLUE_BRIGHT;
            case 'p', 's', 'g' -> TextColor.ANSI.GREEN;
            case 'b', 't' -> TextColor.ANSI.RED_BRIGHT;
            case 'L', 'T', 'C' -> TextColor.ANSI.BLUE;
            case 'R' -> TextColor.ANSI.CYAN_BRIGHT;
            case 'I' -> TextColor.ANSI.CYAN;
            default -> TextColor.ANSI.BLACK;
        };
    }

    private TextColor.ANSI getCommunicatorColor(char communicatorSymbol) {
        return switch (communicatorSymbol) {
            case 'p', 's', 'g' -> TextColor.ANSI.GREEN;
            case 'b', 't' -> TextColor.ANSI.RED_BRIGHT;
            default -> TextColor.ANSI.BLACK;
        };
    }

    private TextColor.ANSI getItemColor(Item item) {
        if (item instanceof Food) {
            return TextColor.ANSI.BLUE;
        }
        if (item instanceof Elixir elixir) {
            return getStatColor(elixir.getStat());
        }
        if (item instanceof Scroll scroll) {
            return getStatColor(scroll.getStat());
        }
        return TextColor.ANSI.BLUE;
    }

    private TextColor.ANSI getStatColor(StatType stat) {
        return switch (stat) {
            case ACT_POWERS -> TextColor.ANSI.RED;
            case RELATION_QUALITY -> TextColor.ANSI.GREEN;
            case IMPULSE_POWER -> TextColor.ANSI.BLUE;
            default -> TextColor.ANSI.BLACK;
        };
    }

    private void fillRoomByFog(Room room) {
        int xRoom = room.getCoordinate(Dimention.X.ordinal());
        int yRoom = room.getCoordinate(Dimention.Y.ordinal());
        int xSize = room.getSize(Dimention.X.ordinal());
        int ySize = room.getSize(Dimention.Y.ordinal());

        for (int x = xRoom + 1; x < xRoom + xSize - 1; x++) {
            for (int y = yRoom + 1; y < yRoom + ySize - 1; y++) {
                setMapElem(y, x, '.');
            }
        }
    }

    private boolean isVerticalDirectionFog(Player player, Room room) {
        ObjectT probe = new ObjectT(new int[] {
            player.getCoordinate(Dimention.X.ordinal()) + 1,
            player.getCoordinate(Dimention.Y.ordinal())
        });
        if (!characterOutsideBorder(probe, room)) {
            return false;
        }

        probe.setCoordinate(Dimention.X.ordinal(), probe.getCoordinate(Dimention.X.ordinal()) - 2);
        return characterOutsideBorder(probe, room);
    }

    private void fillRoomPartByFog(Player player, Room room) {
        int playerX = player.getCoordinate(Dimention.X.ordinal());
        int playerY = player.getCoordinate(Dimention.Y.ordinal());
        boolean isVertical = isVerticalDirectionFog(player, room);

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                ObjectT coords = new ObjectT(new int[] {x, y});
                if (characterOutsideBorder(coords, room))
                    continue;

                int newX = x - playerX;
                int newY = y - playerY;
                if (isVertical && Math.abs(newX) >= Math.abs(newY))
                    setMapElem(y, x, '.');
                if (!isVertical && Math.abs(newX) <= Math.abs(newY))
                    setMapElem(y, x, '.');
            }
        }
    }

    private boolean characterOutsideBorder(ObjectT characterCoords, ObjectT room) {
        int charX = characterCoords.getCoordinate(Dimention.X.ordinal());
        int charY = characterCoords.getCoordinate(Dimention.Y.ordinal());
        int charW = characterCoords.getSize(Dimention.X.ordinal());
        int charH = characterCoords.getSize(Dimention.Y.ordinal());

        int roomX = room.getCoordinate(Dimention.X.ordinal());
        int roomY = room.getCoordinate(Dimention.Y.ordinal());
        int roomW = room.getSize(Dimention.X.ordinal());
        int roomH = room.getSize(Dimention.Y.ordinal());

        return (charX + charW - 1 >= roomX + roomW - 1)
            || (charX <= roomX)
            || (charY <= roomY)
            || (charY + charH - 1 >= roomY + roomH - 1);
    }

    private void ensureVisiblePassageIndex(int index) {
        while (visible_passages.size() <= index) {
            visible_passages.add(false);
        }
    }

    public void fogOfWarToMap(GameInfo gameInfo) {
        int playerRoom = gameInfo.getLevel().getRoomIndexByCoord(gameInfo.getPlayer());
        for (int i = 0; i < Room.ROOMS_NUM; i++) {
            Room room = gameInfo.getLevel().getRoom(i);
            if (i != playerRoom && visible_rooms[i])
                fillRoomByFog(room);

            if (i == playerRoom
                && gameInfo.getLevel().getRoomIndexByCoord(gameInfo.getPlayer()) != -1
                && characterOutsideBorder(gameInfo.getPlayer(), room))
                fillRoomPartByFog(gameInfo.getPlayer(), room);
        }
    }
}
 
