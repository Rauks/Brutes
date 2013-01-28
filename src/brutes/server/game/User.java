package brutes.server.game;

import brutes.server.db.Identifiable;

/**
 *
 * @author Thiktak
 */
public class User implements Identifiable {

    private int id;
    private String pseudo;
    private String password;
    private String token;
    private int imageId;

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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
