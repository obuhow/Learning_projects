package rogue.model.domain.entities.passages;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rogue.model.domain.entities.Room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Edge {
    private int u;
    private int v;

    public static final int MAX_EDGES_NUM = 12;

    private static final Logger log = LoggerFactory.getLogger(Edge.class);

    public static Edge[] generateEdgesForRooms() {
        log.debug("Начало генерации граней");
        int count_edges = 0;
        var edges = new Edge[MAX_EDGES_NUM];

        for (int i = 0; i < Room.ROOMS_IN_HEIGHT; i++) {
            for (int j = 0; j + 1 < Room.ROOMS_IN_WIDTH; j++) {
                int current_room = i * Room.ROOMS_IN_HEIGHT + j;
                edges[count_edges] = new Edge();
                edges[count_edges].u = current_room;
                edges[count_edges].v = current_room + 1;
                count_edges++;
            }
        }
        
        for (int i = 0; i + 1 < Room.ROOMS_IN_HEIGHT; i++) {
            for (int j = 0; j < Room.ROOMS_IN_WIDTH; j++) {
                int current_room = i * Room.ROOMS_IN_HEIGHT + j;
                edges[count_edges] = new Edge();
                edges[count_edges].u = current_room;
                edges[count_edges].v = current_room + Room.ROOMS_IN_WIDTH;
                count_edges++;
            }
        }
        List<Edge> temp = Arrays.asList(edges);
        Collections.shuffle(temp);
        return temp.toArray(edges);
    }

    public void setU(int u) {
        this.u = u;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }
}
