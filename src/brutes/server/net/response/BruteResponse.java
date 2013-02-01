package brutes.server.net.response;

import brutes.net.NetworkWriter;
import brutes.net.Protocol;
import brutes.server.db.DatasManager;
import brutes.server.db.entity.BonusEntity;
import brutes.server.db.entity.BruteEntity;
import brutes.server.db.entity.NotFoundEntityException;
import brutes.server.game.Brute;
import brutes.server.game.Fight;
import brutes.server.game.User;
import brutes.server.net.NetworkResponseException;
import brutes.server.net.NetworkServer;
import brutes.server.ui;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class BruteResponse
 *
 * Server class for Brute. CRUD model
 *
 * @author Thiktak
 */
public class BruteResponse extends Response {

    public BruteResponse(NetworkWriter writer) {
        super(writer);
    }

    /**
     * readCreateBrute
     *
     * Protocol.D_CREATE_BRUTE Create a randomed Brute for the user
     *
     * @param token
     * @param name
     * @throws IOException
     * @throws SQLException
     * @throws NetworkResponseException
     */
    public void readCreateBrute(String token, String name) throws IOException, SQLException, NetworkResponseException {
        User user = BruteResponse.checkTokenAndReturnUser(token);
        if (name.isEmpty()) {
            throw new NetworkResponseException(Protocol.ERROR_CREATE_BRUTE);
        }
        try {
            BruteEntity.findByUser(user);
            throw new NetworkResponseException(Protocol.ERROR_CREATE_BRUTE);
        } catch (NotFoundEntityException ex) {
            short level = 1;
            short strength = (short) ui.random(3, 10);
            short speed = (short) ui.random(3, 10);
            short life = (short) (ui.random(10, 20));
            int imageID = ui.random(NetworkServer.OPT_ID_IMG_MIN_BRUTE, NetworkServer.OPT_ID_IMG_MAX_BRUTE);

            Brute brute = new Brute(0, name, level, life, strength, speed, imageID);
            brute = DatasManager.insert(brute);
            user.setBrute(brute.getId());
            DatasManager.save(user);
            this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                    .send();
        }
    }

    /**
     * readGetMyBruteId
     *
     * Protocol.D_GET_MY_BRUTE_ID Return the Brute's ID of the current user
     *
     * @param token
     * @throws IOException
     * @throws SQLException
     * @throws NetworkResponseException
     */
    public void readGetMyBruteId(String token) throws IOException, SQLException, NetworkResponseException {
        try {
            User user = BruteResponse.checkTokenAndReturnUser(token);
            Brute brute = BruteEntity.findByUser(user);

            this.getWriter().writeDiscriminant(Protocol.R_BRUTE)
                    .writeLongInt(brute.getId())
                    .send();
        } catch (NotFoundEntityException ex) {
            throw new NetworkResponseException(Protocol.ERROR_BRUTE_NOT_FOUND);
        }
    }

    /**
     * readGetChallengerBruteId
     *
     * Protocol.D_GET_CHALLENGER_BRUTE_ID Return Brute's id about a challenger
     * of the current user.
     *
     * @param token
     * @throws IOException
     * @throws SQLException
     * @throws NetworkResponseException
     */
    public void readGetChallengerBruteId(String token) throws IOException, SQLException, NetworkResponseException {
        User user = BruteResponse.checkTokenAndReturnUser(token);
        Fight fight = FightResponse.getFightWithChallenger(user);
        this.getWriter().writeDiscriminant(Protocol.R_BRUTE)
                .writeLongInt(fight.getBrute2().getId())
                .send();
    }

    /**
     * readDataBrute
     *
     * Protocol.D_GET_CHALLENGER_BRUTE_ID Return Brute's datas by id
     *
     * @param id
     * @throws IOException
     * @throws SQLException
     * @throws NetworkResponseException
     */
    public void readDataBrute(int id) throws IOException, SQLException, NetworkResponseException {
        try {
            Brute brute = BruteEntity.findById(id);

            brute.setBonuses(BonusEntity.findAllByBrute(brute));

            this.getWriter().writeDiscriminant(Protocol.R_DATA_BRUTE)
                    .writeLongInt(id)
                    .writeString(brute.getName())
                    .writeShortInt((short) brute.getLevel())
                    .writeShortInt((short) brute.getLife())
                    .writeShortInt((short) brute.getStrength())
                    .writeShortInt((short) brute.getSpeed())
                    .writeLongInt(brute.getImageID())
                    .writeLongIntArray(brute.getBonusesIDs())
                    .send();
        } catch (NotFoundEntityException ex) {
            throw new NetworkResponseException(Protocol.ERROR_BRUTE_NOT_FOUND);
        }
    }

    /**
     * readUpdateBrute
     * 
     * Protocol.D_UPDATE_BRUTE
     * Delete name of the Brute of the current User
     * 
     * @param token
     * @param name
     * @throws IOException
     * @throws SQLException
     * @throws NetworkResponseException
     */
    public void readUpdateBrute(String token, String name) throws IOException, SQLException, NetworkResponseException {
        try {
            User user = BruteResponse.checkTokenAndReturnUser(token);

            if (name.isEmpty()) {
                throw new NetworkResponseException(Protocol.ERROR_UPDATE_BRUTE);
            }

            Brute brute = BruteEntity.findByUser(user);
            brute.setName(name);
            DatasManager.save(brute);

            this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                    .send();
        } catch (NotFoundEntityException ex) {
            throw new NetworkResponseException(Protocol.ERROR_BRUTE_NOT_FOUND);
        }
    }

    /**
     * readDeleteBrute
     * 
     * Protocol.D_DELETE_BRUTE
     * Delete the Brute of the current User
     * 
     * @param token
     * @throws IOException
     * @throws SQLException
     * @throws NetworkResponseException
     */
    public void readDeleteBrute(String token) throws IOException, SQLException, NetworkResponseException {
        try {
            User user = BruteResponse.checkTokenAndReturnUser(token);
            Brute brute = BruteEntity.findByUser(user);

            DatasManager.delete(brute);

            this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                    .send();
        } catch (NotFoundEntityException ex) {
            throw new NetworkResponseException(Protocol.ERROR_BRUTE_NOT_FOUND);
        }
    }
}
