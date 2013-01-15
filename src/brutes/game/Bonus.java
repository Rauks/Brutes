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
    private int id;
    private String name;
    private int level;
    private int strength;
    private int speed;
    private BonusImage image;

    public Bonus(int id, String name, int level, int strength, int speed, BonusImage image) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.strength = strength;
        this.speed = speed;
        this.image = image;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getLevel() {
        return level;
    }
    public int getStrength() {
        return strength;
    }
    public int getSpeed() {
        return speed;
    }
    public BonusImage getImage() {
        return image;
    }
}
