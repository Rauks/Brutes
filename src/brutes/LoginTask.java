/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes;

import brutes.net.Protocol;
import brutes.net.client.ErrorResponseException;
import brutes.net.client.InvalidResponseException;
import brutes.net.client.NetworkClient;
import brutes.user.Session;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;

/**
 *
 * @author Karl
 */
public class LoginTask extends Task{
    private String login;
    private String password;
    private String host;
    private ReadOnlyStringWrapper statusMessage = new ReadOnlyStringWrapper();

    public LoginTask(String host, String login, String password) {
        this.login = login;
        this.password = password;
        this.host = host;
    }

    public ReadOnlyStringProperty statusMessageProperty() {
        return this.statusMessage;
    }
    
    
    @Override
    protected Void call() throws Exception {
        this.statusMessage.setValue("Login started");
        try{
            Socket socket = new Socket(host, Protocol.CONNECTION_PORT);
            ScenesContext.getInstance().setNetwork(new NetworkClient(socket));
        } catch(Exception ex){
            this.statusMessage.setValue("Host not found");
            throw new Exception("Login task failed at server connection");
        }
        try{
            String token = ScenesContext.getInstance().getNetwork().sendLogin(this.login, this.password);
            ScenesContext.getInstance().setSession(new Session(token));
        } catch(ErrorResponseException ex){
            ScenesContext.getInstance().getNetwork().disconnect();
            this.statusMessage.setValue(ex.getMessage());
            throw new Exception("Login task failed at login/password validation");
        } catch(InvalidResponseException ex){
            ScenesContext.getInstance().getNetwork().disconnect();
            this.statusMessage.setValue(ex.getMessage());
            throw new Exception("Login task failed at server response");
        }
        this.statusMessage.setValue("Loged");
        return null;
    }
}
