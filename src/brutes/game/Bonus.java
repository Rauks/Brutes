/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import brutes.db.Entity;
import brutes.db.SQL;
import java.sql.ResultSet;

/**
 *
 * @author Karl
 */
public class Bonus extends Entity {
    public static final Bonus EMPTY_BONUS = new Bonus();

    @SQL(name="id", type="int")
    private int id;
    
    @SQL(name="name", type="varchar")
    private String name;
    
    @SQL(name="level", type="int")
    private short level;
    
    @SQL(name="strength", type="int")
    private short strength;
    
    @SQL(name="speed", type="int")
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

    @Override
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
