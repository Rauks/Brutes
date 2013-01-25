/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes;

import brutes.net.server.db.DatasManager;
import brutes.net.server.NetworkLocalTestServer;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
public class ServerThread extends Thread{
    public static final int TIMEOUT_ACCEPT = 10000;
    public static final int TIMEOUT_CLIENT = 1000;
    
    @Override
    public void run(){
        try (ServerSocket sockserv = new ServerSocket (42666)) {
            sockserv.setSoTimeout(ServerThread.TIMEOUT_ACCEPT);
            
            // DEBUG
            (new File("~$bdd.db")).delete();
            DatasManager.getInstance("sqlite", "~$bdd.db");
            
            while(!this.isInterrupted()){
                try{
                    final Socket sockcli = sockserv.accept();
                    sockcli.setSoTimeout(ServerThread.TIMEOUT_CLIENT);
                    new Thread(){
                        @Override
                        public void run(){
                            try(NetworkLocalTestServer n = new NetworkLocalTestServer(sockcli)){
                                n.read();
                            } catch (Exception ex) {
                                Logger.getLogger(Brutes.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }.start();
                } catch(SocketTimeoutException ex){ }
            }
        } catch (IOException ex) {
            Logger.getLogger(Brutes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
