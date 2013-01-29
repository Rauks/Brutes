package brutes.server.net.response;

import brutes.net.NetworkWriter;
import brutes.net.Protocol;
import brutes.server.db.entity.BonusEntity;
import brutes.server.db.entity.NotFoundEntityException;
import brutes.server.game.Bonus;
import brutes.server.net.NetworkResponseException;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Thiktak
 */
public class BonusResponse extends Response {

    public BonusResponse(NetworkWriter writer) {
        super(writer);
    }

    public void readDataBonus(int id) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
        Bonus bonus = BonusEntity.findOneById(id);
        System.out.println("readDataBonus(" + id + ") : imageID=" + bonus.getImageID());
        this.getWriter().writeDiscriminant(Protocol.R_DATA_BONUS)
                .writeLongInt(id)
                .writeString(bonus.getName())
                .writeShortInt((short) bonus.getLevel())
                .writeShortInt((short) bonus.getLife())
                .writeShortInt((short) bonus.getStrength())
                .writeShortInt((short) bonus.getSpeed())
                .writeLongInt(bonus.getImageID())
                .send();
    }
}
