/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net.server;

import brutes.Brutes;
import brutes.db.DatasManager;
import brutes.game.Bonus;
import brutes.game.Fight;
import brutes.game.User;
import brutes.net.Network;
import brutes.net.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
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

    protected String checkToken(String rToken) throws NetworkResponseException {
        if (!rToken.equals(this.token)) {
            //throw new Exception("Bad token: " + rToken + " - " + this.token);
            throw new NetworkResponseException(Protocol.ERROR_TOKEN);
        }
        return rToken;
    }

    public void read() throws IOException, SQLException {
        this.getReader().readMessageSize();
        byte disc = this.getReader().readDiscriminant();
        try {
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
                    throw new NetworkResponseException(Protocol.ERROR_SRLY_WTF);
            }
        } catch (NetworkResponseException e) {
            this.getWriter().writeDiscriminant(e.getError()).send();
        }
    }

    private void readCheatFightWin() throws IOException, SQLException {
        String rToken = this.getReader().readString();

        User user = DatasManager.findUserByToken(rToken);
        Fight fight = DatasManager.findFightByUser(user);
        
        fight.setWinner(fight.getCharacter1());
        DatasManager.save(fight);

        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }

    private void readCheatFightLoose() throws IOException, SQLException {
        String rToken = this.getReader().readString();

        User user = DatasManager.findUserByToken(rToken);
        Fight fight = DatasManager.findFightByUser(user);

        fight.setWinner(fight.getCharacter2());
        DatasManager.save(fight);

        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(false)
                .send();
    }

    private void readCheatFightRandom() throws IOException, SQLException {
        if (Math.random() < 0.5) {
            this.readCheatFightLoose();
        } else {
            this.readCheatFightWin();
        }
    }

    private void readDoFight() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }

    private void readLogin() throws IOException, SQLException, NetworkResponseException {
        String login = this.getReader().readString();
        String password = this.getReader().readString();

        if (login.isEmpty()) {
            throw new NetworkResponseException(Protocol.ERROR_LOGIN_NOT_FOUND);
        } else if (password.isEmpty()) {
            throw new NetworkResponseException(Protocol.ERROR_WRONG_PASSWORD);
        } else {
            PreparedStatement psql = DatasManager.prepare("SELECT id, password FROM users WHERE pseudo = ?");
            psql.setString(1, login);
            ResultSet rs = psql.executeQuery();

            if (!rs.next()) {
                throw new NetworkResponseException(Protocol.ERROR_LOGIN_NOT_FOUND);
            } else {
                if (!password.equals(rs.getString("password"))) {
                    throw new NetworkResponseException(Protocol.ERROR_WRONG_PASSWORD);
                } else {
                    //this.token = DatasManager.updateToken(rs.getInt("id"));
                    this.token = UUID.randomUUID().toString();

                    User user = DatasManager.findUserById(rs.getInt("id"));
                    user.setToken(this.token);
                    DatasManager.save(user);

                    Logger.getLogger(Brutes.class.getName()).log(Level.INFO, "New token [{0}] for user [{1}]", new Object[]{this.token, rs.getInt("id")});
                    this.getWriter().writeDiscriminant(Protocol.R_LOGIN_SUCCESS)
                            .writeString(this.token)
                            .send();
                }
            }
        }
    }

    private void readLogout() throws IOException, SQLException {
        String rToken = this.getReader().readString();

        PreparedStatement psql = DatasManager.prepare("UPDATE users SET token = NULL WHERE token = ?");
        psql.setString(1, rToken);
        psql.executeUpdate();

        this.getWriter().writeDiscriminant(Protocol.R_LOGOUT_SUCCESS)
                .send();
    }

    private void readCreateCharacter() throws IOException {
        this.getReader().readString();
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                .send();
    }

    private void readUpdateCharacter() throws IOException, SQLException {
        String rToken = this.getReader().readString();
        String name = this.getReader().readString();

        User user = DatasManager.findUserByToken(rToken);
        brutes.game.Character character = DatasManager.findCharacterByUser(user);
        character.setName(name);
        DatasManager.save(character);
        
        this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                .send();
    }

    private void readDeleteCharacter() throws IOException {
        this.getReader().readString();
        this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                .send();
    }

    private void readDataBonus() throws IOException, SQLException, NetworkResponseException {
        int id = this.getReader().readLongInt();

        Bonus bonus = DatasManager.findBonusById(id);

        if (bonus == null) {
            throw new NetworkResponseException(Protocol.ERROR_BONUS_NOT_FOUND);
        }

        this.getWriter().writeDiscriminant(Protocol.R_DATA_BONUS)
                .writeLongInt(id)
                .writeString(bonus.getName())
                .writeShortInt((short) bonus.getLevel())
                .writeShortInt((short) bonus.getStrength())
                .writeShortInt((short) bonus.getSpeed())
                .writeLongInt(id)
                .send();
    }

    private void readDataCharacter() throws IOException, SQLException, NetworkResponseException {
        int id = this.getReader().readLongInt();

        brutes.game.Character character = DatasManager.findCharacterById(id);

        if (character == null) {
            throw new NetworkResponseException(Protocol.ERROR_CHARACTER_NOT_FOUND);
        }

        this.getWriter().writeDiscriminant(Protocol.R_DATA_CHARACTER)
                .writeLongInt(id)
                .writeString(character.getName() + " #" + id)
                .writeShortInt((short) character.getLevel())
                .writeShortInt((short) character.getLife())
                .writeShortInt((short) character.getStrength())
                .writeShortInt((short) character.getSpeed())
                .writeLongInt(id) // @TODO : image
                .writeLongIntArray(new int[]{1, 2}) // @TODO : bonus
                .send();
    }

    private void readGetChallengerCharacterId() throws IOException, SQLException {
        String rToken = this.getReader().readString();

        User user = DatasManager.findUserByToken(rToken);
        brutes.game.Character character = DatasManager.findCharacterByUser(user);
        Fight fight = DatasManager.findFightByUser(user);
        if (fight == null) {
            PreparedStatement psql = DatasManager.prepare("SELECT id FROM Brutes WHERE user_id <> ? ORDER BY RANDOM() LIMIT 1");
            psql.setInt(1, user.getId());
            ResultSet query = psql.executeQuery();
            query.next();

            psql = DatasManager.prepare("INSERT INTO fights (brute_id1, brute_id2) VALUES (?, ?)");
            psql.setInt(1, character.getId());
            psql.setInt(2, query.getInt("id")); // @TODO: random
            psql.executeUpdate();

            fight = DatasManager.findFightByUser(user);
        }

        this.getWriter().writeDiscriminant(Protocol.R_CHARACTER)
                .writeLongInt(fight.getCharacter2().getId())
                .send();
    }

    private void readGetMyCharacterId() throws IOException, SQLException {
        String rToken = this.getReader().readString();

        User user = DatasManager.findUserByToken(rToken);
        brutes.game.Character character = DatasManager.findCharacterByUser(user);

        this.getWriter().writeDiscriminant(Protocol.R_CHARACTER)
                .writeLongInt(character.getId())
                .send();
    }
}
