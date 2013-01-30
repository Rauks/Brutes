package brutes.server.db.entity;

import brutes.server.db.DatasManager;
import brutes.server.db.Entity;
import brutes.server.game.Fight;
import brutes.server.game.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thiktak
 */
public class FightEntity implements Entity {

    static public Fight create(ResultSet r) throws SQLException, IOException {
        try {
            return new Fight(r.getInt("id"), BruteEntity.findById(r.getInt("brute_id1")), BruteEntity.findById(r.getInt("brute_id2")));
        } catch (NotFoundEntityException ex) {
            Logger.getLogger(FightEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void save(Connection con, Fight fight) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("UPDATE Fights SET brute_id1 = ?, brute_id2 = ?, winner_id = ? WHERE id = ?");
        psql.setInt(1, fight.getBrute1().getId());
        psql.setInt(2, fight.getBrute2().getId());
        psql.setInt(3, fight.getWinner() != null ? fight.getWinner().getId() : null);
        psql.setInt(4, fight.getId());
        psql.executeUpdate();
    }

    public static Fight insert(Connection con, Fight fight) throws IOException, SQLException {
        PreparedStatement psql = con.prepareStatement("INSERT INTO Fights (brute_id1, brute_id2) VALUES(?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        psql.setInt(1, fight.getBrute1().getId());
        psql.setInt(2, fight.getBrute2().getId());
        psql.executeUpdate();
        fight.setId(psql.getGeneratedKeys().getInt(1));
        return fight;
    }

    public static Fight findByUser(User user) throws IOException, SQLException, NotFoundEntityException {
        return findByBruteId(BruteEntity.findByUser(user).getId());
    }
    
    public static Fight findOneByUser(User user) throws IOException, SQLException, NotFoundEntityException {
        Fight object = findByUser(user);
        if (object == null) {
            throw new NotFoundEntityException(User.class);
        }
        return object;
    }

    public static Fight findByBruteId(int id) throws IOException, SQLException, NotFoundEntityException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM fights WHERE (brute_id1 = ? OR brute_id2 = ?) AND winner_id IS NULL ORDER BY date_created DESC LIMIT 1");
        psql.setInt(1, id);
        psql.setInt(2, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return FightEntity.create(rs);
        }
        throw new NotFoundEntityException(Fight.class);
    }
    
    public static Fight findById(int id) throws IOException, SQLException, NotFoundEntityException {
        PreparedStatement psql = DatasManager.prepare("SELECT * FROM fights WHERE id = ?");
        psql.setInt(1, id);
        ResultSet rs = psql.executeQuery();
        if (rs.next()) {
            return FightEntity.create(rs);
        }
        throw new NotFoundEntityException(Fight.class);
    }
}
