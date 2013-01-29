package brutes.net;

/**
 *
 * @author Karl
 */
class NetworkException extends Exception {
    public final static String SERVER_UNDEFINED = "server adresse undefined";
    public final static String ARRAY_TYPE = "wrong array type";
    
    public NetworkException(String message) {
        super(message);
    } 
}
