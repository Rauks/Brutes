/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes;

import brutes.db.DatasManager;
import brutes.game.User;
import brutes.net.Protocol;
import brutes.net.client.NetworkClient;
import brutes.net.server.NetworkLocalTestServer;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;

/**
 *
 * @author Karl
 */
public class Brutes extends Application {
    private static ServerThread SERVER = new ServerThread();
    
    public static void exit(){
        Brutes.SERVER.interrupt();
        Platform.exit();
    }
        
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Les brutes (TP Réseaux 2012/2013 - Karl Woditsch)");
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler(){
            @Override
            public void handle(Event t) {
                Brutes.exit();
            }
        });

        Brutes.SERVER.start();
        
        ScenesContext.getInstance().setStage(stage);
        ScenesContext.getInstance().showLogin();
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
