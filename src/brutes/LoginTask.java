/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes;

import brutes.net.Network;
import brutes.user.Session;
import javafx.concurrent.Task;

/**
 *
 * @author Karl
 */
public class LoginTask extends Task{
    private String login;
    private String password;
    private String host;
    private Session session;

    public LoginTask(String host, String login, String password) {
        this.login = login;
        this.password = password;
        this.host = host;
    }
    
    @Override
    protected Void call() throws Exception {
        if(this.login.isEmpty() || this.password.isEmpty()){
            throw new Exception("Bad login");
        }
        ScenesContext.getInstance().getNetwork().setServer(this.host);
        
        //TODO : login stuff
        
        ScenesContext.getInstance().setSession(new Session(this.toString()));
        return null;
    }
}
