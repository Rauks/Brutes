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
public class ObservableCharacter {
    private ReadOnlyIntegerWrapper id;
    private ReadOnlyStringWrapper name;
    private ReadOnlyIntegerWrapper level;
    private ReadOnlyIntegerWrapper life;
    private ReadOnlyIntegerWrapper strength;
    private ReadOnlyIntegerWrapper speed;
    private ReadOnlyIntegerWrapper imageID;
    private ObservableBonus[] bonuses;
    private ReadOnlyBooleanWrapper isLoaded;
    
    public ObservableCharacter(){
        this.isLoaded = new ReadOnlyBooleanWrapper();
        this.isLoaded.set(false);
        this.id = new ReadOnlyIntegerWrapper();
        this.name = new ReadOnlyStringWrapper();
        this.level = new ReadOnlyIntegerWrapper();
        this.life = new ReadOnlyIntegerWrapper();
        this.strength = new ReadOnlyIntegerWrapper();
        this.speed = new ReadOnlyIntegerWrapper();
        this.imageID = new ReadOnlyIntegerWrapper();
        this.bonuses = new ObservableBonus[Character.MAX_BONUSES];
        for(int i = 0; i < Character.MAX_BONUSES; i++){
            this.bonuses[i] = new ObservableBonus();
        }
    }
    
    public void loadCharacter(Character c){
        this.isLoaded.set(true);
        this.id.set(c.getId());
        this.name.set(c.getName());
        this.level.set(c.getLevel());
        this.life.set(c.getLife());
        this.strength.set(c.getStrength());
        this.speed.set(c.getSpeed());
        this.imageID.set(c.getImageID());
        Bonus[] bonus = c.getBonuses();
        for(int i = 0; i < Character.MAX_BONUSES; i++){
            if(bonus[i] != null){
                this.bonuses[i].loadBonus(bonus[i]);
            }
            else{
                this.bonuses[i].loadBonus(Bonus.EMPTY_BONUS);
            }
        }
    }
    public ReadOnlyBooleanProperty isLoaded(){
        return this.isLoaded;
    }

    public ObservableBonus getBonus(int id){
        return this.bonuses[id];
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
    public ReadOnlyIntegerProperty getLife() {
        return this.life;
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
