/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.gui;

import brutes.ScenesContext;
import brutes.gui.LoginController;
import brutes.net.Protocol;
import brutes.net.client.ErrorResponseException;
import brutes.net.client.InvalidResponseException;
import brutes.net.client.NetworkClient;
import brutes.user.Session;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
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
    private ReadOnlyBooleanWrapper loginError;
    private ReadOnlyBooleanWrapper passwordError;
    private ReadOnlyBooleanWrapper hostError;

    public LoginTask(String host, String login, String password) {
        this.loginError = new ReadOnlyBooleanWrapper();
        this.passwordError = new ReadOnlyBooleanWrapper();
        this.hostError = new ReadOnlyBooleanWrapper();
        this.login = login;
        this.password = password;
        this.host = host;
    }

    public ReadOnlyBooleanProperty getLoginErrorProperty(){
        return this.loginError.getReadOnlyProperty();
    }
    public ReadOnlyBooleanProperty getPasswordErrorProperty(){
        return this.passwordError.getReadOnlyProperty();
    }
    public ReadOnlyBooleanProperty getHostErrorProperty(){
        return this.hostError.getReadOnlyProperty();
    }
    
    @Override
    protected Void call() throws Exception {
        try (NetworkClient connection = new NetworkClient(new Socket(this.host, Protocol.CONNECTION_PORT))) {
            String token;
            token = connection.sendLogin(this.login, this.password);
            ScenesContext.getInstance().setSession(new Session(this.host, token));
        } catch (UnknownHostException ex) {
            this.hostError.set(true);
            throw ex;
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch(ErrorResponseException ex){
            if(ex.getErrorCode() == Protocol.ERROR_LOGIN_NOT_FOUND){
                this.loginError.set(true);
            }
            if(ex.getErrorCode() == Protocol.ERROR_WRONG_PASSWORD){
                this.passwordError.set(true);
            }
            throw ex;
        } catch(InvalidResponseException ex){
            Logger.getLogger(LoginController.class.getName()).log(Level.WARNING, null, ex);
            throw ex;
        }
        return null;
    }
}
