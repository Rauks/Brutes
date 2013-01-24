package brutes.db.entity;

import brutes.db.DatasManager;
import brutes.db.Entity;
import brutes.game.Bonus;
import brutes.game.Character;
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
        return new Bonus(r.getInt("id"), r.getString("name"), r.getShort("level"), r.getShort("strength"), r.getShort("speed"), r.getInt("id") /* TODO: change ID -> IMG */);
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

    public static Bonus findById(int id) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Bonus WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return BonusEntity.create(rs);
        }
        return null;
    }

    public static Bonus[] findAllByCharacter(Character character) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM Bonus WHERE brute_id = ?");
        psql.setInt(1, character.getId());
        ResultSet rs = psql.executeQuery();

        Bonus[] bonus = new Bonus[Character.MAX_BONUSES];

        int i = 0;
        while (rs.next() && i < 3) {
            bonus[i++] = BonusEntity.create(rs);
        }

        return bonus;
    }
}
