/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes;

import brutes.user.Session;
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
public class SceneManager {
    private final static SceneManager instance = new SceneManager();
    
    private Stage stage;
    
    public static void setStage(Stage stage){
        SceneManager.getInstance().stage = stage;
    }
    
    public static SceneManager getInstance(){
        return SceneManager.instance;
    }
    
    private void show(Parent root){
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void showLogin(){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("Login.fxml"));
            show(root);
        } catch (IOException ex) {
            Logger.getLogger(SceneManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void showFight(Session session){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("Fight.fxml"));
            show(root);
        } catch (IOException ex) {
            Logger.getLogger(SceneManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
