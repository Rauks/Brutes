package brutes.db.entity;

import brutes.db.DatasManager;
import brutes.db.Entity;
import brutes.game.Brute;
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
public class BruteEntity implements Entity {

    public static Brute create(ResultSet r) throws IOException, SQLException {
        Brute brute = new Brute(r.getInt("id"), r.getString("name"), r.getShort("level"), r.getShort("life"), r.getShort("strength"), r.getShort("speed"), r.getInt("id") /* TODO: change ID -> IMG */);
        brute.setUserId(r.getInt("user_id"));
        brute.setBonuses(BonusEntity.findAllByBrute(brute));
        return brute;
    }

    public static int save(Connection con, Brute brute) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("UPDATE Brutes SET name = ?, level = ?, life = ?, strength = ?, speed = ? WHERE id = ?");
        psql.setString(1, brute.getName());
        psql.setInt(2, brute.getLevel());
        psql.setInt(3, brute.getLife());
        psql.setInt(4, brute.getStrength());
        psql.setInt(5, brute.getSpeed());
        psql.setInt(6, brute.getId());
        return psql.executeUpdate();
    }

    public static Brute insert(Connection con, Brute brute) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("INSERT INTO Brutes (user_id, name, level, life, strength, speed) VALUES(?, ?, ?, ?, ?, ?)");
        psql.setInt(1, brute.getUserId());
        psql.setString(2, brute.getName());
        psql.setInt(3, brute.getLevel());
        psql.setInt(4, brute.getLife());
        psql.setInt(5, brute.getStrength());
        psql.setInt(6, brute.getSpeed());
        return findById(psql.executeUpdate());
    }

    public static int delete(Connection con, Brute brute) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("DELETE FROM Brutes WHERE id = ?");
        psql.setInt(1, brute.getId());
        return psql.executeUpdate();
    }

    public static Brute findById(int id) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Brutes WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return BruteEntity.create(rs);
        }
        return null;
    }

    public static Brute findByUser(User user) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM brutes WHERE user_id = ?");
        psql.setInt(1, user.getId());
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return BruteEntity.create(rs);
        }
        return null;
    }
}
