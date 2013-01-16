/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

import brutes.ScenesContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
public class Network{
    private Socket connection;
    private NetworkReader reader;
    private NetworkWriter writer;
    
    public Network(Socket connection) throws IOException{
        this.connection = connection;
        this.reader = new NetworkReader(this.connection.getInputStream());
        this.writer = new NetworkWriter(this.connection.getOutputStream());
    }
    
    public String login(String user, String password){
        this.writer.writeDiscriminant(Protocol.D_LOGIN)
                .writeString(user)
                .writeString(password)
                .send();
        this.reader.readMessageSize();
        this.reader.readDiscriminant();
        return this.reader.readString();
    }
    public void readLogin(){//Test purpose
        System.out.println(this.reader.readMessageSize());
        System.out.println(this.reader.readDiscriminant());
        String login = this.reader.readString();
        System.out.println(login);
        String password = this.reader.readString();
        System.out.println(password);

        String token = login + '@' + password;
        Logger.getLogger(Network.class.getName()).log(Level.INFO, token);

        this.writer.writeDiscriminant(Protocol.R_LOGIN_SUCCESS)
                .writeString(token)
                .send();
    }
    
    public void disconnect(){
        try {
            this.connection.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
