package brutes.server.game;

import brutes.server.db.Identifiable;

/**
 *
 * @author Karl
 */
public class Bonus implements Identifiable {
    public static final Bonus EMPTY_BONUS = new Bonus();
    
    private int id;
    private String name;
    private short level;
    private short strength;
    private short speed;
    private int imageID;
    
    /* more */
    private int bruteId;
    
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
        return (short) (strength*(1+ (double) this.level/2));
    }
    public short getSpeed() {
        return speed;
    }
    public int getImage() {
        return imageID;
    }
    public void setLevel(short level) {
        this.level = level;
    }
    public int getBruteId() {
        return bruteId;
    }
    public void setBruteId(int bruteId) {
        this.bruteId = bruteId;
    }
    public void setStrength(short strength) {
        this.strength = strength;
    }
    public void setSpeed(short speed) {
        this.speed = speed;
    }
    
}
