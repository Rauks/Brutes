package brutes.server.net;

/**
 *
 * @author Thiktak
 */
public class NetworkResponseException extends Exception {
    private final byte error;

    public NetworkResponseException(byte error) {
        super();
        this.error = error;
    }
    
    public byte getError() {
        return this.error;
    }
    
}
