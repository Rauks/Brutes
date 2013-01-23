/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.db.entity;

import brutes.db.DatasManager;
import brutes.db.Entity;
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
public class CharacterEntity implements Entity {

    public static brutes.game.Character create(ResultSet r) throws SQLException {
        return new brutes.game.Character(r.getInt("id"), r.getString("name"), r.getShort("level"), r.getShort("life"), r.getShort("strength"), r.getShort("speed"), r.getInt("id") /* TODO: change ID -> IMG */);
    }

    public static int save(Connection con, brutes.game.Character character) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("UPDATE Brutes SET name = ?, level = ?, life = ?, strength = ?, speed = ? WHERE id = ?");
        psql.setString(1, character.getName());
        psql.setInt(2, character.getLevel());
        psql.setInt(3, character.getLife());
        psql.setInt(4, character.getStrength());
        psql.setInt(5, character.getSpeed());
        psql.setInt(6, character.getId());
        return psql.executeUpdate();
    }

    public static brutes.game.Character findById(int id) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Brutes WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return CharacterEntity.create(rs);
        }
        return null;
    }
    
    public static brutes.game.Character findByUser(User user) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM brutes WHERE user_id = ?");
        psql.setInt(1, user.getId());
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return CharacterEntity.create(rs);
        }
        return null;
    }
}
