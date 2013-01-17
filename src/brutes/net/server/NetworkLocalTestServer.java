/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net.server;

import brutes.net.Network;
import brutes.net.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
public class NetworkServer extends Network{
    public NetworkServer(Socket connection) throws IOException{
        super(connection);
    }
    
    public void readLogin() throws IOException{//Test purpose
        this.getReader().readMessageSize();
        this.getReader().readDiscriminant();
        String login = this.getReader().readString();
        String password = this.getReader().readString();
        
        if(login.equals("kikoo")){
            this.getWriter().writeDiscriminant(Protocol.ERROR_SRLY_WTF)
                    .send();
        }
        else if(login.isEmpty()){
            this.getWriter().writeDiscriminant(Protocol.ERROR_LOGIN_NOT_FOUND)
                    .send();
        }
        else if(password.isEmpty()){
            this.getWriter().writeDiscriminant(Protocol.ERROR_WRONG_PASSWORD)
                    .send();
        }
        else{
            String token = login + '@' + password;
            Logger.getLogger(Network.class.getName()).log(Level.INFO, "TEST SERVER : generated token #" + token);

            this.getWriter().writeDiscriminant(Protocol.R_LOGIN_SUCCESS)
                    .writeString(token)
                    .send();
        }
    }
}
