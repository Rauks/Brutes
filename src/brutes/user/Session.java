/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.user;

import brutes.ScenesContext;
import brutes.game.ObservableCharacter;
import brutes.gui.FightController;
import brutes.gui.LoginController;
import brutes.net.Protocol;
import brutes.net.client.ErrorResponseException;
import brutes.net.client.InvalidResponseException;
import brutes.net.client.NetworkClient;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public void netLoadMyCharacter(){
        int myId = -1;
        try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
            try {
                myId = connection.sendGetMyCharacterId(ScenesContext.getInstance().getSession().getToken());
            } catch (InvalidResponseException | ErrorResponseException ex) {
                Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(myId != -1){
            try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                try {
                    this.me.loadCharacter(connection.getDataCharacter(myId));
                } catch (InvalidResponseException | ErrorResponseException ex) {
                    Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void netLoadChallengerCharacter(){
        int chId = -1;
        try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
            try {
                chId = connection.sendGetChallengerCharacterId(ScenesContext.getInstance().getSession().getToken());
            } catch (InvalidResponseException | ErrorResponseException ex) {
                Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(chId != -1){
            try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                try {
                    this.chalenger.loadCharacter(connection.getDataCharacter(chId));
                } catch (InvalidResponseException | ErrorResponseException ex) {
                    Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
