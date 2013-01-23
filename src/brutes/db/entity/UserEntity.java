/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.db.entity;

import brutes.game.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
