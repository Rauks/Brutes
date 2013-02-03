package brutes.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
public class NetworkWriter {    
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
        this.writeByte(value);
        return this;
    }
    public NetworkWriter writeByte(byte value){
        try {
            this.baos.write(ByteBuffer.allocate(Protocol.SIZE_BYTE).put(value).array());
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
            byte[] str = value.getBytes(Charset.forName("UTF-8"));
            this.writeShortInt((short)str.length);
            this.baos.write(str);
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
    public NetworkWriter writeBooleanArray(boolean[] array){
        this.writeShortInt((short)array.length);
        this.writeByte(Protocol.TYPE_BOOLEAN);
        for(int i = 0; i < array.length; i++){
            this.writeBoolean(array[i]);
        }
        return this;
    }
    public NetworkWriter writeLongIntArray(int[] array){
        this.writeShortInt((short)array.length);
        this.writeByte(Protocol.TYPE_LONG);
        for(int i = 0; i < array.length; i++){
            this.writeLongInt(array[i]);
        }
        return this;
    }
    public NetworkWriter writeShortIntArray(short[] array){
        this.writeShortInt((short)array.length);
        this.writeByte(Protocol.TYPE_SHORT);
        for(int i = 0; i < array.length; i++){
            this.writeShortInt(array[i]);
        }
        return this;
    }
    public NetworkWriter writeStringArray(String[] array){
        this.writeShortInt((short)array.length);
        this.writeByte(Protocol.TYPE_STRING);
        for(int i = 0; i < array.length; i++){
            this.writeString(array[i]);
        }
        return this;
    }
    public NetworkWriter writeImage(String uri){
        File file = new File(uri);
        byte[] bFile = new byte[(int)file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bFile);
            this.writeShortInt((short)bFile.length);
            this.baos.write(bFile);
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
        return this;
    }
    
    public void send(){
        try {
            ByteArrayOutputStream message = new ByteArrayOutputStream();
            int messageSize = baos.size();
            message.write(ByteBuffer.allocate(Protocol.SIZE_LONGINT).putInt(messageSize).array());
            message.write(baos.toByteArray());
            byte[] send = message.toByteArray();
            
        //Begin debug trace
        StringBuilder hexString = new StringBuilder();
        hexString.append("SEND{");
        final int maxBytesPrinted = 50;
        for (int i = 0; i < ((send.length < maxBytesPrinted)?send.length:maxBytesPrinted); i++) {
            String hex = Integer.toHexString(0xFF & send[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
            if(i != send.length - 1){
                hexString.append(".");
            }
            if(i == maxBytesPrinted - 1 && send.length != maxBytesPrinted){
                hexString.append("....");
            }
        }
        hexString.append("}");
        Logger.getLogger(NetworkWriter.class.getName()).log(Level.INFO, hexString.toString());
        //End debug trace
            
            this.os.write(send);
            this.flush();
        } catch (IOException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.WARNING, null, ex);
        }
    }
}
