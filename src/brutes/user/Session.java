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
    private String token;
    private ObservableCharacter me;
    private ObservableCharacter chalenger;

    public Session(String token) {
        this.token = token;
        this.me = new ObservableCharacter();
        this.chalenger = new ObservableCharacter();
    }

    public String getToken() {
        return this.token;
    }
    public ObservableCharacter getMyCharacter(){
        return this.me;
    }
    public ObservableCharacter getChalengerCharacter(){
        return this.chalenger;
    }
}
