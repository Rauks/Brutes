/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.user;

/**
 *
 * @author Karl
 */
public class Session {
    private String token;
    private Character me;
    private Character chalenger;

    public Session(String token) {
        this.token = token;
        this.me = new Character();
    }

    public String getToken() {
        return this.token;
    }
    public Character getMyCharacter(){
        return this.me;
    }
    public Character getChalengerCharacter(){
        return this.chalenger;
    }
}
