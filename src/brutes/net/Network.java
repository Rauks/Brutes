/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

import brutes.net.client.InvalidResponseException;
import brutes.net.client.ErrorResponseException;
import brutes.game.Bonus;
import brutes.game.Character;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
public abstract class Network{
    private Socket connection;
    private NetworkReader reader;
    private NetworkWriter writer;
    
    public Network(Socket connection) throws IOException{
        this.connection = connection;
        this.reader = new NetworkReader(this.connection.getInputStream());
        this.writer = new NetworkWriter(this.connection.getOutputStream());
    }
    
    protected Socket getConnection(){
        return this.connection;
    }
    protected NetworkReader getReader(){
        return this.reader;
    }
    protected NetworkWriter getWriter(){
        return this.writer;
    }
    
    public void disconnect(){
        try {
            if(this.connection != null){
                this.connection.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
