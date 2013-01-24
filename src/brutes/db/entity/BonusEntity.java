package brutes.db.entity;

import brutes.db.DatasManager;
import brutes.db.Entity;
import brutes.game.Bonus;
import brutes.game.Brute;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Thiktak
 */
public class BonusEntity implements Entity {

    static public Bonus create(ResultSet r) throws SQLException {
        Bonus bonus = new Bonus(r.getInt("id"), r.getString("name"), r.getShort("level"), r.getShort("strength"), r.getShort("speed"), r.getInt("id") /* TODO: change ID -> IMG */);
        bonus.setBruteId(r.getInt("brute_id"));
        return bonus;
    }

    public static int save(Connection con, Bonus bonus) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("UPDATE Bonus SET name = ?, level = ?, strength = ?, speed = ? WHERE id = ?");
        psql.setString(1, bonus.getName());
        psql.setInt(2, bonus.getLevel());
        psql.setInt(3, bonus.getStrength());
        psql.setInt(4, bonus.getSpeed());
        psql.setInt(5, bonus.getId());
        return psql.executeUpdate();
    }
    
    public static int insert(Connection con, Bonus bonus) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("INSERT INTO Bonus (brute_id, name, level, strength, speed) VALUES(?, ?, ?, ?, ?)");
        psql.setInt(1, bonus.getBruteId());
        psql.setString(2, bonus.getName());
        psql.setInt(3, bonus.getLevel());
        psql.setInt(4, bonus.getStrength());
        psql.setInt(5, bonus.getSpeed());
        return psql.executeUpdate();
    }

    public static Bonus findById(int id) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Bonus WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return BonusEntity.create(rs);
        }
        return null;
    }

    public static Bonus[] findAllByBrute(Brute brute) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Bonus WHERE brute_id = ?");
        psql.setInt(1, brute.getId());
        ResultSet rs = psql.executeQuery();

        Bonus[] bonus = new Bonus[Brute.MAX_BONUSES];

        int i = 0;
        while (rs.next() && i < 3) {
            bonus[i++] = BonusEntity.create(rs);
        }

        return bonus;
    }

    public static Bonus findRandomByBrute(Brute brute) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Bonus WHERE brute_id = ? ORDER BY Random() LIMIT 1");
        psql.setInt(1, brute.getId());
        ResultSet rs = psql.executeQuery();

        if( rs.next() ) {
            return BonusEntity.create(rs);
        }
        return null;
    }
    
    public static Bonus findRandom() throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Bonus ORDER BY Random() LIMIT 1");
        ResultSet rs = psql.executeQuery();

        if( rs.next() ) {
            return BonusEntity.create(rs);
        }
        return null;
    }
}
