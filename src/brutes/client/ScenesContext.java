package brutes.client;

import brutes.client.user.Session;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Karl
 */
public class ScenesContext {
    private final static ScenesContext instance = new ScenesContext();
    
    private Stage stage;
    private Session session;
    
    public static ScenesContext getInstance(){
        return ScenesContext.instance;
    }
    
    public void setStage(Stage stage){
        ScenesContext.getInstance().stage = stage;
    }
    public void setSession(Session session){
        this.session = session;
    }
    public void destroySession(){
        this.session = null;
    }
    public Session getSession(){
        return this.session;
    }
    
    private void show(Parent root){
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void showLogin(){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("gui/Login.fxml"));
            show(root);
        } catch (IOException ex) {
            Logger.getLogger(ScenesContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void showFight(){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("gui/Fight.fxml"));
            show(root);
        } catch (IOException ex) {
            Logger.getLogger(ScenesContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
