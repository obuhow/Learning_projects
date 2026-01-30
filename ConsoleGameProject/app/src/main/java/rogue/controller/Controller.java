package rogue.controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import rogue.view.*;


public class Controller {
    private static void game_cycle(Presentation presentation, Map map) { //player_t *player, level_t *level, map_t *map, battle_info_t *battles, const char *save, const char *score, const char *stat) {
        boolean running = true;
        while (running) {
            presentation.display_map(map);
            // if (process_user_input(player, level, battles, stat))
            //     running = false;

            // if (check_level_end(level, player))
            // {
            //     clear_map_data(map);
            //     generate_next_level(level, player);
            // }
            // if (check_death(player))
            // {
            //     running = false;
            //     update_statistics(score, stat);
            //     get_standart_save(save, stat);
            //     dead_screen();
            // }
            // if (check_end(level))
            // {
            //     running = false;
            //     update_statistics(score, stat);
            //     get_standart_save(save, stat);
            //     endgame_screen();
            // }

            // if (running)
            //     save_data(player, level, map, save);
        }
    }

    public static void main(String[] args) {
        var presentation = new Presentation();
        var map = new Map();
        try {
            boolean running_menu = true;
            int current_option = 0;
            while (running_menu){
                presentation.menu_screen(current_option);
                KeyStroke key = presentation.readKey();
                if (key.getKeyType() == KeyType.Enter) {
                    switch (current_option){
                        case 0:
                            // init_level(&player, &level, &map, battles, STATISTICS_PATH);
                            System.out.println("hello");
                            game_cycle(presentation, map);
                            break;
                        case 1:
                            // load_data(&player, &level, &map, battles, SAVE_PATH, STATISTICS_PATH);
                            // game_cycle(&player, &level, &map, battles, SAVE_PATH, SCOREBOARD_PATH, STATISTICS_PATH);
                            break;
                        case 2:
                            // display_scoreboard(SCOREBOARD_PATH);
                            while (presentation.readKey().getKeyType() != KeyType.Escape);
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