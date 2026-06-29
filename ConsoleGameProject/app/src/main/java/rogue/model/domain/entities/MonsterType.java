package rogue.model.domain.entities;

public enum MonsterType {
    ZOMBIE,
    VAMPIRE, 
    GHOST,
    OGRE,
    SNAKE,
    MONSTER_TYPE_NUM
}

// public enum HostilityType {
//     LOW, ///< Круг радиусом в LOW_HOSTILITY_RADIUS клетки
//     AVERAGE, ///< Круг радиусом в AVERAGE_HOSTILITY_RADIUS клеток
//     HIGH
// }

// public enum Directions {
//     FORWARD, ///< Вперед (^)
//     BACK, ///< Назад (v)
//     LEFT, ///< Влево (<)
//     RIGHT, ///< Вправо (>)
//     DIAGONNALY_FORWARD_LEFT, ///< Влево вверх по диагонали (<^)
//     DIAGONALLY_FORWARD_RIGHT, ///< Вправо вверх по диагонали (>^)
//     DIAGONALLY_BACK_LEFT, ///< Влево вниз по диагонали (<v)
//     DIAGONALLY_BACK_RIGHT, ///< Вправо вниз по диагонали (>v)
//     STOP
// }

// public class ObjectT {
//     private int[] coordinates = new int[Dimention.COORDS_NUM.ordinal()];
//     int[] size = new int[Dimention.COORDS_NUM.ordinal()];
// }

// public class Character {
//     ObjectT coords; ///< Координаты персонажа
//     double health; ///< Текущее количество здоровья
//     int agility; ///< Показатель ловкости
//     int strength; ///< Показатель силы
// }

// public class Monster {
//     Character base_stats; ///< Общие для всех персонажей характеристики
//     MonsterType type; ///< Тип монстра
//     HostilityType hostility; ///< Уровень враждебности монстра
//     boolean is_chasing; ///< Флаг, устанавливающий, преследует ли монстр игрока
//     Directions dir; ///
// }

// public class Treasure {
//     int value;
// }

// public class Food {
//     int to_regen; ///< Количество восстанавливаемого здоровья
//     char name[MAX_NAME_LEN]; ///< Название продукта
// }