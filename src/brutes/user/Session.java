/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.user;

import brutes.game.ObservableCharacter;

/**
 *
 * @author Karl
 */
public class Session {
    private String server;
    private String token;
    private ObservableCharacter me;
    private ObservableCharacter chalenger;

    public Session(String server, String token) {
        this.token = token;
        this.server = server;
        this.me = new ObservableCharacter();
        this.chalenger = new ObservableCharacter();
    }

    public String getToken() {
        return this.token;
    }
    public String getServer() {
        return this.server;
    }
    public ObservableCharacter getMyCharacter(){
        return this.me;
    }
    public ObservableCharacter getChallengerCharacter(){
        return this.chalenger;
    }
}
