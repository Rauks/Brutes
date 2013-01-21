/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net.server;

import brutes.Brutes;
import brutes.db.DatasManager;
import brutes.net.Network;
import brutes.net.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
public class NetworkLocalTestServer extends Network {

    protected String token;
    
    public NetworkLocalTestServer(Socket connection) throws IOException {
        super(connection);
    }
    
    protected void checkToken(String token) throws Exception {
        if( !token.equals(this.token) ) {
            throw new Exception("Bad token");
        }
    }

    public void read() throws IOException, Exception {
        this.getReader().readMessageSize();
        byte disc = this.getReader().readDiscriminant();
        switch (disc) {
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
                this.readCreateCharacter();
                break;
            case Protocol.D_UPDATE_CHARACTER:
                this.readUpdateCharacter();
                break;
            case Protocol.D_DELETE_CHARACTER:
                this.readDeleteCharacter();
                break;
            case Protocol.D_DO_FIGHT:
                this.readDoFight();
                break;
            case Protocol.D_GET_BONUS:
                this.readDataBonus();
                break;
            case Protocol.D_GET_CHALLENGER_CHARACTER_ID:
                this.readGetChallengerCharacterId();
                break;
            case Protocol.D_GET_CHARACTER:
                this.readDataCharacter();
                break;
            case Protocol.D_GET_MY_CHARACTER_ID:
                this.readGetMyCharacterId();
                break;
            case Protocol.D_LOGIN:
                this.readLogin();
                break;
            case Protocol.D_LOGOUT:
                this.readLogout();
                break;
            default:
                this.getWriter().writeDiscriminant(Protocol.ERROR_SRLY_WTF).send();
        }
    }

    private void readCheatFightWin() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }

    private void readCheatFightLoose() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(false)
                .send();
    }

    private void readCheatFightRandom() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(Math.random() < 0.5)
                .send();
    }

    private void readDoFight() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }

    private void readLogin() throws IOException, Exception {
        String login = this.getReader().readString();
        String password = this.getReader().readString();

        if (login.isEmpty()) {
            this.getWriter().writeDiscriminant(Protocol.ERROR_LOGIN_NOT_FOUND)
                    .send();
        } else if (password.isEmpty()) {
            this.getWriter().writeDiscriminant(Protocol.ERROR_WRONG_PASSWORD)
                    .send();
        } else {
            PreparedStatement psql = DatasManager.prepare("SELECT id, password FROM users WHERE pseudo = ?");
            psql.setString(1, login);
            ResultSet rs = psql.executeQuery();

            if (!rs.next()) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_LOGIN_NOT_FOUND)
                        .send();
            } else {
                if (!password.equals(rs.getString("password"))) {
                    this.getWriter().writeDiscriminant(Protocol.ERROR_WRONG_PASSWORD)
                            .send();
                } else {
                    this.token = DatasManager.updateToken(rs.getInt("id"));

                    Logger.getLogger(Brutes.class.getName()).log(Level.INFO, "New token [{0}] for user [{1}]", new Object[]{this.token, rs.getInt("id")});
                    this.getWriter().writeDiscriminant(Protocol.R_LOGIN_SUCCESS)
                            .writeString(this.token)
                            .send();
                }
            }
        }
    }

    private void readLogout() throws IOException, Exception {
        String rToken = this.getReader().readString();
        
        PreparedStatement psql = DatasManager.prepare("UPDATE users SET token = NULL WHERE token = ?");
        psql.setString(1, rToken);
        psql.executeQuery();
        
        this.getWriter().writeDiscriminant(Protocol.R_LOGOUT_SUCCESS)
                .send();
    }

    private void readCreateCharacter() throws IOException {
        this.getReader().readString();
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                .send();
    }

    private void readUpdateCharacter() throws IOException {
        this.getReader().readString();
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                .send();
    }

    private void readDeleteCharacter() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                .send();
    }

    private void readDataBonus() {
        this.getWriter().writeDiscriminant(Protocol.R_DATA_BONUS)
                .writeLongInt(1)
                .writeString("Hache")
                .writeShortInt((short) 2)
                .writeShortInt((short) 5)
                .writeShortInt((short) 7)
                .writeLongInt(2)
                .send();
    }

    private void readDataCharacter() {
        this.getWriter().writeDiscriminant(Protocol.R_DATA_CHARACTER)
                .writeLongInt(1)
                .writeString("Rauks")
                .writeShortInt((short) 15)
                .writeShortInt((short) 17)
                .writeShortInt((short) 6)
                .writeShortInt((short) 12)
                .writeLongInt(1)
                .writeLongIntArray(new int[]{1, 1})
                .send();
    }

    private void readGetChallengerCharacterId() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_CHARACTER)
                .writeLongInt(1)
                .send();
    }

    private void readGetMyCharacterId() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_CHARACTER)
                .writeLongInt(2)
                .send();
    }
}
