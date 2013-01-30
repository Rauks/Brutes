package brutes.server.game;

import brutes.server.db.Identifiable;

/**
 *
 * @author Thiktak
 */
public class Fight implements Identifiable {

    private int id;
    private Brute brute1;
    private Brute brute2;
    private Brute winner;

    public Fight() {
    }

    public Fight(int id, Brute brute1, Brute brute2) {
        this.id = id;
        this.brute1 = brute1;
        this.brute2 = brute2;
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public Brute getBrute1() {
        return brute1;
    }

    public void setBrute1(Brute brute1) {
        this.brute1 = brute1;
    }

    public Brute getBrute2() {
        return brute2;
    }

    public void setBrute2(Brute brute2) {
        this.brute2 = brute2;
    }

    public Brute getWinner() {
        return winner;
    }

    public void setWinner(Brute winner) {
        this.winner = winner;
    }
}
