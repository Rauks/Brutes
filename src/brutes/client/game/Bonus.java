package brutes.client.game;

import brutes.client.game.media.DataImage;

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
    private DataImage image;

    private Bonus() {
        this.id = 0;
        this.name = null;
        this.level = 0;
        this.strength = 0;
        this.speed = 0;
        this.image = null;
    }

    public Bonus(int id, String name, short level, short strength, short speed, DataImage image) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.strength = strength;
        this.speed = speed;
        this.image = image;
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

    public short getStrength() {
        return this.strength;
    }

    public short getSpeed() {
        return this.speed;
    }

    public DataImage getDataImage() {
        return this.image;
    }
}
