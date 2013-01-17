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
    
    public void readLogin() throws IOException{//Test purpose
        System.out.println(this.reader.readMessageSize());
        System.out.println(this.reader.readDiscriminant());
        String login = this.reader.readString();
        System.out.println(login);
        String password = this.reader.readString();
        System.out.println(password);
        
        if(login.equals("kikoo")){
            this.writer.writeDiscriminant(Protocol.ERROR_SRLY_WTF)
                    .send();
        }
        else if(login.isEmpty()){
            this.writer.writeDiscriminant(Protocol.ERROR_LOGIN_NOT_FOUND)
                    .send();
        }
        else if(password.isEmpty()){
            this.writer.writeDiscriminant(Protocol.ERROR_WRONG_PASSWORD)
                    .send();
        }
        else{
            String token = login + '@' + password;
            Logger.getLogger(Network.class.getName()).log(Level.INFO, "TEST SERVER : generated token #" + token);

            this.writer.writeDiscriminant(Protocol.R_LOGIN_SUCCESS)
                    .writeString(token)
                    .send();
        }
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
