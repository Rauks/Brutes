package brutes.db.entity;

import brutes.db.DatasManager;
import brutes.game.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 *
 * @author Thiktak
 */
public class UserEntity {

    public static User create(ResultSet r) throws SQLException {
        return new User(r.getInt("id"), r.getString("pseudo"), r.getString("password"), r.getString("token"));
    }

    public static int save(Connection con, User user) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("UPDATE Users SET pseudo = ?, password = ?, token = ? WHERE id = ?");
        psql.setString(1, user.getPseudo());
        psql.setString(2, user.getPassword());
        psql.setString(3, user.getToken());
        psql.setInt(4, user.getId());
        return psql.executeUpdate();
    }

    public static User findById(int id) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Users WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return UserEntity.create(rs);
        }
        return null;
    }
    
    public static User findByToken(String token) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Users WHERE token = ?");
        psql.setString(1, token);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return UserEntity.create(rs);
        }
        return null;
    }
    
    public static User findByPseudo(String pseudo) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Users WHERE pseudo = ?");
        psql.setString(1, pseudo);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return UserEntity.create(rs);
        }
        return null;
    }
    
    public static String updateToken(int id) throws IOException, SQLException {
        String token = UUID.randomUUID().toString();

        User user = UserEntity.findById(id);
        user.setToken(token);
        DatasManager.save(user);
        
        return token;
    }

    public static void updateTokenToNull(String token) throws IOException, SQLException {
        User user = UserEntity.findByToken(token);
        user.setToken(null);
        DatasManager.save(user);
        /*PreparedStatement psql = DatasManager.prepare("UPDATE users SET token = NULL WHERE token = ?");
        psql.setString(1, token);
        psql.executeUpdate();*/
    }
}
