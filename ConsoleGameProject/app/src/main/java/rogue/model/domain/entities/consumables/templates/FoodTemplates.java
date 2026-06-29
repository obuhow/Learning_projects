package rogue.model.domain.entities.consumables.templates;

import java.util.List;
import java.util.Random;

import rogue.model.domain.entities.consumables.items.Food;

public class FoodTemplates extends ItemTemplates<Food>{

    public static final FoodTemplates INSTANCE = new FoodTemplates();
    public final List<FoodTemplate> templates;
    
    private record FoodTemplate(String name, int minRegen, int maxRegen) {}
    private Random random = new Random();

    private FoodTemplates() { 
        templates = List.of(
            new FoodTemplate("Каша домашняя на молоке", 60, 90),
            new FoodTemplate("Салат из огурцов и помидоров", 70, 95),

            new FoodTemplate("Странный овощ с кислым вкусом", 90, 140),
            new FoodTemplate("Каша домашняя на молоке с ягодами", 130, 160),
            
            new FoodTemplate("Каша домашняя на молоке с ягодами", 150, 190),
            new FoodTemplate("Щи с хлебом и горчицей", 180, 230)
        );
    }

    public Food getRandomFood(int levelNum) {
        int minOrdinal = levelNum <= 2 ? 0 : 2;
        int maxOrdinal = levelNum <= 2 ? 3 : templates.size();
        FoodTemplate foodTemplate = templates.get(random.nextInt(minOrdinal, maxOrdinal));
        return new Food(foodTemplate.name, random.nextInt(foodTemplate.minRegen, foodTemplate.maxRegen));
    }    
}
