/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

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
public class Network{
    private Socket connection;
    private NetworkReader reader;
    private NetworkWriter writer;
    
    public Network(Socket connection) throws IOException{
        this.connection = connection;
        this.reader = new NetworkReader(this.connection.getInputStream());
        this.writer = new NetworkWriter(this.connection.getOutputStream());
    }
    
    public String sendLogin(String user, String password) throws IOException, ErrorResponseException, InvalidResponseException{
        this.writer.writeDiscriminant(Protocol.D_LOGIN)
                .writeString(user)
                .writeString(password)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_LOGIN_SUCCESS:
                return this.reader.readString();
            case Protocol.ERROR_LOGIN_NOT_FOUND:
                throw new ErrorResponseException("Invalid login");
            case Protocol.ERROR_WRONG_PASSWORD:
                throw new ErrorResponseException("Wrong password");
            default:
                throw new InvalidResponseException();
        }
    }
    public void sendLogout(String token) throws IOException, InvalidResponseException{
        this.writer.writeDiscriminant(Protocol.D_LOGOUT)
                .writeString(token)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_LOGOUT_SUCCESS:
                break;
            default:
                throw new InvalidResponseException();
        }
    }
    
    public void sendCreateCharacter(String token, String name) throws IOException, InvalidResponseException{
        this.writer.writeDiscriminant(Protocol.D_CREATE_CHARACTER)
                .writeString(token)
                .writeString(name)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_ACTION_SUCCESS:
                break;
            default:
                throw new InvalidResponseException();
        }
    }
    public void sendUpdateCharacter(String token, String name) throws IOException, InvalidResponseException{
        this.writer.writeDiscriminant(Protocol.D_UPDATE_CHARACTER)
                .writeString(token)
                .writeString(name)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_ACTION_SUCCESS:
                break;
            default:
                throw new InvalidResponseException();
        }
    }
    public void sendDeleteCharacter(String token) throws IOException, InvalidResponseException{
        this.writer.writeDiscriminant(Protocol.D_DELETE_CHARACTER)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_ACTION_SUCCESS:
                break;
            default:
                throw new InvalidResponseException();
        }
    }
    
    private int sendGetCharacterId(byte getIdDiscriminant) throws IOException, InvalidResponseException{
        this.writer.writeDiscriminant(getIdDiscriminant)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_CHARACTER:
                return this.reader.readLongInt();
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
        this.writer.writeDiscriminant(fightType)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_FIGHT_RESULT:
                return this.reader.readBoolean();
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
    public Character getDataCharacter(int id) throws IOException, InvalidResponseException{
        this.writer.writeDiscriminant(Protocol.D_GET_CHARACTER)
                .writeLongInt(id)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_DATA_CHARACTER:
                int chId = this.reader.readLongInt();
                String name = this.reader.readString();
                short level = this.reader.readShortInt();
                short life = this.reader.readShortInt();
                short strength = this.reader.readShortInt();
                short speed = this.reader.readShortInt();
                int imageID = this.reader.readShortInt();
                ArrayList<Integer> bonusesID = (ArrayList<Integer>) this.reader.readArray();
                return new Character(chId, name, level, life, strength, speed, imageID, bonusesID);
            default:
                throw new InvalidResponseException();
        }
    }
    public Bonus getDataBonus(int id) throws IOException, InvalidResponseException{
        this.writer.writeDiscriminant(Protocol.D_GET_BONUS)
                .writeLongInt(id)
                .send();
        this.reader.readMessageSize();
        switch(this.reader.readDiscriminant()){
            case Protocol.R_DATA_BONUS:
                int boId = this.reader.readLongInt();
                String name = this.reader.readString();
                short level = this.reader.readShortInt();
                short strength = this.reader.readShortInt();
                short speed = this.reader.readShortInt();
                int imageID = this.reader.readShortInt();
                return new Bonus(boId, name, level, strength, speed, imageID);
            default:
                throw new InvalidResponseException();
        }
    }
    
    public void readLogin() throws IOException{//Test purpose
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
            if(this.connection != null){
                this.connection.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
