package brutes.net.server.db.entity;

import brutes.net.server.db.DatasManager;
import brutes.net.server.db.Entity;
import brutes.game.Fight;
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
public class FightEntity implements Entity {

    static public Fight create(ResultSet r) throws SQLException, IOException {
        return new Fight(r.getInt("id"), BruteEntity.findById(r.getInt("brute_id1")), BruteEntity.findById(r.getInt("brute_id2")));
    }

    public static int save(Connection con, Fight fight) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("UPDATE Fights SET brute_id1 = ?, brute_id2 = ?, winner_id = ? WHERE id = ?");
        psql.setInt(1, fight.getBrute1().getId());
        psql.setInt(2, fight.getBrute2().getId());
        psql.setInt(3, fight.getWinner() != null ? fight.getWinner().getId() : null);
        psql.setInt(4, fight.getId());
        return psql.executeUpdate();
    }

    public static Fight insert(Connection con, Fight fight) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("INSERT INTO Fights (brute_id1, brute_id2) VALUES(?, ?)");
        psql.setInt(1, fight.getBrute1().getId());
        psql.setInt(2, fight.getBrute2().getId());
        return findById(psql.executeUpdate());
    }

    public static Fight findByUser(User user) throws IOException, SQLException {
        return findByBruteId(BruteEntity.findByUser(user).getId());
    }
    
    public static Fight findOneByUser(User user) throws IOException, SQLException, NotFoundEntityException {
        Fight object = findByUser(user);
        if (object == null) {
            throw new NotFoundEntityException(User.class);
        }
        return object;
    }

    public static Fight findByBruteId(int id) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM fights WHERE (brute_id1 = ? OR brute_id2 = ?) AND winner_id IS NULL ORDER BY date_created DESC LIMIT 1");
        psql.setInt(1, id);
        psql.setInt(2, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return FightEntity.create(rs);
        }
        return null;
    }
    
    public static Fight findById(int id) throws IOException, SQLException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM fights WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return FightEntity.create(rs);
        }
        return null;
    }
}
