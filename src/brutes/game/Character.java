/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import java.util.ArrayList;

/**
 *
 * @author Karl
 */
public class Character {
    public static final int MAX_BONUSES = 3;
    
    private int id;
    private String name;
    private short level;
    private short life;
    private short strength;
    private short speed;
    private int imageID;
    private Bonus[] bonuses;

    public Character(int id, String name, short level, short life, short strength, short speed, int imageID) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.life = life;
        this.strength = strength;
        this.speed = speed;
        this.imageID = imageID;
        this.bonuses = new Bonus[Character.MAX_BONUSES];
    }
    public Character(int id, String name, short level, short life, short strength, short speed, int imageID, Bonus[] bonuses) {
        this(id, name, level, life, strength, speed, imageID);
        this.bonuses = bonuses;
    }
    
    public void addBonus(Bonus b){
        this.bonuses[this.bonuses.length] = b;
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public short getLevel() {
        return this.level;
    }
    public short getLife() {
        return this.life;
    }
    public short getStrength() {
        return this.strength;
    }
    public short getSpeed() {
        return this.speed;
    }
    public int getImageID() {
        return this.imageID;
    }
    public Bonus[] getBonuses() {
        return this.bonuses;
    }
}
