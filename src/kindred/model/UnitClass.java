package kindred.model;

public class UnitClass {

    private final String name;
    private final Attribute attribute;
 
    public UnitClass(String name, Attribute attribute) {
        this.name = name;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public Attribute getAttribute(){
        return attribute;
    }
    
    public void set_mods(){
        if(name.equals('Lancer')){
            attribute.hitPoints = 3;
            attribute.attack = 4;
            attribute.defense = 1;
            attribute.agility = -5;
        }
        
        if(name.equals('Archer')){
            attribute.hitPoints = -8;
            attribute.attack = 5;
            attribute.defense = -7;
            attribute.agility = 10;
        }
        
        if(name.equals('Knight')){
            attribute.hitPoints = 7;
            attribute.attack = 2;
            attribute.defense = 7;
            attribute.agility = -8;
        }
        
        if(name.equals('Swordsman')){
            attribute.hitPoints = -2;
            attribute.attack = 6;
            attribute.defense = -4;
            attribute.agility = 7;
        }
    }
}
