/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.client.net;

/**
 *
 * @author Karl
 */
class UncachedException extends Exception {
    public UncachedException(){
        super("Image non disponible en cache");
    }
}
