/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.game.media;

import javafx.scene.image.Image;

/**
 *
 * @author Karl
 */
public class DataImage {
    private int id;
    private Image image;

    public DataImage(int id) {
        this.id = id;
    }
    
    public Image getImage(){
        return this.image;
    }
    public int getId(){
        return this.id;
    }
}
