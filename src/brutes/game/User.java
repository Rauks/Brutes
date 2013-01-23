/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import brutes.db.Entity;
import brutes.db.SQL;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 *
 * @author Thiktak
 */
public class User extends Entity {
    
    public String tableName = "Users";
    
    @SQL(name="id", type="int")
    private int id;
    
    @SQL(name="pseudo", type="varchar")
    private String pseudo;
    
    @SQL(name="password", type="varchar")
    private String password;
    
    @SQL(name="token", type="varchar")
    private String token;
    
    private HashMap<String, Character> characters = new HashMap<>();

    public User(ResultSet r) throws Exception {
        this(r.getInt("id"), r.getString("pseudo"), r.getString("password"), r.getString("token"));
    }

    public User(int id, String pseudo, String password, String token) {
        this.id = id;
        this.pseudo = pseudo;
        this.password = password;
        this.token = token;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HashMap<String, Character> getCharacters() {
        return characters;
    }

    public void setCharacters(HashMap<String, Character> characters) {
        this.characters = characters;
    }

    public void addCharacter(String name, Character character) {
        this.characters.put(name, character);
    }

    /*
     public void removeCharacter(String name, Character character) {
     this.characters.remove(name);
     }
     //*/
}
