package rogue.view;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import rogue.controller.GameInfo;
import rogue.model.datalayer.*;
import rogue.model.domain.entities.characters.Player;
import rogue.model.domain.entities.consumables.items.Food;
import rogue.model.domain.entities.consumables.items.Item;
import rogue.model.domain.entities.places.Place;
import rogue.model.domain.enums.ConsumableTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Presentation {
    private Terminal terminal;
    private TextGraphics textGraphics;

    private static final Logger log = LoggerFactory.getLogger(Presentation.class);

    public Presentation() {
        init_terminal();
        init_textGraphics();
    }
    private void init_terminal() {
        try {
            terminal = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(120, 35))
                .createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    private void init_textGraphics() {
        try {
            textGraphics = terminal.newTextGraphics();
            textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
            textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void close_presentation() {
        if(terminal != null) {
            try {
                terminal.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void menuScreen(int current_line) {
        try {
            terminal.clearScreen();
            String strings[] = {
                "           GAME  MENU           ",
                "+------------------------------+",
                "|                              |",
                "|          NEW   GAME          |",
                "|          LOAD  GAME          |",
                "|          SCOREBOARD          |",
                "|          EXIT  GAME          |",
                "|                              |",
                "+------------------------------+",
            };
            
            final int width = strings[0].length();
            final int height = strings.length;
            
            int row = terminal.getTerminalSize().getRows();
            int col = terminal.getTerminalSize().getColumns();
            
            int shift_x = (col - width) / 2;
            int shift_y = (row - height) / 2;
            
            for (int i = 0; i < height; i++) {
                textGraphics.putString(shift_x, shift_y+i, strings[i]);
            }

            textGraphics.putString(shift_x+5, shift_y+current_line+3, "<<<");
            textGraphics.putString(shift_x+24, shift_y+current_line+3, ">>>");

            terminal.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void endScreen(int end) {
        try {
            terminal.clearScreen();
            String strings[][] = {{
"                            ТИШИНА НЕ ВНЕ                                                  ",
"          .----.                                                                          ",
"        .'      '.                                                                        ",
"       /   -  -   \\        Нет ни мира, ни ума.                                          ",
"      :     o      :        Только покой                                                 ",
"      :    ___     :        без опоры.                                                   ",
"       \\  (   )   /        ~ Ланкаватара-сутра                                           ",
"        \\  \\ /   /                                                                       ",
"         \\  U   /                                                                        ",
"          \\___/                                                                         ",
"          /   \\                                                                         ",
"         /     \\                                                                        ",
"        /       \\                                                                       ",
"       /         \\                                                                      ",
"      /___________\\                                                                      "
                }, {
"                             ЗАБУДЬ РЕЗУЛЬТАТ                                              ",
"            .---.                                                                         ",
"          .'     '.                                                                       ",
"         /    O    \\        Где искать ум?                                               ",
"        :     |     :        Ветер гнёт бамбук —                                         ",
"        :     |     :        это и есть ответ.                                           ",
"         \\    |    /        ~ Хуайнэн (6-й патриарх)                                   ",
"          \\   '   /                                                                      ",
"           \\_/ \\_/                                                                       ",
"           /     \\                                                                       ",
"          /       \\                                                                      ",
"         /         \\                                                                     ",
"        /           \\                                                                    ",
"       /             \\                                                                   ",
"      /_______________\\                                                                   "
                }
            };
            
            final int width = strings[end][0].length();
            final int height = strings[end].length;
            
            int row = terminal.getTerminalSize().getRows();
            int col = terminal.getTerminalSize().getColumns();
            
            int shift_x = (col - width) / 2;
            int shift_y = (row - height) / 2;
            
            for (int i = 0; i < height; i++) {
                textGraphics.putString(shift_x, shift_y+i, strings[end][i]);
            }

            String continue_text = "Press any key to continue...";
            textGraphics.putString((col - continue_text.length())/2, shift_y+height+1, continue_text);

            terminal.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public KeyStroke readKey() {
        KeyStroke keyStroke = null;
        try {
            keyStroke = terminal.readInput();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return keyStroke;
    }

    private void createNewMap(GameInfo gameInfo) {
        log.debug("createNewMap");
        gameInfo.getMap().clearFrame();
        log.debug("createNewMap - > rooms");
        gameInfo.getMap().roomsToMap(gameInfo);
        log.debug("createNewMap - > passages");
        gameInfo.getMap().passagesToMap(gameInfo);
        log.debug("createNewMap - > player");
        gameInfo.getMap().playerToMap(gameInfo);
        log.debug("createNewMap - > communicator");
        gameInfo.getMap().communicatorsToMap(gameInfo);
        log.debug("createNewMap - > consumable");
        gameInfo.getMap().consumablesToMap(gameInfo);
        log.debug("createNewMap - > levelEnd");
        gameInfo.getMap().levelEndToMap(gameInfo);
        gameInfo.getMap().fogOfWarToMap(gameInfo);
        log.debug("createNewMap - > places");
        gameInfo.getMap().placesToMap(gameInfo);
        log.debug("Player here {}", gameInfo.getPlayer().getCoordinates());
    }


    public void displayMap(GameInfo gameInfo) {
        createNewMap(gameInfo);
        try {
            int row = terminal.getTerminalSize().getRows();
            int col = terminal.getTerminalSize().getColumns();
            
            int shift_x = (col - Map.MAP_WIDTH) / 2;
            int shift_y = (row - Map.MAP_HEIGHT) / 2;

            for (int i = 0; i < Map.MAP_HEIGHT; i++) {
                for (int j = 0; j < Map.MAP_WIDTH; j++) {
                    textGraphics.setForegroundColor(gameInfo.getMap().getMapColor(i, j));
                    textGraphics.putString(shift_x + j, shift_y + i, String.valueOf(gameInfo.getMap().getMapElem(i, j)));
                }
            }
            textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
            Player player = gameInfo.getPlayer();
            
            textGraphics.putString(shift_x, shift_y + Map.MAP_HEIGHT, 
                String.format(Locale.US,"Level: %-2d Gold: %-2d ActPowers: %.2f/%-2d RQuality: %-4d ImpulsePower: %d(+%d)", 
                        gameInfo.getLevel().getLevelNum(), 
                        player.getBackpack().getTreasure(), 
                        player.getActPowers(), player.getRegenLimit(),
                        player.getRelationQuality(),
                        player.getImpulsePower(), (player.getWeapon() != null ? player.getWeapon().getImpulsePower() : 0)
                )
            );
            
            terminal.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void displayScoreboard(SessionStat[] stats) {
        try {
            terminal.clearScreen();
            int row = terminal.getTerminalSize().getRows();
            int col = terminal.getTerminalSize().getColumns();

            int field_size = 10;
            int width = field_size * 10;
            int height = stats.length * 2 + 3;

            int shift_x = (col - width) / 2;
            int shift_y = (row - height) / 2;

            textGraphics.drawLine(shift_x, shift_y-2, shift_x+width-1, shift_y-2, '-');
            textGraphics.putString(shift_x, shift_y - 1, "| treasures|   level  |  enemies |   food   |  elixirs |  scrolls |  talkTos |  missed  |   moves  |");

            for (int i = 0; i < stats.length; i++) {
                textGraphics.drawLine(shift_x, shift_y+2*i, shift_x+width-1, shift_y+2*i, '-');
                textGraphics.putString(shift_x, shift_y+2*i+1, 
                String.format("|%10d|%10d|%10d|%10d|%10d|%10d|%10d|%10d|%10d|", 
                        stats[i].getTreasures(), 
                        stats[i].getLevel(), 
                        stats[i].getEnemies(), 
                        stats[i].getFood(), 
                        stats[i].getElixirs(), 
                        stats[i].getScrolls(), 
                        stats[i].getAttacks(),
                        stats[i].getMissed(), 
                        stats[i].getMoves())
                );
            }
            textGraphics.drawLine(shift_x, shift_y+2*stats.length, shift_x+width-1, shift_y+2*stats.length, '-');
            textGraphics.putString((col - 20) / 2, shift_y+2*(stats.length+1),"Press ESCAPE to exit.");

            terminal.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void printComsumableMenu(Player player, ConsumableTypes type) {
        try {
            terminal.clearScreen();
            
            int row = terminal.getTerminalSize().getRows();
            int col = terminal.getTerminalSize().getColumns();
            
            int shift_x = (col - 30) / 2;
            int shift_y = (row - 10) / 2;

            String str_type = type.toString().toLowerCase();

            int count_consumable = switch (type) {
                case WEAPON -> player.getBackpack().getWeaponNum();
                case FOOD -> player.getBackpack().getFoodNum();
                case ELIXIR -> player.getBackpack().getElixirNum();
                case SCROLL -> player.getBackpack().getScrollNum();
                default -> 0;
            };
            String choose = String.format("Choose %s:", str_type);
            textGraphics.putString(shift_x, shift_y - 1, choose);
            
            if (type == ConsumableTypes.WEAPON && count_consumable > 0) {
                textGraphics.putString(shift_x, shift_y, "0. Without weapon");
            }

            String buff_type[] = { "actPowers", "relationQuality", "impulsePower", "max_actPowers" };
            for (int i = 0; i < count_consumable; i++) {
                String statType_str = switch (type) {
                    case WEAPON -> "impulsePower";
                    case FOOD -> "actPowers";
                    case ELIXIR -> buff_type[player.getBackpack().getElixir(i).getStat().ordinal()];
                    case SCROLL -> buff_type[player.getBackpack().getScroll(i).getStat().ordinal()];
                    default -> " ";
                };
                String name_cons = switch (type) {
                    case WEAPON -> player.getBackpack().getWeapon(i).getName();
                    case FOOD -> player.getBackpack().getFood(i).getName();
                    case ELIXIR -> player.getBackpack().getElixir(i).getName();
                    case SCROLL -> player.getBackpack().getScroll(i).getName();
                    default -> " ";
                };
                int num_cons = switch (type) {
                    case WEAPON -> player.getBackpack().getWeapon(i).getImpulsePower();
                    case FOOD -> player.getBackpack().getFood(i).getToRegen();
                    case ELIXIR -> player.getBackpack().getElixir(i).getIncrease();
                    case SCROLL -> player.getBackpack().getScroll(i).getIncrease();
                    default -> 0;
                };
                
                String table_str = "%d. %s %+d " + statType_str;
                
                if (type == ConsumableTypes.ELIXIR)
                    table_str += String.format(" for %d seconds", player.getBackpack().getElixir(i).getDuration().getSeconds());
                
                String str = String.format(table_str, i+1, name_cons, num_cons);
                textGraphics.putString(shift_x, shift_y+i+(type == ConsumableTypes.WEAPON ? 1 : 0), str);
            }
            if (count_consumable == 0) {
                String no_consumable = String.format("You haven't %s", str_type);
                textGraphics.putString(shift_x, shift_y+1, no_consumable);
                textGraphics.putString(shift_x, shift_y+2, "Press any key to continue...");
            } else {
                String str = String.format("Press 1-%d key to choose %s or any key to continue", count_consumable, str_type);
                textGraphics.putString(shift_x, shift_y+count_consumable+1, str);
            }
            terminal.flush();
        } catch(Exception | Error e) {
            e.printStackTrace();
        }
    }

    public void printPlaceInfo(Place place) {
        try {
            terminal.clearScreen();
            
            int row = terminal.getTerminalSize().getRows();
            int col = terminal.getTerminalSize().getColumns();
            
            int shift_x = (col - 30) / 2;
            int shift_y = (row - 10) / 2;

            List<Item> items = place.getItems();
            int itemCount = items.size();
            String choose = String.format("Choose Food");
            textGraphics.putString(shift_x, shift_y - 1, choose);
            

            for (int i = 0; i < itemCount; i++) {
                Item item = items.get(i);
                ConsumableTypes type = item.getType();
                String statType_str = switch (type) {
                    case WEAPON -> "impulsePower";
                    case FOOD -> "actPowers";
                    default -> " ";
                };
                String name_cons = switch (type) {
                    case FOOD -> item.getName();
                    default -> " ";
                };
                int num_cons = switch (type) {
                    case FOOD -> ((Food) item).getToRegen();
                    default -> 0;
                };
                
                String table_str = "%d. %s %+d " + statType_str;

                String str = String.format(table_str, i+1, name_cons, num_cons);
                textGraphics.putString(shift_x, shift_y+i, str);
            }    
            if (itemCount == 0) {
                textGraphics.putString(shift_x, shift_y+1, "Кафе закрыто");
                textGraphics.putString(shift_x, shift_y+2, "Press any key to continue...");
            } else {
                String str = String.format("Press 1-%d key to choose Food or any key to continue", itemCount);
                textGraphics.putString(shift_x, shift_y+itemCount+1, str);
            }
            terminal.flush();
        } catch(Exception | Error e) {
            e.printStackTrace();
        }
        
    }
}
