/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.db.entity;

import brutes.db.DatasManager;
import brutes.db.Entity;
import brutes.game.Bonus;
import brutes.game.Fight;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Thiktak
 */
public class FightEntity implements Entity {

    static public Fight create(ResultSet r) throws SQLException, IOException {
        return new Fight(r.getInt("id"), CharacterEntity.findById(r.getInt("brute_id1")), CharacterEntity.findById(r.getInt("brute_id2")));
    }

    public static int save(Connection con, Fight fight) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("UPDATE Fights SET brute_id1 = ?, brute_id2 = ?, winner_id = ? WHERE id = ?");
        psql.setInt(1, fight.getCharacter1().getId());
        psql.setInt(2, fight.getCharacter2().getId());
        psql.setInt(3, fight.getWinner() != null ? fight.getWinner().getId() : null);
        psql.setInt(4, fight.getId());
        return psql.executeUpdate();
    }
}
