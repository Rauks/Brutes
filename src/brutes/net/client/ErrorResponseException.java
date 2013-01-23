/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net.client;

/**
 *
 * @author Karl
 */
public class ErrorResponseException extends Exception{
    private byte discriminant;
    
    public ErrorResponseException(byte code){
        this.discriminant = code;
    }
    
    public byte getErrorCode(){
        return this.discriminant;
    }
}
