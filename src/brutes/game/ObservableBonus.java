/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 *
 * @author Karl
 */
public class ObservableBonus {
    private ReadOnlyIntegerWrapper id;
    private ReadOnlyStringWrapper name;
    private ReadOnlyIntegerWrapper level;
    private ReadOnlyIntegerWrapper strength;
    private ReadOnlyIntegerWrapper speed;
    private ReadOnlyIntegerWrapper imageID;
    
    public ObservableBonus(){
        this.id = new ReadOnlyIntegerWrapper();
        this.name = new ReadOnlyStringWrapper();
        this.level = new ReadOnlyIntegerWrapper();
        this.strength = new ReadOnlyIntegerWrapper();
        this.speed = new ReadOnlyIntegerWrapper();
        this.imageID = new ReadOnlyIntegerWrapper();
    }
    
    public void loadBonus(Bonus b){
        this.id.set(b.getId());
        this.name.set(b.getName());
        this.level.set(b.getLevel());
        this.strength.set(b.getStrength());
        this.speed.set(b.getSpeed());
        this.imageID.set(b.getImage());
    }

    public ReadOnlyIntegerProperty getId() {
        return this.id;
    }
    public ReadOnlyStringProperty getName() {
        return this.name;
    }
    public ReadOnlyIntegerProperty getLevel() {
        return this.level;
    }
    public ReadOnlyIntegerProperty getStrength() {
        return this.strength;
    }
    public ReadOnlyIntegerProperty getSpeed() {
        return this.speed;
    }
    public ReadOnlyIntegerProperty getImageID() {
        return this.imageID;
    }
}
