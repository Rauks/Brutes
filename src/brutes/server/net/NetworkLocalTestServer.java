package brutes.server.net;

import brutes.net.Network;
import brutes.net.NetworkReader;
import brutes.net.Protocol;
import brutes.server.db.entity.NotFoundEntityException;
import brutes.server.game.Bonus;
import brutes.server.game.Brute;
import brutes.server.game.Fight;
import brutes.server.game.User;
import brutes.server.net.response.BonusResponse;
import brutes.server.net.response.BruteResponse;
import brutes.server.net.response.FightResponse;
import brutes.server.net.response.UserResponse;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 * @author Karl
 * @author Thiktak
 */
public class NetworkLocalTestServer extends Network {

    public NetworkLocalTestServer(Socket connection) throws IOException {
        super(connection);
    }

    synchronized public void read() throws IOException, SQLException {
        NetworkReader r = this.getReader();
        r.readMessageSize();
        byte disc = this.getReader().readDiscriminant();
        try {
            switch (disc) {
                case Protocol.D_CHEAT_FIGHT_LOOSE:
                    // token, newName
                    (new FightResponse(this.getWriter())).readCheatFightLoose(r.readString());
                    break;
                case Protocol.D_CHEAT_FIGHT_RANDOM:
                    // token, newName
                    (new FightResponse(this.getWriter())).readCheatFightRandom(r.readString());
                    break;
                case Protocol.D_CHEAT_FIGHT_WIN:
                    // token, newName
                    (new FightResponse(this.getWriter())).readCheatFightWin(r.readString());
                    break;
                case Protocol.D_CREATE_BRUTE:
                    // token, name
                    (new BruteResponse(this.getWriter())).readCreateBrute(r.readString(), r.readString());
                    break;
                case Protocol.D_UPDATE_BRUTE:
                    // token, newName
                    (new BruteResponse(this.getWriter())).readUpdateBrute(r.readString(), r.readString());
                    break;
                case Protocol.D_DELETE_BRUTE:
                    // token
                    (new BruteResponse(this.getWriter())).readDeleteBrute(r.readString());
                    break;
                case Protocol.D_DO_FIGHT:
                    // token
                    (new FightResponse(this.getWriter())).readDoFight(r.readString());
                    break;
                case Protocol.D_GET_BONUS:
                    // token
                    (new BonusResponse(this.getWriter())).readDataBonus(r.readLongInt());
                    break;
                case Protocol.D_GET_CHALLENGER_BRUTE_ID:
                    // id
                    (new BruteResponse(this.getWriter())).readGetChallengerBruteId(r.readString());
                    break;
                case Protocol.D_GET_BRUTE:
                    // token
                    (new BruteResponse(this.getWriter())).readDataBrute(r.readLongInt());
                    break;
                case Protocol.D_GET_MY_BRUTE_ID:
                    // id
                    (new BruteResponse(this.getWriter())).readGetMyBruteId(r.readString());
                    break;
                case Protocol.D_LOGIN:
                    // pseudo, password
                    (new UserResponse(this.getWriter())).readLogin(r.readString(), r.readString());
                    break;
                case Protocol.D_LOGOUT:
                    // token
                    (new UserResponse(this.getWriter())).readLogout(r.readString());
                    break;
                case Protocol.D_GET_IMAGE:
                    // id
                    this.readGetImage(r.readLongInt());
                    break;
                /* @TODO define it !
                 * case Protocol.D_CREATE_USER:
                 *    // pseudo, password
                 *    this.readCreateUser(r.readString(), r.readString());
                 *    break;
                 * case Protocol.D_DELETE_USER:
                 *    // token
                 *    this.readDeleteUser(r.readString());
                 *    break;
                 */
                default:
                    throw new NetworkResponseException(Protocol.ERROR_SRLY_WTF);
            }
        } catch (NetworkResponseException e) {
            this.getWriter().writeDiscriminant(e.getError()).send();
        } catch (NotFoundEntityException e) {
            System.out.println("NotFoundEntityException " + e.getClassType() + " <- " + e.toString());
            if( e.getClassTypeIs(Bonus.class) ) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_BONUS_NOT_FOUND).send();
            }
            else if( e.getClassTypeIs(Brute.class) ) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_BRUTE_NOT_FOUND).send();
            }
            else if( e.getClassTypeIs(Fight.class) ) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_FIGHT).send();
                /*@TODO define it ! ERROR_FIGHT_NOT_FOUND */
            }else if( e.getClassTypeIs(User.class) ) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_TOKEN).send();
                /*@TODO define it ! ERROR_FIGHT_NOT_FOUND */
            }
            else {
                this.getWriter().writeDiscriminant(Protocol.ERROR_SRLY_WTF).send();
            }
        }
    }

    private void readGetImage(int id) throws NetworkResponseException {
        String fileUrl = "res/" + id + ".png";
        
        if( id == 0 ) {
            System.out.println("SRLY_WTF " + fileUrl);
            throw new NetworkResponseException(Protocol.ERROR_SRLY_WTF);
        }
        
        if( !(new File(fileUrl)).exists() ) {
            System.out.println("NOT_FOUND " + fileUrl);
            throw new NetworkResponseException(Protocol.ERROR_IMAGE_NOT_FOUND);
        }
        System.out.println("IMG:" + fileUrl);
        
        this.getWriter().writeDiscriminant(Protocol.R_DATA_IMAGE)
                .writeLongInt(id)
                .writeImage(fileUrl)
                .send();
    }
}
