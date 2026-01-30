package rogue.model.domain.entities;

public class Food {
    private int toRegen;
    private String name;

    public Food(int toRegen, String name) {
        this.toRegen = toRegen;
        this.name = name;
    }

     public int getToRegen() {
        return toRegen;
    }

    public void setToRegen(int toRegen) {
        this.toRegen = toRegen;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
