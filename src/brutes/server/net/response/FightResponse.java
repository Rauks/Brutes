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
import brutes.server.net.NetworkServer;
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
        User user = FightResponse.checkTokenAndReturnUser(token);
        Fight fight = FightResponse.getFightWithChallenger(user);

        if (fight == null) {
            throw new NetworkResponseException(Protocol.ERROR_FIGHT);
        }

        Brute brute = fight.getBrute1();

        // level UP !
        brute.setLevel((short) Math.min(brute.getLevel() + 1, 100));

        // force UP !

        switch (ui.random(1, 4)) {
            case 1:
            case 4:
                brute.setLife((short) (brute.getLife() + ui.random(1, 10)));
                break;
            case 2:
                brute.setSpeed((short) (brute.getSpeed() + ui.random(1, 5)));
                break;
            case 3:
                brute.setStrength((short) (brute.getStrength() + ui.random(1, 5)));
                break;
        }

        // Bonus UP
        if (ui.random()) {
            Logger.getLogger(NetworkServer.class.getName()).log(Level.INFO, "readCheatFightWin : bonus - random = yes");

            Bonus bonusCharacter = BonusEntity.findRandomByBrute(brute);
            int AllCharacterBonus = ui.lengthObjects(BonusEntity.findAllByBrute(brute));
            
            if (bonusCharacter != null && ui.random(AllCharacterBonus, 6) > 4 ) {
                Logger.getLogger(NetworkServer.class.getName()).log(Level.INFO, "readCheatFightWin : bonus - delete ({0})", bonusCharacter.getName());
                DatasManager.delete(bonusCharacter);
            }

            AllCharacterBonus = ui.lengthObjects(BonusEntity.findAllByBrute(brute));

            Logger.getLogger(NetworkServer.class.getName()).log(Level.INFO, "readCheatFightWin : bonus - length = {0}", AllCharacterBonus);
            if (AllCharacterBonus < 3 && ui.random() ) {
                // On trouve quelque chose ...
                Bonus bonusTreasure = BonusEntity.findRandomShop();
                bonusTreasure.setBruteId(brute.getId());

                // Level du bonus en fontion du level de la brute
                bonusTreasure.setLevel((short) ui.randomMiddle(brute.getLevel() / 2, .5));

                // On ne modifie les paramètres que s'il sont > 0
                if (brute.getLife() > 0) {
                    bonusTreasure.setLife((short) ui.randomMiddle(brute.getLife() / 2, .5));
                }
                if (brute.getStrength() > 0) {
                    bonusTreasure.setStrength((short) ui.randomMiddle(brute.getStrength() / 2, .5));
                }
                if (brute.getSpeed() > 0) {
                    bonusTreasure.setSpeed((short) ui.randomMiddle(brute.getSpeed() / 2, .5));
                }

                DatasManager.insert(bonusTreasure);
                Logger.getLogger(NetworkServer.class.getName()).log(Level.INFO, "readCheatFightWin : bonus - insert ({0})", bonusTreasure.getName());
            }
        }

        DatasManager.save(brute);

        fight.setWinner(brute);
        DatasManager.save(fight);

        this.getWriter().writeDiscriminant(Protocol.R_FIGHT_RESULT)
                .writeBoolean(true)
                .send();
    }

    public void readCheatFightLoose(String token) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
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

    public void readCheatFightRandom(String token) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
        if (Math.random() < 0.5) {
            this.readCheatFightLoose(token);
        } else {
            this.readCheatFightWin(token);
        }
    }

    public void readDoFight(String token) throws IOException, SQLException, NetworkResponseException, NotFoundEntityException {
        User user = FightResponse.checkTokenAndReturnUser(token);
        Fight fight = FightResponse.getFightWithChallenger(user);

        StringBuilder tmp = new StringBuilder();
        tmp.append("Fight between ").append(fight.getBrute1()).append(" and ").append(fight.getBrute2());

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
                } else {// We prepare a bonus for the special attack
                    // We prepare a bonus for the special attack
                    Bonus bonusUsed = null;//BonusEntity.findRandomByBrute(ch1);
                    if (bonusUsed != null) {
                        tmp.append("attacks with his ").append(bonusUsed);
                    } else {
                        bonusUsed = new Bonus(); // for pWin
                        tmp.append("attacks with bare hands.");
                    }

                    // and attaks !!!
                    double pWin = 1 + ((double) (5 * ch1.getLevel() + ch1.getWithBonusStrength()));
                    pWin *= 1 + ((((double) bonusUsed.getSpeed()) + ((double) ch1.getWithBonusSpeed())) / ((double) (1 + ch1.getWithBonusSpeed() + ch2.getWithBonusSpeed())));
                    pWin *= 1 + ((((double) bonusUsed.getStrength()) + ((double) ch1.getWithBonusStrength())) / ((double) (1 + ch1.getWithBonusStrength() + ch2.getWithBonusStrength())));
                    pWin *= 1 + ((double) ch1.getLife() / ((double) (1 + ch1.getLife() + ch2.getLife())));
                    pWin /= ch2.getStrength() + ch2.getBonusStrength()/2 + ch2.getLife();
                    pWin = Math.max(0, pWin);
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
        System.out.println(tmp.toString());
    }
}