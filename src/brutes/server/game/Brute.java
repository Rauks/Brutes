package brutes.server.game;

import brutes.server.db.Identifiable;
import java.util.ArrayList;

/**
 *
 * @author Karl
 */
public class Brute implements Identifiable {

    public static final int MAX_BONUSES = 3;
    private int id;
    private String name;
    private short level;
    private short life;
    private short strength;
    private short speed;
    private int imageID;
    private Bonus[] bonuses;
    /* more */
    private int userid;

    public Brute(int id, String name, short level, short life, short strength, short speed, int imageID) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.life = life;
        this.strength = strength;
        this.speed = speed;
        this.imageID = imageID;
        this.bonuses = new Bonus[Brute.MAX_BONUSES];
        for (int i = 0; i < this.bonuses.length; i++) {
            this.bonuses[i] = Bonus.EMPTY_BONUS;
        }
    }

    public Brute(int id, String name, short level, short life, short strength, short speed, int imageID, Bonus[] bonuses) {
        this(id, name, level, life, strength, speed, imageID);
        System.arraycopy(bonuses, 0, this.bonuses, 0, (bonuses.length < Brute.MAX_BONUSES) ? bonuses.length : Brute.MAX_BONUSES);
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
        return this.strength;
    }

    public short getBonusStrength() {
        short sum = 0;
        for (int i = 0; i < this.bonuses.length; i++) {
            if (!this.bonuses[i].equals(Bonus.EMPTY_BONUS)) {
                sum += this.bonuses[i].getStrength();
            }
        }
        return sum;
    }

    public short getWithBonusStrength() {
        return (short) (this.getStrength() + this.getBonusStrength());
    }

    public short getSpeed() {
        return this.speed;
    }

    public short getWithBonusSpeed() {
        return (short) (this.getSpeed() + this.getBonusSpeed());
    }

    public short getBonusSpeed() {
        short sum = 0;
        for (int i = 0; i < bonuses.length; i++) {
            if (!this.bonuses[i].equals(Bonus.EMPTY_BONUS)) {
                sum += bonuses[i].getSpeed();
            }
        }
        return sum;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getImageID() {
        return this.imageID;
    }

    public Bonus[] getBonuses() {
        return this.bonuses;
    }

    public int[] getBonusesIDs() {
        ArrayList<Integer> bonusesIds = new ArrayList<>(Brute.MAX_BONUSES);
        for (int i = 0; i < Brute.MAX_BONUSES; i++) {
            if (this.bonuses[i] != Bonus.EMPTY_BONUS) {
                bonusesIds.add(new Integer(this.bonuses[i].getId()));
            }
        }
        int[] intIDs = new int[bonusesIds.size()];
        for (int i = 0; i < intIDs.length; i++) {
            intIDs[i] = bonusesIds.get(i).intValue();
        }
        return intIDs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public void setLife(short life) {
        this.life = life;
    }

    public void setStrength(short strength) {
        this.strength = strength;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public void setBonuses(Bonus[] bonuses) {
        for (int i = 0; i < Brute.MAX_BONUSES; i++) {
            this.bonuses[i] = Bonus.EMPTY_BONUS;
        }
        if (bonuses != null) {
            for (int i = 0; i < ((bonuses.length < Brute.MAX_BONUSES) ? bonuses.length : Brute.MAX_BONUSES); i++) {
                if (bonuses[i] != null) {
                    this.bonuses[i] = bonuses[i];
                }
            }
        }
    }

    public int getUserId() {
        return userid;
    }

    public void setUserId(int userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return (String) this.getName();
    }
}
