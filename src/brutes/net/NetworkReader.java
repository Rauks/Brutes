/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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
    public byte readDiscriminant() throws IOException{
        byte[] b = new byte[1];
        this.is.read(b);
        return b[0];
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
}
