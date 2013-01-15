/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game;

/**
 *
 * @author Karl
 */
public class Fight {
    private Character me;
    private Character chalenger;

    public Fight(Character me, Character chalenger) {
        this.me = me;
        this.chalenger = chalenger;
    }

    public Character getMe() {
        return me;
    }
    public Character getChalenger() {
        return chalenger;
    }
}
