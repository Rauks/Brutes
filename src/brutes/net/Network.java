/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
public class Network{
    public final static Network instance = new Network();
    
    private Socket socket;
    
    public static Network getInstance(){
        if(Network.instance.socket == null) {
            Network.instance.setServer("localhost");
        }
        return Network.instance;
    }
    
    public void setServer(String host){
        try {
            this.socket = new Socket(host, Protocol.CONNECTION_PORT);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
