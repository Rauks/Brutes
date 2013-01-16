/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

/**
 *
 * @author Karl
 */
public class Network {
    public final static Network instance = new Network();
    
    public static Network getInstance(){
        return Network.instance;
    }
}
