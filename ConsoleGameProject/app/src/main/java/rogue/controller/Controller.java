package rogue.controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rogue.view.*;
import rogue.model.datalayer.*;
import rogue.model.domain.entities.Level;
import rogue.model.domain.entities.ObjectT;
import rogue.model.domain.entities.Room;
import rogue.model.domain.entities.Backpack;
import rogue.model.domain.entities.buff.Buff;
import rogue.model.domain.entities.characters.Player;
import rogue.model.domain.entities.characters.communicators.Communicator;
import rogue.model.domain.enums.ConsumableTypes;
import rogue.model.domain.enums.Dimention;
import rogue.model.domain.enums.Directions;

public class Controller {
    private static final int FINAL_LEVEL = 21;

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private void game_cycle(GameInfo gameInfo, Presentation presentation) throws InterruptedException { 
        boolean running = true;
        Player player = gameInfo.getPlayer();
        while (running) {
            syncCoreSessionStat(gameInfo);
            presentation.displayMap(gameInfo);
            if (processUserInput(gameInfo, presentation)) {
                running = false;
                continue;
            }
            if (player.isInPlace()) {
                presentation.printPlaceInfo(gameInfo.getLevel().getPlaceAt(player.getCoordinate(Dimention.X.ordinal()), player.getCoordinate(Dimention.Y.ordinal())));
                presentation.readKey();
                continue;
            }
                if (isTired(gameInfo)) {
                log.info("Game over");
                SessionStat.appendToScoreboard(gameInfo.getSessionStat());
                SessionStat.resetCurrent();
                presentation.endScreen(0);
                Thread.sleep(10000);
                presentation.readKey();
                running = false;
            } else if (gameInfo.getLevel().checkLevelEnd(gameInfo.getPlayer())) {
                if (gameInfo.getLevel().getLevelNum() >= FINAL_LEVEL) {
                    log.info("Player won");
                    SessionStat.appendToScoreboard(gameInfo.getSessionStat());
                    SessionStat.resetCurrent();
                    Thread.sleep(10000);
                    presentation.endScreen(1);
                    presentation.readKey();
                    running = false;
                } else {
                    advanceToNextLevel(gameInfo);
                }
            }

            if (running)
                LoadGameRepository.saveGameInfo(gameInfo);
        }
    }

    public boolean processUserInput(GameInfo gameInfo, Presentation presentation) {//const char *filename) {
        KeyStroke key = presentation.readKey();
        if (key == null)
            return false;

        syncCoreSessionStat(gameInfo);

        SessionStat sessionStat = gameInfo.getSessionStat();
        Player player = gameInfo.getPlayer();
        Level level = gameInfo.getLevel();
        Backpack backpackBefore = snapshotBackpack(player);
        int communicatorsBefore = countCommunicators(level);

        boolean quit = false;
        boolean actionPerformed = false;
        if (key.getKeyType() == KeyType.Escape) {
            quit = true;
        } else if (key.getKeyType() == KeyType.Character && key.getCharacter() != null) {
            switch (key.getCharacter()) {
                case 'W':
                case 'w':
                    sessionStat.incMoves();
                    if (isPlayerAttackAttempt(player, Directions.FORWARD, level)) sessionStat.incAttacks();
                    actionPerformed = player.tryMove(Directions.FORWARD, level);
                    break;
                case 'A':
                case 'a':
                    sessionStat.incMoves();
                    if (isPlayerAttackAttempt(player, Directions.LEFT, level)) sessionStat.incAttacks();
                    actionPerformed = player.tryMove(Directions.LEFT, level);
                    break;
                case 'S':
                case 's':
                    sessionStat.incMoves();
                    if (isPlayerAttackAttempt(player, Directions.BACK, level)) sessionStat.incAttacks();
                    actionPerformed = player.tryMove(Directions.BACK, level);
                    break;
                case 'D':
                case 'd':
                    sessionStat.incMoves();
                    if (isPlayerAttackAttempt(player, Directions.RIGHT, level)) sessionStat.incAttacks();
                    actionPerformed = player.tryMove(Directions.RIGHT, level);
                    break;
                case 'H':
                case 'h':
                    chooseConsumable(gameInfo, presentation, ConsumableTypes.WEAPON);
                    actionPerformed = true;
                    break;
                case 'J':
                case 'j':
                    chooseConsumable(gameInfo, presentation, ConsumableTypes.FOOD);
                    actionPerformed = true;
                    break;
                case 'K':
                case 'k':
                    chooseConsumable(gameInfo, presentation, ConsumableTypes.ELIXIR);
                    actionPerformed = true;
                    break;
                case 'E':
                case 'e':
                    chooseConsumable(gameInfo, presentation, ConsumableTypes.SCROLL);
                    actionPerformed = true;
                    break;
                case 'o':
                    getExitHere(gameInfo.getLevel(), gameInfo.getPlayer());
                    actionPerformed = true;
                    break;
                default:
                    break;
            }
        }

        int communicatorsAfterPlayerAction = countCommunicators(level);
        if (communicatorsAfterPlayerAction < communicatorsBefore) {
            for (int i = 0; i < communicatorsBefore - communicatorsAfterPlayerAction; i++) sessionStat.incEnemies();
        }

        Backpack backpackAfterAction = player.getBackpack();
        if (backpackAfterAction.getFoodNum() < backpackBefore.getFoodNum()) sessionStat.incFood();
        if (backpackAfterAction.getElixirNum() < backpackBefore.getElixirNum()) sessionStat.incElixirs();
        if (backpackAfterAction.getScrollNum() < backpackBefore.getScrollNum()) sessionStat.incScrolls();
        
        updateBuffs(gameInfo.getPlayer());
        if (actionPerformed && !quit) {
            processCommunicatorsTurn(gameInfo, sessionStat);
            level.checkAndSetCommunicatorsRoom();
            updateBuffs(gameInfo.getPlayer());
        }
        syncCoreSessionStat(gameInfo);
        SessionStat.saveCurrent(sessionStat);
        return quit;
    }

    public void chooseConsumable(GameInfo gameInfo, Presentation presentation, ConsumableTypes type) {
        int count_consumable = switch (type) {
            case WEAPON -> gameInfo.getPlayer().getBackpack().getWeaponNum();
            case FOOD -> gameInfo.getPlayer().getBackpack().getFoodNum();
            case ELIXIR -> gameInfo.getPlayer().getBackpack().getElixirNum();
            case SCROLL -> gameInfo.getPlayer().getBackpack().getScrollNum();
            default -> 0;
        };
        presentation.printComsumableMenu(gameInfo.getPlayer(), type);
        KeyStroke key = presentation.readKey();
        if (key.getKeyType() != KeyType.Character) 
            return;
        int key_ch = key.getCharacter() - '0';
        if ((1 <= key_ch && key_ch <= count_consumable && (type != ConsumableTypes.WEAPON || gameInfo.getLevel().getRooms() != null))
            || (key_ch == 0 && type == ConsumableTypes.WEAPON)) {
            Room room = (type == ConsumableTypes.WEAPON ? gameInfo.getLevel().getRoomByCoord(gameInfo.getPlayer()) : null);
            gameInfo.getPlayer().useConsumable(type, key_ch-1, room);
        }
    }

    private boolean isTired(GameInfo gameInfo) {
        return gameInfo.getPlayer().getActPowers() <= 0;
    }

    private void advanceToNextLevel(GameInfo gameInfo) {
        Level level = gameInfo.getLevel();
        level.generateNextLevel();
        ObjectT startCell = level.getRoom(Level.START_ROOM_INDEX).getRandomFreeCellInRoom();
        gameInfo.getPlayer().setCoordinates(startCell.getCoordinates());
        gameInfo.getMap().clear();
    }

    private void processCommunicatorsTurn(GameInfo gameInfo, SessionStat sessionStat) {
        Room[] rooms = gameInfo.getLevel().getRooms();
        if (rooms == null)
            return;
        for (Room room : rooms) {
            if (room == null || room.getCommunicators() == null)
                continue;
            ArrayList<Communicator> communicators = room.getCommunicators();
            for (Communicator communicator : communicators) {
                if (communicator != null) {
                    processOneCommunicatorTurn(gameInfo, sessionStat, communicator);
                }
            }
        }
        ArrayList<Communicator> communicatorsOutsideRooms = gameInfo.getLevel().getCommunicatorsOutsideRooms();
        if (!communicatorsOutsideRooms.isEmpty()) {
            for (Communicator communicator : communicatorsOutsideRooms) {
                processOneCommunicatorTurn(gameInfo, sessionStat, communicator);
            }
        }
    }

    private void processOneCommunicatorTurn(GameInfo gameInfo, SessionStat sessionStat, Communicator communicator) {
        double hpBefore = gameInfo.getPlayer().getActPowers();
        communicator.takeTurn(gameInfo.getLevel(), gameInfo.getPlayer());
        if (gameInfo.getPlayer().getActPowers() < hpBefore) {
            sessionStat.incMissed();
        }
        if (isTired(gameInfo)) {
            return;
        }
    }

    private Backpack snapshotBackpack(Player player) {
        Backpack bp = new Backpack();
        for (int i = 0; i < player.getBackpack().getFoodNum(); i++) bp.addFood(player.getBackpack().getFood(i));
        for (int i = 0; i < player.getBackpack().getElixirNum(); i++) bp.addElixir(player.getBackpack().getElixir(i));
        for (int i = 0; i < player.getBackpack().getScrollNum(); i++) bp.addScroll(player.getBackpack().getScroll(i));
        for (int i = 0; i < player.getBackpack().getWeaponNum(); i++) bp.addWeapon(player.getBackpack().getWeapon(i));
        bp.setTreasure(player.getBackpack().getTreasure());
        return bp;
    }

    private int countCommunicators(Level level) {
        int count = 0;
        for (Room room : level.getRooms()) {
            if (room != null && room.getCommunicators() != null) {
                count += room.getCommunicators().size();
            }
        }
        count += level.getCommunicatorsOutsideRooms().size();
        return count;
    }

    private boolean isPlayerAttackAttempt(Player player, Directions direction, Level level) {
        int x = player.getCoordinate(Dimention.X.ordinal());
        int y = player.getCoordinate(Dimention.Y.ordinal());
        switch (direction) {
            case FORWARD -> y--;
            case BACK -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
            default -> {
                return false;
            }
        }
        return level.getCommunicatorAt(x, y) != null;
    }

    private void syncCoreSessionStat(GameInfo gameInfo) {
        SessionStat sessionStat = gameInfo.getSessionStat();
        sessionStat.setTreasures(gameInfo.getPlayer().getBackpack().getTreasure());
        sessionStat.setLevel(gameInfo.getLevel().getLevelNum());
    }

    private void updateBuffs(Player player) {
        updateIntBuffArray(player.getElixirBuffs().getRelationQuality(), player::getRelationQuality, player::setRelationQuality);
        updateIntBuffArray(player.getElixirBuffs().getImpulsePower(), player::getImpulsePower, player::setImpulsePower);
        updateDoubleBuffArray(player.getElixirBuffs().getMaxActPowers(), player::getActPowers, player::setActPowers);
        updateIntBuffArray(player.getElixirBuffs().getMaxActPowers(), player::getRegenLimit, player::setRegenLimit);
    }

    private void updateIntBuffArray(ArrayList<Buff> buffs, Supplier<Integer> getter, Consumer<Integer> setter){
        Iterator<Buff> iterator = buffs.iterator();
        while (iterator.hasNext()) {
            Buff buff = iterator.next();
            if (buff.isExpired()) {
                setter.accept(getter.get() - buff.getStatIncrease());
                log.info("Эффект баффа {} закончился", buff.getName());
                iterator.remove();
            }
        }
    }

    private void updateDoubleBuffArray(ArrayList<Buff> buffs, Supplier<Double> getter, Consumer<Double> setter){
        Iterator<Buff> iterator = buffs.iterator();
        while (iterator.hasNext()) {
            Buff buff = iterator.next();
            if (buff.isExpired()) {
                double newValue = getter.get() - buff.getStatIncrease();
                if (newValue <= 0) newValue = 1.0;
                setter.accept(newValue);
                log.info("Эффект баффа {} закончился", buff.getName());
                iterator.remove();
            }
        }
    }


    private void getExitHere(Level level, Player player) {
        level.setEndOfLevel(new ObjectT(player.getCoordinates()));
    }

    public static void main(String[] args) {
        Presentation presentation = new Presentation();
        Controller controller = new Controller();
        GameInfo gameInfo;
        try {
            boolean running_menu = true;
            int current_option = 0;
            while (running_menu){
                presentation.menuScreen(current_option);
                KeyStroke key = presentation.readKey();
                if (key.getKeyType() == KeyType.Enter) {
                    switch (current_option){
                        case 0:
                            gameInfo = LoadGameRepository.initGameInfo();
                            controller.game_cycle(gameInfo, presentation);
                            break;
                        case 1:
                            gameInfo = LoadGameRepository.loadGameInfo();
                            controller.game_cycle(gameInfo, presentation);
                            break;
                        case 2:
                            presentation.displayScoreboard(SessionStat.getSessionStats());
                            presentation.readKey();
                            break;
                        case 3:
                            running_menu = false;
                            break;
                    }
                } else if (key.getKeyType() == KeyType.Character) {
                    switch (key.getCharacter()) {
                        case 'W':
                        case 'w':
                            current_option = Math.max(0, current_option - 1);
                            break;
                        case 'S':
                        case 's':
                            current_option = Math.min(3, current_option + 1);
                            break;
                        default:
                            break;
                    }
                }
            }
    
        }
        catch(Throwable e) {
            e.printStackTrace();
        }
        presentation.close_presentation();
    }
}
