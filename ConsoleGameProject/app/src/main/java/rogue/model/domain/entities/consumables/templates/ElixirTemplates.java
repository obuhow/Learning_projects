package rogue.model.domain.entities.consumables.templates;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import rogue.model.domain.entities.consumables.items.Elixir;
import rogue.model.domain.enums.StatType;

public class ElixirTemplates extends ItemTemplates<Elixir> {
    public static final ElixirTemplates INSTANCE = new ElixirTemplates();
    public final List<ElixirTemplate> templates;

    private record ElixirTemplate(String name, StatType type, int minValue, int maxValue, Duration duration) {}
    private final Random random = new Random();

    private ElixirTemplates() {
        templates = List.of(
            new ElixirTemplate("Серия упражнений на артикуляцию", StatType.RELATION_QUALITY, 20, 40, Duration.ofSeconds(300)),
            new ElixirTemplate("Упражнения на громкость", StatType.IMPULSE_POWER, 40, 60, Duration.ofSeconds(300)),

            new ElixirTemplate("Курс распознавания народных диалектов", StatType.RELATION_QUALITY, 40, 60, Duration.ofSeconds(180)),
            new ElixirTemplate("Тренинг уверенности в себе", StatType.MAX_ACT_POWERS, 150, 200, Duration.ofSeconds(300))
        );
    }

    public Elixir getRandomElixir(int levelNum) {
        int minOrdinal = levelNum <= 2 ? 0 : 2;
        int maxOrdinal = levelNum <= 2 ? 3 : templates.size();
        ElixirTemplate elixirTemplate = templates.get(random.nextInt(minOrdinal, maxOrdinal));
        return new Elixir(elixirTemplate.name, elixirTemplate.type, random.nextInt(elixirTemplate.minValue, elixirTemplate.maxValue), elixirTemplate.duration);
    }
}
