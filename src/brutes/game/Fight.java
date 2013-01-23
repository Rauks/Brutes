/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import brutes.db.DatasManager;
import brutes.db.Identifiable;
import brutes.db.SQL;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Thiktak
 */
public class Fight implements Identifiable {

    private int id;
    private Character character1;
    private Character character2;
    private Character winner;

    public Fight() {
    }

    public Fight(int id, Character character1, Character character2) {
        this.id = id;
        this.character1 = character1;
        this.character2 = character2;
    }

    public int getId() {
        return id;
    }

    public Character getCharacter1() {
        return character1;
    }

    public void setCharacter1(Character character1) {
        this.character1 = character1;
    }

    public Character getCharacter2() {
        return character2;
    }

    public void setCharacter2(Character character2) {
        this.character2 = character2;
    }

    public Character getWinner() {
        return winner;
    }

    public void setWinner(Character winner) {
        this.winner = winner;
    }
}
