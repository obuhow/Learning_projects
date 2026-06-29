package rogue.model.domain.entities.consumables.templates;

import java.util.List;
import java.util.Random;

import rogue.model.domain.entities.consumables.items.Scroll;
import rogue.model.domain.enums.StatType;

public class ScrollTemplates extends ItemTemplates<Scroll> {
    public static final ScrollTemplates INSTANCE = new ScrollTemplates();

    private final List<ScrollTemplate> templates;
    private record ScrollTemplate(String name, StatType type, int minValue) {}
    
    private final Random random = new Random();

    private ScrollTemplates() {
        templates = List.of (
            new ScrollTemplate("Свиток неизведанных корней", StatType.MAX_ACT_POWERS, 90),
            new ScrollTemplate("Свиток юных богатырей", StatType.MAX_ACT_POWERS, 180),

            new ScrollTemplate("Свиток осенней тени", StatType.IMPULSE_POWER, 30),
            new ScrollTemplate("Свиток закрытых наречий", StatType.RELATION_QUALITY, 25),
            
            new ScrollTemplate("Свиток тайных лесных знаний", StatType.MAX_ACT_POWERS, 150),
            new ScrollTemplate("Свиток календарных открытий", StatType.MAX_ACT_POWERS, 150),
            
            new ScrollTemplate("Свиток очевидных приоритетов", StatType.RELATION_QUALITY, 60),
            new ScrollTemplate("Свиток могучих богатырей", StatType.IMPULSE_POWER, 80),

            new ScrollTemplate("Свиток благотворных очертаний", StatType.MAX_ACT_POWERS, 200),
            new ScrollTemplate("Свиток растущих теней", StatType.IMPULSE_POWER, 60),

            new ScrollTemplate("Свиток быстрорастущих теней", StatType.IMPULSE_POWER, 95)
        );
    }

    public Scroll getRandomScroll(int levelNum) {
        int minOrdinal = levelNum <= 2 ? 0 : 2;
        int maxOrdinal = levelNum <= 2 ? 5 : templates.size();
        ScrollTemplate scrollTemplate = templates.get(random.nextInt(minOrdinal, maxOrdinal));
        int value = scrollTemplate.minValue;
        if (levelNum > 5) {
            int ratio = scrollTemplate.type == StatType.MAX_ACT_POWERS ? 5 : 2;
            value = scrollTemplate.minValue + levelNum * ratio;
        }        
        return new Scroll(scrollTemplate.name, scrollTemplate.type, value);
    }
}
