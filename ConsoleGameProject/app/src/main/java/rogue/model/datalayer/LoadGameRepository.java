package rogue.model.datalayer;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import rogue.controller.GameInfo;
import rogue.model.domain.entities.Backpack;
import rogue.model.domain.entities.Level;
import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.buff.Buff;
import rogue.model.domain.entities.buff.Buffs;
import rogue.model.domain.entities.characters.Character;
import rogue.model.domain.entities.characters.Player;
import rogue.model.domain.entities.characters.communicators.Communicator;
import rogue.model.domain.entities.characters.communicators.CommunicatorFactory;
import rogue.model.domain.entities.consumables.ConsumablesRoom;
import rogue.model.domain.entities.consumables.ItemRoom;
import rogue.model.domain.entities.consumables.items.Elixir;
import rogue.model.domain.entities.consumables.items.Food;
import rogue.model.domain.entities.consumables.items.Item;
import rogue.model.domain.entities.consumables.items.Scroll;
import rogue.model.domain.entities.consumables.items.Weapon;
import rogue.model.domain.entities.passages.Passages;
import rogue.model.domain.entities.places.Place;
import rogue.model.domain.enums.Directions;
import rogue.model.domain.enums.Dimention;
import rogue.model.domain.enums.AttractivenessType;
import rogue.model.domain.enums.CommunicatorType;
import rogue.model.domain.enums.StatType;
import rogue.view.Map;

public class LoadGameRepository {

    public static final String SAVE_PATH       = "./data/save.json";
    public static final String STATISTICS_PATH = "./data/statistics.json";

    public static GameInfo initGameInfo() {
        GameInfo gameInfo = new GameInfo();
        gameInfo.getMap().clear();
        SessionStat.resetCurrent();
        gameInfo.setSessionStat(SessionStat.loadCurrent());
        Level level = gameInfo.getLevel();
        level.generateNextLevel();
        gameInfo.getPlayer().setCoordinates(level.getRoom(Level.START_ROOM_INDEX).getRandomFreeCellInRoom().getCoordinates());
        return gameInfo;
    }

    public static GameInfo loadGameInfo() {
        try {
            Object obj = new JSONParser().parse(new FileReader(SAVE_PATH));
            JSONObject jo = (JSONObject) obj;

            JSONObject mapData    = (JSONObject) jo.get("map");
            JSONObject playerData = (JSONObject) jo.get("player");
            JSONObject levelData  = (JSONObject) jo.get("level");

            Map    map    = loadMapData(mapData);
            Player player = loadPlayerData(playerData);
            Level  level  = loadLevelData(levelData);

            SessionStat currentStat = SessionStat.loadCurrent();
            return new GameInfo(map, player, level, currentStat);
        } catch (Throwable some) {
            some.printStackTrace();
            return initGameInfo();
        }
    }

    public static void saveGameInfo(GameInfo gameInfo) {
        try {
            JSONObject data = new JSONObject();
            data.put("player", playerToJson(gameInfo.getPlayer()));
            data.put("level",  levelToJson(gameInfo.getLevel()));
            data.put("map",    mapToJson(gameInfo.getMap()));

            try (FileWriter writer = new FileWriter(SAVE_PATH)) {
                writer.write(data.toJSONString());
            }
        } catch (Throwable some) {
            some.printStackTrace();
        }
    }

    // ---- LOAD helpers ----

    private static ObjectT loadCoords(JSONObject coordsData) {
        JSONArray coordinates = (JSONArray) coordsData.get("coordinates");
        JSONArray size        = (JSONArray) coordsData.get("size");
        int x = (int)(long) coordinates.get(0);
        int y = (int)(long) coordinates.get(1);
        int w = (int)(long) size.get(0);
        int h = (int)(long) size.get(1);
        return new ObjectT(new int[]{x, y}, new int[]{w, h});
    }

    private static Map loadMapData(JSONObject mapData) {
        try {
            JSONArray visibleRoomsJSON    = (JSONArray) mapData.get("visibleRooms");
            JSONArray visiblePassagesJSON = (JSONArray) mapData.get("visiblePassages");

            boolean[] visibleRooms = new boolean[Room.ROOMS_NUM];
            for (int i = 0; i < visibleRooms.length && i < visibleRoomsJSON.size(); i++) {
                visibleRooms[i] = (boolean) visibleRoomsJSON.get(i);
            }

            ArrayList<Boolean> visiblePassages = new ArrayList<>();
            for (int i = 0; i < visiblePassagesJSON.size(); i++) {
                visiblePassages.add((boolean) visiblePassagesJSON.get(i));
            }

            return new Map(visibleRooms, visiblePassages);
        } catch (Throwable some) {
            some.printStackTrace();
            return new Map();
        }
    }

    private static Player loadPlayerData(JSONObject playerData) {
        JSONObject baseStatsData = (JSONObject) playerData.get("baseStats");
        int maxHP     = (int)(long) playerData.get("maxHP");
        JSONObject backpackData  = (JSONObject) playerData.get("backpack");
        int weaponStr = (int)(long) playerData.get("weaponImpulsePower");
        JSONObject buffsData     = (JSONObject) playerData.get("buffs");

        int actPowers   = (int)(long) baseStatsData.get("actPowers");
        int relationQuality  = (int)(long) baseStatsData.get("relationQuality");
        int impulsePower = (int)(long) baseStatsData.get("impulsePower");
        ObjectT coords = loadCoords((JSONObject) baseStatsData.get("coords"));

        Backpack backpack = loadBackpack(backpackData);
        Weapon weapon = weaponStr > 0 ? new Weapon("", weaponStr) : null;
        Buffs buffs = loadBuffs(buffsData);

        return new Player(coords, actPowers, relationQuality, impulsePower, maxHP, backpack, weapon, buffs);
    }

    private static Backpack loadBackpack(JSONObject backpackData) {
        LinkedList<Food> foods = new LinkedList<>();
        JSONObject foodsData = (JSONObject) backpackData.get("foods");
        JSONArray foodRegen  = (JSONArray) foodsData.get("foodRegen");
        JSONArray foodName   = (JSONArray) foodsData.get("foodName");
        for (int i = 0; i < foodRegen.size(); i++) {
            int regen   = (int)(long) foodRegen.get(i);
            String name = (String) foodName.get(i);
            foods.add(new Food(name, regen));
        }

        LinkedList<Elixir> elixirs = new LinkedList<>();
        JSONObject elixirsData   = (JSONObject) backpackData.get("elixirs");
        JSONArray elixirType     = (JSONArray) elixirsData.get("elixirType");
        JSONArray elixirValue    = (JSONArray) elixirsData.get("elixirValue");
        JSONArray elixirDuration = (JSONArray) elixirsData.get("elixirDuration");
        JSONArray elixirName     = (JSONArray) elixirsData.get("elixirName");
        for (int i = 0; i < elixirType.size(); i++) {
            int typeOrd  = (int)(long) elixirType.get(i);
            int value    = (int)(long) elixirValue.get(i);
            int duration = (int)(long) elixirDuration.get(i);
            String name  = (String) elixirName.get(i);
            elixirs.add(new Elixir(name, StatType.values()[typeOrd], value, Duration.ofSeconds(duration)));
        }

        LinkedList<Scroll> scrolls = new LinkedList<>();
        JSONObject scrollsData = (JSONObject) backpackData.get("scrolls");
        JSONArray scrollType   = (JSONArray) scrollsData.get("scrollType");
        JSONArray scrollValue  = (JSONArray) scrollsData.get("scrollValue");
        JSONArray scrollName   = (JSONArray) scrollsData.get("scrollName");
        for (int i = 0; i < scrollType.size(); i++) {
            int typeOrd = (int)(long) scrollType.get(i);
            int value   = (int)(long) scrollValue.get(i);
            String name = (String) scrollName.get(i);
            scrolls.add(new Scroll(name, StatType.values()[typeOrd], value));
        }

        LinkedList<Weapon> weapons = new LinkedList<>();
        JSONObject weaponsData   = (JSONObject) backpackData.get("weapons");
        JSONArray weaponImpulsePower = (JSONArray) weaponsData.get("weaponImpulsePower");
        JSONArray weaponName     = (JSONArray) weaponsData.get("weaponName");
        for (int i = 0; i < weaponImpulsePower.size(); i++) {
            int str     = (int)(long) weaponImpulsePower.get(i);
            String name = (String) weaponName.get(i);
            weapons.add(new Weapon(name, str));
        }

        int treasure = (int)(long) backpackData.get("treasures");
        return new Backpack(foods, elixirs, scrolls, weapons, treasure);
    }

    private static Buffs loadBuffs(JSONObject buffsData) {
        JSONObject maxActPowersData = (JSONObject) buffsData.get("max_actPowers");
        JSONArray mhIncrease     = (JSONArray) maxActPowersData.get("increase");
        JSONArray mhDuration     = (JSONArray) maxActPowersData.get("duration");
        int actPowersBuffNum = mhIncrease.size();
        ArrayList<Buff> maxActPowers = new ArrayList<>(actPowersBuffNum);
        for (int i = 0; i < actPowersBuffNum; i++) {
            maxActPowers.add(new Buff((int)(long) mhIncrease.get(i), Duration.ofSeconds((long) mhDuration.get(i))));
        }

        JSONObject relationQualityData = (JSONObject) buffsData.get("relationQuality");
        JSONArray agIncrease   = (JSONArray) relationQualityData.get("increase");
        JSONArray agDuration   = (JSONArray) relationQualityData.get("duration");
        int relationQualityBuffNum = agIncrease.size();
        ArrayList<Buff> relationQuality = new ArrayList<>(relationQualityBuffNum);
        for (int i = 0; i < relationQualityBuffNum; i++) {
            relationQuality.add(new Buff((int)(long) agIncrease.get(i), Duration.ofSeconds((long) agDuration.get(i))));
        }

        JSONObject impulsePowerData = (JSONObject) buffsData.get("impulsePower");
        JSONArray stIncrease    = (JSONArray) impulsePowerData.get("increase");
        JSONArray stDuration    = (JSONArray) impulsePowerData.get("duration");
        int impulsePowerBuffNum = stIncrease.size();
        ArrayList<Buff> impulsePower = new ArrayList<>(impulsePowerBuffNum);
        for (int i = 0; i < impulsePowerBuffNum; i++) {
            impulsePower.add(new Buff((int)(long) stIncrease.get(i), Duration.ofSeconds((long) stDuration.get(i))));
        }

        return new Buffs(maxActPowers, actPowersBuffNum, relationQuality, relationQualityBuffNum, impulsePower, impulsePowerBuffNum);
    }

    private static Place loadPlace(JSONObject placeData) {
        JSONObject itemsObj = (JSONObject) placeData.get("items");
        JSONArray nameFood = (JSONArray) itemsObj.get("name");
        JSONArray regenFood = (JSONArray) itemsObj.get("regen");
        JSONObject placeCoords = (JSONObject) placeData.get("placeCoords");

        int foodNum = regenFood.size();
        List<Item> items = new ArrayList<>(foodNum);
        for (int i = 0; i < foodNum; i++) {
            int regen = (int)(long) regenFood.get(i);
            String name = (String) nameFood.get(i);
            items.add(new Food(name, regen));
        }

        ObjectT geometry = loadCoords(placeCoords);

        return new Place(items, geometry);
    }

    private static ConsumablesRoom loadConsumables(JSONObject consumData) {
        List<ItemRoom<?>> itemRooms = new ArrayList<>();

        JSONObject roomFoodData = (JSONObject) consumData.get("roomFood");
        JSONArray foodRegen     = (JSONArray) roomFoodData.get("regen");
        JSONArray foodGeometry  = (JSONArray) roomFoodData.get("geometry");
        JSONArray foodName      = (JSONArray) roomFoodData.get("name");
        int foodNum = foodRegen.size();
        for (int i = 0; i < foodNum; i++) {
            int regen   = (int)(long) foodRegen.get(i);
            String name = (String) foodName.get(i);
            ObjectT geom = loadCoords((JSONObject) foodGeometry.get(i));
            itemRooms.add(new ItemRoom<Food>(new Food(name, regen), geom));
        }

        JSONObject roomElixirsData = (JSONObject) consumData.get("roomElixirs");
        JSONArray elixirType       = (JSONArray) roomElixirsData.get("elixirType");
        JSONArray elixirValue      = (JSONArray) roomElixirsData.get("elixirValue");
        JSONArray elixirDuration   = (JSONArray) roomElixirsData.get("elixirDuration");
        JSONArray elixirGeometry   = (JSONArray) roomElixirsData.get("geometry");
        JSONArray elixirName       = (JSONArray) roomElixirsData.get("name");
        int elixirNum = elixirType.size();
        List<ItemRoom<Elixir>> elixirRooms = new ArrayList<>(elixirNum);
        for (int i = 0; i < elixirNum; i++) {
            int typeOrd  = (int)(long) elixirType.get(i);
            int value    = (int)(long) elixirValue.get(i);
            int duration = (int)(long) elixirDuration.get(i);
            String name  = (String) elixirName.get(i);
            ObjectT geom = loadCoords((JSONObject) elixirGeometry.get(i));
            itemRooms.add(new ItemRoom<Elixir>(
                new Elixir(name, StatType.values()[typeOrd], value, Duration.ofSeconds(duration)), geom));
        }

        JSONObject roomScrollsData = (JSONObject) consumData.get("roomScrolls");
        JSONArray scrollType       = (JSONArray) roomScrollsData.get("scrollType");
        JSONArray scrollValue      = (JSONArray) roomScrollsData.get("scrollValue");
        JSONArray scrollGeometry   = (JSONArray) roomScrollsData.get("geometry");
        JSONArray scrollName       = (JSONArray) roomScrollsData.get("name");
        int scrollNum = scrollType.size();
        List<ItemRoom<Scroll>> scrollRooms = new ArrayList<>(scrollNum);
        for (int i = 0; i < scrollNum; i++) {
            int typeOrd = (int)(long) scrollType.get(i);
            int value   = (int)(long) scrollValue.get(i);
            String name = (String) scrollName.get(i);
            ObjectT geom = loadCoords((JSONObject) scrollGeometry.get(i));
            itemRooms.add(new ItemRoom<Scroll>(new Scroll(name, StatType.values()[typeOrd], value), geom));
        }

        JSONObject roomWeaponData = (JSONObject) consumData.get("roomWeapon");
        JSONArray weaponImpulsePower  = (JSONArray) roomWeaponData.get("impulsePower");
        JSONArray weaponGeometry  = (JSONArray) roomWeaponData.get("geometry");
        JSONArray weaponName      = (JSONArray) roomWeaponData.get("name");
        int weaponNum = weaponImpulsePower.size();
        List<ItemRoom<Weapon>> weaponRooms = new ArrayList<>(weaponNum);
        for (int i = 0; i < weaponNum; i++) {
            int str     = (int)(long) weaponImpulsePower.get(i);
            String name = (String) weaponName.get(i);
            ObjectT geom = loadCoords((JSONObject) weaponGeometry.get(i));
            itemRooms.add(new ItemRoom<Weapon>(new Weapon(name, str), geom));
        }

        return new ConsumablesRoom(itemRooms);
    }

    private static Level loadLevelData(JSONObject levelData) {
        try {
            JSONArray roomsData    = (JSONArray) levelData.get("rooms");
            JSONArray passagesData = (JSONArray) levelData.get("passages");
            int levelNum           = (int)(long) levelData.get("levelNum");
            ObjectT endOfLevel     = loadCoords((JSONObject) levelData.get("endOfLevel"));

            Room[] rooms = new Room[Room.ROOMS_NUM];
            for (int i = 0; i < Room.ROOMS_NUM; i++) {
                JSONObject roomData    = (JSONObject) roomsData.get(i);
                ObjectT roomCoords     = loadCoords((JSONObject) roomData.get("coords"));
                ConsumablesRoom consums = loadConsumables((JSONObject) roomData.get("consumablesRoom"));
                JSONArray communicatorsData = (JSONArray) roomData.get("communicators");
                Place place = loadPlace((JSONObject) roomData.get("place"));

                int communicatorNum = communicatorsData.size();
                ArrayList<Communicator> communicators = new ArrayList<Communicator>();
                for (int j = 0; j < communicatorNum; j++) {
                    JSONObject md     = (JSONObject) communicatorsData.get(j);
                    JSONObject mStats = (JSONObject) md.get("baseStats");
                    int mActPowers   = (int)(long) mStats.get("actPowers");
                    int mRelationQuality  = (int)(long) mStats.get("relationQuality");
                    int mImpulsePower = (int)(long) mStats.get("impulsePower");
                    ObjectT mCoords = loadCoords((JSONObject) mStats.get("coords"));
                    int typeOrd     = (int)(long) md.get("type");
                    int hostOrd     = (int)(long) md.get("attractiveness");
                    boolean chasing = (boolean) md.get("chasing");
                    int dirOrd      = (int)(long) md.get("direction");
                    communicators.add(CommunicatorFactory.getCommunicator(CommunicatorType.values()[typeOrd], mCoords, 
                    mActPowers, mRelationQuality, mImpulsePower, AttractivenessType.values()[hostOrd], chasing, Directions.values()[dirOrd]));
                }

                rooms[i] = new Room(consums, communicators);
                rooms[i].setPlace(place);
                rooms[i].setCoordinates(roomCoords.getCoordinates());
                rooms[i].setSizes(roomCoords.getSizes());
            }

            int passagesNum = passagesData.size();
            ObjectT[] passageObjs = new ObjectT[Passages.MAX_PASSAGES_NUM];
            for (int i = 0; i < passagesNum; i++) {
                passageObjs[i] = loadCoords((JSONObject) passagesData.get(i));
            }
            Passages passages = new Passages(passageObjs, passagesNum);

            return new Level(rooms, passages, levelNum, endOfLevel);
        } catch (Throwable some) {
            some.printStackTrace();
            return new Level();
        }
    }

    // ---- SAVE helpers ----

    private static JSONObject coordsToJson(ObjectT obj) {
        JSONObject coords = new JSONObject();
        JSONArray coordinates = new JSONArray();
        JSONArray size        = new JSONArray();
        coordinates.add((long) obj.getCoordinate(Dimention.X.ordinal()));
        coordinates.add((long) obj.getCoordinate(Dimention.Y.ordinal()));
        size.add((long) obj.getSize(Dimention.X.ordinal()));
        size.add((long) obj.getSize(Dimention.Y.ordinal()));
        coords.put("coordinates", coordinates);
        coords.put("size", size);
        return coords;
    }

    private static JSONObject baseStatsToJson(Character character) {
        JSONObject stats = new JSONObject();
        stats.put("actPowers",   (long)(int) character.getActPowers());
        stats.put("relationQuality",  (long) character.getRelationQuality());
        stats.put("impulsePower", (long) character.getImpulsePower());
        stats.put("coords",   coordsToJson(character));
        return stats;
    }

    private static JSONObject backpackToJson(Backpack backpack) {
        JSONObject bp = new JSONObject();

        JSONObject foods    = new JSONObject();
        JSONArray foodRegen = new JSONArray();
        JSONArray foodName  = new JSONArray();
        for (int i = 0; i < backpack.getFoodNum(); i++) {
            foodRegen.add((long) backpack.getFood(i).getToRegen());
            foodName.add(backpack.getFood(i).getName());
        }
        foods.put("foodRegen", foodRegen);
        foods.put("foodName",  foodName);

        JSONObject elixirs      = new JSONObject();
        JSONArray elixirType     = new JSONArray();
        JSONArray elixirValue    = new JSONArray();
        JSONArray elixirDuration = new JSONArray();
        JSONArray elixirName     = new JSONArray();
        for (int i = 0; i < backpack.getElixirNum(); i++) {
            Elixir e = backpack.getElixir(i);
            elixirType.add((long) e.getStat().ordinal());
            elixirValue.add((long) e.getIncrease());
            elixirDuration.add(e.getDuration().toSeconds());
            elixirName.add(e.getName());
        }
        elixirs.put("elixirType",     elixirType);
        elixirs.put("elixirValue",    elixirValue);
        elixirs.put("elixirDuration", elixirDuration);
        elixirs.put("elixirName",     elixirName);

        JSONObject scrolls   = new JSONObject();
        JSONArray scrollType  = new JSONArray();
        JSONArray scrollValue = new JSONArray();
        JSONArray scrollName  = new JSONArray();
        for (int i = 0; i < backpack.getScrollNum(); i++) {
            Scroll s = backpack.getScroll(i);
            scrollType.add((long) s.getStat().ordinal());
            scrollValue.add((long) s.getIncrease());
            scrollName.add(s.getName());
        }
        scrolls.put("scrollType",  scrollType);
        scrolls.put("scrollValue", scrollValue);
        scrolls.put("scrollName",  scrollName);

        JSONObject weapons       = new JSONObject();
        JSONArray weaponImpulsePower = new JSONArray();
        JSONArray weaponName     = new JSONArray();
        for (int i = 0; i < backpack.getWeaponNum(); i++) {
            Weapon w = backpack.getWeapon(i);
            weaponImpulsePower.add((long) w.getImpulsePower());
            weaponName.add(w.getName());
        }
        weapons.put("weaponImpulsePower", weaponImpulsePower);
        weapons.put("weaponName",     weaponName);

        bp.put("foods",    foods);
        bp.put("elixirs",  elixirs);
        bp.put("scrolls",  scrolls);
        bp.put("weapons",  weapons);
        bp.put("treasures", (long) backpack.getTreasure());
        return bp;
    }

    private static JSONObject buffsToJson(Buffs buffs) {
        JSONObject buffsObj = new JSONObject();

        JSONObject maxActPowers = new JSONObject();
        JSONArray mhInc = new JSONArray();
        JSONArray mhDur = new JSONArray();
        for (int i = 0; i < buffs.getMaxActPowers().size(); i++) {
            mhInc.add((long) buffs.getMaxActPowers().get(i).getStatIncrease());
            mhDur.add((long) buffs.getMaxActPowers().get(i).getEffectEnd().toSeconds());
        }
        maxActPowers.put("increase", mhInc);
        maxActPowers.put("duration", mhDur);

        JSONObject relationQuality = new JSONObject();
        JSONArray agInc = new JSONArray();
        JSONArray agDur = new JSONArray();
        for (int i = 0; i < buffs.getRelationQuality().size(); i++) {
            agInc.add((long) buffs.getRelationQuality().get(i).getStatIncrease());
            agDur.add((long) buffs.getRelationQuality().get(i).getEffectEnd().toSeconds());
        }
        relationQuality.put("increase", agInc);
        relationQuality.put("duration", agDur);

        JSONObject impulsePower = new JSONObject();
        JSONArray stInc = new JSONArray();
        JSONArray stDur = new JSONArray();
        for (int i = 0; i < buffs.getImpulsePower().size(); i++) {
            stInc.add((long) buffs.getImpulsePower().get(i).getStatIncrease());
            stDur.add((long) buffs.getImpulsePower().get(i).getEffectEnd().toSeconds());
        }
        impulsePower.put("increase", stInc);
        impulsePower.put("duration", stDur);

        buffsObj.put("max_actPowers", maxActPowers);
        buffsObj.put("relationQuality",    relationQuality);
        buffsObj.put("impulsePower",   impulsePower);
        return buffsObj;
    }

    private static JSONObject playerToJson(Player player) {
        JSONObject playerObj = new JSONObject();
        playerObj.put("baseStats",     baseStatsToJson(player));
        playerObj.put("maxHP",         (long) player.getRegenLimit());
        playerObj.put("backpack",      backpackToJson(player.getBackpack()));
        playerObj.put("weaponImpulsePower", (long)(player.getWeapon() != null ? player.getWeapon().getImpulsePower() : 0));
        playerObj.put("buffs",         buffsToJson(player.getElixirBuffs()));
        return playerObj;
    }

    private static JSONObject placeToJson(Place place) {
        JSONObject placeObj = new JSONObject();

        JSONObject items = new JSONObject();
        JSONArray toRegen      = new JSONArray();
        JSONArray nameFood     = new JSONArray();

        for (Item item : place.getItems()) {
            Food food = (Food) item;
            toRegen.add(food.getToRegen());
            nameFood.add(food.getName());
        }

        JSONObject placeCoords = coordsToJson(place.getGeometry());

        items.put("name", nameFood);
        items.put("regen",    toRegen);
        placeObj.put("items", items);
        placeObj.put("placeCoords", placeCoords);

        return placeObj;
    }

    private static JSONObject consumablesToJson(ConsumablesRoom consumables) {
        JSONObject consum = new JSONObject();

        JSONObject roomFood    = new JSONObject();
        JSONArray toRegen      = new JSONArray();
        JSONArray geometryFood = new JSONArray();
        JSONArray nameFood     = new JSONArray();

        JSONObject roomElixirs    = new JSONObject();
        JSONArray elixirType       = new JSONArray();
        JSONArray elixirValue      = new JSONArray();
        JSONArray elixirDuration   = new JSONArray();
        JSONArray geometryElixirs  = new JSONArray();
        JSONArray nameElixirs      = new JSONArray();

        JSONObject roomScrolls    = new JSONObject();
        JSONArray scrollType       = new JSONArray();
        JSONArray scrollValue      = new JSONArray();
        JSONArray geometryScrolls  = new JSONArray();
        JSONArray nameScrolls      = new JSONArray();
        
        JSONObject roomWeapon     = new JSONObject();
        JSONArray impulsePower         = new JSONArray();
        JSONArray geometryWeapon   = new JSONArray();
        JSONArray nameWeapon       = new JSONArray();

        for (ItemRoom<?> itemRoom : consumables.getItems()) {
            Item item = itemRoom.getItem();

            if (item instanceof Food) {
                Food food = (Food) item;
                toRegen.add(food.getToRegen());
                geometryFood.add(coordsToJson(itemRoom.getCoordinates()));
                nameFood.add(item.getName());

            } else if (item instanceof Elixir) {
                Elixir elixir = (Elixir) item;
                elixirType.add(elixir.getStat().ordinal());
                elixirValue.add(elixir.getIncrease());
                elixirDuration.add(elixir.getDuration().toSeconds());
                geometryElixirs.add(coordsToJson(itemRoom.getCoordinates()));
                nameElixirs.add(item.getName());

            } else if (item instanceof Scroll) {
                Scroll scroll = (Scroll) item;
                scrollType.add(scroll.getStat().ordinal());
                scrollValue.add(scroll.getIncrease());
                geometryScrolls.add(coordsToJson(itemRoom.getCoordinates()));
                nameScrolls.add(item.getName());

            } else if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                impulsePower.add(weapon.getImpulsePower());
                geometryWeapon.add(coordsToJson(itemRoom.getCoordinates()));
                nameWeapon.add(item.getName());
        }
    }

        roomFood.put("regen",    toRegen);
        roomFood.put("geometry", geometryFood);
        roomFood.put("name",     nameFood);

       
        roomElixirs.put("elixirType",     elixirType);
        roomElixirs.put("elixirValue",    elixirValue);
        roomElixirs.put("elixirDuration", elixirDuration);
        roomElixirs.put("geometry",       geometryElixirs);
        roomElixirs.put("name",           nameElixirs);

        roomScrolls.put("scrollType",  scrollType);
        roomScrolls.put("scrollValue", scrollValue);
        roomScrolls.put("geometry",    geometryScrolls);
        roomScrolls.put("name",        nameScrolls);

        roomWeapon.put("impulsePower", impulsePower);
        roomWeapon.put("geometry", geometryWeapon);
        roomWeapon.put("name",     nameWeapon);

        consum.put("roomFood",    roomFood);
        consum.put("roomScrolls", roomScrolls);
        consum.put("roomElixirs", roomElixirs);
        consum.put("roomWeapon",  roomWeapon);
        return consum;
    }

    private static JSONObject levelToJson(Level level) {
        JSONObject levelObj = new JSONObject();

        JSONArray rooms = new JSONArray();
        for (int i = 0; i < Room.ROOMS_NUM; i++) {
            Room room = level.getRoom(i);
            JSONObject roomObj = new JSONObject();
            roomObj.put("coords",          coordsToJson(room));
            roomObj.put("consumablesRoom", consumablesToJson(room.getConsumables()));
            Place place = room.getPlace();
            if (Objects.nonNull(place)) roomObj.put("place", placeToJson(room.getPlace()));

            JSONArray communicators = new JSONArray();
            ArrayList<Communicator> roomCommunicators = room.getCommunicators();
            if (roomCommunicators != null) {
                for (int j = 0; j < room.getCommunicators().size(); j++) {
                    Communicator m = roomCommunicators.get(j);
                    if (m == null) continue;
                    JSONObject communicatorObj = new JSONObject();
                    communicatorObj.put("baseStats",  baseStatsToJson(m));
                    communicatorObj.put("type",       (long) m.getType().ordinal());
                    communicatorObj.put("attractiveness",  (long) m.getAttractiveness().ordinal());
                    communicatorObj.put("chasing",    m.isChasing());
                    communicatorObj.put("direction",  (long) m.getDir().ordinal());
                    communicators.add(communicatorObj);
                }
            }
            roomObj.put("communicators", communicators);
            rooms.add(roomObj);
        }
        levelObj.put("rooms", rooms);

        JSONArray passages = new JSONArray();
        for (int i = 0; i < level.getPassages().getPassagesNum(); i++) {
            passages.add(coordsToJson(level.getPassages().getPassages()[i]));
        }
        levelObj.put("passages",   passages);
        levelObj.put("levelNum",   (long) level.getLevelNum());
        levelObj.put("endOfLevel", coordsToJson(level.getEndOfLevel()));
        return levelObj;
    }

    private static JSONObject mapToJson(Map map) {
        JSONObject mapObj = new JSONObject();

        JSONArray visibleRooms = new JSONArray();
        for (int i = 0; i < Room.ROOMS_NUM; i++) {
            visibleRooms.add(map.getVisibleRoom(i));
        }
        mapObj.put("visibleRooms", visibleRooms);

        JSONArray visiblePassages = new JSONArray();
        for (Boolean b : map.getVisiblePassages()) {
            visiblePassages.add(b);
        }
        mapObj.put("visiblePassages", visiblePassages);
        return mapObj;
    }
}
