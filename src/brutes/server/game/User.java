package brutes.server.game;

import brutes.server.db.Identifiable;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class User implements Identifiable {

    private int id;
    private String pseudo;
    private String password;
    private int brute;
    private String token;

    public User(int id, String pseudo, String password, int brute, String token) {
        this.id = id;
        this.pseudo = pseudo;
        this.password = password;
        this.brute = brute;
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

    public void setBrute(int brute) {
        this.brute = brute;
    }

    public int getBrute() {
        return brute;
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
}
