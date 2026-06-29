package rogue.model.datalayer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SessionStat {
    public static final String SCOREBOARD_PATH = "./data/scoreboard.json";
    public static final String STATISTICS_PATH = "./data/statistics.json";
    public static final int MAX_SCOREBOARD_SIZE = 10;

    private int treasures;
    private int level;
    private int enemies;
    private int food;
    private int elixirs;
    private int scrolls;
    private int talkTos;
    private int missed;
    private int moves;

    public SessionStat() {
        reset();
    }

    public void reset() {
        treasures = 0;
        level = 0;
        enemies = 0;
        food = 0;
        elixirs = 0;
        scrolls = 0;
        talkTos = 0;
        missed = 0;
        moves = 0;
    }

    public int getTreasures() {
        return treasures;
    }

    public void setTreasures(int treasures) {
        this.treasures = treasures;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnemies() {
        return enemies;
    }

    public void incEnemies() {
        enemies++;
    }

    public int getFood() {
        return food;
    }

    public void incFood() {
        food++;
    }

    public int getElixirs() {
        return elixirs;
    }

    public void incElixirs() {
        elixirs++;
    }

    public int getScrolls() {
        return scrolls;
    }

    public void incScrolls() {
        scrolls++;
    }

    public int getAttacks() {
        return talkTos;
    }

    public void incAttacks() {
        talkTos++;
    }

    public int getMissed() {
        return missed;
    }

    public void incMissed() {
        missed++;
    }

    public int getMoves() {
        return moves;
    }

    public void incMoves() {
        moves++;
    }

    private JSONObject toJson() {
        JSONObject statJson = new JSONObject();
        statJson.put("treasures", (long) treasures);
        statJson.put("level", (long) level);
        statJson.put("enemies", (long) enemies);
        statJson.put("food", (long) food);
        statJson.put("elixirs", (long) elixirs);
        statJson.put("scrolls", (long) scrolls);
        statJson.put("talkTos", (long) talkTos);
        statJson.put("missed", (long) missed);
        statJson.put("moves", (long) moves);
        return statJson;
    }

    private static SessionStat fromJson(JSONObject ob) {
        SessionStat stat = new SessionStat();
        stat.treasures = getInt(ob, "treasures");
        stat.level = getInt(ob, "level");
        stat.enemies = getInt(ob, "enemies");
        stat.food = getInt(ob, "food");
        stat.elixirs = getInt(ob, "elixirs");
        stat.scrolls = getInt(ob, "scrolls");
        stat.talkTos = getInt(ob, "talkTos");
        stat.missed = getInt(ob, "missed");
        stat.moves = getInt(ob, "moves");
        return stat;
    }

    private static int getInt(JSONObject ob, String key) {
        Object value = ob.get(key);
        if (value instanceof Number num) {
            return num.intValue();
        }
        return 0;
    }

    public static SessionStat loadCurrent() {
        try {
            File statFile = new File(STATISTICS_PATH);
            if (!statFile.exists()) {
                SessionStat empty = new SessionStat();
                saveCurrent(empty);
                return empty;
            }

            Object obj = new JSONParser().parse(new FileReader(statFile));
            if (obj instanceof JSONObject jsonObject) {
                return fromJson(jsonObject);
            }
        } catch (Throwable some) {
            some.printStackTrace();
        }
        return new SessionStat();
    }

    public static void saveCurrent(SessionStat stat) {
        try (FileWriter writer = new FileWriter(STATISTICS_PATH)) {
            writer.write(stat.toJson().toJSONString());
        } catch (Throwable some) {
            some.printStackTrace();
        }
    }

    public static void resetCurrent() {
        saveCurrent(new SessionStat());
    }

    public static void appendCurrentToScoreboard() {
        appendToScoreboard(loadCurrent());
    }

    public static void appendToScoreboard(SessionStat stat) {
        try {
            JSONObject scoreboard = new JSONObject();
            JSONArray sessionStats = new JSONArray();
            File scoreboardFile = new File(SCOREBOARD_PATH);

            if (scoreboardFile.exists()) {
                Object obj = new JSONParser().parse(new FileReader(scoreboardFile));
                if (obj instanceof JSONObject existingScoreboard) {
                    scoreboard = existingScoreboard;
                }
            }

            Object existingArray = scoreboard.get("sessionStats");
            if (existingArray instanceof JSONArray arr) {
                sessionStats = arr;
            }

            sessionStats.add(stat.toJson());
            scoreboard.put("sessionStats", sessionStats);

            try (FileWriter writer = new FileWriter(scoreboardFile)) {
                writer.write(scoreboard.toJSONString());
            }
        } catch (Throwable some) {
            some.printStackTrace();
        }
    }

    public static SessionStat[] getSessionStats() {
        List<SessionStat> array = new ArrayList<>();

        try {
            Object obj = new JSONParser().parse(new FileReader(SCOREBOARD_PATH));
            JSONObject jo = (JSONObject) obj;
            JSONArray sessionStats = (JSONArray) jo.get("sessionStats");

            if (sessionStats != null) {
                for (Object entry : sessionStats) {
                    if (entry instanceof JSONObject statJson) {
                        array.add(fromJson(statJson));
                    }
                }
            }
        } catch (Throwable some) {
            some.printStackTrace();
        }

        array.sort(Comparator.comparingInt(SessionStat::getTreasures).reversed());

        int totalSize = Math.min(array.size(), MAX_SCOREBOARD_SIZE);
        SessionStat[] stats = new SessionStat[totalSize];
        for (int i = 0; i < totalSize; ++i) {
            stats[i] = array.get(i);
        }
        return stats;
    }
}
