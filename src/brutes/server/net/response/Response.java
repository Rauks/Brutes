package brutes.server.net.response;

import brutes.net.NetworkWriter;
import brutes.net.Protocol;
import brutes.server.db.entity.NotFoundEntityException;
import brutes.server.db.entity.UserEntity;
import brutes.server.game.User;
import brutes.server.net.NetworkResponseException;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Thiktak
 */
public class Response {

    private NetworkWriter writer;

    public Response(NetworkWriter writer) {
        this.writer = writer;
    }

    public NetworkWriter getWriter() {
        return this.writer;
    }

    static public User checkTokenAndReturnUser(String token) throws IOException, SQLException, NetworkResponseException {
        User user = UserEntity.findByToken(token);
        if (user == null) {
            throw new NetworkResponseException(Protocol.ERROR_TOKEN);
        }
        return user;
    }
}
