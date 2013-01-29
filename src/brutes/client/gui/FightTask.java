package brutes.client.gui;

import brutes.client.ScenesContext;
import brutes.client.net.ErrorResponseException;
import brutes.client.net.InvalidResponseException;
import brutes.client.net.NetworkClient;
import brutes.net.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Task;

/**
 *
 * @author Karl
 */
public class FightTask extends Task{
    public static enum FightType {CHEAT_WIN, CHEAT_LOOSE, CHEAT_RANDOM, REGULAR};
    
    private FightType type;
    private ReadOnlyBooleanWrapper result;
    
    public FightTask(FightType type) {
        this.result = new ReadOnlyBooleanWrapper();
        this.type = type;
    }
    
    public ReadOnlyBooleanProperty getResultProperty(){
        return this.result;
    }
    
    @Override
    protected Object call() throws Exception {
        switch(this.type){
            case CHEAT_WIN:
                try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                    try {
                        this.result.set(connection.sendCheatFightWin(ScenesContext.getInstance().getSession().getToken()));
                    } catch (InvalidResponseException | ErrorResponseException ex) {
                        Logger.getLogger(FightController.class.getName()).log(Level.WARNING, null, ex);
                        throw ex;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
                break;
            case CHEAT_LOOSE:
                try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                    try {
                        this.result.set(connection.sendCheatFightLoose(ScenesContext.getInstance().getSession().getToken()));
                    } catch (InvalidResponseException | ErrorResponseException ex) {
                        Logger.getLogger(FightController.class.getName()).log(Level.WARNING, null, ex);
                        throw ex;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
                break;
            case CHEAT_RANDOM:
                try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                    try {
                        this.result.set(connection.sendCheatFightRandom(ScenesContext.getInstance().getSession().getToken()));
                    } catch (InvalidResponseException | ErrorResponseException ex) {
                        Logger.getLogger(FightController.class.getName()).log(Level.WARNING, null, ex);
                        throw ex;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
                break;
            case REGULAR:
                try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                    try {
                        this.result.set(connection.sendDoFight(ScenesContext.getInstance().getSession().getToken()));
                    } catch (InvalidResponseException | ErrorResponseException ex) {
                        Logger.getLogger(FightController.class.getName()).log(Level.WARNING, null, ex);
                        throw ex;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
                break;
            default:
                throw new Exception("Unknow fight type");
        }
        return null;
    }
    
}
