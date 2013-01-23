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
public class Character extends Entity {
    public static final int MAX_BONUSES = 3;
    
    public String tableName = "Brutes";
    
    @SQL(name="id", type="int")
    private int id;
    
    @SQL(name="name", type="varchar")
    private String name;
    
    @SQL(name="level", type="int")
    private short level;
    
    @SQL(name="life", type="int")
    private short life;
    
    @SQL(name="strength", type="int")
    private short strength;
    
    @SQL(name="speed", type="int")
    private short speed;
    
    //@SQL(name="id", type="int")
    private int imageID;
    
    private Bonus[] bonuses;

    public Character(ResultSet r) throws Exception {
        this(r.getInt("id"), r.getString("name"), r.getShort("level"), r.getShort("life"), r.getShort("strength"), r.getShort("speed"), r.getInt("id") /* TODO: change ID -> IMG */);
    }
    
    public Character(int id, String name, short level, short life, short strength, short speed, int imageID) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.life = life;
        this.strength = strength;
        this.speed = speed;
        this.imageID = imageID;
        this.bonuses = new Bonus[Character.MAX_BONUSES];
        for (int i = 0; i < this.bonuses.length; i++) {
            this.bonuses[i] = Bonus.EMPTY_BONUS;
        }
    }
    
    public Character(int id, String name, short level, short life, short strength, short speed, int imageID, Bonus[] bonuses) {
        this(id, name, level, life, strength, speed, imageID);
        System.arraycopy(bonuses, 0, this.bonuses, 0, (bonuses.length < Character.MAX_BONUSES) ? bonuses.length : Character.MAX_BONUSES);
    }

    @Override
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
        short sum = this.strength;
        for (int i = 0; i < this.bonuses.length; i++) {
            if(!this.bonuses[i].equals(Bonus.EMPTY_BONUS)){
                sum += this.bonuses[i].getStrength();
            }
        }
        return sum;
    }
    public short getSpeed() {
        short sum = this.speed;
        for (int i = 0; i < bonuses.length; i++) {
            if(!this.bonuses[i].equals(Bonus.EMPTY_BONUS)){
                sum += bonuses[i].getSpeed();
            }
        }
        return sum;
    }
    public int getImageID() {
        return this.imageID;
    }
    public Bonus[] getBonuses() {
        return this.bonuses;
    }
}
