/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net.client;

import brutes.game.Bonus;
import brutes.net.Network;
import brutes.net.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Karl
 */
public class NetworkClient extends Network{
        public NetworkClient(Socket connection) throws IOException{
            super(connection);
        }
    
        public String sendLogin(String user, String password) throws IOException, ErrorResponseException, InvalidResponseException{
        this.getWriter().writeDiscriminant(Protocol.D_LOGIN)
                .writeString(user)
                .writeString(password)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_LOGIN_SUCCESS:
                return this.getReader().readString();
            case Protocol.ERROR_LOGIN_NOT_FOUND:
                throw new ErrorResponseException("Invalid login");
            case Protocol.ERROR_WRONG_PASSWORD:
                throw new ErrorResponseException("Wrong password");
            default:
                throw new InvalidResponseException();
        }
    }
    public void sendLogout(String token) throws IOException, InvalidResponseException{
        this.getWriter().writeDiscriminant(Protocol.D_LOGOUT)
                .writeString(token)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_LOGOUT_SUCCESS:
                break;
            default:
                throw new InvalidResponseException();
        }
    }
    
    public void sendCreateCharacter(String token, String name) throws IOException, InvalidResponseException{
        this.getWriter().writeDiscriminant(Protocol.D_CREATE_CHARACTER)
                .writeString(token)
                .writeString(name)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_ACTION_SUCCESS:
                break;
            default:
                throw new InvalidResponseException();
        }
    }
    public void sendUpdateCharacter(String token, String name) throws IOException, InvalidResponseException{
        this.getWriter().writeDiscriminant(Protocol.D_UPDATE_CHARACTER)
                .writeString(token)
                .writeString(name)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_ACTION_SUCCESS:
                break;
            default:
                throw new InvalidResponseException();
        }
    }
    public void sendDeleteCharacter(String token) throws IOException, InvalidResponseException{
        this.getWriter().writeDiscriminant(Protocol.D_DELETE_CHARACTER)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_ACTION_SUCCESS:
                break;
            default:
                throw new InvalidResponseException();
        }
    }
    
    private int sendGetCharacterId(byte getIdDiscriminant) throws IOException, InvalidResponseException{
        this.getWriter().writeDiscriminant(getIdDiscriminant)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_CHARACTER:
                return this.getReader().readLongInt();
            default:
                throw new InvalidResponseException();
        }
    }
    public int sendGetMyCharacterId(String token) throws IOException, InvalidResponseException{
        return this.sendGetCharacterId(Protocol.D_GET_MY_CHARACTER_ID);
    }
    public int sendGetChallengerCharacterId(String token) throws IOException, InvalidResponseException{
        return this.sendGetCharacterId(Protocol.D_GET_CHALLENGER_CHARACTER_ID);
    }
    
    private boolean sendFight(byte fightType) throws IOException, InvalidResponseException{
        this.getWriter().writeDiscriminant(fightType)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_FIGHT_RESULT:
                return this.getReader().readBoolean();
            default:
                throw new InvalidResponseException();
        }
    }
    public boolean sendCheatFightWin(String token) throws IOException, InvalidResponseException{
        return sendFight(Protocol.D_CHEAT_FIGHT_WIN);
    }
    public boolean sendCheatFightLoose(String token) throws IOException, InvalidResponseException{
        return sendFight(Protocol.D_CHEAT_FIGHT_LOOSE);
    }
    public boolean sendCheatFightRandom(String token) throws IOException, InvalidResponseException{
        return sendFight(Protocol.D_CHEAT_FIGHT_RANDOM);
    }
    public boolean sendDoFight(String token) throws IOException, InvalidResponseException{
        return sendFight(Protocol.D_DO_FIGHT);
    }
    public brutes.game.Character getDataCharacter(int id) throws IOException, InvalidResponseException{
        this.getWriter().writeDiscriminant(Protocol.D_GET_CHARACTER)
                .writeLongInt(id)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_DATA_CHARACTER:
                int chId = this.getReader().readLongInt();
                String name = this.getReader().readString();
                short level = this.getReader().readShortInt();
                short life = this.getReader().readShortInt();
                short strength = this.getReader().readShortInt();
                short speed = this.getReader().readShortInt();
                int imageID = this.getReader().readShortInt();
                ArrayList<Integer> bonusesID = (ArrayList<Integer>) this.getReader().readArray();
                return new brutes.game.Character(chId, name, level, life, strength, speed, imageID, bonusesID);
            default:
                throw new InvalidResponseException();
        }
    }
    public Bonus getDataBonus(int id) throws IOException, InvalidResponseException{
        this.getWriter().writeDiscriminant(Protocol.D_GET_BONUS)
                .writeLongInt(id)
                .send();
        this.getReader().readMessageSize();
        switch(this.getReader().readDiscriminant()){
            case Protocol.R_DATA_BONUS:
                int boId = this.getReader().readLongInt();
                String name = this.getReader().readString();
                short level = this.getReader().readShortInt();
                short strength = this.getReader().readShortInt();
                short speed = this.getReader().readShortInt();
                int imageID = this.getReader().readShortInt();
                return new Bonus(boId, name, level, strength, speed, imageID);
            default:
                throw new InvalidResponseException();
        }
    }
}
