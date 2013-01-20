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
public class NetworkLocalTestServer extends Network{
    public NetworkLocalTestServer(Socket connection) throws IOException{
        super(connection);
    }
    
    public void read() throws IOException{
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.D_CHEAT_FIGHT_LOOSE:
                this.readCheatFightLoose();
                break;
            case Protocol.D_CHEAT_FIGHT_RANDOM:
                this.readCheatFightRandom();
                break;
            case Protocol.D_CHEAT_FIGHT_WIN:
                this.readCheatFightWin();
                break;
            case Protocol.D_CREATE_CHARACTER:
                break;
            case Protocol.D_DELETE_CHARACTER:
                break;
            case Protocol.D_DO_FIGHT:
                this.readDoFight();
                break;
            case Protocol.D_GET_BONUS:
                break;
            case Protocol.D_GET_CHALLENGER_CHARACTER_ID:
                break;
            case Protocol.D_GET_CHARACTER:
                break;
            case Protocol.D_GET_MY_CHARACTER_ID:
                break;
            case Protocol.D_LOGIN:
                this.readLogin();
                break;
            case Protocol.D_LOGOUT:
                break;
            case Protocol.D_UPDATE_CHARACTER:
                break;
            default:
                this.getWriter().writeDiscriminant(Protocol.ERROR_SRLY_WTF).send();
        }
    }
    
    private void readCheatFightWin(){
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }
    private void readCheatFightLoose(){
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(false)
                .send();
    }
    private void readCheatFightRandom(){
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(Math.random() < 0.5)
                .send();
    }
    private void readDoFight(){
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }
    
    private void readLogin() throws IOException{
        String login = this.getReader().readString();
        String password = this.getReader().readString();
        
        if(login.isEmpty()){
            this.getWriter().writeDiscriminant(Protocol.ERROR_LOGIN_NOT_FOUND)
                    .send();
        }
        else if(password.isEmpty()){
            this.getWriter().writeDiscriminant(Protocol.ERROR_WRONG_PASSWORD)
                    .send();
        }
        else{
            String token = login + '@' + password;
            this.getWriter().writeDiscriminant(Protocol.R_LOGIN_SUCCESS)
                    .writeString(token)
                    .send();
        }
    }
}
