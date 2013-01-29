package brutes.server.net.response;

import brutes.net.NetworkWriter;
import brutes.net.Protocol;
import brutes.server.db.DatasManager;
import brutes.server.db.entity.BonusEntity;
import brutes.server.db.entity.BruteEntity;
import brutes.server.db.entity.FightEntity;
import brutes.server.db.entity.NotFoundEntityException;
import brutes.server.game.Bonus;
import brutes.server.game.Brute;
import brutes.server.game.Fight;
import brutes.server.game.User;
import brutes.server.net.NetworkLocalTestServer;
import brutes.server.net.NetworkResponseException;
import brutes.server.ui;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thiktak
 */
public class FightResponse extends Response {

    public FightResponse(NetworkWriter writer) {
        super(writer);
    }

    static public Fight getFightWithChallenger(User user) throws IOException, SQLException, NotFoundEntityException, NetworkResponseException {
        Brute brute = BruteEntity.findOneByUser(user);

        Fight fight = FightEntity.findByUser(user); // and not findOneByUser

        if (fight == null) {
            Brute otherBrute = BruteEntity.findOneRandomAnotherToBattleByUser(user);

            fight = new Fight();
            fight.setBrute1(brute);
            fight.setBrute2(otherBrute);
            DatasManager.insert(fight);
        }

        if (fight == null) {
            throw new NetworkResponseException(Protocol.ERROR_FIGHT);
        }
        return fight;
    }

    public void readCheatFightWin(String token) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
        User user = this.checkTokenAndReturnUser(token);
        Fight fight = this.getFightWithChallenger(user);

        if (fight == null) {
            throw new NetworkResponseException(Protocol.ERROR_FIGHT);
        }

        Brute brute = fight.getBrute1();
        System.out.println("!! win !!");

        // level UP !
        brute.setLevel((short) Math.min(brute.getLevel() + 1, 100));
        System.out.println("Life UP");

        // force UP !
        if (ui.random()) // 50%
        {
            System.out.println("Force:");
            switch (ui.random(1, 3)) {
                case 1:
                    brute.setLife((short) (brute.getLife() + ui.random(1, 10)));
                    System.out.println("  Life UP");
                    break;
                case 2:
                    brute.setSpeed((short) (brute.getSpeed() + ui.random(1, 5)));
                    System.out.println("  Speed UP");
                    break;
                case 3:
                    brute.setStrength((short) (brute.getStrength() + ui.random(1, 5)));
                    System.out.println("  Strength UP");
                    break;
            }
        }

        // Bonus UP
        if (ui.random()) {
            System.out.println("Bonus:");
            Logger.getLogger(NetworkLocalTestServer.class.getName()).log(Level.INFO, "readCheatFightWin : bonus - random = yes");
            // S'il y a déjà 3 bonus
            Bonus bonusCharacter = BonusEntity.findRandomByBrute(brute);
            if (bonusCharacter != null && ui.random()) {
                Logger.getLogger(NetworkLocalTestServer.class.getName()).log(Level.INFO, "readCheatFightWin : bonus - delete (" + bonusCharacter.getName() + ")");
                DatasManager.delete(bonusCharacter);
                System.out.println("  delete");
            }

            int AllCharacterBonus = ui.lengthObjects(BonusEntity.findAllByBrute(brute));

            Logger.getLogger(NetworkLocalTestServer.class.getName()).log(Level.INFO, "readCheatFightWin : bonus - length = " + AllCharacterBonus);
            if (AllCharacterBonus < 3) {
                // On trouve quelque chose ...
                Bonus bonusTreasure = BonusEntity.findRandom();
                bonusTreasure.setBruteId(brute.getId());

                bonusTreasure.setLevel((short) ui.randomMiddle(brute.getLevel() / 2, .5));
                bonusTreasure.setLife((short) ui.randomMiddle(brute.getLife() / 2, .5));
                bonusTreasure.setStrength((short) ui.randomMiddle(brute.getStrength() / 2, .5));
                bonusTreasure.setSpeed((short) ui.randomMiddle(brute.getSpeed() / 2, .5));

                DatasManager.insert(bonusTreasure);
                System.out.println("  insert");
                Logger.getLogger(NetworkLocalTestServer.class.getName()).log(Level.INFO, "readCheatFightWin : bonus - insert (" + bonusTreasure.getName() + ")");
            }
        }

        DatasManager.save(brute);

        /*switch(ui.random(1, 6))
         {
         case 1: // Level Up
         brute.setLevel((short)(brute.getLevel()+1));
         Logger.getLogger(NetworkLocalTestServer.class.getName()).log(Level.INFO, "Result: +1 brute level ({0})", brute.getLevel());
         DatasManager.save(brute);
         break;
         case 2: // Bonus Up
         case 3: // Bonus Up
         Bonus bonus = BonusEntity.findRandomByBrute(brute);
         if( bonus != null )
         {
         bonus.setLevel((short)(bonus.getLevel()+1));
         bonus.setStrength((short)(((double)bonus.getStrength())*(1+Math.random())/2));
         bonus.setSpeed((short)(((double)bonus.getSpeed())*(1+Math.random())/2));
         DatasManager.save(bonus);
         Logger.getLogger(NetworkLocalTestServer.class.getName()).log(Level.INFO, "Result: +1 bonus level ({0} [{1}])", new Object[]{bonus.getName(), bonus.getLevel()});
         }
         else
         {
         bonus = BonusEntity.findRandomByBrute(fight.getBrute2());
         if( bonus != null )
         {
         bonus.setLevel((short) ui.random(1, (int)(brute.getLevel()/2)));
         bonus.setStrength((short)(((double)bonus.getStrength())*(1+Math.random())/2));
         bonus.setSpeed((short)(((double)bonus.getSpeed())*(1+Math.random())/2));
         bonus.setBruteId(brute.getId());
         DatasManager.insert(bonus);
         Logger.getLogger(NetworkLocalTestServer.class.getName()).log(Level.INFO, "Result: new bonus ({0} [{1}])", new Object[]{bonus.getName(), bonus.getLevel()});
         }
                    
         }
         break;
         default: // New
         Logger.getLogger(NetworkLocalTestServer.class.getName()).log(Level.INFO, "Result: Nothing ...");
         break;
         }*/

        fight.setWinner(brute);
        DatasManager.save(fight);

        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }

    public void readCheatFightLoose(String token) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
        User user = this.checkTokenAndReturnUser(token);
        Fight fight = this.getFightWithChallenger(user);

        if (fight == null) {
            throw new NetworkResponseException(Protocol.ERROR_FIGHT);
        }

        fight.setWinner(fight.getBrute2());
        DatasManager.save(fight);

        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(false)
                .send();
    }

    public void readCheatFightRandom(String token) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
        if (Math.random() < 0.5) {
            this.readCheatFightLoose(token);
        } else {
            this.readCheatFightWin(token);
        }
    }

    public void readDoFight(String token) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
        User user = this.checkTokenAndReturnUser(token);
        Fight fight = this.getFightWithChallenger(user);

        int i = 0;
        int lost;
        while (fight.getBrute1().getLife() > 0 && fight.getBrute2().getLife() > 0) {

            for (int j = 0; j < 2; j++) {
                Brute ch1 = j == 0 ? fight.getBrute1() : fight.getBrute2();
                Brute ch2 = j == 0 ? fight.getBrute2() : fight.getBrute1();
                int random = ui.random(0, 10);
                if (random == 0) {
                } else if (random == 1) {
                    ch1.setSpeed((short) (ch1.getSpeed() + 1));
                    ch1.setStrength((short) (ch1.getStrength() + 1));
                } else {
                    double pWin = ((double) (10 * ch1.getLevel() + ch1.getStrength()));
                    pWin *= ((double) ch1.getSpeed() / ((double) (1 + ch1.getSpeed() + ch2.getSpeed())));
                    pWin *= ((double) ch1.getStrength() / ((double) (1 + ch1.getStrength() + ch2.getStrength())));
                    pWin *= 1 + ((double) ch1.getLife() / ((double) (1 + ch1.getLife() + ch2.getLife())));
                    // DEBUG
                    //System.out.println("@@" + pWin);
                    //System.out.println("@@ 100*(10*" + ch1.getLevel() + "+" + ch1.getStrength() + ")*(" + ch1.getSpeed() + "/(1+" + ch1.getSpeed() + "+" + ch2.getSpeed() + ")");
                    //System.out.println(ch1.getLife() + "/(1+" + ch1.getLife() + "+" + ch2.getLife() + ")");
                    ch2.setLife((short) (ch2.getLife() - pWin));
                }
            }
        }
        if (fight.getBrute1().getLife() > 0) {
            this.readCheatFightWin(token);
        } else {
            this.readCheatFightLoose(token);
        }
    }
}
