package rogue.model.domain.entities.passages;

import java.util.Random;
import java.util.stream.IntStream;

import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.enums.Dimention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Passages {
    private ObjectT[] passages;
    private int passagesNum;
    private final Random random = new Random();

    public static final int MAX_PASSAGE_PARTS = 3;
    public static final int MAX_PASSAGES_NUM = (Room.ROOMS_NUM - 1) * MAX_PASSAGE_PARTS;

    private static final Logger log = LoggerFactory.getLogger(Passages.class);

    public Passages() {
        this(0);
    }
    public Passages(int nums) {
        this(new ObjectT[MAX_PASSAGES_NUM], nums);
    }

    public Passages(ObjectT[] passages, int passagesNum) {
        this.passages = passages;
        this.passagesNum = passagesNum;
    }

    public void generatePassages(Room[] rooms) {
        passagesNum = 0;
        log.info("Начало генерации проходов");
        Edge[] edges = Edge.generateEdgesForRooms();

        int[] parent = IntStream.iterate(0, i -> i + 1).limit(Room.ROOMS_NUM).toArray();
        var rank = new int[Room.ROOMS_NUM];

        for (int i = 0; i < Edge.MAX_EDGES_NUM; i++) {
            if (findSet(edges[i].getU(), parent) != findSet(edges[i].getV(), parent)) {
                unionSets(edges[i].getU(), edges[i].getV(), parent, rank);
                if (Math.abs(edges[i].getU() - edges[i].getV()) == 1)
                    generateHorizontalPassage(edges[i].getU(), edges[i].getV(), rooms);
                else
                    generateVerticalPassage(edges[i].getU(), edges[i].getV(), rooms);
            }
        }
    }

    private int findSet(int v, int[] parent) {
        if (v == parent[v]) return v;
        parent[v] = findSet(parent[v], parent);
        return parent[v];
    }

    private void unionSets(int v, int u, int[] parent, int[] rank) {
        v = findSet(v, parent);
        u = findSet(u, parent);
        
        if (u != v) {
            if (rank[u] >= rank[v])
                parent[v] = u;
            else
                parent[u] = v;
            if (rank[u] == rank[v])
                rank[u]++;
        }
    }

    public void createPassage(int coord_x, int coord_y, int width, int height) {
        if (passagesNum < passages.length) {
            passages[passagesNum] = new ObjectT();
            passages[passagesNum].setCoordinate(Dimention.X.ordinal(), coord_x - 1);
            passages[passagesNum].setCoordinate(Dimention.Y.ordinal(), coord_y - 1);
            passages[passagesNum].setSize(Dimention.X.ordinal(), width + 2);
            passages[passagesNum].setSize(Dimention.Y.ordinal(), height + 2);
            passagesNum++;
        } else {
            log.error("Невозможно создать проход: массив переполнен ({} >= {})", passagesNum, passages.length);
        }
    }

    private int randomInclusive(int min, int max) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        return random.nextInt(min, max + 1);
    }

    public void generateHorizontalPassage(int first_room, int second_room, Room[] rooms) {
        Room first_coords = rooms[first_room];
        Room second_coords = rooms[second_room];

        int first_x            = first_coords.getCoordinate(Dimention.X.ordinal()) + first_coords.getSize(Dimention.X.ordinal()) - 1;
        int up_range_coord     = first_coords.getCoordinate(Dimention.Y.ordinal()) + 1;
        int bottom_range_coord = first_coords.getCoordinate(Dimention.Y.ordinal()) + first_coords.getSize(Dimention.Y.ordinal()) - 2;
        int first_y            = randomInclusive(up_range_coord, bottom_range_coord);
        
        int second_x       = second_coords.getCoordinate(Dimention.X.ordinal());
        up_range_coord     = second_coords.getCoordinate(Dimention.Y.ordinal()) + 1;
        bottom_range_coord = second_coords.getCoordinate(Dimention.Y.ordinal()) + second_coords.getSize(Dimention.Y.ordinal()) - 2;
        int second_y       = randomInclusive(up_range_coord, bottom_range_coord);
        

        if (first_y == second_y) {
            createPassage(first_x, first_y, Math.abs(second_x - first_x) + 1, 1);
        } else {
            int vertical = randomInclusive(Math.min(first_x, second_x) + 1, Math.max(first_x, second_x) - 1);
            createPassage(first_x,  first_y,                Math.abs(vertical - first_x) + 1, 1);
            createPassage(vertical, Math.min(first_y, second_y), 1, Math.abs(second_y - first_y) + 1);
            createPassage(vertical, second_y,               Math.abs(second_x - vertical) + 1, 1);
        }
    }

    public void generateVerticalPassage(int first_room, int second_room, Room[] rooms) {
        Room first_coords = rooms[first_room];
        Room second_coords = rooms[second_room];
        
        int first_y            = first_coords.getCoordinate(Dimention.Y.ordinal()) + first_coords.getSize(Dimention.Y.ordinal()) - 1;
        int up_range_coord     = first_coords.getCoordinate(Dimention.X.ordinal()) + 1;
        int bottom_range_coord = first_coords.getCoordinate(Dimention.X.ordinal()) + first_coords.getSize(Dimention.X.ordinal()) - 2;
        int first_x            = randomInclusive(up_range_coord, bottom_range_coord);
        
        int second_y       = second_coords.getCoordinate(Dimention.Y.ordinal());
        up_range_coord     = second_coords.getCoordinate(Dimention.X.ordinal()) + 1;
        bottom_range_coord = second_coords.getCoordinate(Dimention.X.ordinal()) + second_coords.getSize(Dimention.X.ordinal()) - 2;
        int second_x       = randomInclusive(up_range_coord, bottom_range_coord);
        

        if (first_x == second_x) {
            createPassage(first_x, first_y, 1, Math.abs(second_y - first_y) + 1);
        } else {
            int horizont = randomInclusive(Math.min(first_y, second_y) + 1, Math.max(first_y, second_y) - 1);
            createPassage(first_x,                first_y,  1, Math.abs(horizont - first_y) + 1);
            createPassage(Math.min(first_x, second_x), horizont, Math.abs(second_x - first_x) + 1, 1);
            createPassage(second_x,               horizont, 1, Math.abs(second_y - horizont) + 1);
        }
    }

    public ObjectT[] getPassages() {
        return passages;
    }

    public void setPassages(ObjectT[] passages) {
        this.passages = passages;
    }

    public int getPassagesNum() {
        return passagesNum;
    }

    public void setPassagesNum(int passagesNum) {
        this.passagesNum = passagesNum;
    }
}
