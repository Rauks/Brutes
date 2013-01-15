/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import brutes.game.media.CharacterImage;
import java.util.ArrayList;

/**
 *
 * @author Karl
 */
public class Character {
    private int id;
    private String name;
    private int level;
    private int life;
    private int strength;
    private int speed;
    private CharacterImage image;
    private ArrayList<Bonus> bonuses;

    public Character(int id, String name, int level, int life, int strength, int speed, CharacterImage image) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.life = life;
        this.strength = strength;
        this.speed = speed;
        this.image = image;
    }
    public Character(int id, String name, int level, int life, int strength, int speed, CharacterImage image, ArrayList<Bonus> bonuses){
        this(id, name, level, life, strength, speed, image);
        this.bonuses = bonuses;
    }
    
    public void addBonus(Bonus bonus){
        this.bonuses.add(bonus);
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
    public int getLife() {
        return life;
    }
    public int getStrength() {
        return strength;
    }
    public int getSpeed() {
        return speed;
    }
    public CharacterImage getImage() {
        return image;
    }
    public ArrayList<Bonus> getBonuses() {
        return bonuses;
    }
}
