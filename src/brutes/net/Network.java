package brutes.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
public class Network implements Closeable{
    private static final int CONNECTION_TIMEOUT = 10000;
    
    private Socket connection;
    private NetworkReader reader;
    private NetworkWriter writer;
    
    public Network(Socket connection) throws IOException{
        this.connection = connection;
        this.connection.setSoTimeout(Network.CONNECTION_TIMEOUT);
        this.reader = new NetworkReader(this.connection.getInputStream());
        this.writer = new NetworkWriter(this.connection.getOutputStream());
    }
    
    protected Socket getSocket(){
        return this.connection;
    }
    protected NetworkReader getReader(){
        return this.reader;
    }
    protected NetworkWriter getWriter(){
        return this.writer;
    }

    @Override
    public void close() throws IOException {
        try {
            this.connection.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
