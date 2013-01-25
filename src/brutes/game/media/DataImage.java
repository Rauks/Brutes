/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game.media;

import brutes.net.server.db.Identifiable;
import javafx.scene.image.Image;

/**
 *
 * @author Karl
 */
public class DataImage implements Identifiable{
    private int id;
    private Image image;

    public DataImage(int id) {
        this.id = id;
    }
    
    public Image getImage(){
        return this.image;
    }
    @Override
    public int getId(){
        return this.id;
    }
}
