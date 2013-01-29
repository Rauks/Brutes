package brutes.client.game.media;

import javafx.scene.image.Image;

/**
 *
 * @author Karl
 */
public class DataImage{
    private Image image;

    public DataImage(String uri) {
        this.image = new Image("file:" + uri);
    }
    
    public Image getImage(){
        return this.image;
    }
}
