/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import brutes.game.media.BonusImage;

/**
 *
 * @author Karl
 */
public class Bonus {
    public static final Bonus EMPTY_BONUS = new Bonus();

    private int id;
    private String name;
    private short level;
    private short strength;
    private short speed;
    private int imageID;

    private Bonus(){
    }
    
    public Bonus(int id, String name, short level, short strength, short speed, int imageID) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.strength = strength;
        this.speed = speed;
        this.imageID = imageID;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public short getLevel() {
        return level;
    }
    public short getStrength() {
        return strength;
    }
    public short getSpeed() {
        return speed;
    }
    public int getImage() {
        return imageID;
    }
}
