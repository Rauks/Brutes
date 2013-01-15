/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes;

import brutes.user.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Karl
 */
public class Brutes extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        SceneManager.getInstance().showLogin();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
