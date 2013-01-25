/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.server.net;

/**
 *
 * @author Thiktak
 */
class NetworkResponseException extends Exception {
    private final byte error;

    public NetworkResponseException(byte error) {
        super();
        this.error = error;
    }
    
    public byte getError() {
        return this.error;
    }
    
}
