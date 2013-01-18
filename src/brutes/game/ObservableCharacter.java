/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author Karl
 */
public class ObservableCharacter {
    private ReadOnlyIntegerWrapper id;
    private ReadOnlyStringWrapper name;
    private ReadOnlyIntegerWrapper level;
    private ReadOnlyIntegerWrapper life;
    private ReadOnlyIntegerWrapper strength;
    private ReadOnlyIntegerWrapper speed;
    private ReadOnlyIntegerWrapper imageID;
    private ReadOnlyListWrapper<Integer> bonusesID;
    
    public ObservableCharacter(){
        this.id = new ReadOnlyIntegerWrapper();
        this.name = new ReadOnlyStringWrapper();
        this.level = new ReadOnlyIntegerWrapper();
        this.life = new ReadOnlyIntegerWrapper();
        this.strength = new ReadOnlyIntegerWrapper();
        this.speed = new ReadOnlyIntegerWrapper();
        this.imageID = new ReadOnlyIntegerWrapper();
        this.bonusesID = new ReadOnlyListWrapper<>();
    }
    
    public void loadCharacter(Character c){
        this.id.set(c.getId());
        this.name.set(c.getName());
        this.level.set(c.getLevel());
        this.life.set(c.getLife());
        this.strength.set(c.getStrength());
        this.speed.set(c.getSpeed());
        this.imageID.set(c.getImageID());
        this.bonusesID.setAll(c.getBonuseIDs());
    }
}
