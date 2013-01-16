/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
class NetworkWriter {    
    private OutputStream os;
    private ByteArrayOutputStream baos;

    public NetworkWriter(OutputStream os) {
        this.os = os;
        this.baos = new ByteArrayOutputStream();
    }
    public NetworkWriter writeDiscriminant(int value){
        try {
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_DISCRININANT).putInt(value).array());
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    public NetworkWriter writeChar(char value){
        try {
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_CHAR).putChar(value).array());
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    public NetworkWriter writeShortInt(int value){
        try {
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_SHORTINT).putInt(value).array());
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    public NetworkWriter writeLongInt(int value){
        try {
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_LONGINT).putInt(value).array());
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    public NetworkWriter writeFloat(float value){
        try {
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_LONGINT).putFloat(value).array());
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    public NetworkWriter writeString(String value){
        try {        
            this.baos.write(value.getBytes(Charset.forName("UTF-8")));
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    
    public void send(){
        try {
            ByteArrayOutputStream message = new ByteArrayOutputStream();
            int messageSize = baos.size() + Protocol.MESSAGE_SIZE;
            message.write(ByteBuffer.allocate(Protocol.MESSAGE_SIZE).putInt(messageSize).array());
            message.write(baos.toByteArray());
            byte[] send = message.toByteArray();
            this.os.write(send);
            this.os.flush();
            this.baos.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
    }
}
