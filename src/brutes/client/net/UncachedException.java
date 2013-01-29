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
