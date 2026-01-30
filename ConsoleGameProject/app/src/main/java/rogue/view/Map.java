package rogue.view;

public class Map {
    public static final int MAP_HEIGHT = 50;// EntitiesConst.ROOMS_IN_HEIGHT * EntitiesConst.REGION_HEIGHT;
    public static final int MAP_WIDTH = 50;// EntitiesConst.ROOMS_IN_WIDTH * EntitiesConst.REGION_WIDTH;

    private char[][] map = new char[MAP_HEIGHT][MAP_WIDTH];
    // private boolean[] visible_rooms = new boolean[4];//EntitiesConst.ROOMS_NUM];
    // private boolean[] visible_passages = new boolean[4];//EntitiesConst.MAX_PASSAGES_NUM];

    public char[] getMapLine(int y) {
        return map[y];
    }
    public char getMapElem(int y, int x) {
        return map[y][x];
    }
    public void setMapElem(int y, int x, char elem) {
        map[y][x] = elem;
    }

}
 