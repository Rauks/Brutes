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
public class NetworkServer extends Network {
    public static final int OPT_ID_IMG_MIN_BRUTE = 1;
    public static final int OPT_ID_IMG_MAX_BRUTE = 13;
    public static final int OPT_ID_IMG_MIN_BONUS = 31;
    public static final int OPT_ID_IMG_MAX_BONUS = 85;

    public NetworkServer(Socket connection) throws IOException {
        super(connection);
    }

    synchronized public void read() throws IOException, SQLException {
        NetworkReader r = this.getReader();
        r.readMessageSize();
        byte disc = this.getReader().readDiscriminant();
        try {
            switch (disc) {
                case Protocol.D_CHEAT_FIGHT_LOOSE:
                    (new FightResponse(this.getWriter())).readCheatFightLoose(r.readString());
                    break;
                case Protocol.D_CHEAT_FIGHT_RANDOM:
                    (new FightResponse(this.getWriter())).readCheatFightRandom(r.readString());
                    break;
                case Protocol.D_CHEAT_FIGHT_WIN:
                    (new FightResponse(this.getWriter())).readCheatFightWin(r.readString());
                    break;
                case Protocol.D_CREATE_BRUTE:
                    (new BruteResponse(this.getWriter())).readCreateBrute(r.readString(), r.readString());
                    break;
                case Protocol.D_UPDATE_BRUTE:
                    (new BruteResponse(this.getWriter())).readUpdateBrute(r.readString(), r.readString());
                    break;
                case Protocol.D_DELETE_BRUTE:
                    (new BruteResponse(this.getWriter())).readDeleteBrute(r.readString());
                    break;
                case Protocol.D_DO_FIGHT:
                    (new FightResponse(this.getWriter())).readDoFight(r.readString());
                    break;
                case Protocol.D_GET_BONUS:
                    (new BonusResponse(this.getWriter())).readDataBonus(r.readLongInt());
                    break;
                case Protocol.D_GET_CHALLENGER_BRUTE_ID:
                    (new BruteResponse(this.getWriter())).readGetChallengerBruteId(r.readString());
                    break;
                case Protocol.D_GET_BRUTE:
                    (new BruteResponse(this.getWriter())).readDataBrute(r.readLongInt());
                    break;
                case Protocol.D_GET_MY_BRUTE_ID:
                    (new BruteResponse(this.getWriter())).readGetMyBruteId(r.readString());
                    break;
                case Protocol.D_LOGIN:
                    (new UserResponse(this.getWriter())).readLogin(r.readString(), r.readString());
                    break;
                case Protocol.D_LOGOUT:
                    (new UserResponse(this.getWriter())).readLogout(r.readString());
                    break;
                case Protocol.D_GET_IMAGE:
                    this.readGetImage(r.readLongInt());
                    break;
                default:
                    throw new NetworkResponseException(Protocol.ERROR_SRLY_WTF);
            }
        } catch (NetworkResponseException e) {
            this.getWriter().writeDiscriminant(e.getError()).send();
        } catch (NotFoundEntityException e) {
            if (e.getClassTypeIs(Bonus.class)) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_BONUS_NOT_FOUND).send();
            } else if (e.getClassTypeIs(Brute.class)) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_BRUTE_NOT_FOUND).send();
            } else if (e.getClassTypeIs(Fight.class)) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_FIGHT_NOT_FOUND).send();
            } else if (e.getClassTypeIs(User.class)) {
                this.getWriter().writeDiscriminant(Protocol.ERROR_USER_NOT_FOUND).send();
            } else {
                this.getWriter().writeDiscriminant(Protocol.ERROR_SRLY_WTF).send();
            }
        }
    }

    private void readGetImage(int id) throws NetworkResponseException {
        String fileUrl = "res/" + id + ".png";

        if (id == 0 || !(new File(fileUrl)).exists()) {
            throw new NetworkResponseException(Protocol.ERROR_IMAGE_NOT_FOUND);
        }

        this.getWriter().writeDiscriminant(Protocol.R_DATA_IMAGE)
                .writeLongInt(id)
                .writeImage(fileUrl)
                .send();
    }
}
