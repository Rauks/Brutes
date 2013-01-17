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
    
    public void flush() throws IOException{
        this.baos.flush();
        this.os.flush();
    }
    
    public NetworkWriter writeDiscriminant(byte value){
        try {
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_DISCRININANT).put(value).array());
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    public NetworkWriter writeShortInt(short value){
        try {
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_SHORTINT).putShort(value).array());
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
    public NetworkWriter writeString(String value){
        try {
            this.writeShortInt((short)value.length());
            this.baos.write(value.getBytes(Charset.forName("UTF-8")));
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    public NetworkWriter writeBoolean(boolean value){
        try {
            byte bool = (byte)(value?0xFF:0x00);
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_BOOLEAN).put(bool).array());
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    
    public void send(){
        try {
            ByteArrayOutputStream message = new ByteArrayOutputStream();
            int messageSize = baos.size() + Protocol.SIZE_LONGINT;
            message.write(ByteBuffer.allocate(Protocol.SIZE_LONGINT).putInt(messageSize).array());
            message.write(baos.toByteArray());
            byte[] send = message.toByteArray();
            this.os.write(send);
            this.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
    }
}
