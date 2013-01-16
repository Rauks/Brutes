/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karl
 */
class NetworkReader {
    private InputStream is;

    public NetworkReader(InputStream is) {
        this.is = is;
    }
    
    public long readMessageSize() throws IOException{
        return this.readLongInt();
    }
    public byte readByte() throws IOException{
        byte[] b = new byte[1];
        this.is.read(b);
        return b[0];
    }
    public byte readDiscriminant() throws IOException{
        return this.readByte();
    }
    public short readShortInt() throws IOException{
        byte[] b = new byte[Protocol.SIZE_SHORTINT];
        this.is.read(b);
        ByteBuffer bb = ByteBuffer.wrap(b);
        return bb.getShort();
    }
    public int readLongInt() throws IOException{
        byte[] b = new byte[Protocol.SIZE_LONGINT];
        this.is.read(b);
        ByteBuffer bb = ByteBuffer.wrap(b);
        return bb.getInt();
    }
    public String readString() throws IOException{
        String out = null;
        try {
            short length = this.readShortInt();
            byte[] b = new byte[length];
            this.is.read(b);
            out =  new String(b, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NetworkReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }
    public boolean readBoolean() throws IOException {
        byte[] b = new byte[1];
        this.is.read(b);
        return (b[0] != 0x00);
    }
    public ArrayList readArray() throws IOException{
        short nbElements = this.readShortInt();
        byte type = this.readByte();
        ArrayList list = new ArrayList();
        for(int i = 0; i < nbElements; i++){
            switch(type){
                case Protocol.TYPE_BOOLEAN:
                    list.add(this.readBoolean()?Boolean.TRUE:Boolean.FALSE);
                    break;
                case Protocol.TYPE_LONG:
                    list.add(new Integer(this.readLongInt()));
                    break;
                case Protocol.TYPE_SHORT:
                    list.add(new Short(this.readShortInt()));
                    break;
                case Protocol.TYPE_STRING:
                    list.add(this.readString());
                    break;
                default:
                    throw new IOException("Array type invalid or not supported");
            }
        }
        return list; 
    }
}
