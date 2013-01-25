/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.client.net;

/**
 *
 * @author Karl
 */
public class InvalidResponseException extends Exception{
    public InvalidResponseException(){
        super("Erreur du serveur");
    }
}
