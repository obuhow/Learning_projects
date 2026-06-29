package rogue.model.domain.entities.consumables.templates;

import java.util.List;

import rogue.model.domain.entities.consumables.items.Weapon;

public class WeaponTemplates extends ItemTemplates<Weapon> {
    public static final WeaponTemplates INSTANCE = new WeaponTemplates();
    public final List<WeaponTemplate> templates;

    private record WeaponTemplate(String name, int minImpulsePower) {}

    private WeaponTemplates() { 
        templates = List.of(
            new WeaponTemplate("Вежливая просьба", 15),
            new WeaponTemplate("Уместный пример", 20),

            new WeaponTemplate("Неразборчивая, но приятная похвала", 25),
            new WeaponTemplate("Неразборчивая, но приятная похвала", 40),
            
            new WeaponTemplate("Одобрительный взгляд", 55),
            new WeaponTemplate("Похлопывание по плечу", 60),

            new WeaponTemplate("Совместный проект", 80),
            new WeaponTemplate("Внимательное молчание", 97),

            new WeaponTemplate("Подробный ответ на важный вопрос", 105)
        );
    }

    public Weapon getWeapon(int levelNum) {
        int i = levelNum <= templates.size() ? levelNum - 1 : templates.size() - 1;
        WeaponTemplate weaponTemplate = templates.get(i);
        int impulsePower = levelNum <= templates.size() ? weaponTemplate.minImpulsePower : weaponTemplate.minImpulsePower + (levelNum - i) * 5;
        return new Weapon(weaponTemplate.name, impulsePower);
    }
}
