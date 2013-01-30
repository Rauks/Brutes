package brutes.server.net.response;

import brutes.net.NetworkWriter;
import brutes.net.Protocol;
import brutes.server.db.DatasManager;
import brutes.server.db.entity.NotFoundEntityException;
import brutes.server.db.entity.UserEntity;
import brutes.server.game.User;
import brutes.server.net.NetworkServer;
import brutes.server.net.NetworkResponseException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thiktak
 */
public class UserResponse extends Response {

    public UserResponse(NetworkWriter writer) {
        super(writer);
    }

    public void readLogin(String login, String password) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {

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
                    String token = UserEntity.updateToken(rs.getInt("id"));

                    Logger.getLogger(NetworkServer.class.getName()).log(Level.INFO, "New token [{0}] for user [{1}]", new Object[]{token, rs.getInt("id")});
                    this.getWriter().writeDiscriminant(Protocol.R_LOGIN_SUCCESS)
                            .writeString(token)
                            .send();
                }
            }
        }
    }

    public void readLogout(String token) throws IOException, SQLException, NotFoundEntityException {
        UserEntity.updateTokenToNull(token);

        this.getWriter().writeDiscriminant(Protocol.R_LOGOUT_SUCCESS)
                .send();
    }

    /*
     private void readCreateUser(String pseudo, String password) throws IOException, SQLException, NetworkResponseException {
        if (pseudo.isEmpty() || password.isEmpty()) {
            throw new NetworkResponseException(Protocol.ERROR); // @TODO Protocol.ERROR_INPUT_DATAS
        }


        / * @TODO define it !
         * if( UserEntity.findByPseudo(pseudo) != null ) {
         *    throw new NetworkResponseException(Protocol.ERROR_USER_ALREADY_USED);
         * }
         * /

        User user = new User(0, pseudo, password, null);
        DatasManager.insert(user);

        this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                .send();

    }

    private void readDeleteUser(String token) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
        User user = UserResponse.checkTokenAndReturnUser(token);
        DatasManager.delete(user);

        this.getWriter().writeDiscriminant(Protocol.R_ACTION_SUCCESS)
                .send();
    }
    */
}
