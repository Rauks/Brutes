/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

import brutes.db.IsIdentifiable;
import java.util.HashMap;

/**
 *
 * @author Thiktak
 */
public class User implements IsIdentifiable {
    
    private int id;
    private String pseudo;
    private String password;
    private String token;
    private HashMap<String, Character> characters = new HashMap<>();

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
