package brutes.client.user;

import brutes.client.ScenesContext;
import brutes.client.game.ObservableBrute;
import brutes.client.gui.FightController;
import brutes.client.gui.LoginController;
import brutes.client.net.ErrorResponseException;
import brutes.client.net.InvalidResponseException;
import brutes.client.net.NetworkClient;
import brutes.net.Protocol;
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
    private ObservableBrute me;
    private ObservableBrute chalenger;

    public Session(String server, String token) {
        this.token = token;
        this.server = server;
        this.me = new ObservableBrute();
        this.chalenger = new ObservableBrute();
    }

    public String getToken() {
        return this.token;
    }
    public String getServer() {
        return this.server;
    }
    public ObservableBrute getMyBrute(){
        return this.me;
    }
    public ObservableBrute getChallengerBrute(){
        return this.chalenger;
    }
    
    public void netLoadMyBrute(){
        new Thread(){
            @Override
            public void run() {
                int myId = -1;
                try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                    try {
                        myId = connection.sendGetMyBruteId(ScenesContext.getInstance().getSession().getToken());
                    } catch (ErrorResponseException ex) {
                        if(ex.getErrorCode() == Protocol.ERROR_BRUTE_NOT_FOUND){
                            Logger.getLogger(FightController.class.getName()).log(Level.INFO, "No brute created");
                        }
                        else{
                            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (InvalidResponseException ex) {
                        Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(myId != -1){
                    try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                        try {
                            me.loadBrute(connection.getDataBrute(myId));
                        } catch (InvalidResponseException | ErrorResponseException ex) {
                            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
        
    }
    public void netLoadChallengerBrute(){
        new Thread(){
            @Override
            public void run() {
                int chId = -1;
                try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                    try {
                        chId = connection.sendGetChallengerBruteId(ScenesContext.getInstance().getSession().getToken());
                    } catch (ErrorResponseException ex) {
                        if(ex.getErrorCode() == Protocol.ERROR_BRUTE_NOT_FOUND){
                            Logger.getLogger(FightController.class.getName()).log(Level.INFO, "No ennemy assigned");
                        }
                        else{
                            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (InvalidResponseException ex) {
                        Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(chId != -1){
                    try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                        try {
                            chalenger.loadBrute(connection.getDataBrute(chId));
                        } catch (InvalidResponseException | ErrorResponseException ex) {
                            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
                
    }
}
