package rogue.controller;

import java.util.LinkedList;

import rogue.model.domain.entities.BattleInfo;
import rogue.model.domain.entities.Level;
import rogue.model.domain.entities.characters.Player;
import rogue.model.datalayer.SessionStat;
import rogue.view.Map;

public class GameInfo {
    private Map map;
    private Player player;
    private Level level;
    private LinkedList<BattleInfo> battles;
    private SessionStat sessionStat;

    public static final int MAXIMUM_FIGHTS = 8;

    public GameInfo() {
        this.map = new Map();
        this.level = new Level();
        this.player = new Player();
        this.battles = new LinkedList<>();
        this.sessionStat = new SessionStat();
    }

    public GameInfo(Map map, Player player, Level level, LinkedList<BattleInfo> battles) {
        this.map = map;
        this.player = player;
        this.level = level;
        this.battles = battles;
        this.sessionStat = new SessionStat();
    }

    public GameInfo(Map map, Player player, Level level) {
        this.map = map;
        this.player = player;
        this.level = level;
        this.battles = new LinkedList<>();
        this.sessionStat = new SessionStat();
    }

    public GameInfo(Map map, Player player, Level level, SessionStat sessionStat) {
        this.map = map;
        this.player = player;
        this.level = level;
        this.battles = new LinkedList<>();
        this.sessionStat = sessionStat;
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public LinkedList<BattleInfo> getBattles() {
        return battles;
    }

    public SessionStat getSessionStat() {
        return sessionStat;
    }

    public void setSessionStat(SessionStat sessionStat) {
        this.sessionStat = sessionStat;
    }

    public void setBattle(BattleInfo battle) {
        if (battles.size()+1 <= MAXIMUM_FIGHTS) {
            battles.add(battle);
        }
    }

    public BattleInfo getBattle(int index) {
        return battles.get(index);
    }
}
