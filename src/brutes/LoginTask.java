/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes;

import brutes.net.ErrorResponseException;
import brutes.net.InvalidResponseException;
import brutes.net.Network;
import brutes.net.Protocol;
import brutes.user.Session;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 *
 * @author Karl
 */
public class LoginTask extends Task{
    private String login;
    private String password;
    private String host;

    public LoginTask(String host, String login, String password) {
        this.login = login;
        this.password = password;
        this.host = host;
    }
    
    @Override
    protected Void call() throws Exception {
        try{
            Socket socket = new Socket(host, Protocol.CONNECTION_PORT);
            ScenesContext.getInstance().setNetwork(new Network(socket));
        } catch(Exception ex){
            Logger.getLogger(LoginTask.class.getName()).log(Level.WARNING, "Host not found");
            throw new Exception("Login task failed at server connection");
        }
        try{
            String token = ScenesContext.getInstance().getNetwork().sendLogin(this.login, this.password);
            ScenesContext.getInstance().setSession(new Session(token));
        } catch(ErrorResponseException ex){
            ScenesContext.getInstance().getNetwork().disconnect();
            Logger.getLogger(LoginTask.class.getName()).log(Level.INFO, ex.getMessage());
            throw new Exception("Login task failed at login/password validation");
        } catch(InvalidResponseException ex){
            ScenesContext.getInstance().getNetwork().disconnect();
            Logger.getLogger(LoginTask.class.getName()).log(Level.WARNING, ex.getMessage());
            throw new Exception("Login task failed at server response");
        }
        return null;
    }
}
