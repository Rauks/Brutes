/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import brutes.db.Entity;
import java.sql.ResultSet;

/**
 *
 * @author Karl
 */
public class Bonus implements Entity {
    public static final Bonus EMPTY_BONUS = new Bonus();

    private int id;
    private String name;
    private short level;
    private short strength;
    private short speed;
    private int imageID;

    public Bonus(ResultSet r) throws Exception {
        this(r.getInt("id"), r.getString("name"), r.getShort("level"), r.getShort("strength"), r.getShort("speed"), r.getInt("id") /* TODO: change ID -> IMG */);
    }
    
    private Bonus(){
        this.id = 0;
        this.name = null;
        this.level = 0;
        this.strength = 0;
        this.speed = 0;
        this.imageID = 0;
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
