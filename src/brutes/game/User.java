package brutes.game;

import brutes.db.Identifiable;
import java.util.HashMap;

/**
 *
 * @author Thiktak
 */
public class User implements Identifiable {
    
    private int id;
    private String pseudo;
    private String password;
    private String token;
    private HashMap<String, Brute> brutes = new HashMap<>();

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

    public HashMap<String, Brute> getBrutes() {
        return brutes;
    }

    public void setBrutes(HashMap<String, Brute> brutes) {
        this.brutes = brutes;
    }

    public void addBrute(String name, Brute brute) {
        this.brutes.put(name, brute);
    }

    /*
     public void removeBrute(String name, Brute brute) {
     this.brutes.remove(name);
     }
     //*/
}
