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
    
    public void set_attributes(){
        if(name.equals('Lancer')){
            attribute.hitPoints *= 1.1;
            attribute.attack *= 1.1;
            attribute.defense *= 0.9;
            attribute.agility *= 0.8;
        }
        
        if(name.equals('Archer')){
            attribute.hitPoints *= 0.8;
            attribute.attack *= 1.1;
            attribute.defense *= 0.7;
            attribute.agility *= 1.3;
        }
        
        if(name.equals('Knight')){
            attribute.hitPoints *= 1.2;
            attribute.attack *= 1.05;
            attribute.defense *= 1.2;
            attribute.agility *= 0.7;
        }
        
        if(name.equals('Swordsman')){
            attribute.hitPoints *= 0.9;
            attribute.attack *= 1.2;
            attribute.defense *= 0.8;
            attribute.agility *= 1.2;
        }
    }
}
