/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.client.game.media;

import java.net.URI;
import javafx.scene.image.Image;

/**
 *
 * @author Karl
 */
public class DataImage{
    private Image image;

    public DataImage(URI uri) {
        this.image = new Image(uri.getPath());
    }
    
    public Image getImage(){
        return this.image;
    }
}
