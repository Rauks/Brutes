package brutes.server.net.response;

import brutes.net.NetworkWriter;
import brutes.net.Protocol;
import brutes.server.DoFight;
import brutes.server.db.DatasManager;
import brutes.server.db.entity.BonusEntity;
import brutes.server.db.entity.BruteEntity;
import brutes.server.db.entity.FightEntity;
import brutes.server.db.entity.NotFoundEntityException;
import brutes.server.game.Bonus;
import brutes.server.game.Brute;
import brutes.server.game.Fight;
import brutes.server.game.User;
import brutes.server.net.NetworkResponseException;
import brutes.server.ui;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Thiktak
 */
public class FightResponse extends Response {

    public FightResponse(NetworkWriter writer) {
        super(writer);
    }

    static public Fight getFightWithChallenger(User user) throws IOException, SQLException, NetworkResponseException {
        try {
            Brute brute = BruteEntity.findByUser(user);

            Fight fight;
            try {
                return FightEntity.findByUser(user);
            } catch (NotFoundEntityException ex) {
                Brute otherBrute = BruteEntity.findOneRandomAnotherToBattleByUser(user);

                fight = new Fight();
                fight.setBrute1(brute);
                fight.setBrute2(otherBrute);
                return DatasManager.insert(fight);
            }
        } catch (NotFoundEntityException ex) {
            throw new NetworkResponseException(Protocol.ERROR_BRUTE_NOT_FOUND);
        }
    }

    public void readCheatFightWin(String token) throws IOException, SQLException, NetworkResponseException {
        User user = FightResponse.checkTokenAndReturnUser(token);
        Fight fight = FightResponse.getFightWithChallenger(user);

        if (fight == null) {
            throw new NetworkResponseException(Protocol.ERROR_FIGHT);
        }

        Brute brute = fight.getBrute1();

        // level UP !
        brute.setLevel((short) Math.min(brute.getLevel() + 1, 100));

        // stats UP !
        switch (ui.random(2)) {
            case 0:
                brute.setLife((short) (brute.getLife() + ui.random(1, 10)));
                break;
            case 1:
                brute.setSpeed((short) (brute.getSpeed() + ui.random(1, 5)));
                break;
            case 2:
                brute.setStrength((short) (brute.getStrength() + ui.random(1, 5)));
                break;
        }

        // bonus UP/RM
        // Une chance sur 2
        if (ui.random()) {
            int select = ui.random(1, 2);

            // Une chance sur 3 de perdre un bonus
            brute.setBonus(select, ui.random(1, 3) == 1 ? Bonus.EMPTY_BONUS : BonusEntity.findMathematicalRandom());
        }

        DatasManager.save(brute);
        fight.setWinner(brute);
        DatasManager.save(fight);

        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }

    public void readCheatFightLoose(String token) throws IOException, SQLException, NetworkResponseException {
        User user = FightResponse.checkTokenAndReturnUser(token);
        Fight fight = FightResponse.getFightWithChallenger(user);

        if (fight == null) {
            throw new NetworkResponseException(Protocol.ERROR_FIGHT);
        }

        fight.setWinner(fight.getBrute2());
        DatasManager.save(fight);

        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(false)
                .send();
    }

    public void readCheatFightRandom(String token) throws IOException, SQLException, NetworkResponseException {
        if (Math.random() < 0.5) {
            this.readCheatFightLoose(token);
        } else {
            this.readCheatFightWin(token);
        }
    }

    public void readDoFight(String token) throws IOException, SQLException, NetworkResponseException {
        User user = FightResponse.checkTokenAndReturnUser(token);
        Fight fight = FightResponse.getFightWithChallenger(user);

        DoFight f = new DoFight(fight);
        Brute winner = f.exec();
        fight.setWinner(winner);
        DatasManager.save(fight);

        if (winner == fight.getBrute1()) {
            this.readCheatFightWin(token);
        } else {
            this.readCheatFightLoose(token);
        }

        System.out.println(f.getLogs());

        /*StringBuilder tmp = new StringBuilder();
         tmp.append("Fight between ").append(fight.getBrute1()).append(" and ").append(fight.getBrute2());

         // On n'oublie pas de rectifier la vie en fonction des bonus
         fight.getBrute1().setLife(fight.getBrute1().getWithBonusLife());
         fight.getBrute2().setLife(fight.getBrute2().getWithBonusLife());

         int round = 0;
         int k = ui.random(-fight.getBrute1().getBonusSpeed(), fight.getBrute2().getBonusSpeed());

         while (fight.getBrute1().getLife() > 0 && fight.getBrute2().getLife() > 0) {
         tmp.append("\n\tROUND ").append(++round);
         tmp.append(": ").append(fight.getBrute1()).append(" (").append(fight.getBrute1().getLife()).append(" pv) VS ").append(fight.getBrute2()).append(" (").append(fight.getBrute2().getLife()).append(" pv)");

         for (int j = 0; j < 2; j++) {
         Brute ch1 = j == 0 ? fight.getBrute1() : fight.getBrute2();
         Brute ch2 = j == 0 ? fight.getBrute2() : fight.getBrute1();

         tmp.append("\n\t- Player ").append(ch1).append(" ");

         // ATK or not ?
         int random = ui.random(0, 10);
         if (random == 0) {
         switch (ui.random(1, 3)) {
         case 1:
         tmp.append("falls asleep ...");
         break;
         case 2:
         tmp.append("is stunned ...");
         break;
         case 3:
         tmp.append("forgetting what he does ...");
         break;
         case 4:
         tmp.append("look at the pretty cloud ...");
         break;
         }
         } else if (random == 1) {
         // Nothing happens ...
         tmp.append("is really, really mad (and win +1 in VIT and STR) !!!");
         ch1.setSpeed((short) (ch1.getSpeed() + 1));
         ch1.setStrength((short) (ch1.getStrength() + 1));
         } else {
         // We prepare a bonus for the special attack

         int select = 0;
         switch (ui.random(1, 4)) {
         case 2:
         select = 1;
         break;
         case 3:
         select = 2;
         break;
         }
         Bonus bonusUsed;
         try {
         bonusUsed = BonusEntity.findById(ch1.getBonuses()[select].getId());
         tmp.append("attacks with his ").append(bonusUsed);
         } catch (NotFoundEntityException ex) {
         bonusUsed = Bonus.EMPTY_BONUS; // for pWin
         tmp.append("attacks with bare hands.");
         }

         // and attaks !!!
         double pWin = 1 + ((double) (5 * ch1.getLevel() + ch1.getWithBonusStrength()));
         pWin *= 1 + ((((double) bonusUsed.getSpeed()) + ((double) ch1.getWithBonusSpeed())) / ((double) (1 + ch1.getWithBonusSpeed() + ch2.getWithBonusSpeed())));
         pWin *= 1 + ((((double) bonusUsed.getStrength()) + ((double) ch1.getWithBonusStrength())) / ((double) (1 + ch1.getWithBonusStrength() + ch2.getWithBonusStrength())));
         pWin *= 1 + ((((double) bonusUsed.getLife()) + ((double) ch1.getWithBonusLife())) / ((double) (1 + ch1.getLife() + ch2.getLife())));
         pWin /= ch2.getStrength() + ch2.getBonusStrength() / 2 + ch2.getWithBonusLife();
         pWin = Math.max(0, pWin / 2);
         // @TODO getBonusLife()

         tmp.append("\t\t").append((short) pWin).append(" DEG");

         ch2.setLife((short) (ch2.getLife() - pWin)); // and the opponent loses life
         }
         }
         }

         int win = 0;

         // We choose the winner
         if (fight.getBrute1().getLife() < 0 && fight.getBrute1().getLife() < 0) {
         win = k > 0 ? 1 : 0;
         } else if (fight.getBrute1().getLife() > 0) {
         win = 1;
         }

         // save it !
         if (win == 1) {
         tmp.append("\n").append(fight.getBrute1()).append(" (you) win !");
         this.readCheatFightWin(token);
         } else {
         tmp.append("\n").append(fight.getBrute2()).append(" win ! (you lose)");
         this.readCheatFightLoose(token);
         }
         System.out.println(tmp.toString());*/
    }
}