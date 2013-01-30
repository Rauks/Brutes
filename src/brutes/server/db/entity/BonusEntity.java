package brutes.server.db.entity;

import brutes.server.db.DatasManager;
import brutes.server.db.Entity;
import brutes.server.game.Bonus;
import brutes.server.game.Brute;
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
        return new Bonus(r.getInt("id"), r.getString("name"), r.getShort("level"), r.getShort("life"), r.getShort("strength"), r.getShort("speed"), r.getInt("image_id"));
    }

    public static void save(Connection con, Bonus bonus) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("UPDATE Bonus SET name = ?, level = ?, life = ?, strength = ?, speed = ?, image_id = ? WHERE id = ?");
        psql.setString(1, bonus.getName());
        psql.setInt(2, bonus.getLevel());
        psql.setInt(3, bonus.getLife());
        psql.setInt(4, bonus.getStrength());
        psql.setInt(5, bonus.getSpeed());
        psql.setInt(6, bonus.getImageID());
        psql.setInt(7, bonus.getId());
        psql.executeUpdate();
    }

    public static Bonus insert(Connection con, Bonus bonus) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("INSERT INTO Bonus (name, level, life, strength, speed, image_id) VALUES(?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        psql.setString(1, bonus.getName());
        psql.setInt(2, bonus.getLevel());
        psql.setInt(3, bonus.getLife());
        psql.setInt(4, bonus.getStrength());
        psql.setInt(5, bonus.getSpeed());
        psql.setInt(6, bonus.getImageID());
        psql.executeUpdate();
        bonus.setId(psql.getGeneratedKeys().getInt(1));
        return bonus;
    }

    public static void delete(Connection con, Bonus bonus) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("DELETE FROM Bonus WHERE id = ?");
        psql.setInt(1, bonus.getId());
        psql.executeUpdate();
    }

    public static Bonus findById(int id) throws IOException, SQLException, NotFoundEntityException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Bonus WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return BonusEntity.create(rs);
        }
        throw new NotFoundEntityException(Bonus.class);
    }

    public static Bonus[] findAllByBrute(Brute brute) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Shop WHERE brute_id = ?");
        psql.setInt(1, brute.getId());
        ResultSet rs = psql.executeQuery();

        Bonus[] bonus = new Bonus[Brute.MAX_BONUSES];

        int i = 0;
        while (rs.next() && i < 3) {
            bonus[i++] = BonusEntity.create(rs);
        }

        return bonus;
    }

    public static Bonus findRandom() throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Bonus ORDER BY Random() LIMIT 1");
        ResultSet rs = psql.executeQuery();

        if (rs.next()) {
            return BonusEntity.create(rs);
        }
        return null;
    }
}
