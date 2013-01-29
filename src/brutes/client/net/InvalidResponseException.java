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
