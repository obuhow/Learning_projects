package rogue.view;

import java.io.IOException;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Presentation {
    private Terminal terminal;
    private TextGraphics textGraphics;

    public Presentation() {
        init_terminal();
        init_textGraphics();
    }

    private void init_terminal() {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        
        try {
            terminal = factory.createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();
            terminal.setCursorVisible(false);
        }
        catch(IOException e) {
            System.out.println("Beda");
            e.printStackTrace();
        }
    }
    
    private void init_textGraphics() {
        try {
            textGraphics = terminal.newTextGraphics();
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
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

    public void menu_screen(int current_line) {
        try {
            // terminal.flush();
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
// void clear_map_data(map_t *map);
// void create_new_map(map_t *map, const level_t *level, const player_t *player, const battle_info_t *battles);

    private void create_new_map(Map map) { // , Level level, Player player, BattleInfo battles) {
        for (int i = 0; i < Map.MAP_HEIGHT; i++)
            for (int j = 0; j < Map.MAP_WIDTH; j++)
                map.setMapElem(i, j, 'f');
        // rooms_to_map(map, level->rooms, player);
        // passages_to_map(map, &level->passages, level->rooms, player);
        // monsters_to_map(map, level, player, battles);
        // consumables_to_map(map, level->rooms);
        // exit_to_map(map, level);
        // player_to_map(map, player);
        // fog_of_war_to_map(map, level, player);
    }

    public void display_map(Map map) { // , Level level, Player player, BattleInfo battles) {
        create_new_map(map); // , level, player, battles);
        try {
            int row = terminal.getTerminalSize().getRows();
            int col = terminal.getTerminalSize().getColumns();
            
            int shift_x = (col - Map.MAP_WIDTH) / 2;
            int shift_y = (row - Map.MAP_HEIGHT) / 2;
            TerminalPosition startPosition = terminal.getCursorPosition();

            for (int i = 0; i < Map.MAP_HEIGHT; i++) {
                textGraphics.putString(shift_x, shift_y+i, String.copyValueOf(map.getMapLine(i)));
            }
            
            terminal.setCursorPosition(startPosition.withRelativeColumn(shift_y + Map.MAP_HEIGHT).withRelativeRow(shift_x));
            // printw("Level: %-8d ", level->level_num);
            // printw("Gold: %-8d ", player->backpack.treasures.value);
            // printw("Health: %.2lf/%-8d", player->base_stats.health, player->regen_limit);
            // printw("Agility: %-6d ", player->base_stats.agility);
            // printw("Strength: %d(+%d) ", player->base_stats.strength, player->weapon.strength);
            
            terminal.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
// void display_scoreboard(const char *path_scoreboard);

}
