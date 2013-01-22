/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
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
    private ReadOnlyBooleanWrapper isLoaded;
    
    public ObservableBonus(){
        this.isLoaded = new ReadOnlyBooleanWrapper();
        this.isLoaded.set(false);
        this.id = new ReadOnlyIntegerWrapper(0);
        this.name = new ReadOnlyStringWrapper(null);
        this.level = new ReadOnlyIntegerWrapper(0);
        this.strength = new ReadOnlyIntegerWrapper(0);
        this.speed = new ReadOnlyIntegerWrapper(0);
        this.imageID = new ReadOnlyIntegerWrapper(0);
    }
    
    public void loadBonus(Bonus b){
        this.id.set(b.getId());
        this.name.set(b.getName());
        this.level.set(b.getLevel());
        this.strength.set(b.getStrength());
        this.speed.set(b.getSpeed());
        this.imageID.set(b.getImage());
    }
    public void unload(){
        this.isLoaded.set(false);
        this.id.set(0);
        this.name.set(null);
        this.level.set(0);
        this.strength.set(0);
        this.speed.set(0);
        this.imageID.set(0);
    }
    public ReadOnlyBooleanProperty isLoaded(){
        return this.isLoaded;
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
