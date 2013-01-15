/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.user;

/**
 *
 * @author Karl
 */
public class Session {
    private String token;

    public Session(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
